package com.pf.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.Assert;

public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);
			
	/**
	 * 返回绝对路径path信息<br>
	 * 如：http://localhost:8080/phantomjs-demo/test/index
	 * @param request
	 * @return
	 */
	public static String getAbsolutePath(HttpServletRequest request) {
		String contextPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		return contextPath;
	}
	
	/**
	 * 返回服务器的真实目录，如图片上传的目录
	 * @param request
	 * @return
	 */
	public static String getRealPath(HttpServletRequest request) {
		String realPath = request.getSession().getServletContext().getRealPath("/");
		return realPath;
	}
	
	/**
	 * 长传单文件
	 * @param request
	 * @param inputStream
	 * @param fileFolder
	 * @param fileNameAndSuffix
	 */
	public static String uploadSingleFile(HttpServletRequest request,InputStream inputStream,
			String fileFolder,String fileNameAndSuffix) {
		Assert.hasText(fileFolder, "文件的存放文件夹名称不能为空！");
		Assert.hasText(fileNameAndSuffix, "文件名和后缀不能为空！");
		String filePath = getRealPath(request)+"/upload/"+fileFolder+"/"+fileNameAndSuffix;
		File file = new File(filePath);
		if(!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int length= 0 ;
			while((length = inputStream.read(b))!= -1) {
				fos.write(b, 0, length);
			}
		}catch (Exception e) {
			logger.info("文件上传失败！");
			e.printStackTrace();
		}finally {
			logger.info("文件上传成功！");
			try {
				inputStream.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String fileRealPath = filePath.substring(filePath.lastIndexOf("upload"));
		return fileRealPath;
	}
	
	/**
	 * 下载单文件
	 * @param response
	 * @param filePath
	 */
	public static void downLoadSingleFile(HttpServletRequest request,HttpServletResponse response,
			String fileRealPath) {
		String fileName=fileRealPath.substring(fileRealPath.lastIndexOf("/")+1);
		response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        String filePath = getRealPath(request)+fileRealPath;
        File targetFile=new File(filePath);
        FileInputStream fis = null;
        OutputStream os =null;
		try {
			//利用输入流读取上传的那个文件
			fis = new FileInputStream(targetFile);
			//从response中获取到输出流
	        os = response.getOutputStream();
	        byte[] b=new byte[1024];
	        int length=-1;
	        while((length=fis.read(b))!=-1){
	         os.write(b, 0, length);
	        }
		} catch (Exception e) {
			logger.info("下载文件失败！");
			e.printStackTrace();
		}finally{
			try {
				fis.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			logger.info("下载文件成功！");
		}
	}
}
