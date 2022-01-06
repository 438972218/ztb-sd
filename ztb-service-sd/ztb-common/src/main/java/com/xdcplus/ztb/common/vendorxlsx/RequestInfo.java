package com.xdcplus.ztb.common.vendorxlsx;

import com.xdcplus.ztb.common.vendorxlsx.annotation.ExcelField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInfo {

    @ExcelField(header = "轮次", order = 1)
    private List<String> round;

    @ExcelField(header = "单价", order = 2)
    private  List<String> price;

    @ExcelField(header = "ETD", order = 3)
    private  List<String> etd;

    @ExcelField(header = "ETA", order = 4)
    private List<String> eta;

    @ExcelField(header = "船期（每周几）", order = 5)
    private List<String> shippingSchedule;

    @ExcelField(header = "船公司", order = 6)
    private List<String> shippingCompany;

    @ExcelField(header = "供应商名", isTitle = true, order = -1)
    private String vendorName;








}
