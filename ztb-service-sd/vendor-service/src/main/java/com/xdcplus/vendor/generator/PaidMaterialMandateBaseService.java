package com.xdcplus.vendor.generator;

import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateDTO;
import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialMandateVO;

import java.util.List;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:29
 */
public interface PaidMaterialMandateBaseService<S, T, E> extends BaseService<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate> {

    /**
     * 添加竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateDTO 竞价品项授权记录(PaidMaterialMandateDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean savePaidMaterialMandate(PaidMaterialMandateDTO paidMaterialMandateDTO);

    /**
     * 修改竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateDTO 竞价品项授权记录(PaidMaterialMandateDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updatePaidMaterialMandate(PaidMaterialMandateDTO paidMaterialMandateDTO);

    /**
     * 批量保存或更新竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateList 竞价品项授权记录(PaidMaterialMandateList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatch(List<PaidMaterialMandate> paidMaterialMandateList);

    /**
     * 批量保存或更新竞价品项授权记录(PaidMaterialMandateDTOList)
     *
     * @param paidMaterialMandateDTOList 竞价品项授权记录(PaidMaterialMandateDTOList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatchByDTOList(List<PaidMaterialMandateDTO> paidMaterialMandateDTOList);

    /**
     * 删除竞价品项授权记录(PaidMaterialMandate)
     *
     * @param id 竞价品项授权记录(PaidMaterialMandate)主键
     * @return {@link Boolean} 是否成功
     */
    Boolean deletePaidMaterialMandateLogical(Long id);

    /**
     * 查询竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<PaidMaterialMandateVO>} 状态信息
     */
    List<PaidMaterialMandateVO> queryPaidMaterialMandateVOList(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO);

    /**
     * 查询竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<PaidMaterialMandateVO>} 状态信息
     */
    PageVO<PaidMaterialMandateVO> queryPaidMaterialMandate(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO);

    /**
     * 查询一个
     *
     * @param id 竞价品项授权记录(PaidMaterialMandate)主键
     * @return {@link PaidMaterialMandateVO} 竞价品项授权记录(PaidMaterialMandate)信息
     */
    PaidMaterialMandateVO queryPaidMaterialMandateById(Long id);


}
