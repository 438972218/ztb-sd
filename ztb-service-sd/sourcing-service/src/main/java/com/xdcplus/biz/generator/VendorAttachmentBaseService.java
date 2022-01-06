package com.xdcplus.biz.generator;

import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.biz.common.pojo.entity.VendorAttachment;
import com.xdcplus.biz.common.pojo.dto.VendorAttachmentDTO;
import com.xdcplus.biz.common.pojo.dto.VendorAttachmentFilterDTO;
import com.xdcplus.biz.common.pojo.vo.VendorAttachmentVO;

import java.util.List;

/**
 * 招标附件(VendorAttachment)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-03 10:10:03
 */
public interface VendorAttachmentBaseService<S, T, E> extends BaseService<VendorAttachment, VendorAttachmentVO, VendorAttachment> {

    /**
     * 添加招标附件(VendorAttachment)
     *
     * @param vendorAttachmentDTO 招标附件(VendorAttachmentDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveVendorAttachment(VendorAttachmentDTO vendorAttachmentDTO);

    /**
     * 修改招标附件(VendorAttachment)
     *
     * @param vendorAttachmentDTO 招标附件(VendorAttachmentDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updateVendorAttachment(VendorAttachmentDTO vendorAttachmentDTO);

    /**
     * 批量保存或更新招标附件(VendorAttachment)
     *
     * @param vendorAttachmentList 招标附件(VendorAttachmentList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatch(List<VendorAttachment> vendorAttachmentList);

    /**
     * 批量保存或更新招标附件(VendorAttachmentDTOList)
     *
     * @param vendorAttachmentDTOList 招标附件(VendorAttachmentDTOList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatchByDTOList(List<VendorAttachmentDTO> vendorAttachmentDTOList);

    /**
     * 删除招标附件(VendorAttachment)
     *
     * @param id 招标附件(VendorAttachment)主键
     * @return {@link Boolean} 是否成功
     */
    Boolean deleteVendorAttachmentLogical(Long id);

    /**
     * 查询招标附件(VendorAttachment)
     *
     * @param vendorAttachmentFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<VendorAttachmentVO>} 状态信息
     */
    List<VendorAttachmentVO> queryVendorAttachmentVOList(VendorAttachmentFilterDTO vendorAttachmentFilterDTO);

    /**
     * 查询招标附件(VendorAttachment)
     *
     * @param vendorAttachmentFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<VendorAttachmentVO>} 状态信息
     */
    PageVO<VendorAttachmentVO> queryVendorAttachment(VendorAttachmentFilterDTO vendorAttachmentFilterDTO);

    /**
     * 查询一个
     *
     * @param id 招标附件(VendorAttachment)主键
     * @return {@link VendorAttachmentVO} 招标附件(VendorAttachment)信息
     */
    VendorAttachmentVO queryVendorAttachmentById(Long id);


}
