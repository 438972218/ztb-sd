package com.xdcplus.ztb.common.vendorxlsx;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.xdcplus.ztb.common.vendorxlsx.annotation.ExcelField;
import com.xdcplus.ztb.common.vendorxlsx.converter.DefaultConvertible;
import com.xdcplus.ztb.common.vendorxlsx.converter.WriteConvertible;
import com.xdcplus.ztb.common.vendorxlsx.exceptions.ExcelException;
import com.xdcplus.ztb.common.vendorxlsx.handler.CellMergeHolder;
import com.xdcplus.ztb.common.vendorxlsx.handler.ExcelHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
public class ExcelUtils {

    /**
     * 导出excel
     *
     * @param datas      待导出数据
     * @param targetFile 生成的Excel文件
     * @param sheetName  表的名字
     * @throws ExcelException excel异常
     */
    public static void exportExcel(List<?> datas, String sheetName, File targetFile) throws ExcelException {

        Assert.notEmpty(datas, "The exported data set cannot be empty");

        FileUtil.del(targetFile);

        ExcelWriter writer = null;
        try {
            List<List<String>> rows = assemblyValues(datas);
            writer = ExcelUtil.getBigWriter(targetFile, sheetName);
            writer.write(rows);

            List<CellMergeHolder> cellMergeHolders = computeCellMerge(rows);
            for (CellMergeHolder cellMergeHolder : cellMergeHolders) {
                writer.merge(cellMergeHolder.getStartRow(), cellMergeHolder.getEndRow(),
                        cellMergeHolder.getStartCol(), cellMergeHolder.getEndCol(), cellMergeHolder.getValue(), Boolean.FALSE);
            }

            Sheet sheet = writer.getSheet();
            Map<Integer, Integer> columnWidth = calculateColumnWidth(sheet);
            columnWidth.forEach(sheet::setColumnWidth);
        }catch (Exception e) {
            log.error("exportExcel {}", e.getMessage());
            throw new ExcelException(e);
        }finally {
            IoUtil.close(writer);
        }
    }

    public static void exportExcel(List<?> datas, String fileName, HttpServletResponse response) throws ExcelException {

        Assert.notEmpty(datas, "The exported data set cannot be empty");

//        FileUtil.del(targetFile);

        ExcelWriter writer = null;
        try {
            List<List<String>> rows = assemblyValues(datas);
            writer = ExcelUtil.getWriter();
            writer.write(rows);

            List<CellMergeHolder> cellMergeHolders = computeCellMerge(rows);
            for (CellMergeHolder cellMergeHolder : cellMergeHolders) {
                writer.merge(cellMergeHolder.getStartRow(), cellMergeHolder.getEndRow(),
                        cellMergeHolder.getStartCol(), cellMergeHolder.getEndCol(), cellMergeHolder.getValue(), Boolean.FALSE);
            }

            Sheet sheet = writer.getSheet();
            Map<Integer, Integer> columnWidth = calculateColumnWidth(sheet);
            columnWidth.forEach(sheet::setColumnWidth);
            // 获取我们的输出流
            final OutputStream output = response.getOutputStream();
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            writer.flush(output, true);
            writer.close();
        }catch (Exception e) {
            log.error("exportExcel {}", e.getMessage());
            throw new ExcelException(e);
        }finally {
            IoUtil.close(writer);
        }
    }


    /**
     * 导出excel
     *
     * @param datas      待导出数据
     * @param targetFile 生成的Excel文件
     * @throws ExcelException excel异常
     */
    public static void exportExcel(List<?> datas, File targetFile) throws ExcelException {
        exportExcel(datas, null, targetFile);
    }

    /**
     * 导出excel
     *
     * @param datas      待导出数据
     * @param targetFile 生成的Excel文件
     * @param sheetName  表的名字
     * @throws ExcelException excel异常
     */
    public static void exportExcel(List<?> datas, String sheetName, String targetFile) throws ExcelException {
        exportExcel(datas, sheetName, new File(targetFile));
    }

    /**
     * 导出excel
     *
     * @param datas      待导出数据
     * @param targetFile 生成的Excel文件
     * @throws ExcelException excel异常
     */
    public static void exportExcel(List<?> datas, String targetFile) throws ExcelException {
        exportExcel(datas, null, targetFile);
    }

