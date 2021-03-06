package com.xdcplus.ztb.common.watermark.factory;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.xdcplus.ztb.common.exceptions.WatermarkException;
import com.xdcplus.ztb.common.watermark.FileTypeUtils;
import com.xdcplus.ztb.common.watermark.domain.WatermarkParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * pdf处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 10:06:40
 */
public class PdfWatermarkProcessor extends AbstractWatermarkProcessor {

    private static final Logger logger = LoggerFactory.getLogger(PdfWatermarkProcessor.class);

    @Override
    public Boolean supportType(File file) {
        return FileTypeUtils.isPdf(file);
    }

    @Override
    public void process(WatermarkParam watermarkParam) throws WatermarkException {

        super.process(watermarkParam);

        PdfReader reader = null;
        PdfStamper stamper = null;

        try {
            File file = watermarkParam.getFile();
            reader = new PdfReader(new FileInputStream(file));
            stamper = new PdfStamper(reader, new FileOutputStream(file));

            int pageNo = reader.getNumberOfPages();

            // image watermark
            Image img = Image.getInstance(FileUtil.readBytes(watermarkParam.getImageFile()));
            float w = Math.min(img.getScaledWidth(), 460);
            float h = Math.min(img.getScaledHeight(), 300);

            // 设置透明度
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(watermarkParam.getAlpha());

            Integer imgH = Convert.toInt(img.getHeight());
            Integer imgW = Convert.toInt(img.getWidth());

            PdfContentByte over;
            Rectangle pageRect;
            float x, y;
            // loop over every page
            for (int i = 1; i <= pageNo; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                x = (pageRect.getLeft() + pageRect.getRight()) / 2;
                y = (pageRect.getTop() + pageRect.getBottom()) / 2;
                over = stamper.getOverContent(i);

                over.saveState();
                over.setGState(gs);

                if (!watermarkParam.getBespread()) {
                    // 第3个参数：旋转， 第4参数：斜体角度
//                    over.addImage(img, w, 0, 0, h, x - (w / 2), y - (h / 2));
                    over.addImage(img, w, 0, 0, h, watermarkParam.getXMove(), watermarkParam.getYMove());
                }else {

                    for (int height = watermarkParam.getYMove() + imgH;
                         height < pageRect.getHeight();
                         height = height + imgH * 3) {
                        for (int width = watermarkParam.getXMove() + imgW;
                             width < pageRect.getWidth() + imgW;
                             width = width + imgW *2) {
                            // 第3个参数：旋转， 第4参数：斜体角度
                            over.addImage(img, w, 0, 0, h, width - imgW, height - imgH);
                        }
                    }
                }

                over.restoreState();
            }

            if (stamper!=null) {
                stamper.close();
            }
        }catch (Exception e) {
            logger.error("Description Failed to add watermark to PDF :  {}", e.getMessage());
        }finally {
            if (reader!=null) {
                reader.close();
            }
        }

    }
}
