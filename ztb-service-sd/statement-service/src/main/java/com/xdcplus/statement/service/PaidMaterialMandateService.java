package com.xdcplus.statement.service;

import com.xdcplus.statement.common.pojo.dto.PaidMaterialMandateFilterDTO;
import com.xdcplus.statement.generator.PaidMaterialMandateBaseService;
import com.xdcplus.statement.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.statement.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:45:32
 */
public interface PaidMaterialMandateService extends PaidMaterialMandateBaseService<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate> {

}
