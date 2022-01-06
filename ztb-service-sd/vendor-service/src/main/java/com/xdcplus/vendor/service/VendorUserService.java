package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.VendorFilterDTO;
import com.xdcplus.vendor.common.pojo.dto.VendorUserFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.VendorVO;
import com.xdcplus.vendor.generator.VendorUserBaseService;
import com.xdcplus.vendor.common.pojo.entity.VendorUser;
import com.xdcplus.vendor.common.pojo.vo.VendorUserVO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * (VendorUser)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-17 11:29:48
 */
public interface VendorUserService extends VendorUserBaseService<VendorUser, VendorUserVO, VendorUser> {

    /**
     * 查询用户供应商通过用户id
     *
     * @param userId 用户id
     * @return {@link VendorUserVO}
     */
    VendorUserVO findVendorUserByUserId(Long userId);

    List<VendorUserVO> findVendorUserByVendorId(Long vendorId);

    List<SysUserInfoVO> findSysUserInfoVOByVendorId(Long vendorId);

    List<VendorUserVO> findVendorUserWithVendor(VendorUserFilterDTO vendorUserFilterDTO);

    PageVO<VendorUserVO> queryVendorUserPageVO(VendorUserFilterDTO vendorUserFilterDTO);

    void sendMailWithAllVendor();

}
