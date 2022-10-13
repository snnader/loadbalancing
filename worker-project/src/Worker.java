import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.sun.net.httpserver.HttpServer;

public class Worker {
    public InetAddress ipAddress = InetAddress.getByName("0.0.0.0");
    public int port = 8001;
    public HttpServer server;

    public Worker() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(this.ipAddress, this.port), 0);
    }

    public Worker(InetAddress ipAddress) throws IOException {
        this.ipAddress = ipAddress;
        this.server = HttpServer.create(new InetSocketAddress(this.ipAddress, this.port), 0);
    }

    public Worker(InetAddress ipAddress, int port) throws IOException {
        this.ipAddress = ipAddress;
        this.port = port;
        this.server = HttpServer.create(new InetSocketAddress(this.ipAddress, this.port), 0);
    }

    public Worker(InetSocketAddress ipSocketAddress) throws IOException {
        this.ipAddress = ipSocketAddress.getAddress();
        this.port = ipSocketAddress.getPort();
        this.server = HttpServer.create(ipSocketAddress, 0);
    }

    public void start() {
        server.createContext("/", new RequestHandler());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        server.setExecutor(threadPoolExecutor);
        System.out.println("Server started on port " + this.port);
        server.start();
    }
}
