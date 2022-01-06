package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.pojo.dto.VendorAttachmentDTO;
import com.xdcplus.vendor.common.pojo.dto.VendorDTO;
import com.xdcplus.vendor.common.pojo.dto.VendorFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.VendorUserDTO;
import com.xdcplus.vendor.common.pojo.query.VendorQuery;
import com.xdcplus.vendor.common.pojo.vo.VendorUserVO;
import com.xdcplus.vendor.generator.impl.VendorBaseServiceImpl;
import com.xdcplus.vendor.mapper.VendorMapper;
import com.xdcplus.vendor.common.pojo.entity.Vendor;
import com.xdcplus.vendor.common.pojo.vo.VendorVO;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.VendorAttachmentService;
import com.xdcplus.vendor.service.VendorService;
import com.xdcplus.vendor.service.VendorUserService;
import com.xdcplus.ztb.common.cache.RedisUtils;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.dto.RegisterUserDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserRoleInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.dto.VendorExcelDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.constants.CacheConstant;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 供应商(Vendor)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-23 10:14:53
 */
@Slf4j
@Service("vendorService")
public class VendorServiceImpl extends VendorBaseServiceImpl<Vendor, VendorVO, Vendor, VendorMapper> implements VendorService {

    @Autowired
    PermService permService;

    @Autowired
    VendorUserService vendorUserService;

    @Autowired
    VendorAttachmentService vendorAttachmentService;

    @Autowired
    RedisUtils redisUtils;

