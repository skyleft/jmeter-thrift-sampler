package com.yy.expe.gui;

import com.yy.expe.bean.TProtocol;
import com.yy.expe.sampler.ThriftSampler;
import com.yy.expe.utils.Consts;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by zhangzongchao on 2015/11/25.
 */
public class ThriftGui extends AbstractSamplerGui {


    private static final Logger log = LoggingManager.getLoggerForClass();

    private JTextField urlField,portField,timeoutField;
    private JComboBox protocolCombo,methodCombo,paramModeCombo;
    private File thriftFile,thriftExeFile;
    private JButton fileChooserButton,thriftChooserButton;

    public ThriftGui() {
        init();
    }

    public TestElement createTestElement() {
        ThriftSampler thriftSampler = new ThriftSampler();
        thriftSampler.setName(Consts.STATIC_LABEL);
        modifyTestElement(thriftSampler);
        return thriftSampler;
    }


    public String getLabelResource() {
        return null;
    }

    @Override
    public String getStaticLabel() {
        return Consts.STATIC_LABEL;
    }


    private String getSelectedMethods(){
        if (methodCombo == null) return "";
        StringBuilder sb = new StringBuilder("");
        Object[] selectedObjs = methodCombo.getSelectedObjects();
        int i = 0;
        for (Object selectedObj:selectedObjs){
            sb.append((i++>0?",":"")+selectedObj.toString());
        }
        return sb.toString();
    }

