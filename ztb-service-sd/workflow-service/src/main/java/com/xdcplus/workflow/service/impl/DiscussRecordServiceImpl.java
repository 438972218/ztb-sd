package com.xdcplus.workflow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import com.xdcplus.workflow.common.pojo.bo.DiscussRecordBO;
import com.xdcplus.workflow.common.pojo.entity.DiscussRecord;
import com.xdcplus.workflow.common.pojo.vo.DiscussRecordVO;
import com.xdcplus.workflow.mapper.DiscussRecordMapper;
import com.xdcplus.workflow.service.DiscussRecordRelationService;
import com.xdcplus.workflow.service.DiscussRecordService;
import com.xdcplus.ztb.common.mp.service.impl.BaseServiceImpl;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 讨论记录 服务实现类
 *
 * @author Rong.Jia
 * @since 2021-08-18
 */
@Slf4j
@Service
public class DiscussRecordServiceImpl extends BaseServiceImpl<DiscussRecordBO, DiscussRecordVO, DiscussRecord, DiscussRecordMapper> implements DiscussRecordService {

    @Autowired
    private DiscussRecordMapper discussRecordMapper;

    @Autowired
    private DiscussRecordRelationService discussRecordRelationService;

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Long saveDiscussRecord(Long groupId, String content, String currentUser, List<String> toUsers) {

        DiscussRecord discussRecord = new DiscussRecord();
        discussRecord.setGroupId(groupId);
        discussRecord.setContent(content);
        discussRecord.setFromUser(currentUser);
        discussRecord.setCreatedUser(currentUser);
        discussRecord.setCreatedTime(DateUtil.current());
        this.save(discussRecord);

        discussRecordRelationService.saveRelation(discussRecord.getId(), toUsers);

        return discussRecord.getId();
    }

    @Override
    public List<DiscussRecordBO> findDiscussRecordByGroupId(Long groupId) {

        Assert.notNull(groupId, ResponseEnum.THE_ID_CANNOT_BE_EMPTY.getMessage());

        List<DiscussRecord> discussRecordList = discussRecordMapper.findDiscussRecordByGroupId(groupId);

        return copyProperties(discussRecordList);
    }

    @Override
    public List<DiscussRecordBO> findDiscussRecordByGroupIds(Set<Long> groupIds) {

        List<DiscussRecord> discussRecordList = discussRecordMapper.findDiscussRecordByGroupIds(groupIds);
        return copyProperties(discussRecordList);
    }

    /**
     * 复制属性
     *
     * @param discussRecordList 讨论记录列表
     * @return {@link List<DiscussRecordBO>}
     */
    private List<DiscussRecordBO> copyProperties(List<DiscussRecord> discussRecordList) {

        if (CollectionUtil.isNotEmpty(discussRecordList)) {
            return discussRecordList.stream().map(a -> {
                DiscussRecordBO discussRecordBO = new DiscussRecordBO();
                BeanUtil.copyProperties(a, discussRecordBO);
                discussRecordBO.setRelations(discussRecordRelationService.findRelation(a.getId()));
                return discussRecordBO;
            }).collect(Collectors.toList());
        }

        return null;
    }

}
