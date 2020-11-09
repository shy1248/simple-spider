/**
 * @Since: 2020-03-27 21:48:32
 * @Author: shy
 * @Email: yushuibo@ebupt.com / hengchen2005@gmail.com
 * @Version: v1.0
 * @Description: -
 */

package me.shy.spider;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import me.shy.spider.util.FileService;
import me.shy.spider.util.Util;

public final class SpiderWin extends Updatater {

    private JFrame mainWindow;
    // private JLabel checkCodeImageLabel;
    // private JTextField uesrNameTextField;
    // private JPasswordField passwordTextField;
    // private JTextField checkCodeTextField;
    private JTextField addressTextField;
    private JTextField urlTextField;
    private JTextField linkTitleTextField;
    private JTextField dataTextField;
    private JTextField fileDirTextField;
    private JButton browseButton;
    private JComboBox<String> fileExtensionComboBox;
    private JTextField maxNumberTextField;
    private JCheckBox scriptCheckBox;
    private JCheckBox ajaxCheckBox;
    private JCheckBox loginNeededCheckBox;
    private JTextArea messagePrintTextArea;
    private JButton startButton;
    /*
     * private static JScrollPane tableScrollPane; private static JLabel
     * tableLabel; private static JTable visitedTable; private static String[]
     * columns = new String[] { "序号", "标题", "地址" }; private static
     * DefaultTableModel model = new DefaultTableModel( new Object[][] {},
     * columns); JScrollBar scrollBar = tableScrollPane.getVerticalScrollBar();
     */

    private Spider spider = null;
    private Thread thread = null;
    private File configFile = null;

    public static void main(String[] args) {
        SpiderWin spiderWin = new SpiderWin();
        spiderWin.initalizeGui();
        spiderWin.updataMessage(Util.formatInfo("请单击\"开始\"按钮来启动任务！"));
    }

    public void initalizeGui() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 主窗口
        mainWindow = new JFrame("Spider by Java!");
        mainWindow.setBounds(400, 150, 583, 578);
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.getContentPane().setLayout(null);
        mainWindow.setLocationRelativeTo(null);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        // 地址
        JLabel addressLabel = new JLabel("网址：（例如：http://www.w3school.com.cn/xpath/index.asp）");
        addressLabel.setBounds(8, 10, 348, 20);
        mainWindow.getContentPane().add(addressLabel);
        addressLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        addressTextField = new JTextField();
        addressTextField.setToolTipText("需要抓取的首页网址。可输入多个，用 ';'隔开。");
        addressTextField.setBounds(8, 30, 560, 22);
        mainWindow.getContentPane().add(addressTextField);
        addressTextField.setColumns(100);

        // 过滤信息面板
        JPanel filterpPanel = new JPanel();
        filterpPanel.setBounds(5, 60, 567, 54);
        mainWindow.getContentPane().add(filterpPanel);
        filterpPanel.setLayout(null);
        filterpPanel.setBorder(new TitledBorder("过滤条件"));
        JLabel urlLabel = new JLabel("URL：");
        filterpPanel.add(urlLabel);
        urlLabel.setBounds(15, 24, 30, 20);
        urlLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        urlTextField = new JTextField();
        filterpPanel.add(urlTextField);
        urlTextField.setToolTipText("指定URL开头的字符串。如‘http://www.bai.du.com’，'/'等。可以多个异或运算，中间以';'隔开。");
        urlTextField.setBounds(45, 24, 126, 22);
        urlTextField.setColumns(40);
        JLabel linkTitleLabel = new JLabel("链接标题:");
        filterpPanel.add(linkTitleLabel);
        linkTitleLabel.setBounds(181, 24, 60, 20);
        linkTitleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        linkTitleTextField = new JTextField();
        filterpPanel.add(linkTitleTextField);
        linkTitleTextField.setToolTipText("指定链接标题包涵的字符串。可以多个异或运算，中间以';'隔开。");
        linkTitleTextField.setBounds(241, 24, 126, 22);
        linkTitleTextField.setColumns(40);
        JLabel dataLabel = new JLabel("元数据：");
        filterpPanel.add(dataLabel);
        dataLabel.setBounds(377, 24, 50, 20);
        dataLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dataTextField = new JTextField();
        filterpPanel.add(dataTextField);
        dataTextField.setToolTipText("对网页的元数据进行过滤，可以除去网页中广告等不需要的信息。匹配XPath表达式。");
        dataTextField.setBounds(427, 24, 133, 22);
        dataTextField.setColumns(20);

