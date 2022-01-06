package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.dto.QiChaCompanyVerifyDTO;
import com.xdcplus.vendor.common.pojo.vo.qichacha.QiChaCompanyVO;

import java.util.List;

public interface QiChaChaService {


    /**
     * 企业工商模糊搜索
     *
     * @param keyword 关键字
     * @return {@link List<QiChaCompanyVO>}
     */
    List<QiChaCompanyVO> FuzzySearch(String keyword);

    /**
     * 企业工商风险扫描
     *
     * @param keyword 关键字
     * @return {@link List<QiChaCompanyVO>}
     */
    List<QiChaCompanyVO> ECIInfoOverview(String keyword);

    /**
     * 企业三要素核验
     *
     * @param qiChaCompanyVerifyDTO 齐车公司验证dto
     * @return {@link Integer}
     */
    String ECIThreeElVerify(QiChaCompanyVerifyDTO qiChaCompanyVerifyDTO);
}
