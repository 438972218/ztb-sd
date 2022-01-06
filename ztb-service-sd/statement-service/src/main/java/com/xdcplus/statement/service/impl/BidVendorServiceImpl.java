package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.BidVendorBaseServiceImpl;
import com.xdcplus.statement.mapper.BidVendorMapper;
import com.xdcplus.statement.common.pojo.entity.BidVendor;
import com.xdcplus.statement.common.pojo.vo.*;
import com.xdcplus.statement.service.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


/**
 * 招标投标方(BidVendor)表服务实现类
 *
 * @author makejava
 * @since 2021-08-12 14:45:47
 */
@Slf4j
@Service("bidVendorService")
public class BidVendorServiceImpl extends BidVendorBaseServiceImpl<BidVendor, BidVendorVO, BidVendor, BidVendorMapper> implements BidVendorService {

}
