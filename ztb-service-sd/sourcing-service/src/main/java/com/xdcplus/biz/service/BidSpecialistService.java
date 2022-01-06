package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.BidSpecialistDTO;
import com.xdcplus.biz.common.pojo.dto.BidSpecialistFilterDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDTO;
import com.xdcplus.biz.common.pojo.vo.BidVendorVO;
import com.xdcplus.biz.generator.BidSpecialistBaseService;
import com.xdcplus.biz.common.pojo.entity.BidSpecialist;
import com.xdcplus.biz.common.pojo.vo.BidSpecialistVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 专家(BidSpecialist)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-24 16:23:00
 */
public interface BidSpecialistService extends BidSpecialistBaseService<BidSpecialist, BidSpecialistVO, BidSpecialist> {

    /**
     * 查询专家(BidSpecialist)
     *
     * @param bidSpecialistFilterDTO 过程状态过滤DTO
     * @return {@link PageVO <BidSpecialistVO>} 状态信息
     */
    PageVO<BidSpecialistVO> queryBidSpecialistWithUser(BidSpecialistFilterDTO bidSpecialistFilterDTO);

    BidSpecialistVO saveBidSpecialistReturnVO(BidSpecialistDTO bidSpecialistDTO);

}
