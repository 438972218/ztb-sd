package com.xdcplus.ztb.common.tool.anotation;

import java.lang.annotation.*;


/**
 * 去向用户类型注解
 * @author Martin.Ji
 * @since 2021-09-29 14:44
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
public @interface ToUserTypeAnnotation {

    /**
     *  序号
     */
    int sort() default 0;

    /**
     *  名称
     */
    String name();

    /**
     * 描述
     */
    String description();


}