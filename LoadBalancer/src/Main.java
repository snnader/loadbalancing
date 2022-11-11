import algorithm.*;
import loadbalancer.LoadBalancer;
import network.Worker;
import subscriber.FailureRateStatistics;
import subscriber.ResponseTimeStatistics;
import subscriber.Subscriber;
import subscriber.ThroughputStatistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // All worker nodes
        List<Worker> workers = new ArrayList<>(10);
        workers.add(new Worker("172.31.17.150", "8001"));
        workers.add(new Worker("172.31.18.180", "8001"));
        workers.add(new Worker("172.31.14.254", "8001"));
        workers.add(new Worker("172.31.5.174", "8001"));
        workers.add(new Worker("172.31.15.101", "8001"));
        workers.add(new Worker("172.31.12.170", "8001"));
        workers.add(new Worker("172.31.0.255", "8001"));
        workers.add(new Worker("172.31.6.95", "8001"));
        workers.add(new Worker("172.31.11.132", "8001"));
        workers.add(new Worker("172.31.2.68", "8001"));

        // Load Balancing Algorithm
        Scanner scanner = new Scanner(System.in);
        System.out.println("LB algorithm: ");
        String algoName = scanner.nextLine();
        LBAlgorithm algorithm = null;
        while (algorithm == null) {
            switch (algoName) {
                case "least usage", "1" -> algorithm = new LeastUsageFirst(workers);
                case "least connection", "2" -> algorithm = new LeastRecentlyUsed_LeastConnection(workers);
                case "response time", "3" -> algorithm = new LeastAvgResponseTimeAlgo(workers);
                case "consistent hashing", "4" -> algorithm = new ConsistentHashAlgorithm(workers);
                case "finish time", "5" -> algorithm = new EstimatedFinishTime(workers);
                case "round robin", "6" -> algorithm = new RoundRobinAlgorithm(workers);
                case "random", "7" -> algorithm = new RandomAlgorithm(workers);
                default -> {
                    System.out.println(" 1 least usage\n 2 least connection\n 3 response time\n 4 consistent hashing\n 5 finish time\n 6 round robin\n 7 random\n");
                    System.out.println("LB algorithm: ");
                    algoName = scanner.nextLine();
                }
            }
        }

        List<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(new ResponseTimeStatistics());
        subscribers.add(new ThroughputStatistics());
        subscribers.add(new FailureRateStatistics());

        LoadBalancer loadBalancer = new LoadBalancer(subscribers, algorithm);
        loadBalancer.start();
    }
}