package com.xdcplus.workflow.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroupRelation;
import com.xdcplus.workflow.mapper.DiscussGroupRelationMapper;
import com.xdcplus.workflow.service.DiscussGroupRelationService;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 讨论组关系服务实现
 *
 * @author Rong.Jia
 * @date 2021/11/08
 */
@Slf4j
@Service
public class DiscussGroupRelationServiceImpl extends ServiceImpl<DiscussGroupRelationMapper, DiscussGroupRelation> implements DiscussGroupRelationService {

    @Autowired
    private DiscussGroupRelationMapper discussGroupRelationMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Boolean saveRelation(Long groupId, List<String> toUsers) {

        List<DiscussGroupRelation> discussGroupRelations = this.findRelationByGroupId(groupId);

        List<DiscussGroupRelation> discussGroupRelationList = toUsers.stream()
                .filter(a -> discussGroupRelations.stream().noneMatch(b -> b.getToUser().contains(a)))
                .map(a -> {
                    DiscussGroupRelation discus = new DiscussGroupRelation();
                    discus.setGroupId(groupId);
                    discus.setToUser(a);
                    return discus;
                }).collect(Collectors.toList());

        return this.saveBatch(discussGroupRelationList);
    }

    @Override
    public List<DiscussGroupRelation> findRelationByGroupId(Long groupId) {

        Assert.notNull(groupId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return discussGroupRelationMapper.findRelationByGroupId(groupId);
    }
}
