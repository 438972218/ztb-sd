package com.xdcplus.ztb.common.watermark.factory;

import com.xdcplus.ztb.common.exceptions.WatermarkException;
import com.xdcplus.ztb.common.watermark.domain.WatermarkParam;

import java.io.File;

/**
 * 水印处理器
 *
 * @author Rong.Jia
 * @date 2021/08/12 14:59:25
 */
public interface WatermarkProcessor {

    /**
     * 添加水印
     *
     * @param watermarkParam 水印参数
     * @throws WatermarkException 水印异常
     */
    default void process(WatermarkParam watermarkParam) throws WatermarkException {

    }

    /**
     * 支持类型
     *
     * @param file 文件
     * @return {@link Boolean}
     */
    default Boolean supportType(File file) {
        return Boolean.FALSE;
    }










}
