package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.PaidMaterialMandateDTO;
import com.xdcplus.vendor.generator.PaidMaterialMandateBaseService;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialMandateVO;


/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:29
 */
public interface PaidMaterialMandateService extends PaidMaterialMandateBaseService<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate> {

    /**
     * 添加竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateDTO 竞价品项授权记录(PaidMaterialMandateDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean savePaidMaterialMandateWithUser(PaidMaterialMandateDTO paidMaterialMandateDTO);

}
