package com.xdcplus.ztb.common.vendorxlsx;

import com.xdcplus.ztb.common.vendorxlsx.annotation.ExcelField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtInfo {

    @ExcelField(header = "标类型（商业标，资质标，技术标）", order = 1)
    private List<String> bidType;

    @ExcelField(header = "类型(报价须知、国内报价、国外报价)", order = 2)
    private List<String> type;

    @ExcelField(header = "标题", order = 3)
    private List<String> name;

    @ExcelField(header = "说明", order = 4)
    private List<String> explaination;

    @ExcelField(header = "数量", order = 5)
    private List<String> quantity;

    @ExcelField(header = "单位", order = 6)
    private List<String> unit;







}
