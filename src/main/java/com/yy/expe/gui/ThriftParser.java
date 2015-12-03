package com.yy.expe.gui;

import me.skyleft.utils.ExecUtil;
import me.skyleft.utils.JCUtil;
import me.skyleft.utils.Ret;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhangzongchao on 2015/12/2.
 */
public class ThriftParser {

    public ThriftParser(File thriftFile,File thrifeExe) {
        this.thriftFile = thriftFile;
        this.thriftExe = thrifeExe;
        thread = new Thread(new ThriftParserRunner());
    }

    private File thriftFile,thriftExe;
    private Thread thread;
    private boolean hasStarted = false;
    private Ret ret = Ret.FAIL;
    private volatile Class serviceClass;
    private volatile List<Method> serviceMethod = new ArrayList<Method>();
    private volatile ParserStatus parserStatus = new ParserStatus();

    public void run() throws InterruptedException {
        thread.start();

    }

    public ParserStatus getParserStatus(){
        parserStatus.setIsDone(isDone());
        return parserStatus;
    }

    public boolean isDone(){
        return hasStarted && (!thread.isAlive());
    }

    public Class getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class serviceClass) {
        this.serviceClass = serviceClass;
    }

    public List<Method> getServiceMethod() {
        return serviceMethod;
    }

    public void setServiceMethod(List<Method> serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    class ThriftParserRunner implements Runnable{

        @Override
        public void run() {
            hasStarted = true;
            parserStatus.setStatus("调用Thrift.exe生成java源程序...");
            try {
                ret = ExecUtil.run(ExecUtil.buildCommand(thriftExe,thriftFile));
            } catch (Exception e) {
                e.printStackTrace();
                ret = Ret.FAIL;
                ThriftParser.this.ret.setMessage(e.getMessage());
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!ret.getSuccess()) return;
            parserStatus.setStatus("自动编译接口代码...");
            try{
                ret = new JCUtil().doCompile(ExecUtil.javaFilePath+File.separator+"gen-java","",new File(ThriftParser.class.getProtectionDomain().getCodeSource().getLocation().getFile()).getParentFile().getParent()+File.separator+"thrift",ExecUtil.javaDestPath);
            }catch (Exception e){
                e.printStackTrace();
                ret = Ret.FAIL;
                ThriftParser.this.ret.setMessage(e.getMessage());
                return;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!ret.getSuccess()) return;
            parserStatus.setStatus("Thrift接口分析...");
            File f = new File(ExecUtil.javaDestPath);
            File[] classFiles = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".class");
                }
            });
            URL[] urls = null;
            try {
                urls = new URL[]{f.toURI().toURL()};
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (urls==null){
                ret = Ret.FAIL;
                ret.setMessage("class文件目录不正确");
                return;
            }
            ClassLoader classLoader = new URLClassLoader(urls,ThriftParser.class.getClassLoader());
            //Class thriftServiceClass = classLoader.loadClass("TalkGodGatewayService$Client");
            for (File classFile : classFiles){
                String className = classFile.getName().split("\\.")[0];
                Class thriftInterface = null;
                if (className.matches("\\w+\\$Iface")){
                    try {
                        serviceClass = classLoader.loadClass(className.split("\\$Iface")[0]+"$Client");
                        thriftInterface = classLoader.loadClass(className);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    if (serviceClass==null || thriftInterface==null){
                        ret = Ret.FAIL;
                        ret.setMessage("载入Thrift主客户端类失败");
                        return;
                    }
                    serviceMethod.addAll(Arrays.asList(thriftInterface.getDeclaredMethods()));
                    break;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parserStatus.setStatus("Thrift定义文件分析完毕，请继续完成设置");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ret = Ret.SUCCESS;

        }
    }

    class ParserStatus{
        private boolean isDone = false;
        private String status = "";

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isDone() {
            return isDone;
        }

        public void setIsDone(boolean isDone) {
            this.isDone = isDone;
        }
    }

    public Ret getRet() {
        return ret;
    }
}