    private void setSelectedMethods(){

    }
    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        configureTestElement(testElement);
        testElement.setProperty(Consts.THRIFT_URL, urlField.getText());
        testElement.setProperty(Consts.THRIFT_PORT,portField.getText());
        testElement.setProperty(Consts.THRIFT_TIMEOUT,timeoutField.getText());
        testElement.setProperty(Consts.THRIFT_PROTOCOL,protocolCombo.getSelectedItem().toString());
        //testElement.setProperty(Consts.THRIFT_FILE,thriftFile.getAbsolutePath());
        testElement.setProperty(Consts.THRIFT_METHOD,getSelectedMethods());
        testElement.setProperty(Consts.THRIFT_PARAM_MODE,paramModeCombo.getSelectedItem().toString());
    }

    @Override
    public void configure(TestElement testElement) {
        urlField.setText(testElement.getPropertyAsString(Consts.THRIFT_URL));
        portField.setText(testElement.getPropertyAsString(Consts.THRIFT_PORT));
        timeoutField.setText(testElement.getPropertyAsString(Consts.THRIFT_TIMEOUT));
        protocolCombo.setSelectedItem(testElement.getPropertyAsString(Consts.THRIFT_PROTOCOL));
        paramModeCombo.setSelectedItem(testElement.getPropertyAsString(Consts.THRIFT_PARAM_MODE));
        setSelectedMethods();
        super.configure(testElement);
    }

    /*
     * Helper method to set up the GUI screen
     */
    private void init() {
        setLayout(new BorderLayout(0, 5));
        setBorder(makeBorder());
        add(makeTitlePanel(), BorderLayout.NORTH); // Add the standard title
        add(createDataPanel(), BorderLayout.CENTER);
    }

    /*
     * Create a data input text field
     *
     * @return the panel for entering the data
     */
    private Component createDataPanel() {

        // 设置
        JPanel settingPanel = new VerticalPanel(5, 0);

        JLabel urlLabel = new JLabel("Thrift接口地址:");
        urlField = new JTextField();
        urlLabel.setLabelFor(urlField);

        JPanel urlPanel = new HorizontalPanel();
        urlPanel.add(urlLabel);
        urlPanel.add(urlField);
        settingPanel.add(urlPanel);

        JLabel portLabel = new JLabel("Thrift接口端口:");
        portField = new JTextField();
        portLabel.setLabelFor(portField);
        JPanel  portPanel = new HorizontalPanel();
        portPanel.add(portLabel);
        portPanel.add(portField);
        settingPanel.add(portPanel);

        JLabel timeoutLabel = new JLabel("Thrift接口超时时间:");
        timeoutField = new JTextField();
        timeoutLabel.setLabelFor(timeoutField);
        JPanel  timeoutPanel = new HorizontalPanel();
        timeoutPanel.add(timeoutLabel);
        timeoutPanel.add(timeoutField);
        settingPanel.add(timeoutPanel);

        JLabel protocolLabel = new JLabel("Thrift接口协议类型:");
        protocolCombo = new JComboBox(new String[]{TProtocol.TBinaryProtocol.getProtocol(),TProtocol.TCompactProtocol.getProtocol()
        ,TProtocol.TJSONProtocol.getProtocol(),TProtocol.TSimepleJSONProtocol.getProtocol()});
        protocolLabel.setLabelFor(protocolCombo);
        JPanel  protocolPanel = new HorizontalPanel();
        protocolPanel.add(protocolLabel);
        protocolPanel.add(protocolCombo);
        settingPanel.add(protocolPanel);

        JLabel thriftLabel = new JLabel("Thrift.exe路径:");
        thriftChooserButton = new JButton("浏览...选择Thrift.exe");
        thriftChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isFile() && f.getName().toLowerCase().endsWith(".exe");
                    }

                    @Override
                    public String getDescription() {
                        return "请选择thrift.exe";
                    }
                });
                int result = fileChooser.showOpenDialog(fileChooserButton);
                if (result == JFileChooser.APPROVE_OPTION){
                    thriftExeFile = fileChooser.getSelectedFile();
                }else{
                    JOptionPane.showMessageDialog(null,"选择错误");
                }
            }
        });
        thriftLabel.setLabelFor(thriftChooserButton);
        JPanel  thriftPanel = new HorizontalPanel();
        thriftPanel.add(thriftLabel);
        thriftPanel.add(thriftChooserButton);
        settingPanel.add(thriftPanel);

        final JLabel fileLabel = new JLabel("Thrift接口定义文件:");
        fileChooserButton = new JButton("浏览...选择*.thrift");
        final JLabel fileNameLabel = new JLabel();
        fileChooserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileFilter() {
                    @Override
                    public boolean accept(File f) {
                        return f.isFile() && f.getName().toLowerCase().endsWith(".thrift");
                    }

                    @Override
                    public String getDescription() {
                        return "请选择thrift接口定义文件";
                    }
                });
                int result = fileChooser.showOpenDialog(fileChooserButton);
                if (result == JFileChooser.APPROVE_OPTION){
                    thriftFile = fileChooser.getSelectedFile();
                    fileNameLabel.setText(thriftFile.getAbsolutePath());
                    if (thriftExeFile==null||thriftFile==null){
                        JOptionPane.showMessageDialog(null,"必须选择正确的thrift.exe和*.thrift定义文件");
                        return;
                    }
                    ThriftParser thriftParser = new ThriftParser(thriftFile,thriftExeFile);
                    try {
                        thriftParser.run();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    ThriftParser.ParserStatus ps = thriftParser.getParserStatus();
                    while(!thriftParser.getParserStatus().isDone()){
                        fileNameLabel.setText(ps.getStatus());
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                    fileNameLabel.setText(ps.getStatus());

                    java.util.List<Method> serviceMethods = thriftParser.getServiceMethod();
                    for (Method serviceMethod:serviceMethods){
                        methodCombo.addItem(serviceMethod.getName());
                    }


                }else{
                    fileNameLabel.setText("");
                    JOptionPane.showMessageDialog(null,"选择错误");
                }
            }
        });
        fileLabel.setLabelFor(fileChooserButton);
        JPanel  filePanel = new HorizontalPanel();
        filePanel.add(fileLabel);
        filePanel.add(fileChooserButton);
        filePanel.add(fileNameLabel);
        settingPanel.add(filePanel);

        JLabel methodLabel = new JLabel("待测的Thrift接口方法:");
        methodCombo = new JComboBox();
        methodCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,methodCombo.getItemCount());
                if (methodCombo.getItemCount()<=0){
                    JOptionPane.showMessageDialog(null,"请先选择thrift定义文件");
                }
            }
        });
        methodLabel.setLabelFor(methodCombo);
        JPanel  methodPanel = new HorizontalPanel();
        methodPanel.add(methodLabel);
        methodPanel.add(methodCombo);
        settingPanel.add(methodPanel);

        JLabel paramModeLabel = new JLabel("Thrift接口入参生成模式:");
        paramModeCombo = new JComboBox(new String[]{"随机生成"});
        paramModeLabel.setLabelFor(paramModeCombo);
        JPanel  paramModePanel = new HorizontalPanel();
        paramModePanel.add(paramModeLabel);
        paramModePanel.add(paramModeCombo);
        settingPanel.add(paramModePanel);



        // 总panel
        JPanel dataPanel = new JPanel(new BorderLayout(5, 0));

        dataPanel.add(settingPanel, BorderLayout.NORTH);


        return dataPanel;
    }

    public void clearGui() {
        super.clearGui();

        urlField.setText("");
        portField.setText("");
        timeoutField.setText("");
        protocolCombo.setSelectedIndex(0);

        // data.setText(""); // $NON-NLS-1$
    }

    public static void main(String[] args) {
        new ThriftGui().show();
    }


}
