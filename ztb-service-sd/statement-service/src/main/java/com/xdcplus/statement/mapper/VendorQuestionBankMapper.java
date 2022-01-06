package com.xdcplus.statement.mapper;

import com.xdcplus.ztb.common.mp.mapper.IBaseMapper;
import com.xdcplus.statement.common.pojo.entity.VendorQuestionBank;
import com.xdcplus.statement.common.pojo.query.VendorQuestionBankQuery;

import java.util.List;

/**
 * 模板题库表(VendorQuestionBank)表数据库访问层
 *
 * @author Fish.Fei
 * @since 2021-09-27 14:30:16
 */
public interface VendorQuestionBankMapper extends IBaseMapper<VendorQuestionBank> {

    /**
     * 查询模板题库表(VendorQuestionBank)
     *
     * @param vendorQuestionBankQuery 模板题库表(VendorQuestionBank)查询
     * @return {@link List<VendorQuestionBank>}
     */
    List<VendorQuestionBank> queryVendorQuestionBank(VendorQuestionBankQuery vendorQuestionBankQuery);

}