        // 文件输出信息
        JPanel explorerpPanel = new JPanel();
        explorerpPanel.setBounds(5, 120, 567, 54);
        mainWindow.getContentPane().add(explorerpPanel);
        explorerpPanel.setLayout(null);
        explorerpPanel.setBorder(new TitledBorder("文件保存"));
        JLabel dirLabel = new JLabel("目录：");
        explorerpPanel.add(dirLabel);
        dirLabel.setBounds(15, 24, 36, 20);
        dirLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        fileDirTextField = new JTextField();
        explorerpPanel.add(fileDirTextField);
        fileDirTextField.setToolTipText("下载文件保存的目录");
        fileDirTextField.setBounds(51, 24, 215, 22);
        explorerpPanel.add(fileDirTextField);
        fileDirTextField.setColumns(40);
        fileDirTextField.setEditable(false);
        browseButton = new JButton("浏览");
        browseButton.setBounds(270, 22, 60, 24);
        explorerpPanel.add(browseButton);
        JLabel extensionLabel = new JLabel("保存类型：");
        explorerpPanel.add(extensionLabel);
        extensionLabel.setBounds(340, 24, 60, 20);
        extensionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        fileExtensionComboBox = new JComboBox<String>(Constants.FILE_EXTENSION);
        fileExtensionComboBox.setToolTipText("保存文件的类型");
        fileExtensionComboBox.setBounds(400, 23, 55, 22);
        fileExtensionComboBox.setSelectedIndex(1);
        explorerpPanel.add(fileExtensionComboBox);
        JLabel maxNumberLabel = new JLabel("保存数：");
        explorerpPanel.add(maxNumberLabel);
        maxNumberLabel.setBounds(465, 24, 60, 20);
        maxNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        maxNumberTextField = new JTextField();
        explorerpPanel.add(maxNumberTextField);
        maxNumberTextField.setToolTipText("设置抓取网页的最大数量！");
        maxNumberTextField.setText("1000");
        maxNumberTextField.setBounds(520, 23, 40, 22);
        maxNumberTextField.setColumns(10);

        /*
         * // 登陆版面 userInfoPanel = new JPanel(); userInfoPanel.setBounds(5, 180,
         * 567, 54); mainWindow.getContentPane().add(userInfoPanel);
         * userInfoPanel.setLayout(null); userInfoPanel.setBorder(new
         * TitledBorder("登陆信息")); JLabel usernameLabel = new JLabel("用户名：");
         * usernameLabel.setBounds(15, 24, 50, 20);
         * userInfoPanel.add(usernameLabel);
         * usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
         * uesrNameTextField = new JTextField();
         * uesrNameTextField.setToolTipText("要登陆网站的用户名");
         * uesrNameTextField.setBounds(65, 24, 80, 22);
         * userInfoPanel.add(uesrNameTextField);
         * uesrNameTextField.setColumns(10); JLabel passwordLabel = new
         * JLabel("密码："); passwordLabel.setBounds(165, 24, 45, 20);
         * userInfoPanel.add(passwordLabel);
         * passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
         * passwordTextField = new JPasswordField();
         * passwordTextField.setToolTipText("要登陆网站的密码");
         * passwordTextField.setBounds(210, 24, 80, 22);
         * userInfoPanel.add(passwordTextField);
         * passwordTextField.setColumns(10); passwordTextField.setEchoChar('*');
         * JLabel checkCodeLabel = new JLabel("验证码：");
         * checkCodeLabel.setBounds(310, 24, 50, 20);
         * userInfoPanel.add(checkCodeLabel);
         * checkCodeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
         * checkCodeTextField = new JTextField();
         * checkCodeTextField.setBounds(360, 24, 60, 22);
         * userInfoPanel.add(checkCodeTextField);
         * checkCodeTextField.setColumns(10); checkCodeImageLabel = new
         * JLabel(); checkCodeImageLabel.setOpaque(true);
         * checkCodeImageLabel.setBounds(425, 22, 60, 26);
         * userInfoPanel.add(checkCodeImageLabel); loginButton = new
         * JButton("登陆"); loginButton.setBounds(500, 22, 60, 24);
         * userInfoPanel.add(loginButton);
         */

