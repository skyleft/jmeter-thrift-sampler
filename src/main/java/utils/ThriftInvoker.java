package utils;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by zhangzongchao on 2015/12/1.
 */
public class ThriftInvoker {

    private static final String SERVER_IP = "115.231.33.152";
    private static final Integer SERVER_PORT = 35979;
    private static final Integer TIMEOUT = 30000;

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException, TTransportException {
        TTransport transport = new TFramedTransport(new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT));
       transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        File f = new File("E:\\logs\\bin\\");
        URL[] urls = new URL[]{f.toURI().toURL()};
        ClassLoader classLoader = new URLClassLoader(urls);


        Class thriftServiceClass = classLoader.loadClass("TalkGodGatewayService$Client");

        Object client = thriftServiceClass.getConstructor(TProtocol.class).newInstance(protocol);
        for (Method ffff:thriftServiceClass.getDeclaredMethods()){
            System.out.println(ffff.getName());
        }
        Method method = thriftServiceClass.getMethod("IsRoomUserToday",int.class);
        Object returncode = method.invoke(client,50015649);
        System.out.println(returncode);
    }
}
