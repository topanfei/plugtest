package com.pf.util;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * 生成截图工具类
 * @author PF
 *
 */
public class CapturePictureUtil {

	private static Logger logger = Logger.getLogger(CapturePictureUtil.class);  
	
	private final static String phantomjsEnv = "windows";//默认是windows系统
	private final static String phantomjsPath = "phantomjs.binary.path";//phantomjs的路径
	private final static String phantomjsWindowsPath = CapturePictureUtil.class.getClassLoader().getResource("phantomjs/phantomjs.exe").getPath();//windows系统的phantomjs.exe路径
	private final static String phantomjsLinuxPath = "/usr/bin/phantomjs";
	private final static String tempImgPath = "E:/截图";
	private final static String tempImgSuffix = "png";
	
	public static void main(String[] args) {
		try {
			String captureUrl = "http://localhost:8080/phantomjs-demo/phantomjs/index";
			String elementXpath = "//div[@id='main']/div/canvas";
			capture(captureUrl, elementXpath);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			System.exit(0);
		}
	}
	
	/**
	 * 截图
	 * @param captureUrl 被截图的地址
	 * @param elementXpath 被截图页面元素对象
	 * @return
	 */
	public static File capture(String captureUrl,String elementXpath) {
		WebDriver webDriver = setDriverParam(loadPhantomJsDriver());
		//先登录，模拟登录
		/*webDriver.get("http://localhost:8080/bs-crystal-app/login");
		WebElement userNameElement = webDriver.findElement(By.id("username"));
		WebElement userPasswdElement = webDriver.findElement(By.id("userpasswd"));
		userNameElement.sendKeys("Gongke588");
		userPasswdElement.sendKeys("aaaaa888");
		WebElement loginElement = webDriver.findElement(By.id("login-btn"));
		loginElement.submit();*/
		webDriver.get(captureUrl);
		WebElement webElement = webDriver.findElement(By.xpath(elementXpath));
		File file = null;
		try {
			if(webElement == null) {
				throw new NoSuchElementException("没有找到指定的元素，请检查url或者页面元素!");
			}else {
				file = captureElement(webElement);
				
				//到处图片只是为了测试
				//导出图片
				File targetFile = new File(tempImgPath+"\\"+UUID.randomUUID()+"."+tempImgSuffix);
				//截图文件的父目录
				File filePath = new File(targetFile.getParent());
				if(filePath.exists()) {
					filePath.mkdirs();
				}
				FileUtils.copyFile(file, targetFile);
				
				logger.info("图片生成完毕！");
			}
		}catch (Exception e) {
		}finally {
			webDriver.close();
			webDriver.quit();
		}
		return file;
	}
	
	/**
	 * 加载phantomJS驱动
	 * @return
	 */
	private static WebDriver loadPhantomJsDriver() {
		if(phantomjsEnv.contentEquals("liunx")) {
			System.setProperty(phantomjsPath, phantomjsLinuxPath);
		}else {
			System.setProperty(phantomjsPath, phantomjsWindowsPath);
		}
		DesiredCapabilities desiredCapabilities = DesiredCapabilities.phantomjs();
		List<String> cliArgsCap = new ArrayList<String>();
		cliArgsCap.add("--load-images=no");//关闭图片加载
        cliArgsCap.add("--disk-cache=yes");//开启缓存
        cliArgsCap.add("--ignore-ssl-errors=true");//忽略https错误
        desiredCapabilities.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, cliArgsCap);
        //设置请求头参数
        desiredCapabilities.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        desiredCapabilities.setCapability("phantomjs.page.customHeaders.User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:50.0) Gecko/20100101 　　Firefox/50.0");
        desiredCapabilities.setCapability("acceptSslCerts",true);//支持ssl
        desiredCapabilities.setCapability("takesScreenshot",true);//支持截屏
        //css搜索支持
        desiredCapabilities.setCapability("cssSelectorsEnabled", true);
        //js支持
        desiredCapabilities.setJavascriptEnabled(true);
        return new PhantomJSDriver(desiredCapabilities);
	}
	
	/**
	 * 设置phantomjs驱动参数
	 * @param webDriver
	 * @return
	 */
	private static WebDriver setDriverParam(WebDriver webDriver) {
		webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);//设置等待时间  等待页面加载完成
		webDriver.manage().window().maximize();//窗口最大化
        return webDriver;
	}
	
	/**
	 * 截取具体页面元素
	 * @param webElement
	 * @return
	 * @throws IOException
	 */
	private static File captureElement(WebElement webElement) throws IOException {
		WrapsDriver wrapsDriver = (WrapsDriver) webElement;
		// 截图整个页面
        File screenFile = ((RemoteWebDriver) wrapsDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);
        int width = webElement.getSize().getWidth();
        int height = webElement.getSize().getHeight();
        // 创建一个矩形使用上面的高度，和宽度
        java.awt.Rectangle rect = new java.awt.Rectangle(width, height);
        // 得到元素的坐标
        Point p = webElement.getLocation();
        try {
            BufferedImage img = ImageIO.read(screenFile);
            // 获得元素的高度和宽度
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(), rect.width, rect.height);
            // 存为png格式
            ImageIO.write(dest, tempImgSuffix, screenFile);
        }catch (RasterFormatException e){
            throw new RasterFormatException(e.getMessage());
        }
        catch (IOException e) {
            throw  new IOException(e.getMessage());
        }
        return screenFile;
	}
}
