package com.xdcplus.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdcplus.workflow.common.pojo.entity.UserToCustomRelation;
import com.xdcplus.workflow.mapper.UserToCustomRelationMapper;
import com.xdcplus.workflow.service.UserToCustomRelationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户来自定义去向关系服务实现
 *
 * @author Rong.Jia
 * @date 2021/09/28
 */
@Slf4j
@Service
public class UserToCustomRelationServiceImpl extends ServiceImpl<UserToCustomRelationMapper, UserToCustomRelation> implements UserToCustomRelationService {

    @Autowired
    private UserToCustomRelationMapper userToCustomRelationMapper;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Long saveRelation(Integer userToMark, Long configId, String expression) {

        UserToCustomRelation userToCustomRelation = new UserToCustomRelation();
        userToCustomRelation.setUserToMark(userToMark);
        userToCustomRelation.setConfigId(configId);
        userToCustomRelation.setExpression(expression);

        this.save(userToCustomRelation);

        return userToCustomRelation.getId();
    }

    @Override
    public UserToCustomRelation findRelation(Integer userToMark, Long configId) {
        return userToCustomRelationMapper.findRelation(userToMark, configId);
    }
}
