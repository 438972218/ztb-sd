package com.xdcplus.workflow.mapper;

import com.xdcplus.workflow.common.pojo.entity.UserToCustomRelation;
import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户自定义去向的映射关系
 *
 * @author Rong.Jia
 * @date 2021/09/28
 */
public interface UserToCustomRelationMapper extends IBaseMapper<UserToCustomRelation> {

    /**
     * 查询关系
     *
     * @param userToMark 用户去向标识
     * @param configId 流程配置ID
     * @return {@link UserToCustomRelation}
     */
    UserToCustomRelation findRelation(@Param("userToMark") Integer userToMark, @Param("configId") Long configId);










}
