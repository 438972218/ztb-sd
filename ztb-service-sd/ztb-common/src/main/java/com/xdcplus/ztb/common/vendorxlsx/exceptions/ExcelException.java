package com.xdcplus.ztb.common.vendorxlsx.exceptions;

/**
 * excel 异常
 * @author Rong.Jia
 * @date 2021/10/29
 */
public class ExcelException extends RuntimeException {

    public ExcelException() {
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }
}
