package com.xdcplus.statement.service;

import com.xdcplus.statement.common.pojo.dto.PaidMaterialDTO;
import com.xdcplus.statement.generator.PaidMaterialBaseService;
import com.xdcplus.statement.common.pojo.entity.PaidMaterial;
import com.xdcplus.statement.common.pojo.vo.PaidMaterialVO;

import java.util.List;


/**
 * 竞价物品(PaidMaterial)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-23 14:11:11
 */
public interface PaidMaterialService extends PaidMaterialBaseService<PaidMaterial, PaidMaterialVO, PaidMaterial> {

}
