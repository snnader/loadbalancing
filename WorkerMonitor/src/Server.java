import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

public class Server {
    public String ipAddress = "0.0.0.0";
    public int port = 8005;

    public void start() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(ipAddress, port), 0);
            server.createContext("/monitor", new RequestHandler());
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            server.setExecutor(threadPoolExecutor);
            System.out.println("Server started");
            server.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