    /**
     * 计算列宽
     *
     * @param sheet 表
     * @return {@link Map}<{@link Integer}, {@link Integer}>
     */
    private static Map<Integer, Integer> calculateColumnWidth(Sheet sheet) {

        //存储最大列宽
        Map<Integer, Integer> maxWidth = new HashMap<>();

        int maxColumn = sheet.getRow(0).getPhysicalNumberOfCells();
        for(int columnNum = 0; columnNum <= maxColumn; columnNum++){
            int columnWidth = sheet.getColumnWidth(columnNum) / 256;
            for(int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++){
                Row currentRow;
                if(sheet.getRow(rowNum) == null){
                    currentRow = sheet.createRow(rowNum);
                }else{
                    currentRow = sheet.getRow(rowNum);
                }

                if(currentRow.getCell(columnNum) != null){
                    Cell currentCell = currentRow.getCell(columnNum);
                    try {
                        int length = currentCell.toString().getBytes("GBK").length;
                        if(columnWidth < length + 1){
                            columnWidth = length + 8;
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            maxWidth.put(columnNum, columnWidth * 256);
        }

        return maxWidth;
    }



    /**
     * 装配值
     *
     * @param dataList 数据列表
     * @return {@link List}<{@link List}<{@link String}>>
     */
    private static List<List<String>> assemblyValues(List<?> dataList) {

        List<List<String>> rows = new ArrayList<>();

        LinkedHashMap<ExcelHolder, List<ExcelHolder>> excelHolderMap = parseData(dataList);
        for (Map.Entry<ExcelHolder, List<ExcelHolder>> entry : excelHolderMap.entrySet()) {
            List<ExcelHolder> value = entry.getValue();
            ExcelHolder key = entry.getKey();

            for (ExcelHolder holder : value) {
                List<String> row = new ArrayList<>();
                row.add(getProperty(key.getValue(), key.getWriteConverter()));
                row.add(holder.getHeader());
                row.addAll(getValue(holder));
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * 计算单元格合并
     *
     * @param cellValues 单元格的值
     * @return {@link List}<{@link CellMergeHolder}>
     */
    private static List<CellMergeHolder> computeCellMerge(List<List<String>> cellValues) {

        List<CellMergeHolder> cellMergeHolders = CollectionUtil.newArrayList();

        int start = 0;
        String key =null;
        for (int i = 0; i < cellValues.size() - 1; i++) {
            if (CollectionUtil.isNotEmpty(cellValues.get(i))) {
                key = cellValues.get(i).get(0);
                if (key.equals(cellValues.get(i + 1).get(0))) {
                    if (i == cellValues.size() - 2) {
                        cellMergeHolders.add(CellMergeHolder.builder().value(key).endCol(0).startCol(0).startRow(start).endRow(cellValues.size() - 1).build());
                    }
                }else {
                    cellMergeHolders.add(CellMergeHolder.builder().value(key).endCol(0).startCol(0).startRow(start).endRow(i).build());
                    start = i + 1;
                }
            }
        }

        return cellMergeHolders;
    }

    /**
     * 根据属性名与属性类型获取字段内容
     *
     * @param bean             对象
     * @param field        字段
     * @param writeConvertible 写入转换器
     * @return 对象指定字段内容
     */
    private static String getProperty(Object bean, Field field, WriteConvertible writeConvertible) {

        if (bean == null || field == null) {
            throw new IllegalArgumentException("Operating bean or filed class must not be null");
        }

        Object object = ReflectUtil.getFieldValue(bean, field);
        if (null != writeConvertible && writeConvertible.getClass() != DefaultConvertible.class) {
            // 写入转换器
            object = writeConvertible.execWrite(object);
        }
        return object == null ? "" : object.toString();
    }

    /**
     * 数据值根据转换器转换格式
     *
     * @param value     值
     * @param writeConvertible 写入转换器
     * @return 对象指定字段内容
     */
    private static String getProperty(Object value, WriteConvertible writeConvertible) {

        if (null != writeConvertible && writeConvertible.getClass() != DefaultConvertible.class) {
            // 写入转换器
            value = writeConvertible.execWrite(value);
        }
        return value == null ? "" : value.toString();
    }

    /**
     * 获取字段集合
     *
     * @param clz 对象Class
     * @return {@link List}<{@link Field}>
     */
    private static List<Field> getFields(Class<?> clz) {

        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = clz; clazz != Object.class; clazz = clazz.getSuperclass()) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        }
        return fields;
    }

    /**
     * 获取值
     *
     * @param holder 持有人
     * @return {@link List}<{@link String}>
     */
    private static List<String> getValue(ExcelHolder holder) {

        List<String> result = CollectionUtil.newArrayList();
        Object value = holder.getValue();

        if (ObjectUtil.isNull(value)) {
            result.add(StrUtil.EMPTY);
        } else if (value instanceof Map) {
            Map<?,?> map = (Map<?,?>) value;
            for (Object object : map.values()) {
                result.add(getProperty(object, holder.getWriteConverter()));
            }
        } else if (value instanceof Iterable) {
            Iterable<?> iterable = (Iterable<?>) value;
            iterable.forEach(obj -> result.add(getProperty(obj, holder.getWriteConverter())));
        } else {
            result.add(getProperty(value, holder.getWriteConverter()));
        }

        return result;
    }

    /**
     * 解析数据
     *
     * @param dataList 数据列表
     * @return {@link LinkedHashMap}<{@link ExcelHolder}, {@link List}<{@link ExcelHolder}>>
     */
    private static LinkedHashMap<ExcelHolder, List<ExcelHolder>> parseData(List<?> dataList) {

        LinkedHashMap<ExcelHolder, List<ExcelHolder>> holders = new LinkedHashMap<>(32);

        if (CollectionUtil.isNotEmpty(dataList)) {
            for (Object object : dataList) {
                List<Field> fields = getFields(object.getClass());

                List<ExcelHolder> excelHolders = CollectionUtil.newArrayList();
                ExcelHolder holder = null;

                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelField.class)) {
                        ExcelField excelField = field.getAnnotation(ExcelField.class);
                        try {
                            ExcelHolder excelHolder = ExcelHolder.builder()
                                    .fieldName(field.getName()).filedClazz(field.getType())
                                    .header(excelField.header()).isTitle(excelField.isTitle())
                                    .readConverter(excelField.readConverter().newInstance())
                                    .writeConverter(excelField.writeConverter().newInstance())
                                    .order(excelField.order()).value(ReflectUtil.getFieldValue(object, field))
                                    .build();
                            if (excelHolder.getIsTitle()) {
                                holder = excelHolder;
                            }else {
                                excelHolders.add(excelHolder);
                            }
                        }catch (Exception e) {
                            log.error("assemblyValues {}", e.getMessage());
                            throw new ExcelException(e);
                        }
                    }
                }

                if (ObjectUtil.isEmpty(holder)) {
                    holder = ExcelHolder.builder().build();
                }
                Collections.sort(excelHolders);
                holders.put(holder, excelHolders);
            }
        }

        return holders;
    }
























}