        scriptCheckBox = new JCheckBox("运行JavaScript");
        mainWindow.getContentPane().add(scriptCheckBox);
        scriptCheckBox.setToolTipText("尝试加载JavaScript（注意：勾选此选项可能导致性能降低，如果大量网页抓取失败，请不要勾选此选项！）。");
        scriptCheckBox.setBounds(5, 184, 110, 20);
        ajaxCheckBox = new JCheckBox("加载AJax");
        mainWindow.getContentPane().add(ajaxCheckBox);
        ajaxCheckBox.setToolTipText("如果需要加载AJax，请勾选（注意：勾选此选项可能导致性能降低）。");
        ajaxCheckBox.setBounds(135, 184, 74, 20);
        loginNeededCheckBox = new JCheckBox("需要登陆");
        mainWindow.getContentPane().add(loginNeededCheckBox);
        loginNeededCheckBox.setToolTipText("有些网站需要登陆后才能抓取到相应信息！如要使用此功能，请参考软件目录下‘Config.ini’文件说明。");
        loginNeededCheckBox.setBounds(229, 184, 74, 20);
        startButton = new JButton("开始");
        startButton.setToolTipText("单击来启动任务！");
        startButton.setBounds(505, 182, 60, 24);
        mainWindow.getContentPane().add(startButton);

        JScrollPane scrollPane = new JScrollPane();
        // scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED,
        // null, null, null, null));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(5, 214, 565, 328);
        mainWindow.getContentPane().add(scrollPane);
        messagePrintTextArea = new JTextArea();
        scrollPane.setViewportView(messagePrintTextArea);
        messagePrintTextArea.setText("信息输出：\n");
        messagePrintTextArea.setEditable(false);
        messagePrintTextArea.setLineWrap(false);

