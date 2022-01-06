package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.OperationLogBaseServiceImpl;
import com.xdcplus.statement.mapper.OperationLogMapper;
import com.xdcplus.statement.common.pojo.entity.OperationLog;
import com.xdcplus.statement.common.pojo.vo.OperationLogVO;
import com.xdcplus.statement.service.OperationLogService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * (OperationLog)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-10-13 14:22:42
 */
@Slf4j
@Service("operationLogService")
public class OperationLogServiceImpl extends OperationLogBaseServiceImpl<OperationLog, OperationLogVO, OperationLog, OperationLogMapper> implements OperationLogService {


}
