/**
 * @Since: 2020-03-27 21:48:33
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider;

import com.gargoylesoftware.htmlunit.ImmediateRefreshHandler;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import me.shy.spider.util.Util;
import org.apache.commons.io.FileUtils;

public final class Spider {

    private Updatater updatater = null;
    private int succeedCount = 0;
    private int totalCount = 0;
    private boolean isInterrupted = false;
    private String host = "";
    private UnvisitedQueue queue = null;

    @SuppressWarnings("deprecation")
    public void sprawl(boolean isSavedFile, String address, String urlFilter, String titleFilter, String xpathFilter,
        String outpurDir, String extension, long maxPageNumber, boolean isScriptLoad, boolean isAjaxLoad) {
        succeedCount = 0;
        totalCount = 0;
        AnchorFilter anchorFilter = AnchorFilter.getAnchorFilter(urlFilter, titleFilter);
        WebClient client = getWebClient(isScriptLoad, isAjaxLoad);
        queue = new UnvisitedQueue();
        if (initializeQueue(client, address, anchorFilter)) {
            sendMessage(Util.formatInfo("网络初始化成功，准备抓取网页！"));

            // 循环条件：待抓取的链接不为空且不超过max
            while (!queue.isEmpty() && succeedCount <= maxPageNumber && !isInterrupted) {
                HtmlAnchor visitedAnchor = (HtmlAnchor)queue.deQueue();
                if (!visitedAnchor.getTargetAttribute().equals("_blank")) {
                    totalCount++;
                    try {
                        Page page = visitedAnchor.click();
                        if (page.isHtmlPage()) {
                            HtmlPage nextPage = (HtmlPage)page;
                            if (isSavedFile) {
                                if (extension.equals(".txt")) {
                                    savedPageToText(visitedAnchor, nextPage, xpathFilter, outpurDir);
                                } else {
                                    savedPageToHtml(visitedAnchor, nextPage, xpathFilter, outpurDir);
                                }
                            }
                            succeedCount++;
                            sendMessage(Util.formatInfo(
                                "(" + succeedCount + ")完成：http://" + host + visitedAnchor.getHrefAttribute() + "（"
                                    + visitedAnchor.getTextContent() + "）"));
                            List<HtmlAnchor> newAnchors = nextPage.getAnchors();
                            addAnchorToQueue(newAnchors, anchorFilter, false);
                            client.getCache().clear();
                            client.getCurrentWindow().getHistory().removeCurrent();
                        }
                    } catch (Exception e) {
                        sendMessage(Util.formatInfo(
                            "处理链接" + visitedAnchor.getHrefAttribute() + "时出现错误，错误信息为：" + e.getMessage()));
                        e.printStackTrace();
                    }
                }
            }
            Util.waitMoment(800);
            if (!isInterrupted) {
                sendMessage(Util.formatInfo("任务完成！"));
            }
            sendMessage(Util.formatInfo("访问连接总数：" + totalCount + "；成功处理：" + succeedCount));
        }
        client.closeAllWindows();
    }

    public WebClient getWebClient(boolean isScriptLoad, boolean isAjaxLoad) {
        WebClient client = new WebClient();
        if (!isScriptLoad) {
            client.getOptions().setJavaScriptEnabled(false);
        } else {
            client.getOptions().setJavaScriptEnabled(true);
            client.getOptions().setThrowExceptionOnScriptError(false);
            // client.waitForBackgroundJavaScript(5000);
        }
        if (isAjaxLoad) {
            client.setAjaxController(new NicelyResynchronizingAjaxController());
        }
        client.setRefreshHandler(new ImmediateRefreshHandler());
        client.getCookieManager().setCookiesEnabled(true);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setRedirectEnabled(true);
        client.getOptions().setThrowExceptionOnFailingStatusCode(false);
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setTimeout(60000);
        return client;
    }

    public boolean initializeQueue(WebClient client, String address, AnchorFilter achorFilter) {
        try {
            sendMessage(Util.formatInfo("初始化网络..."));
            URL url = new URL(address);
            host = url.getHost();
            HtmlPage homePage = client.getPage(url);
            List<HtmlAnchor> anchors = (List<HtmlAnchor>)homePage.getAnchors();
            addAnchorToQueue(anchors, achorFilter, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            sendMessage(Util.formatInfo("初始化网络发生错误：" + e.getMessage()));
            return false;
        }
    }

    public void addAnchorToQueue(List<HtmlAnchor> anchors, AnchorFilter achorFilter, boolean isInit) {
        for (int i = 0, j = anchors.size(); i < j; i++) {
            HtmlAnchor anchor = anchors.get(i);
            if (achorFilter.isAccept(anchor)) {
                queue.enQueue(anchor, isInit);
            }
        }
    }

    public void savedPageToText(HtmlAnchor anchor, HtmlPage nextPage, String xpath, String outputDir) {
        String textData = null;
        if (!"".equals(xpath)) {
            textData = Util.xpathFilter(nextPage, xpath, true);
        } else {
            textData = nextPage.asText();
        }
        File textFile = new File(outputDir + File.separator + anchor.getTextContent() + ".txt");
        try {
            FileUtils.writeStringToFile(textFile, textData, false);
        } catch (IOException e) {
            sendMessage(Util.formatInfo(textFile.getName() + " 写入失败！原因是：" + e.getMessage()));
        }
    }

    public void savedPageToHtml(HtmlAnchor anchor, HtmlPage page, String xpath, String outputDir) {
        String xmlData = null;
        if (!"".equals(xpath)) {
            xmlData = Util.xpathFilter(page, xpath, false);
            xmlData = Util.formatHtmlString(xmlData, outputDir, true, true, true);
        } else {
            xmlData = page.asXml();
        }
        File htmlFile = new File(Util.getFileName(anchor.getHrefAttribute(), outputDir));
        try {
            FileUtils.writeStringToFile(htmlFile, xmlData, false);
        } catch (IOException e) {
            sendMessage(Util.formatInfo(htmlFile.getName() + " 写入失败！原因是：" + e.getMessage()));
        }
    }

    public void sendMessage(String message) {
        updatater.updataMessage(message);
    }

    /**
     * @return the updatater
     */
    public final Updatater getUpdatater() {
        return updatater;
    }

    /**
     * @param updatater
     *            the updatater to set
     *
     */
    public final void setUpadtater(Updatater updatater) {
        this.updatater = updatater;
    }

    /**
     * @return the isInterrupted
     */
    public final boolean isInterrupted() {
        return isInterrupted;
    }

    /**
     * @param isInterrupted
     *            the isInterrupted to set
     */
    public final void setInterrupted(boolean isInterrupted) {
        this.isInterrupted = isInterrupted;
    }
}
