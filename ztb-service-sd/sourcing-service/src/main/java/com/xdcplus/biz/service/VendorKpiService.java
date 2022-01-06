package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.VendorKpiDTO;
import com.xdcplus.biz.common.pojo.dto.VendorKpiFilterDTO;
import com.xdcplus.biz.common.pojo.vo.BidSheetVO;
import com.xdcplus.biz.generator.VendorKpiBaseService;
import com.xdcplus.biz.common.pojo.entity.VendorKpi;
import com.xdcplus.biz.common.pojo.vo.VendorKpiVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 供应商绩效考核表(VendorKpi)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-13 08:46:18
 */
public interface VendorKpiService extends VendorKpiBaseService<VendorKpi, VendorKpiVO, VendorKpi> {

    /**
     * 查询一个并带出附表中数据
     *
     * @param id 电子调查单(VendorKpi)主键
     * @return {@link VendorKpi} 电子调查单(VendorQuestion)信息
     */
    VendorKpiVO showVendorKpiById(Long id);

    /**
     * 修改供应商绩效考核表以及detail(vendorSiteInsDTO)
     *
     * @param vendorKpiDTO 供应商绩效考核表(vendorSiteInsDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updateVendorKpiWithDetail(VendorKpiDTO vendorKpiDTO);

    /**
     * 保存供应商kpi返回签证官
     *
     * @param vendorKpiDTO 供应商kpi dto
     * @return {@link VendorKpiVO}
     */
    VendorKpiVO saveVendorKpiReturnVO(VendorKpiDTO vendorKpiDTO);

    /**
     * 计算分数
     * 审批完后计算分数
     *
     * @param requestId 申请单ID
     */
    void calculateScore(Long requestId);

    /**
     * 查询供应商kpi与请求
     *
     * @param vendorKpiFilterDTO 供应商kpi过滤器dto
     * @return {@link PageVO<VendorKpiVO>}
     */
    PageVO<VendorKpiVO> queryVendorKpiWithRequest(VendorKpiFilterDTO vendorKpiFilterDTO);


}
