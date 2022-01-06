package com.xdcplus.statement.mapper;

import com.xdcplus.statement.common.pojo.entity.OperationLog;
import com.xdcplus.statement.common.pojo.query.OperationLogQuery;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;

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
