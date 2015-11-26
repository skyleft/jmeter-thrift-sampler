package com.yy.expe.gui;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import com.yy.expe.sampler.ThriftSampler;
import com.yy.expe.utils.Consts;
import org.apache.jmeter.gui.util.HorizontalPanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zhangzongchao on 2015/11/25.
 */
public class ThriftGui extends AbstractSamplerGui {


    private static final Logger log = LoggingManager.getLoggerForClass();

    private JTextField urlField,portField,timeoutField;
    private JComboBox protocolCombo;

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

    public void modifyTestElement(TestElement testElement) {
        testElement.clear();
        configureTestElement(testElement);
        testElement.setProperty(Consts.THRIFT_URL, urlField.getText());
        testElement.setProperty(Consts.THRIFT_PORT,portField.getText());
        testElement.setProperty(Consts.THRIFT_TIMEOUT,timeoutField.getText());
        testElement.setProperty(Consts.THRIFT_PROTOCOL,protocolCombo.getSelectedItem().toString());
    }

    @Override
    public void configure(TestElement testElement) {
        urlField.setText(testElement.getPropertyAsString(Consts.THRIFT_URL));
        portField.setText(testElement.getPropertyAsString(Consts.THRIFT_PORT));
        timeoutField.setText(testElement.getPropertyAsString(Consts.THRIFT_TIMEOUT));
        protocolCombo.setSelectedItem(testElement.getPropertyAsString(Consts.THRIFT_PROTOCOL));
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

        JLabel ipLabel = new JLabel("Thrift接口地址:");
        timeoutField = new JTextField();
        ipLabel.setLabelFor(timeoutField);

        JPanel ipPanel = new HorizontalPanel();
        ipPanel.add(ipLabel);
        ipPanel.add(timeoutField);



        settingPanel.add(ipPanel);




        // 总panel
        JPanel dataPanel = new JPanel(new BorderLayout(5, 0));

        dataPanel.add(settingPanel, BorderLayout.NORTH);


        return dataPanel;
    }

    public void clearGui() {
        super.clearGui();

        timeoutField.setText("");
       // data.setText(""); // $NON-NLS-1$
    }

}
