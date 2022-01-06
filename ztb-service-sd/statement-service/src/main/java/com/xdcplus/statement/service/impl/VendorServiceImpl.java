package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.VendorBaseServiceImpl;
import com.xdcplus.statement.mapper.VendorMapper;
import com.xdcplus.statement.common.pojo.entity.Vendor;
import com.xdcplus.statement.common.pojo.vo.VendorVO;
import com.xdcplus.statement.service.VendorService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 供应商(Vendor)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-12 15:51:15
 */
@Slf4j
@Service("vendorService")
public class VendorServiceImpl extends VendorBaseServiceImpl<Vendor, VendorVO, Vendor, VendorMapper> implements VendorService {

}
