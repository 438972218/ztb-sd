package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.BidDetailDTO;
import com.xdcplus.biz.common.pojo.dto.BidDetailFilterDTO;
import com.xdcplus.biz.common.pojo.dto.BidDetailUpdateDTO;
import com.xdcplus.biz.common.pojo.dto.BidSheetDTO;
import com.xdcplus.biz.common.pojo.query.BidDetailQuery;
import com.xdcplus.biz.common.pojo.vo.BidDetailCollectVO;
import com.xdcplus.biz.generator.BidDetailBaseService;
import com.xdcplus.biz.common.pojo.entity.BidDetail;
import com.xdcplus.biz.common.pojo.vo.BidDetailVO;

import java.util.List;


/**
 * 招标单内容明细（报价须知、国内报价、国外报价）(BidDetail)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:15
 */
public interface BidDetailService extends BidDetailBaseService<BidDetail, BidDetailVO, BidDetail> {

    void batchUpdateBidDetailDTO(BidDetailUpdateDTO bidDetailUpdateDTO);

    int deleteByBidSheetId(Long bidSheetId);

    int deleteByBidSheetIdAndBidType(Long bidSheetId,String bidType);

    List<BidDetailCollectVO> queryBidDetailCollectVO(BidDetailFilterDTO bidDetailFilterDTO);
}
