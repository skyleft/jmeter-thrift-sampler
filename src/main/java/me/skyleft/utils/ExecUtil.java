package me.skyleft.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangzongchao on 2015/12/1.
 */
public class ExecUtil {

    public static String javaFilePath = System.getProperty("java.io.tmpdir")+File.separator+"jmeter_thrift_sampler"+File.separator+"src";
    public static String javaDestPath = System.getProperty("java.io.tmpdir")+File.separator+"jmeter_thrift_sampler"+File.separator+"bin";

    static {
        new File(javaFilePath).mkdirs();
        new File(javaDestPath).mkdirs();
    }

    public static Ret run(String command) throws IOException, InterruptedException {
        Process  process = Runtime.getRuntime().exec(command);
        process.waitFor();
        int resultCode = process.exitValue();
        if (resultCode == 0){
            return Ret.SUCCESS;
        }else{
            InputStream inputStream = process.getErrorStream();
            byte[] buf = new byte[512];
            int len = -1;
            StringBuilder result = new StringBuilder();
            while((len = inputStream.read(buf))>-1){
                result.append(new String(buf,0,len));
            }
            Ret ret = new Ret(false,result.toString());
            return ret;
        }
    }


    public static String buildCommand(File thriftExe, File thriftFile) {
        return thriftExe.getAbsolutePath() + " -r -gen java -o " + javaFilePath + " " + thriftFile.getAbsolutePath();
    }

}
