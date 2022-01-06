package com.xdcplus.ztb.common.vendorxlsx.converter;

/**
 * 写入excel内容转换器
 *
 * @author Rong.Jia
 * @date 2021/10/29
 */
public interface ReadConvertible {

    /**
     * 读取Excel列内容转换
     *
     * @param object 待转换数据
     * @return 转换完成的结果
     */
    Object execRead(String object);
}
