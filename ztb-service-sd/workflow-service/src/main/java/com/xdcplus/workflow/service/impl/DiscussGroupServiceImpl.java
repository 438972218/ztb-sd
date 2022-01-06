package com.xdcplus.workflow.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.xdcplus.workflow.common.pojo.bo.DiscussGroupBO;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroup;
import com.xdcplus.workflow.common.pojo.vo.DiscussGroupVO;
import com.xdcplus.workflow.mapper.DiscussGroupMapper;
import com.xdcplus.workflow.service.DiscussGroupService;
import com.xdcplus.workflow.service.RequestService;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 讨论组服务实现类
 *
 * @author Rong.Jia
 * @since 2021-08-18
 */
@Slf4j
@Service
public class DiscussGroupServiceImpl extends BaseServiceImpl<DiscussGroup, DiscussGroupVO, DiscussGroup, DiscussGroupMapper> implements DiscussGroupService {

    @Autowired
    private DiscussGroupMapper discussGroupMapper;

    @Autowired
    private RequestService requestService;

    @Override
    public List<DiscussGroupBO> findDiscussionGroup(Long requestId) {

        Assert.notNull(requestId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return discussGroupMapper.findDiscussGroupByRequestId(requestId);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Long saveDiscussGroup(Long requestId, String subject, String currentUser) {

        DiscussGroup discussGroup = discussGroupMapper.findDiscussGroupBySubjectAndRequestIdAndCreatedUser(subject, requestId, currentUser);
        if(ObjectUtil.isNull(discussGroup)) {
            discussGroup = new DiscussGroup();
            discussGroup.setCreatedTime(DateUtil.current());
            discussGroup.setCreatedUser(currentUser);
            discussGroup.setRequestId(requestId);
            discussGroup.setSubject(subject);
            this.save(discussGroup);
        }

        return discussGroup.getId();
    }

    @Override
    public DiscussGroupVO findOne(Long id) {

        Assert.notNull(id, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        return this.objectConversion(this.getById(id));
    }

    @Override
    public DiscussGroupVO objectConversion(DiscussGroup discussGroup) {
        DiscussGroupVO discussGroupVO = super.objectConversion(discussGroup);
        Optional.ofNullable(discussGroupVO).ifPresent(a -> a.setRequest(requestService.findOne(discussGroup.getRequestId(), Boolean.FALSE)));
        return discussGroupVO;
    }
}
