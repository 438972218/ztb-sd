package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.PaidMaterialMandateBaseServiceImpl;
import com.xdcplus.statement.mapper.PaidMaterialMandateMapper;
import com.xdcplus.statement.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.statement.common.pojo.vo.PaidMaterialMandateVO;
import com.xdcplus.statement.service.PaidMaterialMandateService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:45:32
 */
@Slf4j
@Service("paidMaterialMandateService")
public class PaidMaterialMandateServiceImpl extends PaidMaterialMandateBaseServiceImpl<PaidMaterialMandate, PaidMaterialMandateVO, PaidMaterialMandate, PaidMaterialMandateMapper> implements PaidMaterialMandateService {

}
