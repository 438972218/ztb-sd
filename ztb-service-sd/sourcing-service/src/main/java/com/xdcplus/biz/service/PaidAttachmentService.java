package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.PaidAttachmentFilterDTO;
import com.xdcplus.biz.generator.PaidAttachmentBaseService;
import com.xdcplus.biz.common.pojo.entity.PaidAttachment;
import com.xdcplus.biz.common.pojo.vo.PaidAttachmentVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;


/**
 * 竞价单附件(PaidAttachment)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:01:57
 */
public interface PaidAttachmentService extends PaidAttachmentBaseService<PaidAttachment, PaidAttachmentVO, PaidAttachment> {

    /**
     * 查询竞价单附件(PaidAttachment)
     *
     * @param paidAttachmentFilterDTO 过程状态过滤DTO
     * @return {@link PageVO <PaidAttachmentVO>} 状态信息
     */
    PageVO<PaidAttachmentVO> queryPaidAttachmentWithUser(PaidAttachmentFilterDTO paidAttachmentFilterDTO);

}
