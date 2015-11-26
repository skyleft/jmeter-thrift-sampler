package com.yy.expe.bean;

import jodd.util.StringUtil;
import org.apache.thrift.protocol.TSimpleJSONProtocol;

/**
 * Created by zhangzongchao on 2015/11/26.
 */
public enum TProtocol {

    TBinaryProtocol("TBinary", "二进制格式"), TCompactProtocol("TCompact", "压缩格式"), TJSONProtocol("TJSON", "JSON格式"), TSimepleJSONProtocol("TSimpleJSON", "提供JSON只写协议, 生成的文件很容易通过脚本语言解析");

    private String protocol;
    private String desc;

    private TProtocol(String protocol, String desc) {
        this.protocol = protocol;
        this.desc = desc;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static TProtocol myValueOf(String protocol){
        if (StringUtil.isNotEmpty(protocol)){
            if ("tbinary".equalsIgnoreCase(protocol)){
                return TBinaryProtocol;
            }else if("tcompact".equalsIgnoreCase(protocol)){
                return TCompactProtocol;
            }else if ("tjson".equalsIgnoreCase(protocol)){
                return TJSONProtocol;
            }else if("tsimplejson".equalsIgnoreCase(protocol)){
                return TSimepleJSONProtocol;
            }
        }
        return TBinaryProtocol;
    }
}
