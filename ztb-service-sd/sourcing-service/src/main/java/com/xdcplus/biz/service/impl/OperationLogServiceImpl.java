package com.xdcplus.biz.service.impl;

import com.xdcplus.biz.generator.impl.OperationLogBaseServiceImpl;
import com.xdcplus.biz.mapper.OperationLogMapper;
import com.xdcplus.biz.common.pojo.entity.OperationLog;
import com.xdcplus.biz.common.pojo.vo.OperationLogVO;
import com.xdcplus.biz.service.OperationLogService;
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