    /**
     * 注册供应商(发邮件)
     *
     * @param vendorDTO 供应商dto
     * @return {@link Boolean}
     */
    @Override
    public Boolean registerVendor(VendorDTO vendorDTO) {
        if (ObjectUtil.isEmpty(vendorDTO.getRegisterUserDTO())) {
            log.error("loginVendor() registerUserDTO select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        //查询供应商
        List<Vendor> vendors = findVendorVOByNameAndBusinessLicense(vendorDTO.getName(), vendorDTO.getBusinessLicense());
        Vendor vendor;
        if (CollectionUtil.isEmpty(vendors)) {
            vendor = new Vendor();
            //生成code
            combineVendorCode(vendorDTO,vendor);

            vendor.setCreatedTime(DateUtil.current());
            vendor.setDeleted(0);
            boolean result = this.save(vendor);
            if (!result) {
                log.error("loginVendor() vendor save faild");
                throw new ZtbWebException(ResponseEnum.ERROR);
            }
        } else {
            vendor = vendors.get(NumberConstant.ZERO);
        }

        if (ObjectUtil.isEmpty(vendor)) {
            log.error("loginVendor() vendor save faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        //注册用户
        SysUserInfoVO sysUserInfoVO = permService.registerUser(vendorDTO.getRegisterUserDTO());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("loginVendor() sysUserInfo save faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        //绑定供应商角色
        SysUserRoleInfoDTO sysUserRoleInfoDTO =SysUserRoleInfoDTO.builder().userId(sysUserInfoVO.getId()).roleId(ZtbConstant.VENDOR_ROLE_ID).build();
        permService.insertUserRole(sysUserRoleInfoDTO);

        //绑定用户
        VendorUserDTO vendorUserDTO = new VendorUserDTO();
        vendorUserDTO.setUserId(sysUserInfoVO.getId());
        vendorUserDTO.setVendorId(vendor.getId());
        Boolean result = vendorUserService.saveVendorUser(vendorUserDTO);

        //附件
        List<VendorAttachmentDTO> vendorAttachmentDTOS = vendorDTO.getVendorAttachmentDTOS();
        if (CollectionUtil.isNotEmpty(vendorAttachmentDTOS)) {
            vendorAttachmentDTOS.forEach(vendorAttachmentDTO -> {
                vendorAttachmentDTO.setVendorId(vendor.getId());
            });
            vendorAttachmentService.saveOrUpdateBatchByDTOList(vendorAttachmentDTOS);
        }

        return result;
    }

    /**
     * 注册供应商(不发送邮件)
     *
     * @param vendorDTO 供应商dto
     */
    @Override
    public void registerVendorNotSendMail(VendorDTO vendorDTO) {
        if (ObjectUtil.isEmpty(vendorDTO.getRegisterUserDTO())) {
            log.error("loginVendor() registerUserDTO select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        //查询供应商
        List<Vendor> vendors = findVendorVOByNameAndBusinessLicense(vendorDTO.getName(), vendorDTO.getBusinessLicense());
        Vendor vendor;
        if (CollectionUtil.isEmpty(vendors)) {
            vendor = new Vendor();
            BeanUtil.copyProperties(vendorDTO, vendor);
            String vendorCode = redisUtils.get(CacheConstant.VENDOR_CODE, new TypeReference<String>(){});
            if(vendorCode==null){
                StringBuffer buffer=new StringBuffer();
                String springWater=String.format("%05d", 1);
                redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
                buffer.append("S").append(springWater);
                vendor.setCode(buffer.toString());
            }else{
                int codeNumberInt = Integer.parseInt(vendorCode);
                if(codeNumberInt == ZtbConstant.CODE_NUMBER_99999){
                    codeNumberInt=NumberConstant.ONE;
                }else{
                    codeNumberInt+=NumberConstant.ONE;
                }
                StringBuffer buffer=new StringBuffer();
                String springWater=String.format("%05d", codeNumberInt);
                redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
                buffer.append("S").append(springWater);
                vendor.setCode(buffer.toString());
            }


            vendor.setCreatedTime(DateUtil.current());
            vendor.setDeleted(0);
            boolean result = this.save(vendor);
            if (!result) {
                log.error("loginVendor() vendor save faild");
                throw new ZtbWebException(ResponseEnum.ERROR);
            }
        } else {
            vendor = vendors.get(NumberConstant.ZERO);
        }

        if (ObjectUtil.isEmpty(vendor)) {
            log.error("loginVendor() vendor save faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        //注册用户(不发邮件)
        SysUserInfoVO sysUserInfoVO = permService.registerUserNotSendEmail(vendorDTO.getRegisterUserDTO());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("loginVendor() sysUserInfo save faild");
//            throw new ZtbWebException(ResponseEnum.ERROR);
            return;
        }

        //绑定供应商角色
        SysUserRoleInfoDTO sysUserRoleInfoDTO =SysUserRoleInfoDTO.builder().userId(sysUserInfoVO.getId()).roleId(ZtbConstant.VENDOR_ROLE_ID).build();
        permService.insertUserRole(sysUserRoleInfoDTO);

        //绑定用户
        VendorUserDTO vendorUserDTO = new VendorUserDTO();
        vendorUserDTO.setUserId(sysUserInfoVO.getId());
        vendorUserDTO.setVendorId(vendor.getId());
        Boolean result = vendorUserService.saveVendorUser(vendorUserDTO);

        //附件
        List<VendorAttachmentDTO> vendorAttachmentDTOS = vendorDTO.getVendorAttachmentDTOS();
        if (CollectionUtil.isNotEmpty(vendorAttachmentDTOS)) {
            vendorAttachmentDTOS.forEach(vendorAttachmentDTO -> {
                vendorAttachmentDTO.setVendorId(vendor.getId());
            });
            vendorAttachmentService.saveOrUpdateBatchByDTOList(vendorAttachmentDTOS);
        }
    }

    @Override
    public List<Vendor> findVendorVOByNameAndBusinessLicense(String name, String businessLicense) {
        VendorQuery vendorQuery = new VendorQuery();
        vendorQuery.setName(name);
        vendorQuery.setBusinessLicense(businessLicense);
        List<Vendor> vendors = vendorMapper.queryVendor(vendorQuery);

        return vendors;
    }

    @Override
    public List<VendorVO> queryVendorsWithUser(VendorFilterDTO vendorFilterDTO) {
        List<VendorVO> vendorVOS = queryVendorVOList(vendorFilterDTO);
        if (CollectionUtil.isEmpty(vendorVOS)) {
            return null;
        }
        vendorVOS.forEach(vendorVO -> {
            List<SysUserInfoVO> sysUserInfoVOS = vendorUserService.findSysUserInfoVOByVendorId(vendorVO.getId());
            vendorVO.setSysUserInfoVOS(sysUserInfoVOS);
        });
        return vendorVOS;
    }

    @Override
    public VendorVO queryVendorsByUserId(Long userId) {
        Vendor vendor = vendorMapper.queryVendorsByUserId(userId);
        if(ObjectUtil.isEmpty(vendor)){
            return null;
        }
        
        return BeanUtil.copyProperties(vendorMapper.queryVendorsByUserId(userId),VendorVO.class);
    }


    @Override
    public void importVendorFile(List<VendorExcelDTO> vendorExcelDTOS) {
        List<VendorDTO> vendorDTOS = combineVendorDTO(vendorExcelDTOS);

        vendorDTOS.forEach(vendorDTO -> registerVendorNotSendMailAndCover(vendorDTO));
    }

    @Override
    public void importVendorFileOnLine(List<VendorExcelDTO> vendorExcelDTOS) {
        List<VendorDTO> vendorDTOS = combineVendorDTO(vendorExcelDTOS);
        vendorDTOS.forEach(vendorDTO -> registerVendorCover(vendorDTO));
    }

    private void combineVendorCode(VendorDTO vendorDTO,Vendor vendor){
        BeanUtil.copyProperties(vendorDTO, vendor);
        String vendorCode = redisUtils.get(CacheConstant.VENDOR_CODE, new TypeReference<String>(){});
        if(vendorCode==null){
            StringBuffer buffer=new StringBuffer();
            String springWater=String.format("%05d", 1);
            redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
            buffer.append("S").append(springWater);
            vendor.setCode(buffer.toString());
        }else{
            int codeNumberInt = Integer.parseInt(vendorCode);
            if(codeNumberInt == ZtbConstant.CODE_NUMBER_99999){
                codeNumberInt=NumberConstant.ONE;
            }else{
                codeNumberInt+=NumberConstant.ONE;
            }
            StringBuffer buffer=new StringBuffer();
            String springWater=String.format("%05d", codeNumberInt);
            redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
            buffer.append("S").append(springWater);
            vendor.setCode(buffer.toString());
        }
    }

    private List<VendorDTO> combineVendorDTO (List<VendorExcelDTO> vendorExcelDTOS){
        List<VendorDTO> vendorDTOS = CollectionUtil.newArrayList();

        for (VendorExcelDTO vendorExcelDTO : vendorExcelDTOS) {
            if(StrUtil.isEmpty(vendorExcelDTO.getCompanyName())){
                continue;
            }
            VendorDTO vendorDTO=new VendorDTO();
            vendorDTO.setName(vendorExcelDTO.getCompanyName());
            vendorDTO.setBusinessLicense(vendorExcelDTO.getBusinessLicense());
            vendorDTO.setVatNumber(vendorExcelDTO.getVatNumber());
            vendorDTO.setRegisteredAddress(vendorExcelDTO.getRegisteredAddress());
            vendorDTO.setPostcode(vendorExcelDTO.getPostcode());
            vendorDTO.setCity(vendorExcelDTO.getCity());
            vendorDTO.setCountry(vendorExcelDTO.getCountry());
            vendorDTO.setWebsite(vendorExcelDTO.getWebsite());

            RegisterUserDTO registerUserDTO=new RegisterUserDTO();
            registerUserDTO.setName(vendorExcelDTO.getUserSurname()+vendorExcelDTO.getUserName());
            registerUserDTO.setUserName(vendorExcelDTO.getUserAlias());
            //生成新的密码
//            String newPassword = getRandomPassword();
//            registerUserDTO.setPassword(DigestUtil.bcrypt(newPassword));
            registerUserDTO.setPassword("123456");
            if(StrUtil.contains(vendorExcelDTO.getUserEmail(),";")){
                registerUserDTO.setMail(vendorExcelDTO.getUserEmail().split(";")[0]);
            }else{
                registerUserDTO.setMail(vendorExcelDTO.getUserEmail());
            }
            registerUserDTO.setPhone(vendorExcelDTO.getUserPhone());

            vendorDTO.setRegisterUserDTO(registerUserDTO);
            vendorDTOS.add(vendorDTO);
        }
        return vendorDTOS;
    }

    /**
     * 注册供应商不发送邮件，覆盖源数据
     *
     * @param vendorDTO 供应商dto
     */
    private void registerVendorNotSendMailAndCover(VendorDTO vendorDTO) {
        if (ObjectUtil.isEmpty(vendorDTO.getRegisterUserDTO())) {
            log.error("loginVendor() registerUserDTO select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        //查询供应商
        List<Vendor> vendors = findVendorVOByNameAndBusinessLicense(vendorDTO.getName(), vendorDTO.getBusinessLicense());
        Vendor vendor;
        if (CollectionUtil.isEmpty(vendors)) {
            vendor = new Vendor();
            BeanUtil.copyProperties(vendorDTO, vendor);
            String vendorCode = redisUtils.get(CacheConstant.VENDOR_CODE, new TypeReference<String>(){});
            if(vendorCode==null){
                StringBuffer buffer=new StringBuffer();
                String springWater=String.format("%05d", 1);
                redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
                buffer.append("S").append(springWater);
                vendor.setCode(buffer.toString());
            }else{
                int codeNumberInt = Integer.parseInt(vendorCode);
                if(codeNumberInt == ZtbConstant.CODE_NUMBER_99999){
                    codeNumberInt=NumberConstant.ONE;
                }else{
                    codeNumberInt+=NumberConstant.ONE;
                }
                StringBuffer buffer=new StringBuffer();
                String springWater=String.format("%05d", codeNumberInt);
                redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
                buffer.append("S").append(springWater);
                vendor.setCode(buffer.toString());
            }


            vendor.setCreatedTime(DateUtil.current());
            vendor.setDeleted(0);
            boolean result = this.save(vendor);
            if (!result) {
                log.error("loginVendor() vendor save faild");
                throw new ZtbWebException(ResponseEnum.ERROR);
            }
        } else {
            vendor = vendors.get(NumberConstant.ZERO);
        }

        if (ObjectUtil.isEmpty(vendor)) {
            log.error("loginVendor() vendor save faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        //注册用户(不发邮件,覆盖源数据)
        SysUserInfoVO sysUserInfoVO = permService.registerUserNotSendEmailAndCover(vendorDTO.getRegisterUserDTO());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("loginVendor() sysUserInfo save faild");
//            throw new ZtbWebException(ResponseEnum.ERROR);
            return;
        }

        //判断是否覆盖原始数据
        VendorUserVO vendorUserVO = vendorUserService.findVendorUserByUserId(sysUserInfoVO.getId());
        if(ObjectUtil.isEmpty(vendorUserVO)){
            //绑定供应商角色
            SysUserRoleInfoDTO sysUserRoleInfoDTO =SysUserRoleInfoDTO.builder().userId(sysUserInfoVO.getId()).roleId(ZtbConstant.VENDOR_ROLE_ID).build();
            permService.insertUserRole(sysUserRoleInfoDTO);

            //绑定用户
            VendorUserDTO vendorUserDTO = new VendorUserDTO();
            vendorUserDTO.setUserId(sysUserInfoVO.getId());
            vendorUserDTO.setVendorId(vendor.getId());
            Boolean result = vendorUserService.saveVendorUser(vendorUserDTO);

            //附件
            List<VendorAttachmentDTO> vendorAttachmentDTOS = vendorDTO.getVendorAttachmentDTOS();
            if (CollectionUtil.isNotEmpty(vendorAttachmentDTOS)) {
                vendorAttachmentDTOS.forEach(vendorAttachmentDTO -> {
                    vendorAttachmentDTO.setVendorId(vendor.getId());
                });
                vendorAttachmentService.saveOrUpdateBatchByDTOList(vendorAttachmentDTOS);
            }
        }
    }

    /**
     * 注册供应商(覆盖源数据)
     *
     * @param vendorDTO 供应商dto
     */
    private void registerVendorCover(VendorDTO vendorDTO) {
        if (ObjectUtil.isEmpty(vendorDTO.getRegisterUserDTO())) {
            log.error("loginVendor() registerUserDTO select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        //查询供应商
        List<Vendor> vendors = findVendorVOByNameAndBusinessLicense(vendorDTO.getName(), vendorDTO.getBusinessLicense());
        Vendor vendor;
        if (CollectionUtil.isEmpty(vendors)) {
            vendor = new Vendor();
            BeanUtil.copyProperties(vendorDTO, vendor);
            String vendorCode = redisUtils.get(CacheConstant.VENDOR_CODE, new TypeReference<String>(){});
            if(vendorCode==null){
                StringBuffer buffer=new StringBuffer();
                String springWater=String.format("%05d", 1);
                redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
                buffer.append("S").append(springWater);
                vendor.setCode(buffer.toString());
            }else{
                int codeNumberInt = Integer.parseInt(vendorCode);
                if(codeNumberInt == ZtbConstant.CODE_NUMBER_99999){
                    codeNumberInt=NumberConstant.ONE;
                }else{
                    codeNumberInt+=NumberConstant.ONE;
                }
                StringBuffer buffer=new StringBuffer();
                String springWater=String.format("%05d", codeNumberInt);
                redisUtils.set(CacheConstant.VENDOR_CODE, JSON.toJSONString(springWater));
                buffer.append("S").append(springWater);
                vendor.setCode(buffer.toString());
            }


            vendor.setCreatedTime(DateUtil.current());
            vendor.setDeleted(0);
            boolean result = this.save(vendor);
            if (!result) {
                log.error("loginVendor() vendor save faild");
                throw new ZtbWebException(ResponseEnum.ERROR);
            }
        } else {
            vendor = vendors.get(NumberConstant.ZERO);
        }

        if (ObjectUtil.isEmpty(vendor)) {
            log.error("loginVendor() vendor save faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        //注册用户(不发邮件,覆盖源数据)
        SysUserInfoVO sysUserInfoVO = permService.registerUserCover(vendorDTO.getRegisterUserDTO());
        if(ObjectUtil.isEmpty(sysUserInfoVO)){
            log.error("loginVendor() sysUserInfo save faild");
//            throw new ZtbWebException(ResponseEnum.ERROR);
            return;
        }

        //判断是否覆盖原始数据
        VendorUserVO vendorUserVO = vendorUserService.findVendorUserByUserId(sysUserInfoVO.getId());
        if(ObjectUtil.isEmpty(vendorUserVO)){
            //绑定供应商角色
            SysUserRoleInfoDTO sysUserRoleInfoDTO =SysUserRoleInfoDTO.builder().userId(sysUserInfoVO.getId()).roleId(ZtbConstant.VENDOR_ROLE_ID).build();
            permService.insertUserRole(sysUserRoleInfoDTO);

            //绑定用户
            VendorUserDTO vendorUserDTO = new VendorUserDTO();
            vendorUserDTO.setUserId(sysUserInfoVO.getId());
            vendorUserDTO.setVendorId(vendor.getId());
            Boolean result = vendorUserService.saveVendorUser(vendorUserDTO);

            //附件
            List<VendorAttachmentDTO> vendorAttachmentDTOS = vendorDTO.getVendorAttachmentDTOS();
            if (CollectionUtil.isNotEmpty(vendorAttachmentDTOS)) {
                vendorAttachmentDTOS.forEach(vendorAttachmentDTO -> {
                    vendorAttachmentDTO.setVendorId(vendor.getId());
                });
                vendorAttachmentService.saveOrUpdateBatchByDTOList(vendorAttachmentDTOS);
            }
        }
    }

    /**
     * 产生随机密码
     *
     * @return {@link String}
     */
    private static String getRandomPassword(){
        int length=16;//密码长度
        //定义一个字符串（A-Z，a-z，0-9）即62位；
        String str="zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        //由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //长度为几就循环几次
        for(int i=0; i<length; ++i){
            //产生0-61的数字
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

}
