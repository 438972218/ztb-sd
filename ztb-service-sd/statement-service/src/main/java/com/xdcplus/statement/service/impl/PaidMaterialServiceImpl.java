package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.PaidMaterialBaseServiceImpl;
import com.xdcplus.statement.mapper.PaidMaterialMapper;
import com.xdcplus.statement.common.pojo.entity.PaidMaterial;
import com.xdcplus.statement.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.statement.service.PaidMaterialService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价物品(PaidMaterial)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-23 14:11:11
 */
@Slf4j
@Service("paidMaterialService")
public class PaidMaterialServiceImpl extends PaidMaterialBaseServiceImpl<PaidMaterial, PaidMaterialVO, PaidMaterial, PaidMaterialMapper> implements PaidMaterialService {

}
