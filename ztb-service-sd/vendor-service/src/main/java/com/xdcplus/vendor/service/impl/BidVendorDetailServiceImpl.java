package com.xdcplus.vendor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.xdcplus.vendor.common.pojo.vo.WebsiteVO;
import com.xdcplus.vendor.common.constants.ZtbConstant;
import com.xdcplus.vendor.common.pojo.dto.BidVendorDTO;
import com.xdcplus.vendor.common.pojo.dto.BidVendorDetailDTO;
import com.xdcplus.vendor.common.pojo.query.BidVendorDetailQuery;
import com.xdcplus.vendor.common.pojo.vo.BidSheetVO;
import com.xdcplus.vendor.common.pojo.vo.BidVendorVO;
import com.xdcplus.vendor.common.pojo.vo.ProjectSheetVO;
import com.xdcplus.vendor.generator.impl.BidVendorDetailBaseServiceImpl;
import com.xdcplus.vendor.mapper.BidVendorDetailMapper;
import com.xdcplus.vendor.common.pojo.entity.BidVendorDetail;
import com.xdcplus.vendor.common.pojo.vo.BidVendorDetailVO;
import com.xdcplus.vendor.remote.service.IeService;
import com.xdcplus.vendor.remote.service.InteriorPermService;
import com.xdcplus.vendor.service.BidSheetService;
import com.xdcplus.vendor.service.BidVendorDetailService;
import com.xdcplus.vendor.service.BidVendorService;
import com.xdcplus.vendor.service.ProjectSheetService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.remote.domain.perm.vo.SysUserInfoVO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.ExtraMailNotificationInfoDTO;
import com.xdcplus.ztb.common.remote.domain.workflow.dto.MailBeanInfo;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipException;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-26 09:41:39
 */
@Slf4j
@Service("bidVendorDetailService")
public class BidVendorDetailServiceImpl extends BidVendorDetailBaseServiceImpl<BidVendorDetail, BidVendorDetailVO, BidVendorDetail, BidVendorDetailMapper> implements BidVendorDetailService {

    @Autowired
    BidVendorService bidVendorService;

    @Autowired
    BidSheetService bidSheetService;

    @Autowired
    InteriorPermService interiorPermService;

    @Autowired
    IeService ieService;

    @Autowired
    ProjectSheetService projectSheetService;

    @Autowired
    WebsiteVO websiteVO;

    @Override
    public void updateBidVendorDetailByOperation(Long bidVendorId, String operation) {
        double totalPrice = NumberConstant.ZERO;
        BidVendorVO bidVendorVO = bidVendorService.queryBidVendorById(bidVendorId);
        BidVendorDTO bidVendorDTO = BeanUtil.copyProperties(bidVendorVO, BidVendorDTO.class);

        BidSheetVO bidSheetVO = bidSheetService.showBidSheetById(bidVendorVO.getBidSheetId());
        if(!StrUtil.equals(bidSheetVO.getRequestStatusName(),ZtbConstant.RUNNING)){
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_REPLY_ERROR);
        }

