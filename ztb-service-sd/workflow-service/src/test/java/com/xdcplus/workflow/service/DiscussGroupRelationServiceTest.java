package com.xdcplus.workflow.service;

import com.xdcplus.workflow.WorkflowServiceApplicationTests;
import com.xdcplus.workflow.common.pojo.entity.DiscussGroupRelation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 讨论组关系服务测试
 *
 * @author Rong.Jia
 * @date 2021/11/08
 */
class DiscussGroupRelationServiceTest extends WorkflowServiceApplicationTests {

    @Autowired
    private DiscussGroupRelationService discussGroupRelationService;

    @Test
    void saveRelation() {
    }

    @Test
    void findRelationByGroupId() {

        List<DiscussGroupRelation> groupRelationList = discussGroupRelationService.findRelationByGroupId(1L);
        System.out.println(groupRelationList.toString());

    }
}