package com.xdcplus.statement.mapper;

import com.xdcplus.statement.common.pojo.entity.BidAttachment;
import com.xdcplus.statement.common.pojo.query.BidAttachmentQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;

import java.util.List;

/**
 * 招标附件(BidAttachment)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-08-25 18:03:32
 */
public interface BidAttachmentMapper extends IBaseMapper<BidAttachment> {

    /**
     * 查询招标附件(BidAttachment)
     *
     * @param bidAttachmentQuery 招标附件(BidAttachment)查询
     * @return {@link List<BidAttachment>}
     */
    List<BidAttachment> queryBidAttachment(BidAttachmentQuery bidAttachmentQuery);

}
