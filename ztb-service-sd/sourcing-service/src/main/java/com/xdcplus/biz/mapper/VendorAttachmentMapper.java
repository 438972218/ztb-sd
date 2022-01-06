package com.xdcplus.biz.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.VendorAttachment;
import com.xdcplus.biz.common.pojo.query.VendorAttachmentQuery;
import org.apache.ibatis.annotations.Param;

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
