package com.xdcplus.vendor.common.constants;

import cn.hutool.core.collection.CollectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ztb常量
 *
 * @author Martin.Ji
 * @date 2021/07/22
 */
public class ZtbConstant {

    public static final String PLANT = "工厂";
    public static final String PERCENTAGE  = "百分比";
    public static final String ABSOLUTE_VALUE  = "绝对值";
    public static final String FORWARD  = "正向";
    public static final String BID_TITLE = "招标";
    public static final String INQUIRY_TITLE = "询价单-";
    public static final String PAID_TITLE = "竞价单-";
    public static final String PUBLIC  = "公开";
    public static final String SUBMIT  = "提交";
    public static final String SAVE  = "保存";
    public static final String HISTORY  = "历史";
    public static final String BALLOT  = "已中签";

    public static final String BID_TYPE  = "招标单类型";

    public static final String ARCHIVED = "已归档";
    public static final String CANCELED = "已取消";
    public static final String TOBESUBMITTED = "待提交";
    public static final String BIDOPENING = "待开标";
    public static final String TOAUDIT = "待审核";
    public static final String DAIFABU = "待发布";
    public static final String YIFABU = "已发布";
    public static final String RUNNING = "正在运行";
    public static final String AWARDOFBID = "待决标";
    public static final String CAIGOUHEJIA = "待核价";
    public static final String HEJIASHENGPI = "待审批";
    public static final String QIANJIAQIAN = "前加签";
    public static final String TOBECHOSEN  = "待候选";
    public static final String TO_REPLY  = "待回复";
    public static final String NO_REPLY  = "未回复";
    public static final String HAS_REPLY  = "已回复";
    public static final String HAS_PARTICIPATE  = "已参与";
    public static final String HAS_REFUSED  = "已拒绝";
    public static final String HAS_WITHDRAWN  = "已撤回";
    public static final String TO_SCORE  = "待评分";
    public static final String EXPERT_RATING  = "专家评分";
    public static final String GROUP_RATING  = "组长评分";
    public static final String FOR_INVESTIGATING  = "待预审";
    public static final String IN_THE_BIDDING  = "招标中";
    public static final String QUALIFICATION  = "资质";
    public static final String TECHNOLOGY  = "技术";
    public static final String COMMERCE  = "商业";
    public static final String COMMERCE_SIGN  = "商业标";
    public static final String  INVITED  = "受邀";
    public static final String TO_PARTICIPATE  = "待参与";
    public static final String NO_PARTICIPATE  = "未参与";
    public static final String TOTAL_SUM  = "总额";
    public static final String DISTINGUISH  = "区分";
    public static final String UNIFY  = "统一";
    public static final String ABSOLUTE_THRESHOLD  = "绝对阈值";
    public static final String MULTIPLE  = "最小出价倍数";
    public static final String OFFER_DESCRIPTION_START_PRICE  = "不符合起始报价规则";
    public static final String OFFER_DESCRIPTION_MIN  = "不符合最小出价配置规则";
    public static final String OFFER_DESCRIPTION_OTHER  = "其他原因";
    public static final String  STATUS_RELEASED  = "已发布";
    public static final String  OFFER_INFORMATION  = "报价须知";

    public static final String  TOTAL_PRICING  = "-总排名";
    public static final String  SINGLE_PRICING  = "-单排名";
    public static final Long VENDOR_ROLE_ID  = 1422122786073538561L;
    //正在运行
    public static final long RUNNING_STATUS_ID = 1430091118399266818L;
    //已归档
    public static final long ARCHIVED_STATUS_ID = 2L;

    public static final String INITIAL_PASSWORD = "123456";

//    //内部端
//    public static final String PLAT_FORM_NAME = "https://epurchase.suntech-power.com.cn";
//
//    //供应商端
//    public static final String VENDOR_PLAT_FORM_NAME = "https://esourcing.suntech-power.com.cn";

    public static final Integer  CODE_NUMBER_99999  = 99999;

    public static final List<Long> PAID_REQUEST_STATUS_LIST =
            CollectionUtil.newArrayList(1430093625968107523L,1430091543366148097L,2L);

    /**
     * 审批流程id
     */
    public static final Long SHENGPI_PROCESS = 1415125159027109889L;
    public static final Long SHENGHE_TYPE_ID = 1424567739789418497L;
    public static final Long SHENGHE_RULE_ID = 5L;

    public static final Map<Integer,String> PAID_STATUS_MAP =new HashMap<>();
    static {
        PAID_STATUS_MAP.put(0,"待竞价");
        PAID_STATUS_MAP.put(1,"进行中");
        PAID_STATUS_MAP.put(2,"已暂停");
        PAID_STATUS_MAP.put(3,"已终止");
        PAID_STATUS_MAP.put(4,"截止");
        PAID_STATUS_MAP.put(-1,"未知");
    }

    public static final Map<Integer,String> BID_STATUS_MAP =new HashMap<>();
    static {
        BID_STATUS_MAP.put(0,"待回复");
        BID_STATUS_MAP.put(1,"未回复");
        BID_STATUS_MAP.put(2,"已回复");
        BID_STATUS_MAP.put(3,"已拒绝");
        BID_STATUS_MAP.put(4,"已撤回");
    }
}
