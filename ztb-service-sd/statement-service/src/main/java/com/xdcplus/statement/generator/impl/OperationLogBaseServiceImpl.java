package com.xdcplus.statement.generator.impl;

import com.xdcplus.statement.common.pojo.dto.OperationLogDTO;
import com.xdcplus.statement.common.pojo.dto.OperationLogFilterDTO;
import com.xdcplus.statement.common.pojo.entity.OperationLog;
import com.xdcplus.statement.common.pojo.query.OperationLogQuery;
import com.xdcplus.statement.common.pojo.vo.OperationLogVO;
import com.xdcplus.statement.generator.OperationLogBaseService;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.statement.mapper.OperationLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import com.xdcplus.ztb.common.tool.utils.BeanUtils;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;
import com.xdcplus.ztb.common.tool.utils.PageableUtils;
import com.xdcplus.ztb.common.tool.utils.PropertyUtils;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * (OperationLog)表服务基础实现类
 *
 * @author Fish.Fei
 * @since 2021-10-13 14:22:42
 */
public class OperationLogBaseServiceImpl<S, T, E, M extends BaseMapper<E>> extends BaseServiceImpl<OperationLog, OperationLogVO, OperationLog, OperationLogMapper> implements OperationLogBaseService<S, T, E> {

    @Autowired
    protected OperationLogMapper operationLogMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean saveOperationLog(OperationLogDTO operationLogDTO) {

        OperationLog operationLog = operationLogMapper.selectById(operationLogDTO.getId());
        if (ObjectUtil.isNotNull(operationLog)) {
            log.error("saveOperationLog() The OperationLog already exists");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        operationLog = new OperationLog();
        BeanUtil.copyProperties(operationLogDTO, operationLog);
        operationLog.setCreatedTime(DateUtil.current());
        operationLog.setDeleted(0);

        return this.save(operationLog);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean updateOperationLog(OperationLogDTO operationLogDTO) {

        OperationLog operationLog = this.getById(operationLogDTO.getId());
        if (ObjectUtil.isNull(operationLog)) {
            log.error("updateOperationLog() The OperationLog does not exist or has been deleted");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        BeanUtil.copyProperties(operationLogDTO, operationLog);
        operationLog.setUpdatedUser(operationLogDTO.getUpdatedUser());
        operationLog.setUpdatedTime(DateUtil.current());

        return this.updateById(operationLog);
    }

    @Override
    public Boolean saveOrUpdateBatch(List<OperationLog> operationLogList) {

        operationLogList.forEach(operationLog -> {
            OperationLog operationLogParam = new OperationLog();
            operationLogParam.setId(operationLog.getId());
            if (ObjectUtil.isNotNull(operationLog.getId())) {
                operationLog.setId(operationLog.getId());
                operationLog.setUpdatedTime(DateUtil.current());
                LambdaUpdateWrapper<OperationLog> lambdaUpdate = Wrappers.lambdaUpdate();
                lambdaUpdate.eq(OperationLog::getId, operationLog.getId());
                update(operationLog, lambdaUpdate);
            } else {
                operationLog.setCreatedTime(DateUtil.current());
                operationLog.setDeleted(0);
                save(operationLog);
            }
        });
        return true;
    }

    @Override
    public Boolean saveOrUpdateBatchByDTOList(List<OperationLogDTO> operationLogDTOList) {

        List<OperationLog> operationLogList = BeanUtils.copyProperties(operationLogDTOList, OperationLog.class);
        return saveOrUpdateBatch(operationLogList);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean deleteOperationLogLogical(Long id) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        OperationLog operationLog = this.getById(id);

        if (ObjectUtil.isNull(operationLog)) {
            log.error("deleteOperationLog() The OperationLog does not exist or has been deleted");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }
        operationLog.setDeleted(1);

        return this.updateById(operationLog);
    }

    private List<OperationLog> queryOperationLogList(OperationLogFilterDTO operationLogFilterDTO) {
        operationLogFilterDTO.setDeleted(0);
        OperationLogQuery operationLogQuery = BeanUtil.copyProperties(operationLogFilterDTO, OperationLogQuery.class);

        return operationLogMapper.queryOperationLog(operationLogQuery);
    }

    @Override
    public List<OperationLogVO> queryOperationLogVOList(OperationLogFilterDTO operationLogFilterDTO) {
        operationLogFilterDTO.setDeleted(0);
        return this.objectConversion(queryOperationLogList(operationLogFilterDTO));
    }

    @Override
    public PageVO<OperationLogVO> queryOperationLog(OperationLogFilterDTO operationLogFilterDTO) {
        operationLogFilterDTO.setDeleted(0);
        PageVO<OperationLogVO> pageVO = new PageVO<>();

        if (operationLogFilterDTO.getCurrentPage() > NumberConstant.ZERO) {
            PageableUtils.basicPage(operationLogFilterDTO.getCurrentPage(), operationLogFilterDTO.getPageSize(),
                    operationLogFilterDTO.getOrderType(), operationLogFilterDTO.getOrderField());
        }

        List<OperationLog> operationLogList = queryOperationLogList(operationLogFilterDTO);

        PageInfo<OperationLog> pageInfo = new PageInfo<>(operationLogList);
        PropertyUtils.copyProperties(pageInfo, pageVO, this.objectConversion(operationLogList));

        return pageVO;
    }

    @Override
    public OperationLogVO queryOperationLogById(Long id) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return this.objectConversion(this.getById(id));
    }


}
