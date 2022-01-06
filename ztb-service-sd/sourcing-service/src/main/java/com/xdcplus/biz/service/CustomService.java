package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.entity.Vendor;
import com.xdcplus.biz.common.pojo.vo.VendorVO;
import com.xdcplus.biz.generator.VendorBaseService;
import com.xdcplus.ztb.common.remote.domain.sourcing.UserToMarkVO;

import java.util.List;
import java.util.Map;


/**
 * 业务通用接口
 *
 * @author Fish.Fei
 * @date 2021/09/28
 */
public interface CustomService  {

    Long getCustomToUserId(String mark,Long requestId);

    List<UserToMarkVO> getUserToMarkVO();

}
