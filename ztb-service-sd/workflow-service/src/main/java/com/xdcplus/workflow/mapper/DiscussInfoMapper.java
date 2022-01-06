package com.xdcplus.workflow.mapper;

import com.xdcplus.workflow.common.pojo.bo.DiscussInfoBO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 讨论信息映射器
 *
 * @author Rong.Jia
 * @date 2021/10/13
 */
public interface DiscussInfoMapper {

    /**
     * 通过表单id找到讨论信息
     *
     * @param requestId 表单ID
     * @return {@link List}<{@link DiscussInfoBO}>
     */
    List<DiscussInfoBO> findDiscussInfoByRequestId(@Param("requestId") Long requestId);

    /**
     * 查询讨论信息通过讨论组ID
     *
     * @param groupId 讨论组ID
     * @return {@link List}<{@link DiscussInfoBO}>
     */
    List<DiscussInfoBO> findDiscussInfoByGroupId(@Param("groupId") Long groupId);























}
