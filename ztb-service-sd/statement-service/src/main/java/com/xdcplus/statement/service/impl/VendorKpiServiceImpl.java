package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.common.pojo.entity.VendorKpi;
import com.xdcplus.statement.common.pojo.vo.VendorKpiVO;
import com.xdcplus.statement.generator.impl.VendorKpiBaseServiceImpl;
import com.xdcplus.statement.mapper.VendorKpiMapper;
import com.xdcplus.statement.service.VendorKpiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 供应商绩效考核表(VendorKpi)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:18
 */
@Slf4j
@Service("vendorKpiService")
public class VendorKpiServiceImpl extends VendorKpiBaseServiceImpl<VendorKpi, VendorKpiVO, VendorKpi, VendorKpiMapper> implements VendorKpiService {

}
