package xsk.com.xsk;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import com.xuhao.didi.socket.server.impl.OkServerOptions;
import com.xuhao.didi.socket.server.impl.ServerManagerImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ServerSocketFactory;

public class MainActivity extends AppCompatActivity {
    public class TestSendData implements ISendable {
        private String str = "";

        public TestSendData() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("cmd", 14);
                jsonObject.put("data", "{x:2,y:1}");
                str = jsonObject.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] parse() {
            //根据服务器的解析规则,构建byte数组
            byte[] body = str.getBytes(Charset.defaultCharset());
            ByteBuffer bb = ByteBuffer.allocate(4 + body.length);
            bb.order(ByteOrder.BIG_ENDIAN);
            bb.putInt(body.length);
            bb.put(body);
            return bb.array();
        }
    }

    ServerSocketFactory serverSocketFactory = ServerSocketFactory.getDefault();
    ServerSocket serverSocket;
    private static final int PORT = 8080;
    private static final int BACKLOG = 8081;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public void startTestSocket(View view) {
//        localhost:8614
          ConnectionInfo info = new ConnectionInfo("192.168.232.2", PORT);
//调用OkSocket,开启这次连接的通道,调用通道的连接方法进行连接.
        final IConnectionManager manager = OkSocket.open(info);
        manager.registerReceiver(new SocketActionAdapter() {
            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                super.onSocketConnectionSuccess(info, action);
                manager.send(new TestSendData());
            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
                super.onSocketWriteResponse(info, action, data);
                System.out.println(new String(data.parse()));
            }

            @Override
            public void onSocketIOThreadStart(String action) {
                super.onSocketIOThreadStart(action);
            }
        });
        manager.connect();
     }

    public void startSocketServer(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerManagerImpl serverManager = new ServerManagerImpl();


                if (serverSocket != null && serverSocket.isBound()) {
                    InetAddress localhost = serverSocket.getInetAddress();
                    int port = serverSocket.getLocalPort();
                    Log.d("info", localhost.getHostAddress() + localhost.getCanonicalHostName() + localhost.getAddress());
                }
try {
                    String ip = NetWorkUtil.getHostIp(new NetWorkUtil.IpResult() {
                                            @Override
                                            public void result(String ip, InetAddress inetAddress) throws IOException {
                                                Log.d("ip", ip + inetAddress);

                                                serverSocket = serverSocketFactory.createServerSocket(PORT, BACKLOG, inetAddress);
                                                InetAddress localhost = serverSocket.getInetAddress();
                                                int port = serverSocket.getLocalPort();
                                                Log.d("info", inetAddress.getCanonicalHostName() + inetAddress.getHostAddress() + port);
                                                Log.d("info", localhost.getHostAddress() + localhost.getCanonicalHostName() + localhost.getAddress());
                                                while (true) {
                                                    Socket socket = serverSocket.accept();
                                                    InputStream inputStream = socket.getInputStream();
                                                    int read = inputStream.read();
                                                    byte[] tmpBytes = new byte[read];
                                                    String tmp = "";
                                                    while (read > 0) {
                                                       read = inputStream.read(tmpBytes);
                                                        tmp += new String(tmpBytes);
                                                    }
                                                    Log.d("接收", tmp);
                                                }
                                            }
                                        });
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }).start();

    }
    @SuppressWarnings("rawtypes")
    private  InetAddress getLocalHostLANAddress() throws UnknownHostException {
        try {



            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
                        if (inetAddr.isSiteLocalAddress()) {
                            // 如果是site-local地址，就是它了
                            return inetAddr;
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            return jdkSuppliedAddress;
        } catch (Exception e) {
            UnknownHostException unknownHostException = new UnknownHostException(
                    "Failed to determine LAN address: " + e);
            unknownHostException.initCause(e);
            throw unknownHostException;
        }
    }


    public static class NetWorkUtil {
        //匹配C类地址的IP
        public static final String regexCIp = "^192\\.168\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$";
        //匹配A类地址
        public static final String regexAIp = "^10\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$";
        //匹配B类地址
        public static final String regexBIp = "^172\\.(1[6-9]|2\\d|3[0-1])\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)\\.(\\d{1}|[1-9]\\d|1\\d{2}|2[0-4]\\d|25\\d)$";

interface IpResult{
    void result(String ip, InetAddress inetAddress) throws IOException;
}
        public static String getHostIp(IpResult ipResult) throws IOException {
            String hostIp;
            Pattern ip = Pattern.compile("(" + regexAIp + ")|" + "(" + regexBIp + ")|" + "(" + regexCIp + ")");
            Enumeration<NetworkInterface> networkInterfaces = null;
            try {
                networkInterfaces = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            InetAddress address;
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    address = inetAddresses.nextElement();
                    String hostAddress = address.getHostAddress();
                    Matcher matcher = ip.matcher(hostAddress);
                    if (matcher.matches()) {
                        hostIp = hostAddress;
                        if (ipResult != null) {
                            ipResult.result(hostAddress, address);
                        }
                        return hostIp;
                    }

                }
            }
            return null;
        }
    }
}
