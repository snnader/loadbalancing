import algorithm.LBAlgorithm;
import algorithm.RoundRobinAlgorithm;
import loadbalancer.LoadBalancer;
import network.Worker;
import subscriber.FailureRateStatistics;
import subscriber.ResponseTimeStatistics;
import subscriber.Subscriber;
import subscriber.ThroughputStatistics;

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

        List<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(new ResponseTimeStatistics());
        subscribers.add(new ThroughputStatistics());
        subscribers.add(new FailureRateStatistics());


        LoadBalancer loadBalancer = new LoadBalancer(subscribers, algorithm);
        loadBalancer.start();
    }
}