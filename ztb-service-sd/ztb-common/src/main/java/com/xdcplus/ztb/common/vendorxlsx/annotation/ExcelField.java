package com.xdcplus.ztb.common.vendorxlsx.annotation;


import com.xdcplus.ztb.common.vendorxlsx.converter.DefaultConvertible;
import com.xdcplus.ztb.common.vendorxlsx.converter.ReadConvertible;
import com.xdcplus.ztb.common.vendorxlsx.converter.WriteConvertible;

import java.lang.annotation.*;

/**
 * excel头
 *
 * @author Rong.Jia
 * @date 2021/10/28
 */
@Target(ElementType.FIELD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelField {

    /**
     * 表头名称
     */
    String header() default "";

    /**
     * 是否标题
     *
     * @return boolean
     */
    boolean isTitle() default false;

    /**
     * 写数据转换器
     *
     * @return 写入Excel数据转换器
     * @see WriteConvertible
     */
    Class<? extends WriteConvertible> writeConverter() default DefaultConvertible.class;

    /**
     * 读数据转换器
     *
     * @return 读取Excel数据转换器
     * @see ReadConvertible
     */
    Class<? extends ReadConvertible> readConverter() default DefaultConvertible.class;

    /**
     * 列排序
     */
    int order() default Integer.MAX_VALUE;
















}
