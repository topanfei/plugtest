package com.pf.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PDFBoxUtil {

	private static Logger logger = Logger.getLogger(PDFBoxUtil.class);
	private final static Integer IMAGE_PADDING = 50;
	
	/**
	 * 生成文字图片pdf
	 * @param imagePath
	 * @param wordText
	 * @return
	 * @throws IOException
	 */
	public static ByteArrayOutputStream createPDFWithWordImage(String imagePath,String wordText) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		//1.创建文档对象
		PDDocument document = new PDDocument();
		//2.创建图片对象
		PDImageXObject pdImage = PDImageXObject.createFromFile(imagePath, document);
		int docWidth = pdImage.getWidth() + IMAGE_PADDING;
		int docHeight = pdImage.getHeight() + IMAGE_PADDING *2;
		PDRectangle pdRectangle = new PDRectangle(docWidth, docHeight);
		//3.创建页面
		PDPage page = new PDPage(pdRectangle);
		//4.添加页面至文档
		document.addPage(page);
		//图片信息
		float scale = 0.6f;
		float imageWidth = pdImage.getWidth() * scale;
		float imageHeight = pdImage.getHeight() * scale;
		//5.准备流
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		//绘制文字
		contentStream.beginText();
		contentStream.newLineAtOffset((docWidth- imageWidth)/2, docHeight-IMAGE_PADDING);
		//设置字体
		InputStream inputStream = PDFBoxUtil.class.getClassLoader().getResourceAsStream("fonts/SIMYOU.TTF");
		PDFont font = PDType0Font.load(document, inputStream);
		contentStream.setFont(font, 14);
		contentStream.showText(wordText);
		contentStream.newLine();
		contentStream.endText();
		//绘制图片
		contentStream.drawImage(pdImage, (docWidth-imageWidth)/2, docHeight-(IMAGE_PADDING+10+imageHeight), imageWidth, imageHeight);
		contentStream.close();
		//保存文档
		document.save(out);
		logger.info("pdf文件生成完毕！");
		document.close();
		return out;
	}
}
