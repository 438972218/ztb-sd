package com.xdcplus.vendor;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xdcplus.vendor.common.pojo.entity.Vendor;
import com.xdcplus.vendor.mapper.VendorMapper;
import com.xdcplus.vendor.remote.service.PermService;
import com.xdcplus.vendor.service.VendorService;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VendorApplicationTest {
//    @Autowired
//    public VendorSiteInsService vendorSiteInsService;
//    @Autowired
//    public VendorBaseService vendorService;

    @Autowired
    PermService sysUserService;

    @Autowired
    VendorService vendorService;

    @Autowired
    VendorMapper vendorMapper;

    @Test
    void contextLoads() {
    }


    @Test
    void testQueryCompanyById() {
//        CompanyVO companyVO = PceCompanyResponseUtils.queryById(1L);
//        System.out.println("aa");
    }

    @Test
    void test2() {
////        vendorSiteInsService.calculateScore(1L);
//        Vendor vendor = new Vendor();
//        vendor.setId(null);
//        vendor.setStatus("已过审");
//        vendorService.saveOrUpdate(vendor);
//    }
//    //    @LoadBalanced
////    public RestTemplate restTemplage() {
////        return new RestTemplate();
////    }
    }


    @Test
    void generatePassword() throws Exception {
        List<SysUserInfoVO> sysUserVos = sysUserService.queryAllVendorUser();
        List<UserPasswordVO> userPasswordVOS= CollectionUtil.newArrayList();
        for (SysUserInfoVO sysUserInfoVO : sysUserVos) {
//            String md5Str = SecureUtil.md5(sysUserVo.getUserName());
            String encode = Base64.encode(sysUserInfoVO.getUserName());
            Vendor vendor = vendorMapper.queryVendorsByUserId(sysUserInfoVO.getId());
            if(ObjectUtil.isNotEmpty(vendor)){
                userPasswordVOS.add(
                        UserPasswordVO.builder().userName(sysUserInfoVO.getUserName())
                                .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).vendorName(vendor.getName()).build());
            }else{
                userPasswordVOS.add(
                        UserPasswordVO.builder().userName(sysUserInfoVO.getUserName())
                                .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).build());

            }
        }
        File file = new File("D:\\文件\\新建文件夹\\vendor.xls");
//        ExcelUtils.exportExcel(userPasswordVOS,file);
//        ExportExcelUtil.export(response, UserPasswordVO.class, "vendor", userPasswordVOS);
        ExportExcelUtil.exportByFile(file, UserPasswordVO.class, "vendor", userPasswordVOS);
        System.out.println("");
    }

    @Test
    void generatePassword1() throws Exception {
        List<SysUserInfoVO> sysUserVos = sysUserService.queryAllVendorUser();
        List<UserPasswordVO> userPasswordVOS= CollectionUtil.newArrayList();
        for (SysUserInfoVO sysUserInfoVO : sysUserVos) {
//            String md5Str = SecureUtil.md5(sysUserVo.getUserName());
            String encode = Base64.encode(sysUserInfoVO.getUserName());
            Vendor vendor = vendorMapper.queryVendorsByUserId(sysUserInfoVO.getId());
            if(ObjectUtil.isNotEmpty(vendor)){
                userPasswordVOS.add(
                        UserPasswordVO.builder().userName(sysUserInfoVO.getUserName())
                                .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).vendorName(vendor.getName()).build());
            }else{
                userPasswordVOS.add(
                        UserPasswordVO.builder().userName(sysUserInfoVO.getUserName())
                                .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).build());
            }
        }
        File file = new File("D:\\文件\\新建文件夹\\vendor.xls");
//        ExcelUtils.exportExcel(userPasswordVOS,file);
//        ExportExcelUtil.export(response, UserPasswordVO.class, "vendor", userPasswordVOS);
        ExportExcelUtil.exportByFile(file, UserPasswordVO.class, "vendor", userPasswordVOS);
        System.out.println("");
    }

    @Test
    void generatePassword2() throws Exception {
        List<SysUserInfoVO> sysUserVos = sysUserService.queryAllVendorUser();
        List<UserPasswordVO> userPasswordVOS= CollectionUtil.newArrayList();
        for (SysUserInfoVO sysUserInfoVO : sysUserVos) {
//            String md5Str = SecureUtil.md5(sysUserVo.getUserName());
            String encode = Base64.encode(sysUserInfoVO.getUserName());
            Vendor vendor = vendorMapper.queryVendorsByUserId(sysUserInfoVO.getId());
            if(ObjectUtil.isNotEmpty(vendor)){
                userPasswordVOS.add(
                        UserPasswordVO.builder().userName(sysUserInfoVO.getUserName())
                                .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).vendorName(vendor.getName()).build());
            }else{
                userPasswordVOS.add(
                        UserPasswordVO.builder().userName(sysUserInfoVO.getUserName())
                                .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).build());
            }
        }
        File file = new File("D:\\文件\\新建文件夹\\vendor.xls");
//        ExcelUtils.exportExcel(userPasswordVOS,file);
//        ExportExcelUtil.export(response, UserPasswordVO.class, "vendor", userPasswordVOS);
        ExportExcelUtil.exportByFile(file, UserPasswordVO.class, "vendor", userPasswordVOS);
        System.out.println("");
    }

}
