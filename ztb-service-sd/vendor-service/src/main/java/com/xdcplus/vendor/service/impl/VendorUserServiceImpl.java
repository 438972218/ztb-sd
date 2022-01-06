package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.vendor.common.pojo.vo.WebsiteVO;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.pojo.dto.VendorFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.VendorUserFilterDTO;
import com.xdcplus.vendor.common.pojo.query.VendorUserQuery;
import com.xdcplus.vendor.common.pojo.vo.VendorVO;
import com.xdcplus.vendor.generator.impl.VendorUserBaseServiceImpl;
import com.xdcplus.vendor.mapper.VendorUserMapper;
import com.xdcplus.vendor.common.pojo.entity.VendorUser;
import com.xdcplus.vendor.common.pojo.vo.VendorUserVO;
import com.xdcplus.vendor.remote.service.IeService;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.VendorService;
import com.xdcplus.vendor.service.VendorUserService;
import com.xdcplus.ztb.common.remote.domain.perm.dto.SysUserInfoDTO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.MailBeanInfo;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (VendorUser)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-17 11:29:49
 */
@Slf4j
@Service("vendorUserService")
public class VendorUserServiceImpl extends VendorUserBaseServiceImpl<VendorUser, VendorUserVO, VendorUser, VendorUserMapper> implements VendorUserService {

    @Autowired
    private VendorUserMapper vendorUserMapper;

    @Autowired
    private PermService permService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private IeService ieService;

    @Autowired
    WebsiteVO websiteVO;

