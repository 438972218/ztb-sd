package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.VendorAttachment;
import com.xdcplus.statement.common.pojo.query.VendorAttachmentQuery;

import java.util.List;

/**
 * 招标附件(VendorAttachment)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:02
 */
public interface VendorAttachmentMapper extends IBaseMapper<VendorAttachment> {

    /**
     * 查询招标附件(VendorAttachment)
     *
     * @param vendorAttachmentQuery 招标附件(VendorAttachment)查询
     * @return {@link List<VendorAttachment>}
     */
    List<VendorAttachment> queryVendorAttachment(VendorAttachmentQuery vendorAttachmentQuery);

}
