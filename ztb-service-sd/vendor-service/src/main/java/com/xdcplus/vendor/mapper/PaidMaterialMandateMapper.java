package com.xdcplus.vendor.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterialMandate;
import com.xdcplus.vendor.common.pojo.query.PaidMaterialMandateQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 竞价品项授权记录(PaidMaterialMandate)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-17 11:49:29
 */
public interface PaidMaterialMandateMapper extends IBaseMapper<PaidMaterialMandate> {

    /**
     * 查询竞价品项授权记录(PaidMaterialMandate)
     *
     * @param paidMaterialMandateQuery 竞价品项授权记录(PaidMaterialMandate)查询
     * @return {@link List<PaidMaterialMandate>}
     */
    List<PaidMaterialMandate> queryPaidMaterialMandate(PaidMaterialMandateQuery paidMaterialMandateQuery);

}
