package com.xdcplus.ztb.common.tool.constants;

/**
 * 缓存常量类
 *
 * @author Rong.Jia
 * @date 2021/06/29
 */
public class CacheConstant {

    /**
     *  项目
     */
    public static final String ZTB_SD = "ztb-sd:";

    /**
     *  工作流
     */
    public static final String WORKFLOW = ZTB_SD + "synergy:";

    /**
     * 供应商端
     */
    public static final String VENDOR = ZTB_SD + "vendor:";

    public static final String LDAP = WORKFLOW + "ladap:";

    public static final String AD_LDAP = LDAP + "ad:";

    public static final String AD_DEPARTMENT_CACHE =  AD_LDAP + "departments_cache";

    public static final String DEPARTMENTS = WORKFLOW + "departments_cache";

    public static final String BIDDING_SUMMARY_CACHE = VENDOR + "bidding_summary_cache";


    public static final String SOURCING_ENGINE = ZTB_SD + "sourcing:";
    public static final String PAIDS = SOURCING_ENGINE + "PAID_cache";
    public static final String PROJECT_CODE_NUMBER = SOURCING_ENGINE + "Project_code_number";

    public static final String VENDOR_ENGINE = ZTB_SD + "vendor:";
    public static final String VENDOR_CODE = VENDOR_ENGINE + "Vendor_code";







}
