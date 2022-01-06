package com.xdcplus.biz.service.impl;

import com.xdcplus.biz.generator.impl.VendorAttachmentBaseServiceImpl;
import com.xdcplus.biz.mapper.VendorAttachmentMapper;
import com.xdcplus.biz.common.pojo.entity.VendorAttachment;
import com.xdcplus.biz.common.pojo.vo.VendorAttachmentVO;
import com.xdcplus.biz.service.VendorAttachmentService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 招标附件(VendorAttachment)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:05
 */
@Slf4j
@Service("vendorAttachmentService")
public class VendorAttachmentServiceImpl extends VendorAttachmentBaseServiceImpl<VendorAttachment, VendorAttachmentVO, VendorAttachment, VendorAttachmentMapper> implements VendorAttachmentService {


}
