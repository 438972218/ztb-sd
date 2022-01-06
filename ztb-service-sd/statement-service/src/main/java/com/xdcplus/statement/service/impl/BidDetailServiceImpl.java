package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.BidDetailBaseServiceImpl;
import com.xdcplus.statement.mapper.BidDetailMapper;
import com.xdcplus.statement.common.pojo.entity.BidDetail;
import com.xdcplus.statement.common.pojo.vo.BidDetailVO;
import com.xdcplus.statement.service.BidDetailService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


/**
 * 招标单内容明细（报价须知、国内报价、国外报价）(BidDetail)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:15
 */
@Slf4j
@Service("bidDetailService")
public class BidDetailServiceImpl extends BidDetailBaseServiceImpl<BidDetail, BidDetailVO, BidDetail, BidDetailMapper> implements BidDetailService {

}
