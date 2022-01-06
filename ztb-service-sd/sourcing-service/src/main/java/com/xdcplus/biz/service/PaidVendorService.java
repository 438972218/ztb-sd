package com.xdcplus.biz.service;

import com.xdcplus.biz.common.pojo.dto.PaidVendorDTO;
import com.xdcplus.biz.common.pojo.dto.PaidVendorFilterDTO;
import com.xdcplus.biz.common.pojo.vo.BidVendorVO;
import com.xdcplus.biz.generator.PaidVendorBaseService;
import com.xdcplus.biz.common.pojo.entity.PaidVendor;
import com.xdcplus.biz.common.pojo.vo.PaidVendorVO;
import com.xdcplus.ztb.common.tool.pojo.vo.PageVO;

import java.util.List;


/**
 * 竞价供应商(PaidVendor)表服务接口层
 *
 * @author Fish.Fei
 * @since 2021-08-16 14:02:06
 */
public interface PaidVendorService extends PaidVendorBaseService<PaidVendor, PaidVendorVO, PaidVendor> {

    /**
     * 供应商修改供应商状态(PaidVendor)
     *
     * @param paidVendorDTO 招标供应商(PaidVendorDTO)
     * @return {@link Boolean} 是否成功
     */
    Boolean updatePaidVendorStatus(PaidVendorDTO paidVendorDTO);

    /**
     * 保存竞价供应商和自动生成标识
     *
     * @param paidVendorDTO 招标供应商(PaidVendorDTO)
     * @return {@link Boolean}
     */
    Boolean savePaidVendorWithMark(PaidVendorDTO paidVendorDTO);

    void refreshPaidVendorMark(List<PaidVendorVO> paidVendorVOS);

    Boolean updatePaidVendorJudgeStartPrice(PaidVendorDTO paidVendorDTO);

    /**
     * 竞价放弃授权
     *
     * @return {@link PageVO <PaidVendorVO>} 状态信息
     */
    PageVO<PaidVendorVO> waiveAuth(Long paidSheetId);

    /**
     * 查询竞价供应商(PaidVendor)WithUser
     *
     * @param paidVendorFilterDTO 过程状态过滤DTO
     * @return {@link PageVO<PaidVendorVO>} 状态信息
     */
    PageVO<PaidVendorVO> queryPaidVendorWithUser(PaidVendorFilterDTO paidVendorFilterDTO);

}
