package com.xdcplus.vendorperm.perm;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.xdcplus.vendorperm.common.pojo.vo.sysuser.SysUserVo;
import com.xdcplus.vendorperm.service.SysUserService;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class PermissionApplicationTests {

    @Autowired
    SysUserService sysUserService;

    @Test
    void contextLoads() {
    }

    @Test
    void generatePassword() throws Exception {
        List<SysUserVo> sysUserVos = sysUserService.getAllSysUserVo();
        List<String> userNames=sysUserVos.stream().map(SysUserVo::getUserName).collect(Collectors.toList());
        List<UserPasswordVO> userPasswordVOS= CollectionUtil.newArrayList();
        for (SysUserVo sysUserVo : sysUserVos) {
//            String md5Str = SecureUtil.md5(sysUserVo.getUserName());
            String encode = Base64.encode(sysUserVo.getUserName());
            userPasswordVOS.add(
                    UserPasswordVO.builder().userName(sysUserVo.getUserName())
                            .password(encode).encryptPassword(DigestUtil.bcrypt(encode)).build());
        }
        File file = new File("D:\\文件\\新建文件夹\\vendor.xls");
//        ExcelUtils.exportExcel(userPasswordVOS,file);
//        ExportExcelUtil.export(response, UserPasswordVO.class, "vendor", userPasswordVOS);
        ExportExcelUtil.exportByFile(file, UserPasswordVO.class, "vendor", userPasswordVOS);
        System.out.println("");
    }
}
