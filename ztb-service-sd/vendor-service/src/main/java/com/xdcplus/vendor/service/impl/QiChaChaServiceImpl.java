package com.xdcplus.vendor.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.xdcplus.vendor.common.pojo.dto.QiChaCompanyVerifyDTO;
import com.xdcplus.vendor.common.pojo.vo.qichacha.QiChaCompanyVO;
import com.xdcplus.vendor.common.utils.HttpHelper;
import com.xdcplus.vendor.common.utils.QiChaChaUtil;
import com.xdcplus.vendor.service.QiChaChaService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("qiChaChaService")
public class QiChaChaServiceImpl implements QiChaChaService {

    @Value("${qichacha.key}")
    private String key;
    @Value("${qichacha.secret}")
    private String secret;
    @Value("${qichacha.fuzzySearchUrl}")
    private String FuzzySearchUrl;
    @Value("${qichacha.eciInfoOverviewUrl}")
    private String ECIInfoOverviewUrl;
    @Value("${qichacha.eciThreeElVerifyUrl}")
    private String ECIThreeElVerifyUrl;


    @Override
    public List<QiChaCompanyVO> FuzzySearch(String keyword) {
        List<QiChaCompanyVO> qiChaCompanyVOS = CollectionUtil.newArrayList();
//        String reqInterNme = "http://api.qichacha.com/FuzzySearch/GetList";
        String paramStr = "searchKey=" + keyword;
        try {
            // auth header setting
            HttpHead reqHeader = new HttpHead();
            String[] autherHeader = QiChaChaUtil.RandomAuthentHeader(key, secret);
            reqHeader.setHeader("Token", autherHeader[0]);
            reqHeader.setHeader("Timespan", autherHeader[1]);
            final String reqUri = FuzzySearchUrl.concat("?key=").concat(key).concat("&").concat(paramStr).concat("&pageSize=20");
            String tokenJson = HttpHelper.httpGet(reqUri, reqHeader.getAllHeaders());

            String status = QiChaChaUtil.FormartJson(tokenJson, "Status");

            if (!QiChaChaUtil.HttpCodeRegex.isAbnornalRequest(status)) {
                String result = QiChaChaUtil.FormartJson(tokenJson, "Result");

                qiChaCompanyVOS = JSON.parseArray(result, QiChaCompanyVO.class);
            }
        } catch (Exception e1) {
            log.error("FuzzySearch() qiChaCompanyVOS select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        return qiChaCompanyVOS;
    }

    @Override
    public List<QiChaCompanyVO> ECIInfoOverview(String keyword) {
        List<QiChaCompanyVO> qiChaCompanyVOS = CollectionUtil.newArrayList();
//        String reqInterNme = "http://api.qichacha.com/FuzzySearch/GetList";
        String paramStr = "searchKey=" + keyword;
        try {
            // auth header setting
            HttpHead reqHeader = new HttpHead();
            String[] autherHeader = QiChaChaUtil.RandomAuthentHeader(key, secret);
            reqHeader.setHeader("Token", autherHeader[0]);
            reqHeader.setHeader("Timespan", autherHeader[1]);
            final String reqUri = ECIInfoOverviewUrl.concat("?key=").concat(key).concat("&").concat(paramStr);
            String tokenJson = HttpHelper.httpGet(reqUri, reqHeader.getAllHeaders());

            String status = QiChaChaUtil.FormartJson(tokenJson, "Status");

            if (!QiChaChaUtil.HttpCodeRegex.isAbnornalRequest(status)) {
                String result = QiChaChaUtil.FormartJson(tokenJson, "Result");

                qiChaCompanyVOS = JSON.parseArray(result, QiChaCompanyVO.class);
            }
        } catch (Exception e1) {
            log.error("FuzzySearch() qiChaCompanyVOS select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        return qiChaCompanyVOS;
    }

    @Override
    public String ECIThreeElVerify(QiChaCompanyVerifyDTO qiChaCompanyVerifyDTO) {
        String verifyResult = null;
        String creditCodeStr = "searchKey=" + qiChaCompanyVerifyDTO.getCreditCode();
        String companyNameStr = "searchKey=" + qiChaCompanyVerifyDTO.getCompanyName();
        String operNameStr = "searchKey=" + qiChaCompanyVerifyDTO.getOperName();
        try {
            // auth header setting
            HttpHead reqHeader = new HttpHead();
            String[] autherHeader = QiChaChaUtil.RandomAuthentHeader(key, secret);
            reqHeader.setHeader("Token", autherHeader[0]);
            reqHeader.setHeader("Timespan", autherHeader[1]);
            final String reqUri = ECIThreeElVerifyUrl.concat("?key=").concat(key).concat("&")
                    .concat(creditCodeStr).concat("&").concat(companyNameStr).concat("&").concat(operNameStr);
            String tokenJson = HttpHelper.httpGet(reqUri, reqHeader.getAllHeaders());

            String status = QiChaChaUtil.FormartJson(tokenJson, "Status");

            if (!QiChaChaUtil.HttpCodeRegex.isAbnornalRequest(status)) {
                verifyResult = QiChaChaUtil.FormartJson(tokenJson, "VerifyResult");

            }
        } catch (Exception e1) {
            log.error("ECIThreeElVerify() qiChaCompanyVOS select faild");
            throw new ZtbWebException(ResponseEnum.ERROR);
        }

        return verifyResult;
    }
}
