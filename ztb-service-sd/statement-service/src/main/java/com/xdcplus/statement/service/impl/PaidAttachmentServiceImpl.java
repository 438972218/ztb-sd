package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.PaidAttachmentBaseServiceImpl;
import com.xdcplus.statement.mapper.PaidAttachmentMapper;
import com.xdcplus.statement.common.pojo.entity.PaidAttachment;
import com.xdcplus.statement.common.pojo.vo.PaidAttachmentVO;
import com.xdcplus.statement.service.PaidAttachmentService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价单附件(PaidAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:01:57
 */
@Slf4j
@Service("paidAttachmentService")
public class PaidAttachmentServiceImpl extends PaidAttachmentBaseServiceImpl<PaidAttachment, PaidAttachmentVO, PaidAttachment, PaidAttachmentMapper> implements PaidAttachmentService {

}
