package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.ArrayList;

public class LeastAvgResponseTimeAlgo extends AbstractLBAlgorithm {

    private String name = "LeastResponseTime";

    class Timestamp {
        public long requestTimestamp;
        public long responseTimestamp = 0L;
        public long responseTime = 0L;
        public Timestamp(long requestTimestamp) {
            this.requestTimestamp = requestTimestamp;
        }

        public void setResponseTimestamp(long responseTimestamp) {
            this.responseTimestamp = responseTimestamp;
            this.responseTime = responseTimestamp - requestTimestamp;
        }

        public long getTime() {
            return responseTime > 0 ? responseTime : System.currentTimeMillis() - requestTimestamp;
        }
    }

    private List<Worker> workers;
    private HashMap<String, Timestamp> requestTimestampMap;
    private HashMap<Worker, Deque<String>> workerRequestMap;
    private Integer windowSize;
    private Integer count;
    private boolean coldstart;
    private HashMap<Worker, Integer> workerTotalMap;

    public LeastAvgResponseTimeAlgo(List<Worker> workers) {
        this.workers = workers;
        windowSize = 20;
        count = 0;
        coldstart = true;
        requestTimestampMap = new HashMap<>();
        workerRequestMap = new HashMap<>();
        workerTotalMap = new HashMap<>();
        workers.forEach(t -> {
            workerRequestMap.put(t, new ArrayDeque<>());
            workerTotalMap.put(t, 0);
        });
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public synchronized Worker choose(Request request) {
        if (coldstart) {
            Worker worker = workers.get(count % workers.size());
            count += 1;
            if (count >= windowSize * workers.size() * 1) {
                coldstart = false;
            }
            workerTotalMap.put(worker, workerTotalMap.get(worker)+1);
            return worker;
        }
  
        List<Double> weights = new ArrayList<>();
        Double weightSum = 0.0;

        for (Worker w : workers) {
            double responseTime = workerRequestMap.get(w).stream().map(id -> requestTimestampMap.get(id).getTime()).reduce(0L, Long::sum);
            weights.add(1/responseTime);
            weightSum += 1/responseTime;
            System.out.println("Server " + w.ip + " " + w.port + ": " + responseTime + " total: " + workerTotalMap.get(w));
        }
        
        for (int i = 1; i < workers.size(); i++) {
            weights.set(i, weights.get(i) + weights.get(i-1));
        }
        double weightSumFinal = weightSum;
        weights = weights.stream().map(w -> w / weightSumFinal).collect(Collectors.toList());
        Random rand = new Random();
        double val = rand.nextDouble();
        int left = 0;
        int right = workers.size();
        while (right > left) {
            int mid = left + (right - left) / 2;
            if (val > weights.get(mid)) {
                left = mid+1;
            } else {
                right = mid;
            }
        }
        workerTotalMap.put(workers.get(left), workerTotalMap.get(workers.get(left))+1);
        return workers.get(left);
    }

    @Override
    public synchronized void onRequest(Request request) {
        // if the number of requests surpass the window size
        Worker worker = request.worker;
        long currentTime = System.currentTimeMillis();
        requestTimestampMap.put(request.uuid, new Timestamp(currentTime));
        Deque<String> queue = workerRequestMap.get(worker);

        if (queue.size() == windowSize) {
            String firstuuid = queue.pop();
            requestTimestampMap.remove(firstuuid);
        }

        queue.addLast(request.uuid);
    }

    @Override
    public synchronized void onResponse(Response response) {
        long currentTime = System.currentTimeMillis();
        requestTimestampMap.get(response.uuid).setResponseTimestamp(currentTime);
    }

    @Override
    public synchronized void onRequestFail(Request request) {
        System.out.println("failed " + request.worker);
        Worker worker = request.worker;
        Deque<String> queue = workerRequestMap.get(worker);
        if (queue.contains(request.uuid)){
            queue.remove(request.uuid);
            requestTimestampMap.remove(request.uuid);
        }
    }
}