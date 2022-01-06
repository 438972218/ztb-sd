package com.xdcplus.workflow.factory.oddrule;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 单号算法的处理器(年月日+三位数字)
 * 数字：子增长，每天从0计算
 *
 * @author Rong.Jia
 * @date 2021/09/15
 */
@Slf4j
@Component
public class RecalculateSelfGrowthDateAlgorithmProcessor extends BaseOddAlgorithmProcessor {

    private static final FastDateFormat SIMPLE_TIME_FORMAT = FastDateFormat.getInstance("yyMMdd");

    @Override
    public Boolean supportType(Integer algorithm) {
        return ObjectUtil.equal(NumberConstant.FIVE, algorithm);
    }

    @Override
    public String postProcess(Long oddRuleId, String prefix, Long autoNumber) {

        String autoNumberStr = Convert.toStr(autoNumber);

        String today = DateUtil.format(new Date(), SIMPLE_TIME_FORMAT);
        String history = today;
        if (autoNumberStr.length() > NumberConstant.THREE) {
            String[] autoNumberArr = StrUtil.split(autoNumberStr, autoNumberStr.length() - NumberConstant.THREE);
            history = autoNumberArr[NumberConstant.ZERO];
            autoNumber = Convert.toLong(autoNumberArr[NumberConstant.ONE]);
        }

        Long next = StrUtil.equals(today, history)
                ? Convert.toLong(history + fillDigits((autoNumber + NumberConstant.ONE.longValue()), NumberConstant.THREE, Boolean.TRUE))
                : Convert.toLong(today + fillDigits(NumberConstant.ONE.longValue(), NumberConstant.THREE, Boolean.TRUE));

        oddRuleService.updateAutoNumber(oddRuleId, next);

        return jointOddNumber(prefix, next);

    }


}
