package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.BidAttachmentBaseServiceImpl;
import com.xdcplus.statement.mapper.BidAttachmentMapper;
import com.xdcplus.statement.common.pojo.entity.BidAttachment;
import com.xdcplus.statement.common.pojo.vo.BidAttachmentVO;
import com.xdcplus.statement.service.BidAttachmentService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 招标附件(BidAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:13
 */
@Slf4j
@Service("bidAttachmentService")
public class BidAttachmentServiceImpl extends BidAttachmentBaseServiceImpl<BidAttachment, BidAttachmentVO, BidAttachment, BidAttachmentMapper> implements BidAttachmentService {


}
