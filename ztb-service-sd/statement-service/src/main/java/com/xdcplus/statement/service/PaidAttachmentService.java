package com.xdcplus.statement.service;

import com.xdcplus.statement.common.pojo.dto.PaidAttachmentFilterDTO;
import com.xdcplus.statement.generator.PaidAttachmentBaseService;
import com.xdcplus.statement.common.pojo.entity.PaidAttachment;
import com.xdcplus.statement.common.pojo.vo.PaidAttachmentVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 竞价单附件(PaidAttachment)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:01:57
 */
public interface PaidAttachmentService extends PaidAttachmentBaseService<PaidAttachment, PaidAttachmentVO, PaidAttachment> {

}
