package utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhangzongchao on 2015/12/1.
 */
public class ExecUtil {
    //private static final String THRIFT_CMD = "D:\\software\\thrift.exe -r -gen java -o Ex:\\logs\\src ";
    public static Ret run(String command) throws IOException, InterruptedException {
        //THRIFT_CMD+" D:\\workspace\\newactivity\\trunk\\thriftman\\src\\main\\resources\\talkgod_gateway.thrift"
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


}
