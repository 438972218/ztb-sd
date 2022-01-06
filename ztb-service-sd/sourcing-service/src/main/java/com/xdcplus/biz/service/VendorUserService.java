package com.xdcplus.biz.service;

import com.xdcplus.biz.generator.VendorUserBaseService;
import com.xdcplus.biz.common.pojo.entity.VendorUser;
import com.xdcplus.biz.common.pojo.vo.VendorUserVO;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;

import java.util.List;


/**
 * 供应商用户(VendorUser)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:28
 */
public interface VendorUserService extends VendorUserBaseService<VendorUser, VendorUserVO, VendorUser> {

    List<VendorUserVO> findVendorUserByVendorId(Long vendorId);

}
