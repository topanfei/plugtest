package com.pf.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pf.util.CapturePictureUtil;
import com.pf.util.FileUtil;
import com.pf.util.PDFBoxUtil;

@Controller
@RequestMapping("/test")
public class TestController {
	
	private static final Logger logger = Logger.getLogger(TestController.class);

	/**
	 * 1.截图生成pdf并上传服务器
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="index")
	public String index(Model model,HttpServletRequest request,HttpServletResponse response) {
		ByteArrayInputStream inputStream = null;
		try {
			logger.info("TestController test/index接口");
			
			//截图
			String captureUrl = "http://localhost:8080/phantomjs-demo/phantomjs/index";
			String elementXpath = "//div[@id='main']/div/canvas";
			File file = CapturePictureUtil.capture(captureUrl, elementXpath);
			
			//生成pdf
			String wordText = "反向路径图图表";
			ByteArrayOutputStream out = PDFBoxUtil.createPDFWithWordImage(file.getPath(), wordText);
			//将输出流转为输入流
			inputStream = new ByteArrayInputStream(out.toByteArray());
			//上传文件至服务器目录
			String fileNameAndSuffix = UUID.randomUUID()+".pdf";
			String filePath = FileUtil.uploadSingleFile(request, inputStream, "pdfFile", fileNameAndSuffix);
			model.addAttribute("filePath", filePath);
		}catch (Exception e) {
			logger.error("TestController index error："+e.getMessage());
			logger.info("TestController index error", e);
		}
		return "test/index";
	}
	
	/**
	 * 下载文件
	 * @param request
	 * @param response
	 * @param fileRealPath
	 */
	@RequestMapping(value="downFile",method=RequestMethod.GET)
	public void downFile(HttpServletRequest request,HttpServletResponse response,
			String fileRealPath) {
		FileUtil.downLoadSingleFile(request, response, fileRealPath);
	}
}
