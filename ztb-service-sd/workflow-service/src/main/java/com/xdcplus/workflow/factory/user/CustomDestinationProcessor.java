package com.xdcplus.workflow.factory.user;

import cn.hutool.core.lang.Validator;
import com.xdcplus.workflow.common.pojo.entity.UserToCustomRelation;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 自定义去向处理器
 *
 * @author Rong.Jia
 * @date 2021/09/28
 */
@Slf4j
@Component
public class CustomDestinationProcessor extends BaseUserDestinationProcessor {

    @Override
    public Boolean supportType(Integer userToType) {
        return Validator.equal(NumberConstant.SIX, userToType);
    }

    @Override
    public Long postProcess(UserDestinationParam userDestinationParam) {

        log.info("Custom direction processing");

        UserToCustomRelation userToCustomRelation = userToCustomRelationService.findRelation(userDestinationParam.getUserToType(), userDestinationParam.getConfigId());
        return httpService.getCustomToUserId(userToCustomRelation.getExpression(), userDestinationParam.getRequestId());
    }
}
