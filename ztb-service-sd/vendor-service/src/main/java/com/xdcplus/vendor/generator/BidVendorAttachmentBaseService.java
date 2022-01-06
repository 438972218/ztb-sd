package com.xdcplus.vendor.generator;

import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.vendor.common.pojo.entity.BidVendorAttachment;
import com.xdcplus.vendor.common.pojo.dto.BidVendorAttachmentDTO;
import com.xdcplus.vendor.common.pojo.dto.BidVendorAttachmentFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.BidVendorAttachmentVO;

import java.util.List;

/**
 * 招标供应商附件(BidVendorAttachment)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-28 18:06:21
 */
public interface BidVendorAttachmentBaseService<S, T, E> extends BaseService<BidVendorAttachment, BidVendorAttachmentVO, BidVendorAttachment> {

    /**
     * 添加招标供应商附件(BidVendorAttachment)
     *
     * @param bidVendorAttachmentDTO 招标供应商附件(BidVendorAttachmentDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveBidVendorAttachment(BidVendorAttachmentDTO bidVendorAttachmentDTO);

    /**
     * 修改招标供应商附件(BidVendorAttachment)
     *
     * @param bidVendorAttachmentDTO 招标供应商附件(BidVendorAttachmentDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updateBidVendorAttachment(BidVendorAttachmentDTO bidVendorAttachmentDTO);

    /**
     * 批量保存或更新招标供应商附件(BidVendorAttachment)
     *
     * @param bidVendorAttachmentList 招标供应商附件(BidVendorAttachmentList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatch(List<BidVendorAttachment> bidVendorAttachmentList);

    /**
     * 批量保存或更新招标供应商附件(BidVendorAttachmentDTOList)
     *
     * @param bidVendorAttachmentDTOList 招标供应商附件(BidVendorAttachmentDTOList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatchByDTOList(List<BidVendorAttachmentDTO> bidVendorAttachmentDTOList);

    /**
     * 删除招标供应商附件(BidVendorAttachment)
     *
     * @param id 招标供应商附件(BidVendorAttachment)主键
     * @return {@link Boolean} 是否成功
     */
    Boolean deleteBidVendorAttachmentLogical(Long id);

    /**
     * 查询招标供应商附件(BidVendorAttachment)
     *
     * @param bidVendorAttachmentFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<BidVendorAttachmentVO>} 状态信息
     */
    List<BidVendorAttachmentVO> queryBidVendorAttachmentVOList(BidVendorAttachmentFilterDTO bidVendorAttachmentFilterDTO);

    /**
     * 查询招标供应商附件(BidVendorAttachment)
     *
     * @param bidVendorAttachmentFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<BidVendorAttachmentVO>} 状态信息
     */
    PageVO<BidVendorAttachmentVO> queryBidVendorAttachment(BidVendorAttachmentFilterDTO bidVendorAttachmentFilterDTO);

    /**
     * 查询一个
     *
     * @param id 招标供应商附件(BidVendorAttachment)主键
     * @return {@link BidVendorAttachmentVO} 招标供应商附件(BidVendorAttachment)信息
     */
    BidVendorAttachmentVO queryBidVendorAttachmentById(Long id);


}
