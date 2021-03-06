package com.xdcplus.ztb.common.exceptions;

/**
 * 水印异常
 *
 * @author Rong.Jia
 * @date 2021/08/12 14:30:37
 */
public class WatermarkException extends Exception {
	
	public WatermarkException(String msg) {
		super(msg);
	}
	
	public WatermarkException(String msg, Exception e) {
		super(msg, e);
	}

	public WatermarkException(String msg, Throwable t) {
		super(msg, t);
	}
}
