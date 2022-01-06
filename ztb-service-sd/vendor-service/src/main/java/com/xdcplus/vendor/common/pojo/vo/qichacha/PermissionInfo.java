package com.xdcplus.vendor.common.pojo.vo.qichacha;

import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionInfo implements Serializable {

    private static final long serialVersionUID = -9179711283574776374L;

    private String Name;

    private String Province;

    private String Liandate;

    private String CaseNo;

}
