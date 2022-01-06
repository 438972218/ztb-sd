package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.BidVendorAttachmentFilterDTO;
import com.xdcplus.vendor.generator.BidVendorAttachmentBaseService;
import com.xdcplus.vendor.common.pojo.entity.BidVendorAttachment;
import com.xdcplus.vendor.common.pojo.vo.BidVendorAttachmentVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 招标供应商附件(BidVendorAttachment)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:22
 */
public interface BidVendorAttachmentService extends BidVendorAttachmentBaseService<BidVendorAttachment, BidVendorAttachmentVO, BidVendorAttachment> {

    PageVO<BidVendorAttachmentVO> queryBidVendorAttachmentWithName(BidVendorAttachmentFilterDTO bidVendorAttachmentFilterDTO);

}
