package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.VendorKpiDetailDTO;
import com.xdcplus.biz.generator.VendorKpiDetailBaseService;
import com.xdcplus.biz.common.pojo.entity.VendorKpiDetail;
import com.xdcplus.biz.common.pojo.vo.VendorKpiDetailVO;

import java.util.List;


/**
 * 供应商绩效考核表明细(VendorKpiDetail)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:21
 */
public interface VendorKpiDetailService extends VendorKpiDetailBaseService<VendorKpiDetail, VendorKpiDetailVO, VendorKpiDetail> {

    /**
     * 删除绩效考核表细节
     *
     * @param id
     * @return {@link int}
     */
    int deleteVendorKpiDetailByKpiId(Long id);

    void insertListWithChild(List<VendorKpiDetailDTO> vendorKpiDetailDTOS,Long vendorKpiId);

    /**
     * 添加供应商绩效考核表明细(VendorKpiDetail)
     *
     * @param vendorKpiDetailDTO 供应商绩效考核表明细(VendorKpiDetailDTO)
     * @return {@link Boolean} 是否成功
     */
    VendorKpiDetailVO saveVendorKpiDetailReturnVO(VendorKpiDetailDTO vendorKpiDetailDTO);

    /**
     * 儿童通过kpi id列表
     *
     * @param vendorKpiId 供应商kpi id
     * @return {@link List<VendorKpiDetailVO>}
     */
    List<VendorKpiDetailVO> listWithChildByKpiId(Long vendorKpiId);
}
