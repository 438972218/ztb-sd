package com.xdcplus.ztb.common.watermark.factory;

import cn.hutool.core.io.FileUtil;
import com.xdcplus.ztb.common.exceptions.WatermarkException;
import com.xdcplus.ztb.common.watermark.FileTypeUtils;
import com.xdcplus.ztb.common.watermark.domain.WatermarkParam;
import org.docx4j.openpackaging.packages.SpreadsheetMLPackage;
import org.docx4j.openpackaging.parts.SpreadsheetML.WorksheetPart;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.relationships.Relationship;
import org.xlsx4j.jaxb.Context;
import org.xlsx4j.sml.CTSheetBackgroundPicture;

import java.io.File;

/**
 * excel处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 14:01:45
 */
public class ExcelWatermarkProcessor extends AbstractWatermarkProcessor {

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isXlsx(file);
    }

        @Override
    public void process(WatermarkParam watermarkParam) throws WatermarkException {

        try {
            File file = watermarkParam.getFile();
            SpreadsheetMLPackage excelMLPackage = SpreadsheetMLPackage.load(file);
            int size = excelMLPackage.getWorkbookPart().getContents().getSheets().getSheet().size();
            for (int i=0;i<size;i++) {
                WorksheetPart worksheet = excelMLPackage.getWorkbookPart().getWorksheet(i);
                createBgPic(worksheet, excelMLPackage, watermarkParam.getImageFile());
            }
            excelMLPackage.save(file);
        } catch (Exception e) {
            throw new WatermarkException("Docx4JException", e);
        }

    }

    /**
     * 使用水印图片作为excel背景，达到水印效果<但打印时不会生效>
     *
     * @param worksheet      工作表
     * @param excelMLPackage excelmlpackage
     * @param imageFile      图像文件
     * @throws WatermarkException 水印异常
     */
    private void createBgPic(WorksheetPart worksheet, SpreadsheetMLPackage excelMLPackage, File imageFile) throws Exception {

        CTSheetBackgroundPicture ctSheetBackgroundPicture = Context.getsmlObjectFactory().createCTSheetBackgroundPicture();
        worksheet.getContents().setPicture(ctSheetBackgroundPicture);
        BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(excelMLPackage, worksheet, FileUtil.readBytes(imageFile));
        Relationship sourceRelationship = imagePart.getSourceRelationships().get(0);
        String imageRelId = sourceRelationship.getId();
        ctSheetBackgroundPicture.setId(imageRelId);
    }



}
