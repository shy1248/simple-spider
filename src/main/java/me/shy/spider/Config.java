/**
 * @Since: 2020-03-27 21:48:35
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider;

import java.io.Serializable;

public final class Config implements Serializable{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String url;
	private String urlFilter;
	private String titleFilter;
	private String dataFilter;
	private String outputDir;
	private int fileExtension;
	private String maxNumber;
	private boolean isScriptLoad;
	private boolean isAjaxLoad;
	private boolean isLogin;

	/**
	 * @return the url
	 */
	public final String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public final void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the urlFilter
	 */
	public final String getUrlFilter() {
		return urlFilter;
	}
	/**
	 * @param urlFilter the urlFilter to set
	 */
	public final void setUrlFilter(String urlFilter) {
		this.urlFilter = urlFilter;
	}
	/**
	 * @return the titleFilter
	 */
	public final String getTitleFilter() {
		return titleFilter;
	}
	/**
	 * @param titleFilter the titleFilter to set
	 */
	public final void setTitleFilter(String titleFilter) {
		this.titleFilter = titleFilter;
	}
	/**
	 * @return the dataFilter
	 */
	public final String getDataFilter() {
		return dataFilter;
	}
	/**
	 * @param dataFilter the dataFilter to set
	 */
	public final void setDataFilter(String dataFilter) {
		this.dataFilter = dataFilter;
	}
	/**
	 * @return the outputDir
	 */
	public final String getOutputDir() {
		return outputDir;
	}
	/**
	 * @param outputDir the outputDir to set
	 */
	public final void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	/**
	 * @return the fileExtension
	 */
	public final int getFileExtension() {
		return fileExtension;
	}
	/**
	 * @param fileExtension the fileExtension to set
	 */
	public final void setFileExtension(int fileExtension) {
		this.fileExtension = fileExtension;
	}
	/**
	 * @return the maxNumber
	 */
	public final String getMaxNumber() {
		return maxNumber;
	}
	/**
	 * @param maxNumber the maxNumber to set
	 */
	public final void setMaxNumber(String maxNumber) {
		this.maxNumber = maxNumber;
	}

	/**
	 * @return the isScriptLoad
	 */
	public final boolean isScriptLoad() {
		return isScriptLoad;
	}
	/**
	 * @param isScriptLoad the isScriptLoad to set
	 */
	public final void setScriptLoad(boolean isScriptLoad) {
		this.isScriptLoad = isScriptLoad;
	}
	/**
	 * @return the isAjaxLoad
	 */
	public final boolean isAjaxLoad() {
		return isAjaxLoad;
	}
	/**
	 * @param isAjaxLoad the isAjaxLoad to set
	 */
	public final void setAjaxLoad(boolean isAjaxLoad) {
		this.isAjaxLoad = isAjaxLoad;
	}
	/**
	 * @return the isLogin
	 */
	public final boolean isLogin() {
		return isLogin;
	}
	/**
	 * @param isLogin the isLogin to set
	 */
	public final void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

}