        if (ZtbConstant.HAS_REPLY.equals(operation)) {
            List<BidVendorDetail> bidVendorDetails = selectBidVendorDetailByRound(bidVendorId, bidVendorVO.getRound());
            bidVendorDetails.forEach(bidVendorDetail -> bidVendorDetail.setStatus(ZtbConstant.SUBMIT));
            saveOrUpdateBatch(bidVendorDetails);

            bidVendorDetails = bidVendorDetails.stream()
                    .filter(bidVendorDetail -> bidVendorDetail.getBidType().equals(ZtbConstant.COMMERCE_SIGN)
                            && !bidVendorDetail.getType().equals(ZtbConstant.OFFER_INFORMATION)
                    && bidVendorDetail.getPrice()!=null && bidVendorDetail.getQuantity()!=null).collect(Collectors.toList());

            //计算总价
            totalPrice = bidVendorDetails.stream().reduce(0.0, (x, y) -> x + (y.getPrice() * y.getQuantity()), Double::sum);
//            totalPrice = bidVendorDetails.stream().filter(bidVendorDetail -> bidVendorDetail.getPrice() != null)
//                    .mapToDouble(BidVendorDetail::getPrice).sum();
            //修改供应商回复时间
            bidVendorDTO.setReplyTime(DateUtil.current());

            sendMailCreatedUserReply(bidVendorDTO.getBidSheetId(),bidVendorDTO.getVendorName());

        } else if (ZtbConstant.HAS_REFUSED.equals(operation)) {
            List<BidVendorDetail> bidVendorDetails = selectBidVendorDetailByRound(bidVendorId, bidVendorVO.getRound());
            bidVendorDetails.forEach(bidVendorDetail -> bidVendorDetail.setStatus(ZtbConstant.HISTORY));
            saveOrUpdateBatch(bidVendorDetails);

            totalPrice = NumberConstant.ZERO;
            //修改供应商回复时间
            bidVendorDTO.setReplyTime(null);

            sendMailCreatedUserRefused(bidVendorDTO.getBidSheetId(),bidVendorDTO.getVendorName());

        } else if (ZtbConstant.HAS_WITHDRAWN.equals(operation)) {
            List<BidVendorDetail> bidVendorDetails = selectBidVendorDetailByRound(bidVendorId, bidVendorVO.getRound());
            bidVendorDetails.forEach(bidVendorDetail -> bidVendorDetail.setStatus(ZtbConstant.SAVE));
            saveOrUpdateBatch(bidVendorDetails);

            totalPrice = NumberConstant.ZERO;
            //修改供应商回复时间
            bidVendorDTO.setReplyTime(null);

            sendMailCreatedUserWithdrawn(bidVendorDTO.getBidSheetId(),bidVendorDTO.getVendorName());
        }

