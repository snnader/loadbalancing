package loadbalancer;

import algorithm.LBAlgorithm;
import com.sun.net.httpserver.HttpServer;
import subscriber.Subscriber;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class LoadBalancer {

    private List<Subscriber> subscribers;
    private LBAlgorithm algorithm;

    public LoadBalancer(List<Subscriber> subscribers, LBAlgorithm algorithm) {
        this.subscribers = subscribers;
        this.algorithm = algorithm;
    }

    public void start() {
        System.out.println(this.algorithm.getName() + " Load Balancer Started");
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", 8001), 0);
            server.createContext("/", new RequestHandler(this.subscribers, this.algorithm));
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
            server.setExecutor(threadPoolExecutor);
            server.start();
        } catch(IOException e) {
            System.out.println(e);
        }
    }
}
