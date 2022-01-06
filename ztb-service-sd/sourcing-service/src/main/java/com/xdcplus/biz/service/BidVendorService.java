package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.BidSheetDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorFilterDTO;
import com.xdcplus.biz.generator.BidVendorBaseService;
import com.xdcplus.biz.common.pojo.entity.BidVendor;
import com.xdcplus.biz.common.pojo.vo.BidVendorVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 招标投标方(BidVendor)表服务接口层
 *
 * @author makejava
 * @since 2021-08-12 14:45:47
 */
public interface BidVendorService extends BidVendorBaseService<BidVendor, BidVendorVO, BidVendor> {

    /**
     * 添加招标投标方并新增VendorDetail(BidVendor)
     *
     * @param bidVendorDTO 招标投标方(BidVendorDTO)
     * @return {@link Boolean} 是否成功
     */
    BidVendorVO saveBidVendorWithDetailReturnVO(BidVendorDTO bidVendorDTO);

    /**
     * 保存投标供应商(存入默认值)
     *
     * @param bidVendorDTO 投标供应商dto
     * @return {@link Boolean}
     */
    BidVendorVO saveBidVendorWithDefaultReturnVO(BidVendorDTO bidVendorDTO);

    /**
     * 退回投标供应商
     *
     */
    void returnBidVendor(BidSheetDTO bidSheetDTO);

    /**
     * 查询招标投标方WithVendor(BidVendor)
     *
     * @param bidVendorFilterDTO 过程状态过滤DTO
     * @return {@link PageVO <BidVendorVO>} 状态信息
     */
    PageVO<BidVendorVO> queryBidVendorWithVendor(BidVendorFilterDTO bidVendorFilterDTO);

    /**
     * 招标放弃授权
     *
     * @return {@link PageVO <BidVendorVO>} 状态信息
     */
    PageVO<BidVendorVO> waiveAuth(Long bidSheetId);

}
