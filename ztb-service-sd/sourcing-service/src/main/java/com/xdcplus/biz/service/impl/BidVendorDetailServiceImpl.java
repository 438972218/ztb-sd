package com.xdcplus.biz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.xdcplus.biz.common.constants.ZtbConstant;
import com.xdcplus.biz.common.pojo.dto.BidDetailFilterDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDetailExcelDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDetailFilterDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorFilterDTO;
import com.xdcplus.biz.common.pojo.query.BidVendorDetailQuery;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.generator.impl.BidVendorDetailBaseServiceImpl;
import com.xdcplus.biz.mapper.BidVendorDetailMapper;
import com.xdcplus.biz.common.pojo.entity.BidVendorDetail;
import com.xdcplus.biz.remote.service.IeService;
import com.xdcplus.biz.service.BidDetailService;
import com.xdcplus.biz.service.BidSheetService;
import com.xdcplus.biz.service.BidVendorDetailService;
import com.xdcplus.biz.service.BidVendorService;
import com.xdcplus.ztb.common.exceptions.ZtbWebException;
import com.xdcplus.ztb.common.mail.EmailUtil;
import com.xdcplus.ztb.common.remote.domain.workflow.vo.RequestVO;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.excel.ExcelDataDTO;
import com.xdcplus.ztb.common.excel.ExportExcelUtil;
import com.xdcplus.ztb.common.tool.enums.ResponseEnum;
import com.xdcplus.ztb.common.vendorxlsx.ExcelUtils;
import com.xdcplus.ztb.common.vendorxlsx.ExtInfo;
import com.xdcplus.ztb.common.vendorxlsx.RequestInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 供应商内容明细（国内报价、国外报价）(BidVendorDetail)表服务实现类
 *
 * @author Fish.Fei
 * @since 2021-08-19 16:23:42
 */
@Slf4j
@Service("bidVendorDetailService")
public class BidVendorDetailServiceImpl extends BidVendorDetailBaseServiceImpl<BidVendorDetail, BidVendorDetailVO, BidVendorDetail, BidVendorDetailMapper> implements BidVendorDetailService {

    @Autowired
    BidVendorService bidVendorService;

    @Autowired
    BidDetailService bidDetailService;

    @Autowired
    BidSheetService bidSheetService;

    @Autowired
    IeService ieService;

    @Override
    public List<BidVendorDetailVO> queryNewBidVendorDetail(BidVendorDetailFilterDTO bidVendorDetailFilterDTO) {
        return this.objectConversion(bidVendorDetailMapper.queryNewBidVendorDetail(
                BeanUtil.copyProperties(bidVendorDetailFilterDTO, BidVendorDetailQuery.class)));
    }

    @Override
    public List<BidVendorDetailMapVO> queryBidVendorDetailMap(BidVendorDetailFilterDTO bidVendorDetailFilterDTO) {
//        List<BidVendorDetailVO> bidVendorDetailVOS = queryNewBidVendorDetail(bidVendorDetailFilterDTO);
//        Map<String,List<BidVendorDetailVO>> map =bidVendorDetailVOS.stream()
//                .collect(Collectors.groupingBy(bidVendorDetailVO -> bidVendorDetailVO.getRound()+"_"+bidVendorDetailVO.getType()));
//        List<BidVendorDetailMapVO> bidVendorDetailMapVOS = map.keySet().stream().map(key ->{
//           String[] temp=key.split("_");
//            String round = temp[0];
//            String type = temp[1];
//            BidVendorDetailMapVO bidVendorDetailMapVO = new BidVendorDetailMapVO();
//            bidVendorDetailMapVO.setRound(Integer.valueOf(round));
//            bidVendorDetailMapVO.setType(type);
//            bidVendorDetailMapVO.setBidVendorDetailVOS(map.get(key));
//            return bidVendorDetailMapVO;
//        }).collect(Collectors.toList());
//
//        return bidVendorDetailMapVOS;

        return bidVendorDetailMapper.queryBidVendorDetailMap(BeanUtil.copyProperties(bidVendorDetailFilterDTO, BidVendorDetailQuery.class));
    }

