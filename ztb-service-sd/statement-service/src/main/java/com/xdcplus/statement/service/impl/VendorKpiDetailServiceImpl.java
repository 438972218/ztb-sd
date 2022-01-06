package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.VendorKpiDetailBaseServiceImpl;
import com.xdcplus.statement.mapper.VendorKpiDetailMapper;
import com.xdcplus.statement.common.pojo.entity.VendorKpiDetail;
import com.xdcplus.statement.common.pojo.vo.VendorKpiDetailVO;
import com.xdcplus.statement.service.VendorKpiDetailService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 供应商绩效考核表明细(VendorKpiDetail)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:21
 */
@Slf4j
@Service("vendorKpiDetailService")
public class VendorKpiDetailServiceImpl extends VendorKpiDetailBaseServiceImpl<VendorKpiDetail, VendorKpiDetailVO, VendorKpiDetail, VendorKpiDetailMapper> implements VendorKpiDetailService {

}
