package com.xdcplus.biz.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.xdcplus.biz.common.pojo.dto.VendorUserFilterDTO;
import com.xdcplus.biz.generator.impl.VendorUserBaseServiceImpl;
import com.xdcplus.biz.mapper.VendorUserMapper;
import com.xdcplus.biz.common.pojo.entity.VendorUser;
import com.xdcplus.biz.common.pojo.vo.VendorUserVO;
import com.xdcplus.biz.remote.service.PermService;
import com.xdcplus.biz.service.VendorUserService;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 供应商用户(VendorUser)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:28
 */
@Slf4j
@Service("vendorUserService")
public class VendorUserServiceImpl extends VendorUserBaseServiceImpl<VendorUser, VendorUserVO, VendorUser, VendorUserMapper> implements VendorUserService {

    @Autowired
    PermService permService;

    @Override
    public List<VendorUserVO> findVendorUserByVendorId(Long vendorId) {
        VendorUserFilterDTO vendorUserFilterDTO =new VendorUserFilterDTO();
        vendorUserFilterDTO.setVendorId(vendorId);
        List<VendorUserVO> vendorUserVOS = queryVendorUserVOList(vendorUserFilterDTO);
        if(CollectionUtil.isEmpty(vendorUserVOS)){
            return null;
        }
        vendorUserVOS.forEach(vendorUserVO -> {
            SysUserInfoVO sysUserInfoVO = permService.sysUserQueryById(vendorUserVO.getUserId());
            vendorUserVO.setSysUserInfoVO(sysUserInfoVO);
        });

        return vendorUserVOS;
    }
}