    @Override
    public void exportExcelCommercial(HttpServletResponse response, Long bidSheetId) {
        BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidSheetId);
        RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());
        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(bidSheetId);
        bidDetailFilterDTO.setBidType(ZtbConstant.COMMERCE);
        bidDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList(ZtbConstant.OFFER_INFORMATION));
        List<BidDetailVO> bidDetailVOS = bidDetailService.queryBidDetailVOList(bidDetailFilterDTO);

        BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(bidSheetId);
        List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);

        Map<Long, List<BidVendorVO>> bidVendorVOMap = bidVendorVOS.stream()
                .collect(Collectors.groupingBy(BidVendorVO::getId));


        BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
        bidVendorDetailFilterDTO.setBidSheetId(bidSheetId);
        bidVendorDetailFilterDTO.setBidType(ZtbConstant.COMMERCE);
        bidVendorDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList(ZtbConstant.OFFER_INFORMATION));
        List<BidVendorDetailVO> bidVendorDetailVOS = queryNewBidVendorDetail(bidVendorDetailFilterDTO);

        Map<Long, List<BidVendorDetailVO>> bidVendorDetailMap = bidVendorDetailVOS.stream()
                .collect(Collectors.groupingBy(BidVendorDetailVO::getBidVendorId));

        //费用名称
        String stus[][] = new String[bidDetailVOS.size() + 1][bidVendorDetailMap.size() + 2];
        for (int i = 0; i < bidDetailVOS.size(); i++) {
            stus[i + 1][0] = bidDetailVOS.get(i).getType();
            stus[i + 1][1] = bidDetailVOS.get(i).getName();
        }

        //供应商信息
        for (int i = 0; i < bidVendorVOS.size(); i++) {
            stus[0][i + 2] = String.valueOf(bidVendorVOS.get(i).getId());
        }

        for (Map.Entry<Long, List<BidVendorDetailVO>> entry : bidVendorDetailMap.entrySet()) {
            String customerId = String.valueOf(entry.getKey());
            List<BidVendorDetailVO> bidVendorDetailVOS1 = bidVendorDetailMap.get(entry.getKey());
            for (int i = 0; i < bidVendorVOS.size(); i++) {
                //供应商
                String d = stus[0][i + 2];
                if (stus[0][i + 2].equals(customerId)) {
                    for (BidVendorDetailVO bidVendorDetailVO : bidVendorDetailVOS1) {
                        for (int j = 0; j < bidDetailVOS.size(); j++) {
                            String type = stus[j + 1][0];
                            String name = stus[j + 1][1];
                            if (bidVendorDetailVO.getType().equals(stus[j + 1][0]) &&
                                    bidVendorDetailVO.getName().equals(stus[j + 1][1])) {
                                if (bidVendorDetailVO.getTotalPrice() == null) {
                                    stus[j + 1][i + 2] = "";
                                } else {
                                    stus[j + 1][i + 2] = String.valueOf(bidVendorDetailVO.getTotalPrice());
                                }
                            }
                        }
                    }
                }
            }
        }

        String[] vendorStus = stus[0];

        for (int i = 2; i < vendorStus.length; i++) {
            List<BidVendorVO> bidVendorVOS1 = bidVendorVOMap.get(Long.valueOf(vendorStus[i]));
            BidVendorVO bidVendorVO = bidVendorVOS1.get(NumberConstant.ZERO);
            vendorStus[i] = bidVendorVO.getVendorName();
        }
        stus[0] = vendorStus;
        try {
            ExcelDataDTO excelDataDTO = new ExcelDataDTO();
            excelDataDTO.setStus(stus);

            ExcelDataDTO data = new ExcelDataDTO();
//            data.setName("test");

            List<String> titles = new ArrayList();
            for (int j = 0; j < stus[0].length; j++) {
                titles.add(stus[0][j]);
            }
            data.setTitles(titles);
            data.setStus(stus);

            ExportExcelUtil.exportExcel(response, requestVO.getOddNumber() + ".xlsx", data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exportExcelbidVendorDetail(HttpServletResponse response, Long bidSheetId) {
        BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
        bidVendorDetailFilterDTO.setBidSheetId(bidSheetId);
        List<BidVendorDetailExcelVO> bidVendorDetailExcelVOS = queryBidVendorDetailExcel(bidVendorDetailFilterDTO);
        BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidSheetId);
        RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());

        try {
            ExportExcelUtil.export(response, BidVendorDetailExcelVO.class, requestVO.getOddNumber(), bidVendorDetailExcelVOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BidVendorDetailExcelVO> queryBidVendorDetailExcel(BidVendorDetailFilterDTO bidVendorDetailFilterDTO) {
        List<BidVendorDetailVO> bidVendorDetailVOS = queryNewBidVendorDetail(bidVendorDetailFilterDTO);
        if(CollectionUtil.isEmpty(bidVendorDetailVOS)){
            return null;
        }

        return bidVendorDetailVOS.stream()
                .filter(bidVendorDetailVO -> bidVendorDetailVO.getBidVendorId()!=null)
                .map(bidVendorDetailVO -> {
            BidVendorVO bidVendorVO = bidVendorService.queryBidVendorById(bidVendorDetailVO.getBidVendorId());
            if(ObjectUtil.isNotEmpty(bidVendorVO)){
                BidVendorDetailExcelVO bidVendorDetailExcelVO = BeanUtil.copyProperties(bidVendorDetailVO,BidVendorDetailExcelVO.class);
                bidVendorDetailExcelVO.setBidVendorName(bidVendorVO.getVendorName());
                return bidVendorDetailExcelVO;
            }else{
                return null;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<BidVendorDetailExcelDTO> getBidVendorDetailExcelDTO(BidVendorDetailFilterDTO bidVendorDetailFilterDTO) {
        List<BidVendorDetail> bidVendorDetails = bidVendorDetailMapper.queryNewBidVendorDetail(BeanUtil.copyProperties(bidVendorDetailFilterDTO, BidVendorDetailQuery.class));


        Map<String, List<BidVendorDetail>> bidVendorDetailMap = bidVendorDetails.stream()
                .collect(Collectors.groupingBy(bidVendorDetail ->
                        bidVendorDetail.getBidType()+"_"+bidVendorDetail.getType()+"_"+bidVendorDetail.getName()));
        List<BidVendorDetailExcelDTO> countRecords = bidVendorDetailMap.keySet().stream().map(key -> {
            String[] temp = key.split("_");
            String bidType = temp[0];
            String type = temp[1];
            String name = temp[2];
            BidVendorDetailExcelDTO bidVendorDetailExcelDTO = new BidVendorDetailExcelDTO();
            bidVendorDetailExcelDTO.setBidType(bidType);
            bidVendorDetailExcelDTO.setType(type);
            bidVendorDetailExcelDTO.setName(name);

            List<BidVendorDetail> bidVendorDetails1 = bidVendorDetailMap.get(key);
            if (CollectionUtil.isNotEmpty(bidVendorDetails1)) {

                List<BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO> tList = new ArrayList<>();
                for (BidVendorDetail bidVendorDetail : bidVendorDetails1) {
                    BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO t = BeanUtil.copyProperties(bidVendorDetail, BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO.class);
                    if(ObjectUtil.isNotEmpty(t)){
                        t.setBidVendorName(bidVendorService.queryBidVendorById(t.getBidVendorId()).getVendorName());
                    }
                    Optional.ofNullable(t).ifPresent(tList::add);
                }
                bidVendorDetailExcelDTO.setBidVendorDetailExcelVendorDTOS(tList);
            }
            return bidVendorDetailExcelDTO;
        }).collect(Collectors.toList());

        return countRecords;
    }

    @Override
    public void exportExcelCommercialNew(HttpServletResponse response, Long bidSheetId) {
        try {
            BidVendorDetailFilterDTO bidVendorDetailFilterDTO=new BidVendorDetailFilterDTO();
            bidVendorDetailFilterDTO.setBidSheetId(bidSheetId);
            bidVendorDetailFilterDTO.setBidType("商业标");
            bidVendorDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList("报价须知"));
            List<BidVendorDetailExcelDTO> bidVendorDetailExcelDTOS = getBidVendorDetailExcelDTO(bidVendorDetailFilterDTO);
            //1.创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle headStyle = ExportExcelUtil.createCellStyle(workbook,(short)10,false,true,false, null,false);
            HSSFCellStyle headStyle1 = ExportExcelUtil.createCellStyle(workbook,(short)10,false,true,true, IndexedColors.GREY_25_PERCENT.getIndex(),false);
            HSSFCellStyle headStyleFigure = ExportExcelUtil.createCellStyle(workbook,(short)10,false,true,false, null,true);
            HSSFCellStyle headStyleFigure1 = ExportExcelUtil.createCellStyle(workbook,(short)10,false,true,true, IndexedColors.GREY_25_PERCENT.getIndex(),true);

            //2.创建工作表
            HSSFSheet sheet = workbook.createSheet("test");

            int customerSize = bidVendorDetailExcelDTOS.get(NumberConstant.ZERO).getBidVendorDetailExcelVendorDTOS().size();

            //3.2创建列标题;并且设置列标题
            HSSFRow row2 = sheet.createRow(1);

            HSSFCell cell2A = row2.createCell(0);
            cell2A.setCellStyle(headStyle1);
            cell2A.setCellValue("标类型（商业标，资质标，技术标）");

            HSSFCell cell2B = row2.createCell(1);
            cell2B.setCellStyle(headStyle1);
            cell2B.setCellValue("类型(报价须知、国内报价、国外报价)");

            HSSFCell cell2C = row2.createCell(2);
            cell2C.setCellStyle(headStyle1);
            cell2C.setCellValue("标题");

            HSSFCell cell2D = row2.createCell(3);
            cell2D.setCellStyle(headStyle1);
            cell2D.setCellValue("说明");

            HSSFCell cell2E = row2.createCell(4);
            cell2E.setCellStyle(headStyle1);
            cell2E.setCellValue("数量");

            HSSFCell cell2F = row2.createCell(5);
            cell2F.setCellStyle(headStyle1);
            cell2F.setCellValue("单位");

            for(int i=0;i<customerSize;i++){
                int offset = i*6;

                CellRangeAddress customerCallRangeAddress = new CellRangeAddress(0,0,6+offset,11+offset);//起始行,结束行,起始列,结束列
                //2.1加载合并单元格对象
                sheet.addMergedRegion(customerCallRangeAddress);

                HSSFCell celliG = row2.createCell(6+offset);
                celliG.setCellStyle(headStyle1);
                celliG.setCellValue("轮次");

                HSSFCell celliH = row2.createCell(7+offset);
                celliH.setCellStyle(headStyle1);
                celliH.setCellValue("单价");

                HSSFCell celliI = row2.createCell(8+offset);
                celliI.setCellStyle(headStyle1);
                celliI.setCellValue("ETD");

                HSSFCell celliJ = row2.createCell(9+offset);
                celliJ.setCellStyle(headStyle1);
                celliJ.setCellValue("ETA");

                HSSFCell celliK = row2.createCell(10+offset);
                celliK.setCellStyle(headStyle1);
                celliK.setCellValue("船期（每周几）");

                HSSFCell celliL = row2.createCell(11+offset);
                celliL.setCellStyle(headStyle1);
                celliL.setCellValue("船公司");
            }

            HSSFRow row1 = sheet.createRow(0);
            for(int i=0;i<bidVendorDetailExcelDTOS.size();i++){
                HSSFRow row3 = sheet.createRow(2+i);

                HSSFCell cell3A = row3.createCell(0);
                cell3A.setCellStyle(headStyle);
                cell3A.setCellValue(bidVendorDetailExcelDTOS.get(i).getBidType());

                HSSFCell cell3B = row3.createCell(1);
                cell3B.setCellStyle(headStyle);
                cell3B.setCellValue(bidVendorDetailExcelDTOS.get(i).getType());

                HSSFCell cell3C = row3.createCell(2);
                cell3C.setCellStyle(headStyle);
                cell3C.setCellValue(bidVendorDetailExcelDTOS.get(i).getName());

                HSSFCell cell3D = row3.createCell(3);
                cell3D.setCellStyle(headStyle);
                cell3D.setCellValue(bidVendorDetailExcelDTOS.get(i).getExplaination());

                HSSFCell cell3E = row3.createCell(4);
                cell3E.setCellStyle(headStyle);
                if(bidVendorDetailExcelDTOS.get(i).getQuantity()!=null){
                    cell3E.setCellValue(bidVendorDetailExcelDTOS.get(i).getQuantity());
                }

                HSSFCell cell3F = row3.createCell(5);
                cell3F.setCellStyle(headStyle);
                cell3F.setCellValue(bidVendorDetailExcelDTOS.get(i).getUnit());

                List<BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO> bidVendorDetailExcelVendorDTOS = bidVendorDetailExcelDTOS.get(i).getBidVendorDetailExcelVendorDTOS();
                int a=0;

                for (BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO bidVendorDetailExcelVendorDTO : bidVendorDetailExcelVendorDTOS) {
                    int offset=a*6;
                    if(i==0){
                        HSSFCell cell1 = row1.createCell(6+offset);
                        if(a%2!=0){
                            cell1.setCellStyle(headStyle);
                        }else{
                            cell1.setCellStyle(headStyle1);
                        }
                        cell1.setCellValue(bidVendorDetailExcelVendorDTO.getBidVendorName());
                    }

                    HSSFCell celliG = row3.createCell(6+offset);
                    celliG.setCellValue(bidVendorDetailExcelVendorDTO.getRound());

                    HSSFCell celliH = row3.createCell(7+offset);
                    if(a%2!=0){
                        celliH.setCellStyle(headStyleFigure);
                    }else{
                        celliH.setCellStyle(headStyleFigure1);
                    }
                    if(bidVendorDetailExcelVendorDTO.getPrice()!=null){
                        celliH.setCellValue(bidVendorDetailExcelVendorDTO.getPrice());
                    }

                    HSSFCell celliI = row3.createCell(8+offset);
                    celliI.setCellValue(bidVendorDetailExcelVendorDTO.getEtd());

                    HSSFCell celliJ = row3.createCell(9+offset);
                    celliJ.setCellValue(bidVendorDetailExcelVendorDTO.getEta());

                    HSSFCell celliK = row3.createCell(10+offset);
                    celliK.setCellValue(bidVendorDetailExcelVendorDTO.getShippingSchedule());

                    HSSFCell celliL = row3.createCell(11+offset);
                    celliL.setCellValue(bidVendorDetailExcelVendorDTO.getShippingCompany());

                    if(a%2!=0){
                        celliG.setCellStyle(headStyle);
                        celliI.setCellStyle(headStyle);
                        celliJ.setCellStyle(headStyle);
                        celliK.setCellStyle(headStyle);
                        celliL.setCellStyle(headStyle);
                    }else{
                        celliG.setCellStyle(headStyle1);
                        celliI.setCellStyle(headStyle1);
                        celliJ.setCellStyle(headStyle1);
                        celliK.setCellStyle(headStyle1);
                        celliL.setCellStyle(headStyle1);
                    }

                    a++;
                }
            }
            BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidSheetId);
            RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());
            ExportExcelUtil.setResponseHeader(response,requestVO.getOddNumber()+".xls");
            OutputStream os = response.getOutputStream();
            workbook.write(os);

        }catch(Exception e){
            throw new ZtbWebException(-1,e.toString());
        }
    }

    @Override
    public void exportExcelCommercialNew1(HttpServletResponse response, Long bidSheetId) {
        List<Object> list = new ArrayList<>();

        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(bidSheetId);
        bidDetailFilterDTO.setBidType(ZtbConstant.COMMERCE);
        bidDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList(ZtbConstant.OFFER_INFORMATION));
        List<BidDetailCollectVO> bidDetailCollectVOS = bidDetailService.queryBidDetailCollectVO(bidDetailFilterDTO);
        BidDetailCollectVO bidDetailCollectVO = bidDetailCollectVOS.get(0);
        List<String> bidTypes = Arrays.asList(bidDetailCollectVO.getBidType().split(","));
        List<String> types = Arrays.asList(bidDetailCollectVO.getType().split(","));
        List<String> names = Arrays.asList(bidDetailCollectVO.getName().split(","));
        List<String> explainations = Arrays.asList(bidDetailCollectVO.getExplaination().split(","));
        List<String> quantitys = Arrays.asList(bidDetailCollectVO.getQuantity().split(","));
        List<String> units = Arrays.asList(bidDetailCollectVO.getUnit().split(","));

        ExtInfo extInfo = new ExtInfo(bidTypes,types,names,explainations,quantitys,units);
        list.add(extInfo);

        BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
        bidVendorDetailFilterDTO.setBidSheetId(bidSheetId);
        bidVendorDetailFilterDTO.setBidType(ZtbConstant.COMMERCE);
        bidVendorDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList(ZtbConstant.OFFER_INFORMATION));
        bidVendorDetailFilterDTO.setNotStatuses(CollectionUtil.newArrayList(ZtbConstant.HAS_REFUSED));

        List<BidVendorDetailVendorVO> bidVendorDetailVendorVOS = queryBidVendorDetailVendorVO(bidVendorDetailFilterDTO);

        for (BidVendorDetailVendorVO bidVendorDetailVendorVO : bidVendorDetailVendorVOS) {
            List<BidVendorDetailVO> bidVendorDetailVOS = bidVendorDetailVendorVO.getBidVendorDetailVOS();
            List<String> rounds = bidVendorDetailVOS.stream().map(bidVendorDetailVO -> String.valueOf(bidVendorDetailVO.getRound())).collect(Collectors.toList());
            List<String> prices = bidVendorDetailVOS.stream()
                    .map(bidVendorDetailVO -> {
                        if(bidVendorDetailVO.getPrice()==null){
                            return "";
                        }else{
                            return String.valueOf(bidVendorDetailVO.getPrice());
                        }
                    })
                    .collect(Collectors.toList());

            List<String> etds = bidVendorDetailVOS.stream()
                    .map(bidVendorDetailVO -> StrUtil.nullToEmpty(bidVendorDetailVO.getEtd()))
                    .collect(Collectors.toList());
            List<String> etas = bidVendorDetailVOS.stream()
                    .map(bidVendorDetailVO -> StrUtil.nullToEmpty(bidVendorDetailVO.getEta()))
                    .collect(Collectors.toList());
            List<String> shippingSchedules = bidVendorDetailVOS.stream()
                    .map(bidVendorDetailVO -> StrUtil.nullToEmpty(bidVendorDetailVO.getShippingSchedule()))
                    .collect(Collectors.toList());
            List<String> shippingCompany = bidVendorDetailVOS.stream()
                    .map(bidVendorDetailVO -> StrUtil.nullToEmpty(bidVendorDetailVO.getShippingCompany()))
                    .collect(Collectors.toList());
            BidVendorVO bidVendorVO = bidVendorService.queryBidVendorById(bidVendorDetailVendorVO.getBidVendorId());
            list.add(new RequestInfo(rounds,prices,etds,etas,shippingSchedules,shippingCompany,bidVendorVO.getVendorName()));
        }

//        for (BidVendorDetailCollectVO bidVendorDetailCollectVO : bidVendorDetailCollectVOS) {
//            List<String> rounds = Arrays.asList(bidVendorDetailCollectVO.getRound().split(","));
//            List<String> prices = Arrays.asList(bidVendorDetailCollectVO.getPrice().split(","));
//            List<String> etds = Arrays.asList(bidVendorDetailCollectVO.getEtd().split(","));
//            List<String> etas = Arrays.asList(bidVendorDetailCollectVO.getEta().split(","));
//            List<String> shippingSchedules = Arrays.asList(bidVendorDetailCollectVO.getShippingSchedule().split(","));
//            List<String> shippingCompany = Arrays.asList(bidVendorDetailCollectVO.getShippingCompany().split(","));
//            BidVendorVO bidVendorVO = bidVendorService.queryBidVendorById(bidVendorDetailCollectVO.getBidVendorId());
//            list.add(new RequestInfo(rounds,prices,etds,etas,shippingSchedules,shippingCompany,bidVendorVO.getVendorName()));
//        }
        BidSheetVO bidSheetVO = bidSheetService.queryBidSheetById(bidSheetId);
        RequestVO requestVO = ieService.findRequestById(bidSheetVO.getRequestId());
        ExcelUtils.exportExcel(list,requestVO.getOddNumber()+".xls", response);
    }

    @Override
    public List<BidVendorDetailCollectVO> queryBidVendorDetailCollectVO(BidVendorDetailFilterDTO bidVendorDetailFilterDTO) {
        return bidVendorDetailMapper.queryBidVendorDetailCollectVO(BeanUtil.copyProperties(bidVendorDetailFilterDTO, BidVendorDetailQuery.class));
    }

    @Override
    public List<BidVendorDetailVendorVO> queryBidVendorDetailVendorVO(BidVendorDetailFilterDTO bidVendorDetailFilterDTO) {
        return bidVendorDetailMapper.queryBidVendorDetailVendorVO(BeanUtil.copyProperties(bidVendorDetailFilterDTO, BidVendorDetailQuery.class));
    }

//    @Override
//    public void batchUpdateBidDetailDTO(BidVendorDTO bidVendorDTO) {
//        //修改供应商文件明细（状态为提交）
//        //修改供应商状态为已回复
//        List<BidVendorDetailDTO> bidVendorDetailDTOS = bidVendorDTO.getBidVendorDetailDTOS();
//        saveOrUpdateBatchByDTOList(bidVendorDetailDTOS);
//        bidVendorDTO.setVendorStatus(ZtbConstant.VENDOR_REPLIED);
//        bidVendorService.updateBidVendorStatus(bidVendorDTO);
//    }
}
