package com.xdcplus.vendor.service;

import com.xdcplus.vendor.common.pojo.vo.OfferVO;
import com.xdcplus.vendor.generator.PaidMaterialBaseService;
import com.xdcplus.vendor.common.pojo.entity.PaidMaterial;
import com.xdcplus.vendor.common.pojo.vo.PaidMaterialVO;
import com.xdcplus.vendor.websocket.BidRankingMessage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 竞价品项(PaidMaterial)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-09-03 09:01:54
 */
public interface PaidMaterialService extends PaidMaterialBaseService<PaidMaterial, PaidMaterialVO, PaidMaterial> {

    /**
     * 查询支付材料voby表id
     *
     * @param id id
     * @return {@link List<PaidMaterialVO>}
     */
    List<PaidMaterialVO> queryPaidMaterialVOBySheetId(Long id);

    /**
     * 查询支付材料voby请求id
     *
     * @param id id
     * @return {@link List<PaidMaterialVO>}
     */
    List<PaidMaterialVO> queryPaidMaterialVOByRequestId(Long id);

    /**
     * 根据表单id查询 最新排名的出价记录
     *
     * @param requestId 表单标识
     * @return {@link List< OfferVO >} 出价记录
     */
    List<PaidMaterialVO> getLatestRankingByRequestId(Long requestId);

    /**
     * 根据requestId或者vendorId查询出排名
     *
     * @param requestId    请求id
     * @param vendorUserId 供应商的用户id
     * @return {@link BidRankingMessage}
     */
    BidRankingMessage getRankingByVendorUserIdAndRequestId(Long vendorUserId, Long requestId);

    /**
     * 根据requestId查询出排名
     *
     * @param requestId 请求id
     * @return {@link List<BidRankingMessage>}
     */
    BidRankingMessage getRankingByRequestId(Long requestId);

    /**
     * 根据requestId和MaterialId查询出排名
     *
     * @param requestId 请求id
     * @return {@link List<BidRankingMessage>}
     */
    BidRankingMessage getRankingByRequestIdAndMaterialId(Long requestId,Long materialId);

    /**
     * 根据表单id和userId查询授权的品项
     *
     * @param requestId    表单标识
     * @param vendorUserId 供应商的用户id
     * @return {@link List<PaidMaterialVO>}
     */
    List<PaidMaterialVO> getMandateByRequestIdAndVendorUserId(Long requestId,Long vendorUserId);

    /**
     * 导出竞价总排名
     *
     * @param requestId 请求id
     */
    void exportExcelpaidTotalOffer(HttpServletResponse response, Long requestId);

    /**
     * 导出竞价单排名
     *
     * @param requestId 请求id
     */
    void exportExcelpaidSingleOffer(HttpServletResponse response,Long requestId);

}
