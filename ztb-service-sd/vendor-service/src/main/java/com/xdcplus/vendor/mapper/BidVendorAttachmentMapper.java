package com.xdcplus.vendor.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.vendor.common.pojo.entity.BidVendorAttachment;
import com.xdcplus.vendor.common.pojo.query.BidVendorAttachmentQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 招标供应商附件(BidVendorAttachment)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:20
 */
public interface BidVendorAttachmentMapper extends IBaseMapper<BidVendorAttachment> {

    /**
     * 查询招标供应商附件(BidVendorAttachment)
     *
     * @param bidVendorAttachmentQuery 招标供应商附件(BidVendorAttachment)查询
     * @return {@link List<BidVendorAttachment>}
     */
    List<BidVendorAttachment> queryBidVendorAttachment(BidVendorAttachmentQuery bidVendorAttachmentQuery);

}
