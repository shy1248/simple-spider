/**
 * @Since: 2020-03-27 21:48:48
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public final class Util {

	public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_PART_FORMAT = "yyyy-MM-dd";
	public static final String TIME_PART_FORMAT = "HH:mm:ss";

	/**
	 * 返回当前日期时间
	 *
	 * @return e.g. 2006-06-06 12:12:50
	 */
	public static String getCurDateTime() {
		return getCurDateTime(TIME_PART_FORMAT);
	}

	/**
	 * 返回当前日期
	 *
	 * @return e.g. 2006-06-06
	 */
	public static String getCurDate() {
		return getCurDateTime(DATE_PART_FORMAT);
	}

	/**
	 * 根据给定的格式返回当前日期或时间
	 *
	 * @param formatStr
	 * @return
	 */
	public static String getCurDateTime(String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		String now = sdf.format(new Date());
		return now;
	}

	/**
	 * 移除html标签
	 *
	 * @param content
	 * @return
	 */
	public static String formatHtmlString (String content, String outputDir,
			boolean isRemoveScripts, boolean isRemoveStyles, boolean isReplaceAnchors) {
		// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
		String scriptRegex = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
		// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
		String styleRegex = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
		// 定义HTML标签的正则表达式
		//String htmlTagRegex = "<[^>]+>";
		// 定义超链接表达式
		String linkRegex = "(<a).+?(href=)([\"\']).+?([\"\'])";
		if (content == null || content.isEmpty()) {
			return "ERROR";
		}

		// 去除js
		if (isRemoveScripts) {
			content = content.replaceAll(scriptRegex, "");
		}

		// 去除style
		if (isRemoveStyles) {
			content = content.replaceAll(styleRegex, "");
		}

		// 替换内部超链接
		if (isReplaceAnchors) {
			Pattern pattern = Pattern.compile(linkRegex);
			Matcher matcher = pattern.matcher(content);
			while (matcher.find()) {
				String temp = matcher.group(0);
				String link = temp.substring(temp.indexOf("href=") + 6, temp.length() - 1);
				String newLink = "<a href=\"" + getFileName(link, outputDir) + "\"";
				content = content.replaceAll(temp, newLink);
			}
		}

		// 合并空格
		// content = content.replaceAll("[\\s+", " ");
		return content.trim();
	}

	public static String getFileName(String href, String outputDir) {
		if (!href.startsWith("http")) {
			if (href.startsWith("/")) {
				href = outputDir + replaceIllegalChar(href) + ".html";
			} else {
				href = outputDir + "/" + replaceIllegalChar(href) + ".html";
			}
		}
		return href;
	}

	public static String replaceIllegalChar(String href) {
		if (href.contains(".")) {
			href = href.replaceAll("\\.", "_");
		}
		if (href.contains("?")) {
			href = href.replaceAll("\\?", "_");
		}
		if (href.contains("=")) {
			href = href.replaceAll("=", "_");
		}
		return href;
	}

	public static String getMessageFromHtml(String content) {
		String regEx_msg = "[\\s]*?var\\s+message\\s+=\\s+\"([\\S]*?)\"";
		if (content == null) {
			return "ERROR";
		}
		String temp = content.trim();
		Pattern p = null;
		Matcher m = null;
		p = Pattern.compile(regEx_msg);
		m = p.matcher(temp);
		try {
			while (m.find()) {
				temp = m.group(1);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	public static int getHour2Min(String hour) {
		int min = 0;
		String hm[] = hour.split(":");
		if (hm.length < 2) {
			min = Integer.parseInt(hour);
		} else {
			int h = Integer.parseInt(hm[0]) * 60;
			int m = Integer.parseInt(hm[1]);
			min = h + m;
		}
		return min;
	}

	/**
	 * 线程等待指定时间
	 */
	public static void waitMoment(long mi) {
		try {
			Thread.sleep(mi);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static byte[] getImageByteArray(BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 *
	 * 获取文件扩展名
	 *
	 * @param file
	 *
	 * @return
	 */

	public static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		} else {
			return "";
		}
	}

	public static String formatInfo(String info) {
		return "[" + getCurDateTime() + "]： " + info + "\n";
	}

	public static List<HtmlAnchor> getAnchorByXpathFormPage(String xPath, HtmlPage page, String xpath){
		List<HtmlAnchor> anchors = new ArrayList<HtmlAnchor>();
		@SuppressWarnings("unchecked")
		List<DomNode>  nodes = (List<DomNode>) page.getByXPath(xpath);
		for(DomNode node : nodes){
			Iterable<HtmlElement> elements =  node.getHtmlElementDescendants();
			for(Iterator<HtmlElement> iterator = elements.iterator();iterator.hasNext();){
				HtmlElement element = iterator.next();
				if (element instanceof HtmlAnchor) {
					HtmlAnchor anchor = (HtmlAnchor) element;
					anchors.add(anchor);
				}
			}
		}
		return anchors;
	}

	public static String xpathFilter(HtmlPage page, String xpath, boolean isReturnForText){
		StringBuilder builder = new StringBuilder();
		@SuppressWarnings("unchecked")
		List<DomNode> nodes = (List<DomNode>) page.getByXPath(xpath);
		for (DomNode node : nodes) {
			if(isReturnForText){
				builder.append(node.asText());
			}else {
				builder.append(node.asXml());
			}
		}
		return builder.toString();
	}

	public static String md5(String string) {
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
		    byte[] bytes = string.getBytes();
		    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		    messageDigest.update(bytes);
		    byte[] updateBytes = messageDigest.digest();
		    int len = updateBytes.length;
		    char myChar[] = new char[len * 2];
		    int k = 0;
		    for (int i = 0; i < len; i++) {
		    	byte byte0 = updateBytes[i];
		    	myChar[k++] = hexDigits[byte0 >>> 4 & 0x0f];
		    	myChar[k++] = hexDigits[byte0 & 0x0f];
		    }
		    return new String(myChar);
		 } catch (Exception e) {
		    return null;
		 }
	}

	/**
	 * 将GUI界面设置为系统样式，一般在程序入口调用即可
	 */
	public static void setDefaultSystemUI(){
		try{
			Locale locale = Locale.getDefault();
			Locale.setDefault(locale);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}
