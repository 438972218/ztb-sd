package com.xdcplus.ztb.common.watermark.factory;

import cn.hutool.core.io.IoUtil;
import com.xdcplus.ztb.common.exceptions.WatermarkException;
import com.xdcplus.ztb.common.watermark.FileTypeUtils;
import com.xdcplus.ztb.common.watermark.domain.WatermarkParam;
import org.apache.poi.sl.usermodel.PictureData.PictureType;
import org.apache.poi.xslf.usermodel.*;

import java.awt.geom.Rectangle2D;
import java.io.*;

/**
 * ppt处理器
 *
 * @author Rong.Jia
 * @date 2021/08/13 17:45:49
 */
public class PowerPointWatermarkProcessor extends AbstractWatermarkProcessor {

	@Override
	public Boolean supportType(File file) {
		return FileTypeUtils.isPptx(file);
	}

	@Override
	public void process(WatermarkParam watermarkParam) throws WatermarkException {

		XMLSlideShow pptx = null;
		FileOutputStream output = null;
		InputStream inputStream = null;
		try {

			File file = watermarkParam.getFile();
			inputStream = new FileInputStream(file);
			pptx = new XMLSlideShow(inputStream);
			XSLFPictureData pictureData = pptx.addPicture(watermarkParam.getImageFile(), PictureType.PNG);
			for (int i=0;i<pptx.getSlideMasters().size();i++) {
				XSLFSlideMaster slideMaster = pptx.getSlideMasters().get(i);
				XSLFSlideLayout[] slideLayouts = slideMaster.getSlideLayouts();
				for (XSLFSlideLayout slidelayout : slideLayouts) {
					XSLFPictureShape pictureShape = slidelayout.createPicture(pictureData);
					pictureShape.setAnchor(new Rectangle2D.Double(20, 20, 640, 400));
				}
			}
			output = new FileOutputStream(file);
			pptx.write(output);

		} catch (FileNotFoundException e) {
			throw new WatermarkException("FileNotFoundException", e);
		} catch (IOException e) {
			throw new WatermarkException("IOException", e);
		} finally {
			IoUtil.close(output);
			IoUtil.close(pptx);
			IoUtil.close(inputStream);
		}
	}
}