        //修改供应商状态
        bidVendorDTO.setVendorStatus(operation);
        bidVendorDTO.setTotalPrice(totalPrice);
        bidVendorService.updateBidVendor(bidVendorDTO);
    }

    @Override
    public void batchUpdateBidDetailDTO(List<BidVendorDetailDTO> bidVendorDetailDTOS) {
        saveOrUpdateBatchByDTOList(bidVendorDetailDTOS);
    }

    @Override
    public void batchUpdateBidDetailDTOWithJudgeStatus(List<BidVendorDetailDTO> bidVendorDetailDTOS) {

        BidSheetVO bidSheetVO = bidSheetService.showBidSheetById(bidVendorDetailDTOS.get(NumberConstant.ZERO).getBidSheetId());
        if(ObjectUtil.isEmpty(bidSheetVO)){
            return;
        }

        if(!StrUtil.equals(bidSheetVO.getRequestStatusName(),ZtbConstant.RUNNING)){
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_REPLY_ERROR);
        }

        saveOrUpdateBatchByDTOList(bidVendorDetailDTOS);
    }

    private List<BidVendorDetail> selectBidVendorDetailByStatusList(long bidVendorId, List<String> statusList) {
        BidVendorDetailQuery bidVendorDetailQuery = new BidVendorDetailQuery();
        bidVendorDetailQuery.setBidVendorId(bidVendorId);
        bidVendorDetailQuery.setStatusList(statusList);
        List<BidVendorDetail> bidVendorDetails = bidVendorDetailMapper.queryBidVendorDetail(bidVendorDetailQuery);
        if (CollectionUtil.isEmpty(bidVendorDetails)) {
            log.error("updateBidVendorDetailByOperation() The BidVendorDetail select faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_DETAIL_SELECT_FAIL);
        }

        return bidVendorDetails;
    }

    private List<BidVendorDetail> selectBidVendorDetailByRound(long bidVendorId, Integer round) {
        BidVendorDetailQuery bidVendorDetailQuery = new BidVendorDetailQuery();
        bidVendorDetailQuery.setBidVendorId(bidVendorId);
        bidVendorDetailQuery.setRound(round);
        List<BidVendorDetail> bidVendorDetails = bidVendorDetailMapper.queryBidVendorDetail(bidVendorDetailQuery);
        if (CollectionUtil.isEmpty(bidVendorDetails)) {
            log.error("updateBidVendorDetailByOperation() The BidVendorDetail select faild");
            throw new ZtbWebException(ResponseEnum.BID_VENDOR_DETAIL_SELECT_FAIL);
        }

        return bidVendorDetails;
    }

    /**
     * 供应商回复后发送邮件
     *
     * @param bidSheetId 报价表的id
     * @param vendorName 供应商的名字
     */
    private void sendMailCreatedUserReply(Long bidSheetId,String vendorName) {

        BidSheetVO bidSheetVO = bidSheetService.showBidSheetById(bidSheetId);
        //发送邮件给创建人
        SysUserInfoVO sysUserInfoVO = interiorPermService.queryByUserName(bidSheetVO.getCreatedUser());

        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.ELEVEN));
        extraMailNotificationInfoDTO.setRequestId(bidSheetVO.getRequestId());

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("vendorName", vendorName);
        objectMap.put("sheetType", ZtbConstant.BID_TITLE);
        objectMap.put("code", bidSheetVO.getOddNumber());
        objectMap.put("title", bidSheetVO.getTitle());
        objectMap.put("platformName", websiteVO.getPlatFromName());
        objectMap.put("bidEndTime", DateUtil.date(bidSheetVO.getBidEndTime()));

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(sysUserInfoVO.getMail());
        mailBeanInfo.setName(sysUserInfoVO.getName());

        extraMailNotificationInfoDTO.setExtra(objectMap);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
        ieService.emailNotification(extraMailNotificationInfoDTO);
    }

    /**
     * 供应商拒绝后发送邮件
     *
     * @param bidSheetId 报价表的id
     * @param vendorName 供应商的名字
     */
    private void sendMailCreatedUserRefused(Long bidSheetId,String vendorName) {

        BidSheetVO bidSheetVO = bidSheetService.showBidSheetById(bidSheetId);
        //发送邮件给创建人
        SysUserInfoVO sysUserInfoVO = interiorPermService.queryByUserName(bidSheetVO.getCreatedUser());

        ProjectSheetVO projectSheetVO = projectSheetService.queryProjectSheetById(bidSheetVO.getProjectSheetId());

        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.TWENTY_ONE));
        extraMailNotificationInfoDTO.setRequestId(bidSheetVO.getRequestId());

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("vendorName", vendorName);
        objectMap.put("sheetType", ZtbConstant.BID_TITLE);
        objectMap.put("code", bidSheetVO.getOddNumber());
        objectMap.put("title", bidSheetVO.getTitle());
        objectMap.put("platformName", websiteVO.getPlatFromName());
        objectMap.put("url", websiteVO.getPlatFromName());
        objectMap.put("projectName", projectSheetVO.getTitle());
        objectMap.put("bidEndTime", DateUtil.date(bidSheetVO.getBidEndTime()));

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(sysUserInfoVO.getMail());
        mailBeanInfo.setName(sysUserInfoVO.getName());

        extraMailNotificationInfoDTO.setExtra(objectMap);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
        ieService.emailNotification(extraMailNotificationInfoDTO);
    }

    /**
     * 供应商撤回后发送邮件
     *
     * @param bidSheetId 报价表的id
     * @param vendorName 供应商的名字
     */
    private void sendMailCreatedUserWithdrawn(Long bidSheetId,String vendorName) {

        BidSheetVO bidSheetVO = bidSheetService.showBidSheetById(bidSheetId);
        //发送邮件给创建人
        SysUserInfoVO sysUserInfoVO = interiorPermService.queryByUserName(bidSheetVO.getCreatedUser());

        ExtraMailNotificationInfoDTO extraMailNotificationInfoDTO = new ExtraMailNotificationInfoDTO();
        extraMailNotificationInfoDTO.setPoint(String.valueOf(NumberConstant.TWENTY));
        extraMailNotificationInfoDTO.setRequestId(bidSheetVO.getRequestId());

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("vendorName", vendorName);
        objectMap.put("sheetType", ZtbConstant.BID_TITLE);
        objectMap.put("code", bidSheetVO.getOddNumber());
        objectMap.put("title", bidSheetVO.getTitle());
        objectMap.put("platformName", websiteVO.getPlatFromName());
        objectMap.put("url", websiteVO.getPlatFromName());
        objectMap.put("bidEndTime", DateUtil.date(bidSheetVO.getBidEndTime()));

        MailBeanInfo mailBeanInfo = new MailBeanInfo();
        mailBeanInfo.setMail(sysUserInfoVO.getMail());
        mailBeanInfo.setName(sysUserInfoVO.getName());

        extraMailNotificationInfoDTO.setExtra(objectMap);
        extraMailNotificationInfoDTO.setTo(Collections.singletonList(mailBeanInfo));
        ieService.emailNotification(extraMailNotificationInfoDTO);
    }

}
