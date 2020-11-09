/**
 * @Since: 2020-03-27 21:48:36
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */
/**
 * @author: Yu Shuibo, E-Mail:yushuibo2010@139.com
 * @date: 2015年3月19日下午9:35:55
 * @fileName: AnchorFilter.java
 * @version:1.0
 * @describe :
 * 			类说明。。。
 *
 * ALL RIGHTS RESERVED,COPYRIGHT(C) Yu Shuibo, LIMITED 2015.
 */

package me.shy.spider;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

public class AnchorFilter {
	private String[] urlFilters;
	private String[] titleFilters;

	public AnchorFilter(String[] urlFilters, String[] titleFilters) {
		this.urlFilters = urlFilters;
		this.titleFilters = titleFilters;
	}

	public static AnchorFilter getAnchorFilter(String urlFilter, String titleFilter) {
		String[] urlFilters = new String[]{""};
		String[] titleFilters  = new String[]{""};
		if (!"".equals(urlFilter.trim())) {
			urlFilters = urlFilter.split(";");
		}
		if (!"".equals(titleFilter.trim())) {
			titleFilters = titleFilter.split(";");
		}
		AnchorFilter anchorFilter = new AnchorFilter(urlFilters, titleFilters);
		return anchorFilter;
	}

	public boolean isAccept(HtmlAnchor anchor) {
		String href = anchor.getHrefAttribute();
		String title = anchor.getTextContent();
		for (int i = 0, j = urlFilters.length; i < j; i++) {
			for (int k = 0, h = titleFilters.length; k < h; k++) {
				if (href.startsWith(urlFilters[i])) {
					if (title.contains(titleFilters[k])) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
