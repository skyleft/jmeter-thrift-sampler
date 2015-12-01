package com.yy.expe.sampler;

import com.yy.expe.bean.TProtocol;
import com.yy.expe.utils.Consts;
import org.apache.bcel.generic.ObjectType;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestStateListener;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zhangzongchao on 2015/11/25.
 */
public class ThriftSampler extends AbstractSampler implements TestStateListener {


    private static final Logger log = LoggingManager.getLoggerForClass();

    private Integer timeout = 3000;

    private String url;

    private Integer port;

    private TProtocol tProtocol;

    private String[] tMethods;

    private String paramMode;

    public ThriftSampler() {
        this.setProperty(Consts.THRIFT_URL, "127.0.0.1");
        this.setProperty(Consts.THRIFT_PORT, 9999);
        this.setProperty(Consts.THRIFT_PROTOCOL, TProtocol.TBinaryProtocol.getProtocol());
        this.setProperty(Consts.THRIFT_TIMEOUT, 3000);
        this.setProperty(Consts.THRIFT_METHOD,"");
        this.setProperty(Consts.THRIFT_PARAM_MODE,"随机生成");

    }

    public void setupTest() {
        this.url = this.getPropertyAsString(Consts.THRIFT_URL);
        this.port = this.getPropertyAsInt(Consts.THRIFT_PORT);
        this.timeout = this.getPropertyAsInt(Consts.THRIFT_TIMEOUT);
        this.tProtocol = TProtocol.myValueOf(this.getPropertyAsString(Consts.THRIFT_PROTOCOL));
    }

    public void teardownTest() {

    }


    public SampleResult runTest() {
        SampleResult result = new SampleResult();
        result.sampleStart(); // start stopwatch
        try {

            result.sampleEnd(); // stop stopwatch
            int headerResCode = 200;
            if (headerResCode == 200) {
                result.setSuccessful(true);
                result.setResponseMessage("done");
                result.setResponseCodeOK(); // 200 code
            } else {
                result.setSuccessful(false);
                result.setResponseCode("" + headerResCode);
                result.setResponseMessage("header code:" + headerResCode);
            }
        } catch (Exception e) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);
            result.setDataType(org.apache.jmeter.samplers.SampleResult.TEXT);
            result.setResponseCode("500");
            log.error("send yyp erroo:", e);
            teardownTest();
            setupTest();
        }
        return result;
    }




    public SampleResult sample(org.apache.jmeter.samplers.Entry entry) {
        return runTest();
    }

    public void testStarted() {
        log.info("test started");
    }

    public void testStarted(String host) {
        log.info("test started host:" + host);
    }

    public void testEnded() {
        log.info("test ended");
        this.teardownTest();
    }

    public void testEnded(String host) {
        log.info("test ended host:" + host);
        this.teardownTest();
    }


}