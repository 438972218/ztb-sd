package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.PaidMaterial;
import com.xdcplus.statement.common.pojo.query.PaidMaterialQuery;

import java.util.List;

/**
 * 竞价品项(PaidMaterial)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-06 14:56:14
 */
public interface PaidMaterialMapper extends IBaseMapper<PaidMaterial> {

    /**
     * 查询竞价品项(PaidMaterial)
     *
     * @param paidMaterialQuery 竞价品项(PaidMaterial)查询
     * @return {@link List<PaidMaterial>}
     */
    List<PaidMaterial> queryPaidMaterial(PaidMaterialQuery paidMaterialQuery);

}
