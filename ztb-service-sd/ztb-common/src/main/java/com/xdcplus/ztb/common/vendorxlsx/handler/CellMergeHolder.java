package com.xdcplus.ztb.common.vendorxlsx.handler;

import lombok.Builder;
import lombok.Getter;

/**
 * 单元格合并信息
 *
 * @author Rong.Jia
 * @date 2021/11/02
 */
@Getter
@Builder
public class CellMergeHolder {

    /**
     *  合并后的内容
     */
    private String value;

    /**
     * 开始行, 合并使用
     */
    private int startRow;

    /**
     * 结束行, 合并使用
     */
    private int endRow;

    /**
     * 开始列, 合并使用
     */
    private int startCol;

    /**
     * 结束列, 合并使用
     */
    private int endCol;












}
