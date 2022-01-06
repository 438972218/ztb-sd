package com.xdcplus.workflow.mapper;

import com.xdcplus.workflow.common.pojo.entity.DiscussGroupRelation;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  讨论组关联信息 Mapper 接口
 *
 * @author Rong.Jia
 * @since 2021-08-18
 */
public interface DiscussGroupRelationMapper extends IBaseMapper<DiscussGroupRelation> {

    /**
     * 查询关系通过讨论组ID
     *
     * @param groupId 组id
     * @return {@link List}<{@link DiscussGroupRelation}>
     */
    List<DiscussGroupRelation> findRelationByGroupId(@Param("groupId") Long groupId);









}
