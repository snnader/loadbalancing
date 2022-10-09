import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // All worker nodes
        List<Worker> workers = new ArrayList<>();
        workers.add(new Worker("127.0.0.1", "8002"));
//        workers.add(new Worker("127.0.0.1", "8003"));
//        workers.add(new Worker("127.0.0.1", "8004"));

        // Load Balancing Algorithm
        LBAlgorithm algorithm = new RoundRobinAlgorithm(workers);

        // All subscribers
        List<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(new RequestCountStatistics());
//        subscribers.add(new ThroughoutStatistics());
//        subscribers.add(new ResponseTimeStatistics());
//        subscribers.add(new FailureRateStatistics());


        LoadBalancer loadBalancer = new LoadBalancer(subscribers, algorithm);
        loadBalancer.start();
    }
}