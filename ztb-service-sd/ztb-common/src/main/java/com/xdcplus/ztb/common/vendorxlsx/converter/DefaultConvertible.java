package com.xdcplus.ztb.common.vendorxlsx.converter;

/**
 * 抽象默认转换器, 实现了{@link WriteConvertible} 与 {@link ReadConvertible}接口
 *
 * @author Rong.Jia
 * @date 2021/10/29
 */
public class DefaultConvertible implements WriteConvertible, ReadConvertible {

    @Override
    public Object execRead(String object) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object execWrite(Object object) {
        throw new UnsupportedOperationException();
    }
}
