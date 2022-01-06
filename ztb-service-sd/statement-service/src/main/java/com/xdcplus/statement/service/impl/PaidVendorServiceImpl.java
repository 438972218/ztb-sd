package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.PaidVendorBaseServiceImpl;
import com.xdcplus.statement.mapper.PaidVendorMapper;
import com.xdcplus.statement.common.pojo.entity.PaidVendor;
import com.xdcplus.statement.common.pojo.vo.PaidVendorVO;
import com.xdcplus.statement.service.PaidVendorService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 竞价供应商(PaidVendor)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:06
 */
@Slf4j
@Service("paidVendorService")
public class PaidVendorServiceImpl extends PaidVendorBaseServiceImpl<PaidVendor, PaidVendorVO, PaidVendor, PaidVendorMapper> implements PaidVendorService {

}
