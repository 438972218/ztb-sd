package com.xdcplus.biz.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.biz.common.pojo.entity.OperationLog;
import com.xdcplus.biz.common.pojo.query.OperationLogQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (OperationLog)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-10-19 09:59:04
 */
public interface OperationLogMapper extends IBaseMapper<OperationLog> {

    /**
     * 查询(OperationLog)
     *
     * @param operationLogQuery (OperationLog)查询
     * @return {@link List<OperationLog>}
     */
    List<OperationLog> queryOperationLog(OperationLogQuery operationLogQuery);

}
