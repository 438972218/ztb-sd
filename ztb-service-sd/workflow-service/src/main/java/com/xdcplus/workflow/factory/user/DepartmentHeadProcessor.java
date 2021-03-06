package com.xdcplus.workflow.factory.user;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Validator;
import com.xdcplus.workflow.common.pojo.entity.UserToCustomRelation;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 部门主管处理器
 *
 * @author Rong.Jia
 * @date 2021/07/19
 */
@Slf4j
@Component
public class DepartmentHeadProcessor extends BaseUserDestinationProcessor {

    @Override
    public Boolean supportType(Integer userToType) {
        return Validator.equal(NumberConstant.FIVE, userToType);
    }

    @Override
    public Long postProcess(UserDestinationParam userDestinationParam) {

        log.info("Handled by department head");

        UserToCustomRelation userToCustomRelation = userToCustomRelationService.findRelation(userDestinationParam.getUserToType(), userDestinationParam.getConfigId());
        return httpService.getDepartmentHeadByDepartmentId(Convert.toLong(userToCustomRelation.getExpression()));
    }
}
