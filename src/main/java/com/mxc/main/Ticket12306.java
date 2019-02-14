package com.mxc.main;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.mxc.constant.AppConstant;
import com.mxc.entity.Ticket;
import com.mxc.util.Base64Util;
import com.mxc.util.YdmUtil;

/**
 * 功能描述:12306自动刷新页面购票
 */
public class Ticket12306 {
	
	public static void main(String[] args) {
		List<Ticket> tickets = new ArrayList<Ticket>();
		//票的开始时间
		List<String> listStartTime = new ArrayList<String>();
		listStartTime.add("20:35");
/*		listStartTime.add("14:54");
		listStartTime.add("16:05");
		listStartTime.add("17:27");*/
		//第一张票
		Ticket ticket1 = new Ticket("dc", "北京,BJP", "天津南,TIP", "N,N,Y", "2019-02-13", listStartTime);
		//第二张票
//		Ticket ticket2 = new Ticket("dc", "麻城北,MBN", "杭州东,HGH", "N,N,Y", "2019-02-11", listStartTime);
		tickets.add(ticket1);
//		tickets.add(ticket2);                                                                                                                                                                                                                          
		Ticket12306 _12306 = new Ticket12306();
		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = tickets.get(i);
			new Thread() {
				@Override
				public void run() {
					_12306.purchaseTicketByChrom(ticket);
				}
			}.start();
		}
	}
	/**
	 * 
	 * 功能描述:通过谷歌浏览器购票
	 * 作者:mxc
	 * 创建时间:2019年1月24日 下午6:28:53
	 * @param ticket
	 */
	public void purchaseTicketByChrom(Ticket ticket) {
		String path = this.getClass().getClassLoader().getResource("chromedriver.exe").getPath();
		System.setProperty("webdriver.chrome.driver", path);
		WebDriver driver = new ChromeDriver();
		purchaseTicket(ticket, driver);
	}
	
	private void purchaseTicket(Ticket ticket, WebDriver driver) {
		String baseUrl = "https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=%s&fs=%s&ts=%s&date=%s&flag=%s";
		String url = String.format(baseUrl, ticket.getLinktypeid(), ticket.getFs(), ticket.getTs(), ticket.getDate(), ticket.getFlag());
		
		begin: while (true) {
			driver.get(url);
			//等待对象
			WebDriverWait driverWait = null;
			try {
				//最多等待15秒  每0.5秒一次
				driverWait = new WebDriverWait(driver,15L,500L);
				//所有行元素的集合
				List<WebElement> trList = driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//tr[starts-with(@id,'ticket_')]")));
				//所有的开始时间
				List<String> listStartTime = ticket.getListStartTime();
				iteratorTrList:
				for (int i = 0; i < trList.size(); i++) {
					WebElement tr = trList.get(i);
					String text = tr.findElement(By.className("start-t")).getText();
					//硬座
					WebElement hardSeat = tr.findElement(By.xpath(".//td[starts-with(@id,'YZ_')]"));
					//无座
					WebElement noSeat = tr.findElement(By.xpath(".//td[starts-with(@id,'WZ_')]"));
					//硬座的文本
					String hardSeatText = hardSeat.getText();
					//无座的文本
					String noSeatText = noSeat.getText();
					if ((i + 1) == trList.size()) {
						Thread.sleep(5000);
						 continue begin;
					}
					//没有硬座和无座的票
					boolean noTicket = ("--".equals(hardSeatText) || "无".equals(hardSeatText)) && ("--".equals(noSeatText) || "无".equals(noSeatText));
					if (noTicket) {
						continue;
					}
					//遍历所有发车时间，如果匹配到需要的票，退出行元素遍历
					for (int j = 0; j < listStartTime.size(); j++) {
						String beginTime = listStartTime.get(j);
						if (beginTime.equals(text)) {
							tr.findElement(By.className("btn72")).click();
							break iteratorTrList;
						}
					}
				}
				//显性等待跳出登录模态框后  点击账号登录
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("账号登录"))).click();
				driver.findElement(By.id("J-userName")).sendKeys(AppConstant.TICKET_USERNAME);
				driver.findElement(By.id("J-password")).sendKeys(AppConstant.TICKET_PASSWORD);
				//登录图片元素
				WebElement loginImg = driver.findElement(By.id("J-loginImg"));
				String base64Img = loginImg.getAttribute("src");
				Base64Util.decodeImage(base64Img.split(",")[1], "/login.png");
			    //像素点的二元数组  将12306验证码图片下载下来，使用ps软件（有在线软件）打开，查看8张小图片的像素点，将这8个像素点写入进数组中
				int[][] pointArray = {{15,67}, {116,72}, {204,67}, {300,75}, {24,159}, {120,157}, {205,157}, {301,151}};
				String result = YdmUtil.decode("/login.png", 6701);
				System.out.println(result);
				char[] resultArray = result.toCharArray();
				Actions actions = new Actions(driver);
				for (int i = 0; i < resultArray.length; i++) {
					String authCodeString = String.valueOf(resultArray[i]);
					Integer authCode = Integer.valueOf(authCodeString);
					System.out.println(resultArray[i]);
					actions.moveToElement(loginImg, pointArray[authCode - 1][0], pointArray[authCode - 1][1]).perform();;
					actions.click().perform();
				}
				while (true) {
					try {
						// 立即登录按钮
						driver.findElement(By.id("J-login")).click();
						break;
					} catch (Exception e) {
						continue begin;
					}
				}
				//选择乘客
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(AppConstant.NORMALPASSENGER))).click();
				//提交订单
				driver.findElement(By.id("submitOrder_id")).click();
				//确认
				driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("qr_submit_id"))).click();
				Thread.sleep(5000L);
				driver.quit();
				break;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				continue begin;
			}
		}
	}
}
