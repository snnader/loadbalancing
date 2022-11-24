import algorithm.*;
import loadbalancer.LoadBalancer;
import network.Worker;
import subscriber.FailureRateStatistics;
import subscriber.ResponseTimeStatistics;
import subscriber.Subscriber;
import subscriber.ThroughputStatistics;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // All worker nodes
        List<Worker> workers = new ArrayList<>(8);
        workers.add(new Worker("172.31.27.142", "8001", 4.8)); // 13
        workers.add(new Worker("172.31.19.112", "8001", 4.8)); // 14
        workers.add(new Worker("172.31.22.212", "8001", 4.8)); // 15
        workers.add(new Worker("172.31.30.64", "8001", 4.8)); // 16
        workers.add(new Worker("172.31.16.251", "8001", 4.8)); // 17
        workers.add(new Worker("172.31.30.223", "8001", 4.8)); // 18
        workers.add(new Worker("172.31.24.55", "8001", 9.6)); // 19
        workers.add(new Worker("172.31.22.37", "8001", 9.6)); // 20

        // Load Balancing Algorithm
        Scanner scanner = new Scanner(System.in);
        System.out.println("LB algorithm: ");
        String algoName = scanner.nextLine();
        LBAlgorithm algorithm = null;
        while (algorithm == null) {
            switch (algoName) {
                case "least usage": case "1":
                    algorithm = new LeastUsageFirst(workers);
                    break;
                case "least connection": case "2":
                    algorithm = new LeastRecentlyUsed_LeastConnection(workers);
                    break;
                case "response time": case "3":
                    algorithm = new LeastAvgResponseTimeAlgo(workers);
                    break;
                case "consistent hashing": case "4":
                    algorithm = new ConsistentHashAlgorithm(workers);
                    break;
                case "finish time": case "5":
                    algorithm = new EstimatedFinishTime(workers);
                    break;
                case "round robin": case "6":
                    algorithm = new RoundRobinAlgorithm(workers);
                    break;
                case "random": case "7":
                    algorithm = new RandomAlgorithm(workers);
                    break;
                default:
                    System.out.println(" 1 least usage\n 2 least connection\n 3 response time\n 4 consistent hashing\n 5 finish time\n 6 round robin\n 7 random\n");
                    System.out.println("LB algorithm: ");
                    algoName = scanner.nextLine();
            }
        }

        Logger.setAlgorithmName(algorithm.getName() + "_" + System.currentTimeMillis());

        List<Subscriber> subscribers = new ArrayList<>();
        subscribers.add(new ResponseTimeStatistics());
        subscribers.add(new ThroughputStatistics());
        subscribers.add(new FailureRateStatistics());

        LoadBalancer loadBalancer = new LoadBalancer(subscribers, algorithm);
        loadBalancer.start();
    }
}