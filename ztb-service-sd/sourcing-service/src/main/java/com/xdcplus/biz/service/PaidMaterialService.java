package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.PaidMaterialDTO;
import com.xdcplus.biz.common.pojo.dto.PaidSheetDTO;
import com.xdcplus.biz.common.pojo.vo.PaidSheetVO;
import com.xdcplus.biz.generator.PaidMaterialBaseService;
import com.xdcplus.biz.common.pojo.entity.PaidMaterial;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialVO;

import java.util.List;


/**
 * 竞价物品(PaidMaterial)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-23 14:11:11
 */
public interface PaidMaterialService extends PaidMaterialBaseService<PaidMaterial, PaidMaterialVO, PaidMaterial> {

    /**
     * 批量保存支付材料
     *
     * @param paidMaterialDTOS 支付材料dto
     * @return {@link Boolean}
     */
    void batchSavePaidMaterial(List<PaidMaterialDTO> paidMaterialDTOS);

    List<PaidMaterialVO> queryPaidMaterialVOBySheetId(Long id);

    void deleteBySheetId(Long id);

    PaidMaterialVO savePaidMaterialReturnVO(PaidMaterialDTO paidMaterialDTO);

    void savePaidMaterialWithChild(PaidMaterialDTO paidMaterialDTO);

    /**
     * 根据表单id查询授权的品项
     *
     * @param requestId    表单标识
     * @return {@link List<PaidMaterialVO>}
     */
    List<PaidMaterialVO> getMandateByRequestId(Long requestId);

    /**
     * 查询支付材料voby请求id
     *
     * @param id id
     * @return {@link List<PaidMaterialVO>}
     */
    List<PaidMaterialVO> queryPaidMaterialVOByRequestId(Long id);



}
