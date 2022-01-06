package com.xdcplus.ztb.common.vendorxlsx.handler;

import com.xdcplus.ztb.common.vendorxlsx.converter.ReadConvertible;
import com.xdcplus.ztb.common.vendorxlsx.converter.WriteConvertible;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * excel 数据信息
 *
 * @author Rong.Jia
 * @date 2021/11/02
 */
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExcelHolder implements Comparable<ExcelHolder> {

    /**
     * 值
     */
    private Object value;

    /**
     * 排序
     */
    private Integer order;

    /**
     * 是否标题
     */
    Boolean isTitle;

    /**
     * excel的表头名称
     */
    private String header;

    /**
     * 写数据转换器
     */
    private WriteConvertible writeConverter;

    /**
     * 读数据转换器
     */
    private ReadConvertible readConverter;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 属性类型
     */
    private Class<?> filedClazz;

    @Override
    public int compareTo(ExcelHolder o) {
        return order - o.order;
    }



}