        /*
         * tableLabel = new JLabel("已处理的页面："); mainWindow.add(tableLabel);
         * tableLabel.setBounds(5, 214, 100, 20); tableScrollPane = new
         * JScrollPane(); // scrollPane1.setViewportBorder(new
         * BevelBorder(BevelBorder.LOWERED, // null, null, null, null));
         * tableScrollPane .setHorizontalScrollBarPolicy(ScrollPaneConstants.
         * HORIZONTAL_SCROLLBAR_AS_NEEDED); tableScrollPane.setBounds(5, 234,
         * 565, 196); visitedTable = new JTable(); visitedTable.setModel(model);
         * tableScrollPane.setViewportView(visitedTable);
         * mainWindow.getContentPane().add(tableScrollPane);
         * DefaultTableColumnModel columnModel = (DefaultTableColumnModel)
         * visitedTable .getColumnModel();
         * columnModel.getColumn(0).setMinWidth(45);
         * columnModel.getColumn(0).setMaxWidth(45);
         */
        browseButton.addActionListener(new browseAction());
        startButton.addActionListener(new startAction());
        loadConfig();
        mainWindow.setVisible(true);
    }

    public String getInputUrls() {
        String url = addressTextField.getText();
        if (!"".equals(url.trim())) {
            return url;
        } else {
            JOptionPane.showMessageDialog(mainWindow, "开始前请输入首页网址！");
        }
        return null;
    }

    public void saveConfig() {
        Config config = new Config();
        config.setUrl(addressTextField.getText().trim());
        config.setUrlFilter(urlTextField.getText().trim());
        config.setTitleFilter(linkTitleTextField.getText().trim());
        config.setDataFilter(dataTextField.getText().trim());
        config.setOutputDir(fileDirTextField.getText().trim());
        config.setFileExtension(fileExtensionComboBox.getSelectedIndex());
        config.setMaxNumber(maxNumberTextField.getText().trim());
        config.setScriptLoad(scriptCheckBox.isSelected());
        config.setAjaxLoad(ajaxCheckBox.isSelected());
        config.setLogin(loginNeededCheckBox.isSelected());
        try {
            FileService.writeObjectToFile(config, configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        updataMessage(Util.formatInfo("正在加载已保存的配置..."));
        try {
            String configPath = this.getClass().getResource("/cache.dat").getPath();
            configFile = new File(configPath);
            Config config = (Config)FileService.readObjectFromFile(configFile);
            if (null != config) {
                addressTextField.setText(config.getUrl());
                urlTextField.setText(config.getUrlFilter());
                linkTitleTextField.setText(config.getTitleFilter());
                dataTextField.setText(config.getDataFilter());
                fileDirTextField.setText(config.getOutputDir());
                fileExtensionComboBox.setSelectedIndex(config.getFileExtension());
                maxNumberTextField.setText(config.getMaxNumber());
                scriptCheckBox.setSelected(config.isScriptLoad());
                ajaxCheckBox.setSelected(config.isAjaxLoad());
                loginNeededCheckBox.setSelected(config.isLogin());
                updataMessage(Util.formatInfo("配置加载完成！"));
            }
        } catch (NullPointerException e) {
            updataMessage(Util.formatInfo("未发现配置文件，跳过..."));
        } catch (ClassNotFoundException e) {
			updataMessage(Util.formatInfo("配置文件格式损坏，忽略..."));
        } catch (IOException e) {
            updataMessage(Util.formatInfo("配置文件读取出错：" + e.getMessage()+", 已忽略。"));
        }
    }

    public void setCompEnable(boolean isEnable) {
        addressTextField.setEnabled(isEnable);
        urlTextField.setEnabled(isEnable);
        linkTitleTextField.setEnabled(isEnable);
        dataTextField.setEnabled(isEnable);
        browseButton.setEnabled(isEnable);
        fileExtensionComboBox.setEnabled(isEnable);
        maxNumberTextField.setEnabled(isEnable);
        scriptCheckBox.setEnabled(isEnable);
        ajaxCheckBox.setEnabled(isEnable);
        loginNeededCheckBox.setEnabled(isEnable);
    }

    /*
     * public void setTableRowData(HtmlAnchor anchor) { Object[] rowData = new
     * Object[columns.length]; rowData[0] = visitedTable.getRowCount() + 1;
     * rowData[1] = anchor.getTextContent(); rowData[2] =
     * anchor.getHrefAttribute(); model.addRow(rowData); }
     */
    /*
     * public void showLoginWindow() { JFrame loginWindow = new JFrame("登录"); }
     */

    /*
     * (non-Javadoc)
     *
     * @see me.shy.spider.Printable#print(java.lang.String)
     */
    @Override public void updataMessage(final String message) {
        messagePrintTextArea.append(message);
        messagePrintTextArea.setCaretPosition(messagePrintTextArea.getText().length());
    }

    private class browseAction implements ActionListener {
        /*
         * (non-Javadoc)
         *
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        @Override public void actionPerformed(ActionEvent e) {
            JFileChooser parseDir = new JFileChooser();
            parseDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            parseDir.setAcceptAllFileFilterUsed(false);
            int result = parseDir.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileDirTextField.setText(parseDir.getSelectedFile().toString());
            }
        }
    }

    private class startAction implements ActionListener {
        /*
         * (non-Javadoc)
         *
         * @see
         * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
         * )
         */
        @Override public void actionPerformed(ActionEvent e) {
            thread = new Thread() {
                @Override public void run() {
                    String address = addressTextField.getText().trim();
                    String outputDir = fileDirTextField.getText().replaceAll("\\\\", "/").trim();
                    String extension = Constants.FILE_EXTENSION[fileExtensionComboBox.getSelectedIndex()];
                    String maxPageNumber = maxNumberTextField.getText().trim();
                    String urlFilter = urlTextField.getText().trim();
                    String titleFilter = linkTitleTextField.getText().trim();
                    String xpath = dataTextField.getText().trim();
                    boolean isScriptLoad = scriptCheckBox.isSelected();
                    boolean isAjaxLoad = ajaxCheckBox.isSelected();
                    // boolean isLoginNeeded = loginNeededCheckBox.isSelected();
                    if ("".equals(address)) {
                        JOptionPane.showMessageDialog(mainWindow, "网址还没填写哦！");
                    } else if ("".equals(maxPageNumber)) {
                        JOptionPane.showMessageDialog(mainWindow, "要抓取的最大网页数还没填写！");
                    } else {
                        messagePrintTextArea.setText("信息输出 ：\n");
                        startButton.setText("停止");
                        startButton.setToolTipText("单击来终止任务！");
                        setCompEnable(false);
                        saveConfig();
                        spider = new Spider();
                        spider.setUpadtater(SpiderWin.this);
                        spider.sprawl(true, address, urlFilter, titleFilter, xpath, outputDir, extension,
                            Long.parseLong(maxPageNumber), isScriptLoad, isAjaxLoad);
                        startButton.setText("开始");
                        startButton.setToolTipText("单击来启动任务！");
                        setCompEnable(true);
                    }
                }
            };
            if ("开始".equals(startButton.getText())) {
                thread.start();
            } else {
                spider.setInterrupted(true);
                Util.waitMoment(500);
                updataMessage(Util.formatInfo("任务已取消！"));
                startButton.setText("开始");
                startButton.setToolTipText("单击来启动任务！");
                setCompEnable(true);
            }
        }
    }
}
