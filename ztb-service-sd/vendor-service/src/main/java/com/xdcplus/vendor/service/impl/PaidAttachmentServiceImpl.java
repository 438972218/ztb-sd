package com.xdcplus.vendor.service.impl;

import com.xdcplus.vendor.generator.impl.PaidAttachmentBaseServiceImpl;
import com.xdcplus.vendor.mapper.PaidAttachmentMapper;
import com.xdcplus.vendor.common.pojo.entity.PaidAttachment;
import com.xdcplus.vendor.common.pojo.vo.PaidAttachmentVO;
import com.xdcplus.vendor.service.PaidAttachmentService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价单附件(PaidAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-03 09:02:27
 */
@Slf4j
@Service("paidAttachmentService")
public class PaidAttachmentServiceImpl extends PaidAttachmentBaseServiceImpl<PaidAttachment, PaidAttachmentVO, PaidAttachment, PaidAttachmentMapper> implements PaidAttachmentService {


}
