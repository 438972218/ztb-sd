package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.VendorQuestionBankFilterDTO;
import com.xdcplus.biz.common.pojo.vo.VendorKpiDetailVO;
import com.xdcplus.biz.generator.VendorQuestionBankBaseService;
import com.xdcplus.biz.common.pojo.entity.VendorQuestionBank;
import com.xdcplus.biz.common.pojo.vo.VendorQuestionBankVO;

import java.util.List;


/**
 * 模板题库表(VendorQuestionBank)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:29
 */
public interface VendorQuestionBankService extends VendorQuestionBankBaseService<VendorQuestionBank, VendorQuestionBankVO, VendorQuestionBank> {

    /**
     * 查询与孩子
     *
     * @param vendorQuestionBankFilterDTO 供应商问题银行过滤dto
     * @return {@link List<VendorQuestionBankVO>}
     */
    List<VendorQuestionBankVO> queryWithChild(VendorQuestionBankFilterDTO vendorQuestionBankFilterDTO);

}
