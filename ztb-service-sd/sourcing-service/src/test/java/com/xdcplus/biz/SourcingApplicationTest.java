package com.xdcplus.biz;


import cn.hutool.core.collection.CollectionUtil;
import com.xdcplus.biz.common.pojo.dto.BidDetailFilterDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDetailExcelDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorDetailFilterDTO;
import com.xdcplus.biz.common.pojo.dto.BidVendorFilterDTO;
import com.xdcplus.biz.common.pojo.vo.*;
import com.xdcplus.biz.service.BidDetailService;
import com.xdcplus.biz.service.BidVendorDetailService;
import com.xdcplus.biz.service.BidVendorService;
import com.xdcplus.ztb.common.tool.constants.NumberConstant;
import com.xdcplus.ztb.common.vendorxlsx.ExcelUtils;
import com.xdcplus.ztb.common.vendorxlsx.ExtInfo;
import com.xdcplus.ztb.common.vendorxlsx.RequestInfo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class SourcingApplicationTest {
//    @Autowired
//    public VendorSiteInsService vendorSiteInsService;
//    @Autowired
//    public VendorBaseService vendorService;

    @Autowired
    BidVendorDetailService bidVendorDetailService;

    @Autowired
    BidDetailService bidDetailService;

    @Autowired
    BidVendorService bidVendorService;

    @Test
    void contextLoads() {
    }


    @Test
    void testQueryCompanyById() {
//        CompanyVO companyVO = PceCompanyResponseUtils.queryById(1L);
//        System.out.println("aa");
    }

    @Test
    void test2() {
////        vendorSiteInsService.calculateScore(1L);
//        Vendor vendor = new Vendor();
//        vendor.setId(null);
//        vendor.setStatus("已过审");
//        vendorService.saveOrUpdate(vendor);
//    }
//    //    @LoadBalanced
////    public RestTemplate restTemplage() {
////        return new RestTemplate();
////    }
    }

    @Test
    public void test3() throws IOException {
        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(1443061377119305729L);
        bidDetailFilterDTO.setTypes(CollectionUtil.newArrayList("国外费用", "国内费用"));
        List<BidDetailVO> bidDetailVOS = bidDetailService.queryBidDetailVOList(bidDetailFilterDTO);

        BidVendorFilterDTO bidVendorFilterDTO = new BidVendorFilterDTO();
        bidVendorFilterDTO.setBidSheetId(1443061377119305729L);
        List<BidVendorVO> bidVendorVOS = bidVendorService.queryBidVendorVOList(bidVendorFilterDTO);

        Map<Long, List<BidVendorVO>> bidVendorVOMap = bidVendorVOS.stream()
                .collect(Collectors.groupingBy(BidVendorVO::getId));

        BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
        bidVendorDetailFilterDTO.setBidSheetId(1443061377119305729L);
        List<BidVendorDetailVO> bidVendorDetailVOS = bidVendorDetailService.queryNewBidVendorDetail(bidVendorDetailFilterDTO);

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
                                stus[j + 1][i + 2] = String.valueOf(bidVendorDetailVO.getTotalPrice());
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

        export1(stus);

        System.out.println("aa");
    }

    public void export1(String[][] array) throws IOException {
        //创建一个内存Excel对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个表格
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //创建表头
        //获取表头内容  也就是resource集合的第零个索引的字符串数组
        String[] headerStr = array[0];
        HSSFRow headeRow = sheet.createRow(0);
        //设置列宽
        for (int i = 0; i < headerStr.length; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        //设置头单元格样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        //设置字体
        HSSFFont headerFont = workbook.createFont();
        //headerFont.setColor(HSSFColor.VIOLET.index);
        headerFont.setFontName("宋体");
        headerStyle.setFont(headerFont);

        //定义表头内容
        for (int i = 0; i < headerStr.length; i++) {
            //定义一个单元格
            HSSFCell headerCell = headeRow.createCell(i);
            headerCell.setCellStyle(headerStyle);
            headerCell.setCellValue(headerStr[i]);
        }

        //标题内容
        //样式
        HSSFCellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        //设置字体
        HSSFFont bodyFont = workbook.createFont();
        //bodyFont.setColor(HSSFColor.BLUE.index);
        bodyFont.setFontName("宋体");
        bodyStyle.setFont(bodyFont);

        //创建行，上面表头已经创建过第0行了，所以这里从1开始
        for (int row = 1; row < array.length; row++) {
            //输出的行数据
            String[] temp = array[row];
            //创建行
            HSSFRow bodyRow = sheet.createRow(row);
            //循环创建列
            for (int cell = 0; cell < temp.length; cell++) {
                HSSFCell bodyCell = bodyRow.createCell(cell);
                bodyCell.setCellStyle(bodyStyle);
                bodyCell.setCellValue(temp[cell]);
            }


        }
        //将内存创建的excel对象，输出到文件夹中
//        workbook.write(outputStream);
        workbook.write(new FileOutputStream(new File("C:\\Users\\fish.fei\\Desktop\\aa1.xls")));
    }

    @Test
    public void test4() throws Exception {
        FileOutputStream fout = new FileOutputStream("D:\\文件\\新建文件夹\\students.xls");
        getValue(fout);
        fout.close();
    }

    public void getValue(FileOutputStream fout) throws Exception {
        try {
            BidVendorDetailFilterDTO bidVendorDetailFilterDTO = new BidVendorDetailFilterDTO();
            bidVendorDetailFilterDTO.setBidSheetId(1447744214976180225L);
            bidVendorDetailFilterDTO.setBidType("商业标");
            bidVendorDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList("报价须知"));
            List<BidVendorDetailExcelDTO> bidVendorDetailExcelDTOS = bidVendorDetailService.getBidVendorDetailExcelDTO(bidVendorDetailFilterDTO);
            //1.创建工作簿
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFCellStyle headStyle = createCellStyle(workbook, (short) 10, false, true, false, null, false);
            HSSFCellStyle headStyle1 = createCellStyle(workbook, (short) 10, false, true, true, IndexedColors.GREY_25_PERCENT.getIndex(), false);
            HSSFCellStyle headStyleFigure = createCellStyle(workbook, (short) 10, false, true, false, null, true);
            HSSFCellStyle headStyleFigure1 = createCellStyle(workbook, (short) 10, false, true, true, IndexedColors.GREY_25_PERCENT.getIndex(), true);

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

            for (int i = 0; i < customerSize; i++) {
                int offset = i * 6;

                CellRangeAddress customerCallRangeAddress = new CellRangeAddress(0, 0, 6 + offset, 11 + offset);//起始行,结束行,起始列,结束列
                //2.1加载合并单元格对象
                sheet.addMergedRegion(customerCallRangeAddress);

                HSSFCell celliG = row2.createCell(6 + offset);
                celliG.setCellStyle(headStyle1);
                celliG.setCellValue("轮次");

                HSSFCell celliH = row2.createCell(7 + offset);
                celliH.setCellStyle(headStyle1);
                celliH.setCellValue("单价");

                HSSFCell celliI = row2.createCell(8 + offset);
                celliI.setCellStyle(headStyle1);
                celliI.setCellValue("ETD");

                HSSFCell celliJ = row2.createCell(9 + offset);
                celliJ.setCellStyle(headStyle1);
                celliJ.setCellValue("ETA");

                HSSFCell celliK = row2.createCell(10 + offset);
                celliK.setCellStyle(headStyle1);
                celliK.setCellValue("船期（每周几）");

                HSSFCell celliL = row2.createCell(11 + offset);
                celliL.setCellStyle(headStyle1);
                celliL.setCellValue("船公司");
            }

            HSSFRow row1 = sheet.createRow(0);
            for (int i = 0; i < bidVendorDetailExcelDTOS.size(); i++) {
                HSSFRow row3 = sheet.createRow(2 + i);

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
                if (bidVendorDetailExcelDTOS.get(i).getQuantity() != null) {
                    cell3E.setCellValue(bidVendorDetailExcelDTOS.get(i).getQuantity());
                }

                HSSFCell cell3F = row3.createCell(5);
                cell3F.setCellStyle(headStyle);
                cell3F.setCellValue(bidVendorDetailExcelDTOS.get(i).getUnit());

                List<BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO> bidVendorDetailExcelVendorDTOS = bidVendorDetailExcelDTOS.get(i).getBidVendorDetailExcelVendorDTOS();
                int a = 0;

                for (BidVendorDetailExcelDTO.BidVendorDetailExcelVendorDTO bidVendorDetailExcelVendorDTO : bidVendorDetailExcelVendorDTOS) {
                    int offset = a * 6;
                    if (i == 0) {
                        HSSFCell cell1 = row1.createCell(6 + offset);
                        if (a % 2 != 0) {
                            cell1.setCellStyle(headStyle);
                        } else {
                            cell1.setCellStyle(headStyle1);
                        }
                        cell1.setCellValue(bidVendorDetailExcelVendorDTO.getBidVendorName());
                    }

                    HSSFCell celliG = row3.createCell(6 + offset);
                    celliG.setCellValue(bidVendorDetailExcelVendorDTO.getRound());

                    HSSFCell celliH = row3.createCell(7 + offset);
                    if (a % 2 != 0) {
                        celliH.setCellStyle(headStyleFigure);
                    } else {
                        celliH.setCellStyle(headStyleFigure1);
                    }
                    if (bidVendorDetailExcelVendorDTO.getPrice() != null) {
                        celliH.setCellValue(bidVendorDetailExcelVendorDTO.getPrice());
                    }

                    HSSFCell celliI = row3.createCell(8 + offset);
                    celliI.setCellValue(bidVendorDetailExcelVendorDTO.getEtd());

                    HSSFCell celliJ = row3.createCell(9 + offset);
                    celliJ.setCellValue(bidVendorDetailExcelVendorDTO.getEta());

                    HSSFCell celliK = row3.createCell(10 + offset);
                    celliK.setCellValue(bidVendorDetailExcelVendorDTO.getShippingSchedule());

                    HSSFCell celliL = row3.createCell(11 + offset);
                    celliL.setCellValue(bidVendorDetailExcelVendorDTO.getShippingCompany());

                    if (a % 2 != 0) {
                        celliG.setCellStyle(headStyle);
                        celliI.setCellStyle(headStyle);
                        celliJ.setCellStyle(headStyle);
                        celliK.setCellStyle(headStyle);
                        celliL.setCellStyle(headStyle);
                    } else {
                        celliG.setCellStyle(headStyle1);
                        celliI.setCellStyle(headStyle1);
                        celliJ.setCellStyle(headStyle1);
                        celliK.setCellStyle(headStyle1);
                        celliL.setCellStyle(headStyle1);
                    }

                    a++;
                }
            }
            workbook.write(fout);

        } catch (Exception e) {
            throw new Exception(e.toString());

        }
    }


//    public void getValue(List<WorkSheetDetail> userList,FileOutputStream fout){
//        try{
//            //1.创建工作簿
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            //1.1创建合并单元格对象
//            CellRangeAddress callRangeAddress = new CellRangeAddress(0,0,0,7);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress1 = new CellRangeAddress(1,1,0,7);//起始行,结束行,起始列,结束列
//            //班组与时间start
//            CellRangeAddress callRangeAddress20 = new CellRangeAddress(2,2,0,2);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress21 = new CellRangeAddress(2,2,3,4);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress22 = new CellRangeAddress(2,2,5,7);//起始行,结束行,起始列,结束列
//            //班组与时间end
//
//            //标题
//            CellRangeAddress callRangeAddress31 = new CellRangeAddress(3,4,0,0);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress32 = new CellRangeAddress(3,4,1,1);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress33 = new CellRangeAddress(3,4,2,2);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress34 = new CellRangeAddress(3,3,3,4);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress35 = new CellRangeAddress(3,4,5,5);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress36 = new CellRangeAddress(3,4,6,6);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddress37 = new CellRangeAddress(3,4,7,7);//起始行,结束行,起始列,结束列
//
//            //金额
//            CellRangeAddress callRangeAddressnumber1 = new CellRangeAddress(userList.size()+5,userList.size()+5,0,2);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddressnumber2 = new CellRangeAddress(userList.size()+5,userList.size()+5,3,7);//起始行,结束行,起始列,结束列
//
//            //负责人
//            CellRangeAddress callRangeAddressPersion1 = new CellRangeAddress(userList.size()+6,userList.size()+6,0,2);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddressPersion2 = new CellRangeAddress(userList.size()+6,userList.size()+6,3,4);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddressPersion3 = new CellRangeAddress(userList.size()+6,userList.size()+6,5,7);//起始行,结束行,起始列,结束列
//
//            //说明
//            CellRangeAddress callRangeAddressinfo = new CellRangeAddress(userList.size()+7,userList.size()+7,0,7);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddressinfo1 = new CellRangeAddress(userList.size()+8,userList.size()+8,0,7);//起始行,结束行,起始列,结束列
//            CellRangeAddress callRangeAddressinfo2 = new CellRangeAddress(userList.size()+9,userList.size()+9,0,7);//起始行,结束行,起始列,结束列
//
//            //部项目经理部
//            HSSFCellStyle headStyle = createCellStyle(workbook,(short)10,false,true);
//            //派工单
//            HSSFCellStyle erStyle = createCellStyle(workbook,(short)13,true,true);
//            //班组和时间
//            HSSFCellStyle sanStyle = createCellStyle(workbook,(short)10,false,false);
//            //标题样式
//            HSSFCellStyle colStyle = createCellStyle(workbook,(short)10,true,true);
//            //内容样式
//            HSSFCellStyle cellStyle = createCellStyle(workbook,(short)10,false,true);
//            //2.创建工作表
//            HSSFSheet sheet = workbook.createSheet("派单");
//            //2.1加载合并单元格对象
//            sheet.addMergedRegion(callRangeAddress);
//            sheet.addMergedRegion(callRangeAddress1);
//            sheet.addMergedRegion(callRangeAddress20);
//            sheet.addMergedRegion(callRangeAddress21);
//            sheet.addMergedRegion(callRangeAddress22);
//            sheet.addMergedRegion(callRangeAddress31);
//            sheet.addMergedRegion(callRangeAddress32);
//            sheet.addMergedRegion(callRangeAddress33);
//            sheet.addMergedRegion(callRangeAddress34);
//            sheet.addMergedRegion(callRangeAddress35);
//            sheet.addMergedRegion(callRangeAddress36);
//            sheet.addMergedRegion(callRangeAddress37);
//            sheet.addMergedRegion(callRangeAddressnumber1);
//            sheet.addMergedRegion(callRangeAddressnumber2);
//            sheet.addMergedRegion(callRangeAddressPersion1);
//            sheet.addMergedRegion(callRangeAddressPersion2);
//            sheet.addMergedRegion(callRangeAddressPersion3);
//            sheet.addMergedRegion(callRangeAddressinfo);
//            sheet.addMergedRegion(callRangeAddressinfo1);
//            sheet.addMergedRegion(callRangeAddressinfo2);
//            //设置默认列宽
//            sheet.setDefaultColumnWidth(15);
//            //3.创建行
//            //3.1创建头标题行;并且设置头标题
//            HSSFRow row = sheet.createRow(0);
//            HSSFCell cell = row.createCell(0);
//            //加载单元格样式
//            cell.setCellStyle(headStyle);
//            cell.setCellValue("xxxx项目部");
//
//            HSSFRow rower = sheet.createRow(1);
//            HSSFCell celler = rower.createCell(0);
//            //加载单元格样式
//            celler.setCellStyle(erStyle);
//            celler.setCellValue("派 工 单");
//
//            HSSFRow rowsan = sheet.createRow(2);
//            HSSFCell cellsan = rowsan.createCell(0);
//            HSSFCell cellsan1 = rowsan.createCell(3);
//            HSSFCell cellsan2 = rowsan.createCell(5);
//            //加载单元格样式
//            cellsan.setCellStyle(sanStyle);
//            cellsan.setCellValue("协作单位：x施工一堆");
//            cellsan1.setCellStyle(sanStyle);
//            cellsan1.setCellValue("");
//            cellsan2.setCellStyle(sanStyle);
//            cellsan2.setCellValue("时间：2017年 10月 20日");
//
//            //3.2创建列标题;并且设置列标题
//            HSSFRow row2 = sheet.createRow(3);
//            String[] titles = {"序号","工作内容","用工总人数","工日数","","单价（元）","金额(元）","备注"};//""为占位字符串
//            for(int i=0;i<titles.length;i++)
//            {
//                HSSFCell cell2 = row2.createCell(i);
//                //加载单元格样式
//                cell2.setCellStyle(colStyle);
//                cell2.setCellValue(titles[i]);
//            }
//
//            HSSFRow rowfour = sheet.createRow(4);
//            String[] titlefour = {"普工用工数","技工用工数"};
//            for(int i=0;i<titlefour.length;i++)
//            {
//                HSSFCell cell2 = rowfour.createCell(i+3);
//                //加载单元格样式
//                cell2.setCellStyle(colStyle);
//                cell2.setCellValue(titlefour[i]);
//            }
//
//
//            //4.操作单元格;将用户列表写入excel
//            if(userList != null)
//            {
//                int i=1;
//                for(int j=0;j<userList.size();j++)
//                {
//                    //创建数据行,前面有两行,头标题行和列标题行
//                    HSSFRow row3 = sheet.createRow(j+5);
//                    HSSFCell cell0 = row3.createCell(0);
//                    cell0.setCellStyle(cellStyle);
//                    cell0.setCellValue(i++);
//
//                    HSSFCell cell1 = row3.createCell(1);
//                    cell1.setCellStyle(cellStyle);
//                    cell1.setCellValue(userList.get(j).getWorkCtx());
//
//                    HSSFCell cell2 = row3.createCell(2);
//                    cell2.setCellStyle(cellStyle);
//                    cell2.setCellValue(userList.get(j).getTotalHumanDays());
//
//                    HSSFCell cell3 = row3.createCell(3);
//                    cell3.setCellStyle(cellStyle);
//                    cell3.setCellValue(userList.get(j).getGwnNum());
//
//                    HSSFCell cell4 = row3.createCell(4);
//                    cell4.setCellStyle(cellStyle);
//                    cell4.setCellValue(userList.get(j).getTmnNum());
//
//                    HSSFCell cell5 = row3.createCell(5);
//                    cell5.setCellStyle(cellStyle);
//                    cell5.setCellValue(userList.get(j).getTotalHumanDays());
//
//                    HSSFCell cell6 = row3.createCell(6);
//                    cell6.setCellStyle(cellStyle);
//                    cell6.setCellValue(userList.get(j).getUnitAmount());
//
//                    HSSFCell cell7= row3.createCell(7);
//                    cell7.setCellStyle(cellStyle);
//                    cell7.setCellValue(userList.get(j).getUnitPrice());
//                }
//            }
//
//            HSSFRow rownumber = sheet.createRow(userList.size()+5);
//            HSSFCell cellnumber = rownumber.createCell(0);
//            HSSFCell cellnumber1 = rownumber.createCell(3);
//            //加载单元格样式
//            cellnumber.setCellStyle(sanStyle);
//            cellnumber.setCellValue("金额合计(大写)");
//            cellnumber1.setCellStyle(sanStyle);
//            cellnumber1.setCellValue("￥ 78 元； 大写：柒拾捌元整");
//
//            HSSFRow rowpersion = sheet.createRow(userList.size()+6);
//            HSSFCell cellpersion = rowpersion.createCell(0);
//            HSSFCell cellpersion1 = rowpersion.createCell(3);
//            HSSFCell cellpersion2 = rowpersion.createCell(5);
//
//            //加载单元格样式
//            cellpersion.setCellStyle(sanStyle);
//            cellpersion.setCellValue("协作单位负责人：");
//            cellpersion1.setCellStyle(sanStyle);
//            cellpersion1.setCellValue("经办人：");
//            cellpersion2.setCellStyle(sanStyle);
//            cellpersion2.setCellValue("部门负责人：");
//
//            HSSFRow rowinfo = sheet.createRow(userList.size()+7);
//            HSSFCell cellinfo = rowinfo.createCell(0);
//            cellinfo.setCellStyle(sanStyle);
//            cellinfo.setCellValue("说明：1、本标工单一式两联，第一联为派工人（工长）存根，第二联用作结算。");
//
//            HSSFRow rowinfo1 = sheet.createRow(userList.size()+8);
//            HSSFCell cellinfo1 = rowinfo1.createCell(0);
//            cellinfo1.setCellStyle(sanStyle);
//            cellinfo1.setCellValue("2、本标工单必须在用工当日签认，否则不予认可；三日内交合同处汇总。");
//
//            HSSFRow rowinfo2 = sheet.createRow(userList.size()+9);
//            HSSFCell cellinfo2 = rowinfo2.createCell(0);
//            cellinfo2.setCellStyle(sanStyle);
//            cellinfo2.setCellValue("3、工日数填写精确到半个工日。");
//            //5.输出
//            workbook.write(fout);
////	            workbook.close();
//            //out.close();
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }


    /**
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    private static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize, boolean flag,
                                                 boolean flag1, boolean ifColor, Short color, boolean ifFigure) {
        // TODO Auto-generated method stub
        HSSFCellStyle style = workbook.createCellStyle();
        //是否水平居中
        if (flag1) {
            style.setAlignment(HorizontalAlignment.CENTER);//水平居中
        }

        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        //创建字体
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        //是否加粗字体
        if (flag) {
            font.setBold(flag);
        }
        //是否添加颜色
        if (ifColor) {
            style.setFillForegroundColor(color);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        font.setFontHeightInPoints(fontsize);
        //加载字体
        style.setFont(font);
        //是否小数点数字
        if (ifFigure) {
            HSSFDataFormat df = workbook.createDataFormat();  //此处设置数据格式
            style.setDataFormat(df.getFormat("#,#0.0"));
        }

        //设置边框
        style.setBorderBottom(BorderStyle.THIN); //下边框
        style.setBorderLeft(BorderStyle.THIN);//左边框
        style.setBorderTop(BorderStyle.THIN);//上边框
        style.setBorderRight(BorderStyle.THIN);//右边框
        return style;
    }

    @Test
    public void test5() throws Exception {
        List<Object> list = new ArrayList<>();

        BidDetailFilterDTO bidDetailFilterDTO = new BidDetailFilterDTO();
        bidDetailFilterDTO.setBidSheetId(1447744214976180225L);
        bidDetailFilterDTO.setBidType("商业标");
        bidDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList("报价须知"));
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
        bidVendorDetailFilterDTO.setBidSheetId(1447744214976180225L);
        bidVendorDetailFilterDTO.setBidType("商业标");
        bidVendorDetailFilterDTO.setNotTypes(CollectionUtil.newArrayList("报价须知"));
        List<BidVendorDetailCollectVO> bidVendorDetailCollectVOS = bidVendorDetailService.queryBidVendorDetailCollectVO(bidVendorDetailFilterDTO);
        for (BidVendorDetailCollectVO bidVendorDetailCollectVO : bidVendorDetailCollectVOS) {
            List<String> rounds = Arrays.asList(bidVendorDetailCollectVO.getRound().split(","));
            List<String> prices = Arrays.asList(bidVendorDetailCollectVO.getPrice().split(","));
            List<String> etds = Arrays.asList(bidVendorDetailCollectVO.getEtd().split(","));
            List<String> etas = Arrays.asList(bidVendorDetailCollectVO.getEta().split(","));
            List<String> shippingSchedules = Arrays.asList(bidVendorDetailCollectVO.getShippingSchedule().split(","));
            List<String> shippingCompany = Arrays.asList(bidVendorDetailCollectVO.getShippingCompany().split(","));
            BidVendorVO bidVendorVO = bidVendorService.queryBidVendorById(bidVendorDetailCollectVO.getBidVendorId());
            list.add(new RequestInfo(rounds,prices,etds,etas,shippingSchedules,shippingCompany,bidVendorVO.getVendorName()));
        }
        File file = new File("D:\\文件\\新建文件夹\\students.xlsx");
        ExcelUtils.exportExcel(list, "测试", file);
    }

}
