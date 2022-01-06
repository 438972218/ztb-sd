package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.BidVendorDetailBaseServiceImpl;
import com.xdcplus.statement.mapper.BidVendorDetailMapper;
import com.xdcplus.statement.common.pojo.entity.BidVendorDetail;
import com.xdcplus.statement.service.BidVendorDetailService;
import com.xdcplus.statement.common.pojo.vo.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:42
 */
@Slf4j
@Service("bidVendorDetailService")
public class BidVendorDetailServiceImpl extends BidVendorDetailBaseServiceImpl<BidVendorDetail, BidVendorDetailVO, BidVendorDetail, BidVendorDetailMapper> implements BidVendorDetailService {

}
