package com.xdcplus.statement.service.impl;

import com.xdcplus.statement.generator.impl.VendorQuestionBankBaseServiceImpl;
import com.xdcplus.statement.mapper.VendorQuestionBankMapper;
import com.xdcplus.statement.common.pojo.entity.VendorQuestionBank;
import com.xdcplus.statement.common.pojo.vo.VendorQuestionBankVO;
import com.xdcplus.statement.service.VendorQuestionBankService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * 模板题库表(VendorQuestionBank)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:29
 */
@Slf4j
@Service("vendorQuestionBankService")
public class VendorQuestionBankServiceImpl extends VendorQuestionBankBaseServiceImpl<VendorQuestionBank, VendorQuestionBankVO, VendorQuestionBank, VendorQuestionBankMapper> implements VendorQuestionBankService {

}
