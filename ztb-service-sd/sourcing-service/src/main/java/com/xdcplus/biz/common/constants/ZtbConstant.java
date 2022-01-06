package com.xdcplus.biz.common.constants;

import java.util.Arrays;
import java.util.List;

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
    public static final String INVERSION  = "反向";
    public static final String BID_TITLE = "招标";
    public static final String INQUIRY_TITLE = "询价单-";
    public static final String PAID_TITLE = "竞价";
    public static final String PUBLIC  = "公开";
    public static final String SUBMIT  = "提交";
    public static final String SAVE  = "保存";
    public static final String BALLOT  = "已中签";
    public static final String HISTORY  = "历史";

    public static final String BID_TYPE  = "招标单类型";

    public static final String ARCHIVED = "已归档";
    public static final String CANCELED = "已取消";
    public static final String TOBESUBMITTED = "待提交";
    public static final String BIDOPENING = "待开标";
    public static final String TOAUDIT = "待审核";
    public static final String DAIFABU = "待发布";
    public static final String YIFABU = "已发布";
    public static final String TO_BE_AUTHORIZED = "待授权";
    public static final String TO_EVALUATE = "待评估";
    public static final String AWARDOFBID = "待决标";
    public static final String CAIGOUHEJIA = "待核价";
    public static final String HEJIASHENGPI = "待审批";
    public static final String QIANJIAQIAN = "前加签";
    public static final String ZHUANJIA = "待专家评分";
    public static final String TOBECHOSEN  = "待候选";
    public static final String TO_PARTICIPATE  = "待参与";
    public static final String NO_PARTICIPATE  = "未参与";
    public static final String HAS_PARTICIPATE  = "已参与";
    public static final String HAS_REFUSED  = "已拒绝";
    public static final String TO_SCORE  = "待评分";
    public static final String EXPERT_RATING  = "专家评分";
    public static final String GROUP_RATING  = "组长评分";
    public static final String FOR_INVESTIGATING  = "待预审";
    public static final String IN_THE_BIDDING  = "招标中";
    public static final String BID_PROCESS  = "招标单流程";
    public static final String QUALIFICATION  = "资质标";
    public static final String TECHNOLOGY  = "技术标";
    public static final String COMMERCE  = "商业标";
    public static final String TOTAL_SUM  = "总额";
    public static final String A_NUMBER_OF_ITEMS  = "多项物品";
    public static final String OFFER_INFORMATION  = "报价须知";
    public static final String DIFFERENTIATE  = "区分";
    public static final String UNIFICATION  = "统一";
    public static final String UNAUTHORIZED  = "未授权";
    public static final String TOTAL_NUMBER  = "显示总排名";
    public static final String BEST_QUOTATION  = "最佳报价";
    public static final String BEST_SCORE  = "最佳得分";

    public static final String BID_SAVE_STATUS  = "保存";
    public static final String VENDOR_REPLIED  = "已回复";
    public static final String  VENDOR_AUTHORIZED  = "已授标";
    public static final String  VENDOR_OUTBID  = "已中标";
    public static final String  INVITED  = "受邀";
    public static final String  UNSENT  = "未发送";
    public static final String  VENDOR_NO_REPLIED  = "未回复";
    public static final String  SPECIALIST_SCORE_NEW  = "最新";
    public static final String  STATUS_UNDERWAY  = "进行中";
    public static final String  STATUS_RUNNING  = "正在运行";
    public static final String  STATUS_PAUSE  = "暂停";
    public static final String  STATUS_TERMINATE  = "截止";
    public static final String  STATUS_STAY_PAID  = "待竞价";
    public static final String  STATUS_RELEASED  = "已发布";
    public static final Integer  CODE_NUMBER_999  = 999;
    public static final String  REQUESTID  = "requestId";
    public static final String  BID_STATEMENT  = "bid_statement";
    public static final String  PAID_STATEMENT  = "paid_statement";

    public static final long RUNNING_STATUS_ID = 1430091118399266818L;
    public static final long RELEASED_STATUS_ID = 1430093625968107523L;
    public static final long ADMIN_USER_ID = 1412584329550307329L;

    //审计员角色id
    public static final long AUDITOR_ROLE_ID = 1430059858722885633L;
    //管理员角色id
    public static final long ADMIN_ROLE_ID = 1422122858442059778L;

    /**
     * 审批流程id
     */
    public static final Long SHENGPI_PROCESS = 1415125159027109889L;
    public static final Long SHENGHE_TYPE_ID = 1424567739789418497L;
    public static final Long SHENGHE_RULE_ID = 5L;

    public static final List<Integer> PAID_SHEET_STATUSES = Arrays.asList(1, 2);

    public static final List<String> BID_STATUS_PINGGU = Arrays.asList("待资质评估", "待技术评估", "待商业评估");

    public static final List<Long> BID_STATUS_PINGGU_IDS = Arrays.asList(1430091285168988162L, 1430091325149093889L, 1430091395760201729L);

//    //内部端
//    public static final String PLAT_FORM_NAME = "https://epurchase.suntech-power.com.cn";
//
//    //供应商端
//    public static final String VENDOR_PLAT_FORM_NAME = "https://esourcing.suntech-power.com.cn";

//    private static final Map<String,String> BID_REQUEST_STATUS_MAP =new HashMap();
//    static {
//        BID_REQUEST_STATUS_MAP.put("前加签","待专家评分");
//        BID_REQUEST_STATUS_MAP.put("后加签","待专家组长确认");
//    }
}
