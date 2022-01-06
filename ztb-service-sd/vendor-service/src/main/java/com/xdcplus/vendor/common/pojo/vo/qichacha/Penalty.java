package com.xdcplus.vendor.common.pojo.vo.qichacha;

import lombok.Data;

import java.io.Serializable;

@Data
public class Penalty implements Serializable {

    private static final long serialVersionUID = -5367973202821159591L;

    private String DocNo;

    private String PenaltyType;

    private String OfficeName;

    private String Content;

    private String PenaltyDate;

    private String PublicDate;

    private String Remark;

}
