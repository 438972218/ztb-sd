package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.VendorUserBaseServiceImpl;
import com.xdcplus.statement.mapper.VendorUserMapper;
import com.xdcplus.statement.common.pojo.entity.VendorUser;
import com.xdcplus.statement.common.pojo.vo.VendorUserVO;
import com.xdcplus.statement.service.VendorUserService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 供应商用户(VendorUser)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:28
 */
@Slf4j
@Service("vendorUserService")
public class VendorUserServiceImpl extends VendorUserBaseServiceImpl<VendorUser, VendorUserVO, VendorUser, VendorUserMapper> implements VendorUserService {

}
