package com.xdcplus.statement.generator;

import com.xdcplus.ztb.common.mp.service.BaseService;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.statement.common.pojo.entity.OperationLog;
import com.xdcplus.statement.common.pojo.dto.OperationLogDTO;
import com.xdcplus.statement.common.pojo.dto.OperationLogFilterDTO;
import com.xdcplus.statement.common.pojo.vo.OperationLogVO;

import java.util.List;

/**
 * (OperationLog)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-10-13 14:22:42
 */
public interface OperationLogBaseService<S, T, E> extends BaseService<OperationLog, OperationLogVO, OperationLog> {

    /**
     * 添加(OperationLog)
     *
     * @param operationLogDTO (OperationLogDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOperationLog(OperationLogDTO operationLogDTO);

    /**
     * 修改(OperationLog)
     *
     * @param operationLogDTO (OperationLogDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updateOperationLog(OperationLogDTO operationLogDTO);

    /**
     * 批量保存或更新(OperationLog)
     *
     * @param operationLogList (OperationLogList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatch(List<OperationLog> operationLogList);

    /**
     * 批量保存或更新(OperationLogDTOList)
     *
     * @param operationLogDTOList (OperationLogDTOList)
     * @return {@link Boolean} 是否成功
     */
    Boolean saveOrUpdateBatchByDTOList(List<OperationLogDTO> operationLogDTOList);

    /**
     * 删除(OperationLog)
     *
     * @param id (OperationLog)主键
     * @return {@link Boolean} 是否成功
     */
    Boolean deleteOperationLogLogical(Long id);

    /**
     * 查询(OperationLog)
     *
     * @param operationLogFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<OperationLogVO>} 状态信息
     */
    List<OperationLogVO> queryOperationLogVOList(OperationLogFilterDTO operationLogFilterDTO);

    /**
     * 查询(OperationLog)
     *
     * @param operationLogFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<OperationLogVO>} 状态信息
     */
    PageVO<OperationLogVO> queryOperationLog(OperationLogFilterDTO operationLogFilterDTO);

    /**
     * 查询一个
     *
     * @param id (OperationLog)主键
     * @return {@link OperationLogVO} (OperationLog)信息
     */
    OperationLogVO queryOperationLogById(Long id);


}