    @Override
    public VendorUserVO findVendorUserByUserId(Long userId) {

        Assert.notNull(userId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return this.objectConversion(vendorUserMapper.findVendorUserByUserId(userId));
    }

    @Override
    public List<VendorUserVO> findVendorUserByVendorId(Long vendorId) {
        VendorUserFilterDTO vendorUserFilterDTO = new VendorUserFilterDTO();
        vendorUserFilterDTO.setVendorId(vendorId);
        List<VendorUserVO> vendorUserVOS = queryVendorUserVOList(vendorUserFilterDTO);
        if (CollectionUtil.isEmpty(vendorUserVOS)) {
            return null;
        }
        vendorUserVOS.forEach(vendorUserVO -> {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(vendorUserVO.getUserId());
            vendorUserVO.setSysUserInfoVO(sysUserInfoVO);
        });

        return vendorUserVOS;
    }

    @Override
    public List<SysUserInfoVO> findSysUserInfoVOByVendorId(Long vendorId) {
        VendorUserFilterDTO vendorUserFilterDTO = new VendorUserFilterDTO();
        vendorUserFilterDTO.setVendorId(vendorId);
        List<VendorUserVO> vendorUserVOS = queryVendorUserVOList(vendorUserFilterDTO);
        if (CollectionUtil.isEmpty(vendorUserVOS)) {
            return null;
        }
        List<SysUserInfoVO> sysUserInfoVOS = CollectionUtil.newArrayList();
        vendorUserVOS.forEach(vendorUserVO -> {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(vendorUserVO.getUserId());
            sysUserInfoVOS.add(sysUserInfoVO);
        });

        return sysUserInfoVOS;
    }

    @Override
    public List<VendorUserVO> findVendorUserWithVendor(VendorUserFilterDTO vendorUserFilterDTO) {
        List<VendorUserVO> vendorUserVOS = queryVendorUserVOList(vendorUserFilterDTO);
        if(CollectionUtil.isEmpty(vendorUserVOS)){
            return null;
        }
        VendorFilterDTO vendorFilterDTO=new VendorFilterDTO();
        vendorFilterDTO.setCurrentPage(NumberConstant.A_NEGATIVE);
        List<VendorVO> vendorVOS = vendorService.queryVendorVOList(vendorFilterDTO);
        Map<Long,VendorVO> vendorVOMap = vendorVOS.stream().collect(Collectors.toMap(VendorVO::getId,vendorVO -> vendorVO));

        List<SysUserInfoVO> sysUserInfoVOS = permService.queryAllVendorUser();
        Map<Long,SysUserInfoVO> sysUserInfoVOMap = sysUserInfoVOS.stream().collect(Collectors.toMap(SysUserInfoVO::getId,sysUserInfoVO -> sysUserInfoVO));

        for (VendorUserVO vendorUserVO : vendorUserVOS) {
            VendorVO vendorVO = vendorVOMap.get(vendorUserVO.getVendorId());
            SysUserInfoVO sysUserInfoVO = sysUserInfoVOMap.get(vendorUserVO.getUserId());
            if(ObjectUtil.isEmpty(sysUserInfoVO)){
                continue;
            }
            vendorUserVO.setSysUserInfoVO(sysUserInfoVO);
            vendorUserVO.setVendorVO(vendorVO);
            vendorUserVO.setUserNameVendorName(sysUserInfoVO.getName()+"-"+vendorVO.getName());
        }
//        vendorUserVOS.forEach(vendorUserVO -> {
//            VendorVO vendorVO = vendorService.queryVendorById(vendorUserVO.getVendorId());
//            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(vendorUserVO.getUserId());
//            if(ObjectUtil.isEmpty(sysUserInfoVO)){
//
//            }
//            vendorUserVO.setSysUserInfoVO(sysUserInfoVO);
//            vendorUserVO.setVendorVO(vendorVO);
//            vendorUserVO.setUserNameVendorName(sysUserInfoVO.getName()+"-"+vendorVO.getName());
//        });
        return vendorUserVOS;
    }

    @Override
    public PageVO<VendorUserVO> queryVendorUserPageVO(VendorUserFilterDTO vendorUserFilterDTO) {
        PageVO<VendorUserVO> pageVO = new PageVO<>();


        //1.查询出外部用户信息ids
        //2.查询出供应商信息
        SysUserInfoDTO sysUserInfoDTO = SysUserInfoDTO.builder().currentPage(NumberConstant.A_NEGATIVE).keyword(vendorUserFilterDTO.getKeyword()).build();
        PageVO<SysUserInfoVO> sysUserInfoVOPageVO = permService.querySysUserVOPageVO(sysUserInfoDTO);

        VendorUserQuery vendorUserQuery = BeanUtil.copyProperties(vendorUserFilterDTO, VendorUserQuery.class);

        if(ObjectUtil.isNotEmpty(sysUserInfoVOPageVO)){
            List<SysUserInfoVO> sysUserInfoVOS = sysUserInfoVOPageVO.getRecords();
            List<Long> userIds = sysUserInfoVOS.stream().map(SysUserInfoVO::getId).collect(Collectors.toList());
            vendorUserQuery.setUserIds(userIds);
        }

        List<VendorUser> vendorUsers = vendorUserMapper.queryVendorUserByKeyword(vendorUserQuery);
        if(CollectionUtil.isEmpty(vendorUsers)){
            return null;
        }


        VendorFilterDTO vendorFilterDTO=new VendorFilterDTO();
        vendorFilterDTO.setCurrentPage(NumberConstant.A_NEGATIVE);
        List<VendorVO> vendorVOS = vendorService.queryVendorVOList(vendorFilterDTO);
        Map<Long,VendorVO> vendorVOMap = vendorVOS.stream().collect(Collectors.toMap(VendorVO::getId,vendorVO -> vendorVO));

        List<SysUserInfoVO> sysUserInfoVOAllS = permService.queryAllVendorUser();
        Map<Long,SysUserInfoVO> sysUserInfoVOMap = sysUserInfoVOAllS.stream().collect(Collectors.toMap(SysUserInfoVO::getId,sysUserInfoVO -> sysUserInfoVO));

        if (vendorUserFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(vendorUserFilterDTO.getCurrentPage(), vendorUserFilterDTO.getPageSize(),
                    vendorUserFilterDTO.getOrderType(), vendorUserFilterDTO.getOrderField());
        }

        List<VendorUserVO> vendorUserVOS = this.objectConversion(vendorUsers);
        PageInfo<VendorUserVO> pageInfo = new PageInfo<>(vendorUserVOS);

        for (VendorUserVO vendorUserVO : vendorUserVOS) {
            VendorVO vendorVO = vendorVOMap.get(vendorUserVO.getVendorId());
            SysUserInfoVO sysUserInfoVO = sysUserInfoVOMap.get(vendorUserVO.getUserId());
            if(ObjectUtil.isEmpty(sysUserInfoVO)){
                continue;
            }
            vendorUserVO.setSysUserInfoVO(sysUserInfoVO);
            vendorUserVO.setVendorVO(vendorVO);
            vendorUserVO.setUserNameVendorName(sysUserInfoVO.getName()+"-"+vendorVO.getName());
        }

        PropertyUtils.copyProperties(pageInfo, pageVO, vendorUserVOS);

        return pageVO;
    }

    @Override
    public void sendMailWithAllVendor() {
        List<SysUserInfoVO> sysUserInfoVOS = permService.queryAllVendorUser();
        if(CollectionUtil.isEmpty(sysUserInfoVOS)){
            return;
        }
        sysUserInfoVOS.forEach(sysUserInfoVO -> {
            //发送邮件给供应商
            ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
            extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.ONE));

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put("name", sysUserInfoVO.getName());
            objectMap.put("homepage", websiteVO.getVendorPlatFromName());
            objectMap.put("userName", sysUserInfoVO.getUserName());
            objectMap.put("password", ZtbConstant.INITIAL_PASSWORD);

            MailBeanInfo mailBeanInfo = new MailBeanInfo();
            mailBeanInfo.setMail(sysUserInfoVO.getMail());
            mailBeanInfo.setName(sysUserInfoVO.getName());

            extraMailNotificationInfoDTO.setExtra(objectMap);
            extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
            ieService.emailNotification(extraMailNotificationInfoDTO);
        });
    }
}
