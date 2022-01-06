package com.xdcplus.ztb.common.excel;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.StyleSet;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExportExcelUtil {
    public static void
    export(HttpServletResponse response, Class clazz, String fileName, List<?> list) throws Exception {
        if (!list.isEmpty()) {
            if (!list.get(0).getClass().equals(clazz)) {
                log.error("数据类型与传入的集合数据类型不一致！数据类型：{}; 集合数据类型：{}", clazz, list.get(0).getClass());
                throw new Exception("数据类型与传入的集合数据类型不一致！");
            } else {
                ExcelWriter writer = ExcelUtil.getWriter();
                // 获取当前类字段
                Field[] fields = clazz.getDeclaredFields();
                // 字段名称集合
                List<String> fieldNames = new ArrayList<>();
                // 字段中文名称集合（获取实体中@ApiModelProperty注解value的值）
                List<String> cnNames = new ArrayList<>();
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        // 关闭反射访问安全检查，为了提高速度
                        field.setAccessible(true);
                    }
                    String fieldName = field.getName();
                    // 排除ID和序号
                    if (!"sid".equals(fieldName) && !"serialVersionUID".equals(fieldName) && !"ordernum".equals(fieldName)) {
                        fieldNames.add(fieldName);
                    }
                    // 判断是否有@ApiModelProperty注解
                    boolean annotationPresent = field.isAnnotationPresent(ApiModelProperty.class);
                    if (annotationPresent && !"sid".equals(fieldName)) {
                        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                        String name = annotation.value();
                        cnNames.add(name);
                    }
                }
                String[] fs = fieldNames.toArray(new String[0]);
                String[] ns = cnNames.toArray(new String[0]);
                for (int i = 0; i < ns.length; i++) {
                    // 设置表头及字段名
                    writer.addHeaderAlias(fs[i], ns[i]);
                }
                // 自动换行
                Workbook workbook = writer.getWorkbook();
                StyleSet styleSet = new StyleSet(workbook);
                styleSet.setWrapText();
                writer.setStyleSet(styleSet);
                writer.write(list, true);
                ServletOutputStream out = response.getOutputStream();
                try {
                    for (int i = 0; i < fieldNames.size(); i++) {
                        writer.setColumnWidth(i, 23);
                    }
                    response.setContentType("application/x-msdownload;charset=utf-8");
                    String ecodeFileName = URLEncoder.encode(fileName, "UTF-8");
                    response.setHeader("Content-Disposition", "attachment;filename=" + ecodeFileName + ".xls");
                    writer.flush(out, true);
                    writer.close();
                    IoUtil.close(out);
                } catch (IOException e) {
                    log.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        } else {
            log.error("数据集合为空");
            throw new Exception("数据集合为空");
        }
    }

    public static void
    exportByFile(File targetFile, Class clazz, String fileName, List<?> list) throws Exception {
        if (!list.isEmpty()) {
            if (!list.get(0).getClass().equals(clazz)) {
                log.error("数据类型与传入的集合数据类型不一致！数据类型：{}; 集合数据类型：{}", clazz, list.get(0).getClass());
                throw new Exception("数据类型与传入的集合数据类型不一致！");
            } else {
//                ExcelWriter writer = ExcelUtil.getWriter();
                ExcelWriter writer = ExcelUtil.getBigWriter(targetFile, fileName);
                // 获取当前类字段
                Field[] fields = clazz.getDeclaredFields();
                // 字段名称集合
                List<String> fieldNames = new ArrayList<>();
                // 字段中文名称集合（获取实体中@ApiModelProperty注解value的值）
                List<String> cnNames = new ArrayList<>();
                for (Field field : fields) {
                    if (!field.isAccessible()) {
                        // 关闭反射访问安全检查，为了提高速度
                        field.setAccessible(true);
                    }
                    String fieldName = field.getName();
                    // 排除ID和序号
                    if (!"sid".equals(fieldName) && !"serialVersionUID".equals(fieldName) && !"ordernum".equals(fieldName)) {
                        fieldNames.add(fieldName);
                    }
                    // 判断是否有@ApiModelProperty注解
                    boolean annotationPresent = field.isAnnotationPresent(ApiModelProperty.class);
                    if (annotationPresent && !"sid".equals(fieldName)) {
                        ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
                        String name = annotation.value();
                        cnNames.add(name);
                    }
                }
                String[] fs = fieldNames.toArray(new String[0]);
                String[] ns = cnNames.toArray(new String[0]);
                for (int i = 0; i < ns.length; i++) {
                    // 设置表头及字段名
                    writer.addHeaderAlias(fs[i], ns[i]);
                }
                // 自动换行
                Workbook workbook = writer.getWorkbook();
                StyleSet styleSet = new StyleSet(workbook);
                styleSet.setWrapText();
                writer.setStyleSet(styleSet);
                writer.write(list, true);
//                ServletOutputStream out = response.getOutputStream();
                for (int i = 0; i < fieldNames.size(); i++) {
                    writer.setColumnWidth(i, 23);
                }
                IoUtil.close(writer);
            }
        } else {
            log.error("数据集合为空");
            throw new Exception("数据集合为空");
        }
    }

    public static void export(HttpServletResponse response, String[][] array) throws IOException {
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
        // 获取输出流
//        String ecodeFileName = URLEncoder.encode("excel", "UTF-8");
//        response.setContentType("application/x-msdownload;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + ecodeFileName + ".xls");

        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.addHeader("Content-Disposition", "attachment;filename=fileName" + ".xls");

        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
//        workbook.flush(out, true);
//        workbook.write(new FileOutputStream(new File("C:\\Users\\fish.fei\\Desktop\\aa1.xls")));
    }


    public static void exportExcel(HttpServletResponse response, String fileName, ExcelDataDTO data) throws Exception {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
        exportExcel(data, response.getOutputStream());
    }

    public static void exportExcel(ExcelDataDTO data, OutputStream out) throws Exception {

        XSSFWorkbook wb = new XSSFWorkbook();
        try {
            String sheetName = data.getName();
            if (null == sheetName) {
                sheetName = "Sheet1";
            }
            XSSFSheet sheet = wb.createSheet(sheetName);
            writeExcel(wb, sheet, data);

            wb.write(out);
        } finally {
            wb.close();
        }
    }

    private static void writeExcel(XSSFWorkbook wb, Sheet sheet, ExcelDataDTO data) {

        int rowIndex = 0;

        rowIndex = writeTitlesToExcel(wb, sheet, data.getTitles());
        if (CollectionUtil.isNotEmpty(data.getRows())) {
            writeRowsToExcel(wb, sheet, data.getRows(), rowIndex);
        } else if (data.getStus() != null) {
            writeStusToExcel(wb, sheet, data.getStus(), rowIndex);
        }
        autoSizeColumns(sheet, data.getTitles().size() + 1);

    }

    private static int writeTitlesToExcel(XSSFWorkbook wb, Sheet sheet, List<String> titles) {
        int rowIndex = 0;
        int colIndex = 0;

        Font titleFont = wb.createFont();
        titleFont.setFontName("simsun");
        titleFont.setBold(true);
        // titleFont.setFontHeightInPoints((short) 14);
        titleFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle titleStyle = wb.createCellStyle();
//        titleStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        titleStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        titleStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(182, 184, 192)));
//        titleStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        setBorder(titleStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0)));

        Row titleRow = sheet.createRow(rowIndex);
        // titleRow.setHeightInPoints(25);
        colIndex = 0;

        for (String field : titles) {
            Cell cell = titleRow.createCell(colIndex);
            cell.setCellValue(field);
            cell.setCellStyle(titleStyle);
            colIndex++;
        }

        rowIndex++;
        return rowIndex;
    }

    private static int writeRowsToExcel(XSSFWorkbook wb, Sheet sheet, List<List<Object>> rows, int rowIndex) {
        int colIndex = 0;

        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
//        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        dataStyle.setFont(dataFont);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0)));

        for (List<Object> rowData : rows) {
            Row dataRow = sheet.createRow(rowIndex);
            // dataRow.setHeightInPoints(25);
            colIndex = 0;

            for (Object cellData : rowData) {
                Cell cell = dataRow.createCell(colIndex);
                if (cellData != null) {
                    cell.setCellValue(cellData.toString());
                } else {
                    cell.setCellValue("");
                }

                cell.setCellStyle(dataStyle);
                colIndex++;
            }
            rowIndex++;
        }
        return rowIndex;
    }

    private static int writeStusToExcel(XSSFWorkbook wb, Sheet sheet, String[][] stus, int rowIndex) {

        Font dataFont = wb.createFont();
        dataFont.setFontName("simsun");
        // dataFont.setFontHeightInPoints((short) 14);
        dataFont.setColor(IndexedColors.BLACK.index);

        XSSFCellStyle dataStyle = wb.createCellStyle();
//        dataStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
//        dataStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
        dataStyle.setFont(dataFont);
        setBorder(dataStyle, BorderStyle.THIN, new XSSFColor(new java.awt.Color(0, 0, 0)));

        for (int row = 1; row < stus.length; row++) {
            Row dataRow = sheet.createRow(row);
            //输出的行数据
            String[] temp = stus[row];
            for (int cell = 0; cell < temp.length; cell++) {
                Cell cell1 = dataRow.createCell(cell);
                cell1.setCellValue(temp[cell]);
                cell1.setCellStyle(dataStyle);
            }
        }

        return rowIndex;
    }

    private static void autoSizeColumns(Sheet sheet, int columnNumber) {

        for (int i = 0; i < columnNumber; i++) {
            int orgWidth = sheet.getColumnWidth(i);
            sheet.autoSizeColumn(i, true);
            int newWidth = (int) (sheet.getColumnWidth(i) + 100);
            if (newWidth > orgWidth) {
                sheet.setColumnWidth(i, newWidth);
            } else {
                sheet.setColumnWidth(i, orgWidth);
            }
        }
    }

    private static void setBorder(XSSFCellStyle style, BorderStyle border, XSSFColor color) {
        style.setBorderTop(border);
        style.setBorderLeft(border);
        style.setBorderRight(border);
        style.setBorderBottom(border);
        style.setBorderColor(XSSFCellBorder.BorderSide.TOP, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.LEFT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.RIGHT, color);
        style.setBorderColor(XSSFCellBorder.BorderSide.BOTTOM, color);
    }


    /**
     * @param workbook
     * @param fontsize
     * @return 单元格样式
     */
    public static HSSFCellStyle createCellStyle(HSSFWorkbook workbook, short fontsize, boolean flag,
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


    public static void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes("iso8859-1"), "utf-8");
                System.out.println(fileName);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
