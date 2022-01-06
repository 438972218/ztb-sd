package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.PaidAttachment;
import com.xdcplus.statement.common.pojo.query.PaidAttachmentQuery;

import java.util.List;

/**
 * 竞价单附件(PaidAttachment)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-08-30 13:40:31
 */
public interface PaidAttachmentMapper extends IBaseMapper<PaidAttachment> {

    /**
     * 查询竞价单附件(PaidAttachment)
     *
     * @param paidAttachmentQuery 竞价单附件(PaidAttachment)查询
     * @return {@link List<PaidAttachment>}
     */
    List<PaidAttachment> queryPaidAttachment(PaidAttachmentQuery paidAttachmentQuery);

}
