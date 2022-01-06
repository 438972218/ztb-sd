package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.biz.generator.PaidMaterialMandateBaseService;
import com.xdcplus.biz.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.biz.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:45:32
 */
public interface PaidMaterialMandateService extends PaidMaterialMandateBaseService<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate> {

    /**
     * 查询竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateFilterDTO 过程状态过滤DTO
     * @return {@link PageVO <PaidMaterialMandateVO>} 状态信息
     */
    List<PaidMaterialMandateVO> queryPaidMaterialMandateVOListWithVendor(PaidMaterialMandateFilterDTO paidMaterialMandateFilterDTO);


}
