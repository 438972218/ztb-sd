package com.xdcplus.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xdcplus.workflow.common.pojo.entity.UserToCustomRelation;

/**
 * 用户自定义去向关系服务
 *
 * @author Rong.Jia
 * @date 2021/09/28
 */
public interface UserToCustomRelationService extends IService<UserToCustomRelation> {

    /**
     * 保存关系
     *
     * @param userToMark   用户去向标识
     * @param configId   流程配置ID
     * @param expression 表达式
     * @return {@link Long} 主键ID
     */
    Long saveRelation(Integer userToMark, Long configId, String expression);

    /**
     * 查询关系
     *
     * @param userToMark 用户去向标识
     * @param configId 流程配置ID
     * @return {@link UserToCustomRelation}
     */
    UserToCustomRelation findRelation(Integer userToMark, Long configId);










}
