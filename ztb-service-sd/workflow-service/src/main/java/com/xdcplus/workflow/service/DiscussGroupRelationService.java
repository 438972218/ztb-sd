package com.xdcplus.workflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroupRelation;

import java.util.List;

/**
 * 讨论组关系服务
 *
 * @author Rong.Jia
 * @date 2021/11/08
 */
public interface DiscussGroupRelationService extends IService<DiscussGroupRelation> {

    /**
     * 保存关系信息
     *
     * @param groupId 讨论组ID
     * @param toUsers 接收用户
     * @return {@link Boolean}
     */
    Boolean saveRelation(Long groupId, List<String> toUsers);

    /**
     * 查询关系通过讨论组ID
     *
     * @param groupId 组id
     * @return {@link List}<{@link DiscussGroupRelation}>
     */
    List<DiscussGroupRelation> findRelationByGroupId(Long groupId);









}
