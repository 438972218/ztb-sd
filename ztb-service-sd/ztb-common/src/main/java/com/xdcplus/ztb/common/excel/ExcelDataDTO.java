package com.xdcplus.ztb.common.excel;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zouLu on 2017-12-14.
 */

@Data
public class ExcelDataDTO implements Serializable {

    private static final long serialVersionUID = 4444017239100620999L;

    // 表头
    private List<String> titles;

    // 数据
    private List<List<Object>> rows;

    private String[][] stus;

    // 页签名称
    private String name;

}

