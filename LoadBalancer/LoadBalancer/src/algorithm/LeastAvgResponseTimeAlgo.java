package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.List;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

public class LeastAvgResponseTimeAlgo extends AbstractLBAlgorithm {

    class Timestamp {
        public long requestTimestamp;
        public long responseTimestamp;
        public Timestamp(long requestTimestamp, long responseTimestamp) {
            this.requestTimestamp = requestTimestamp;
            this.responseTimestamp = responseTimestamp;
        }
    }

    class ResponseTime {
        public long requestTimestampSum = 0;
        public long responseTimestampSum = 0;
        public int unrespondedCnt = 0;

        public long calculate() {
            return responseTimestampSum + unrespondedCnt * System.currentTimeMillis() - requestTimestampSum;
        }
    }

    private List<Worker> workers;
    private HashMap<String, Timestamp> requestTimestampMap;
    private HashMap<Worker, Deque<String>> workerRequestMap;
    private HashMap<Worker, ResponseTime> workerResponseTimeMap;
    private static Integer windowSize = 1000;

    public LeastAvgResponseTimeAlgo(List<Worker> workers) {
        this.workers = workers;
        requestTimestampMap = new HashMap<>();
        workerRequestMap = new HashMap<>();
        workerResponseTimeMap = new HashMap<>();
        workers.forEach(t -> {
            workerRequestMap.put(t, new ArrayDeque<>());
            workerResponseTimeMap.put(t, new ResponseTime());
        });
    }

    @Override
    public Worker choose() {
        // compute avg time for each worker
        Worker lowestWorker = workers.get(0);
        double lowestResponseTime = !workerRequestMap.get(lowestWorker).isEmpty()?
                1.0 * workerResponseTimeMap.get(lowestWorker).calculate() / workerRequestMap.get(lowestWorker).size()
                : 0;
        for (Worker w : workers) {
            double responseTime = !workerRequestMap.get(w).isEmpty()?
                    1.0 * workerResponseTimeMap.get(w).calculate() / workerRequestMap.get(w).size()
                    : 0;
//            System.out.println("Server " + w.ip + " " + w.port + ": " + responseTime + " total: " + workerRequestMap.get(w).size());
            if (responseTime < lowestResponseTime) {
                lowestWorker = w;
                lowestResponseTime = responseTime;
            }
        }
        return lowestWorker;
    }

    @Override
    public void onRequest(Request request) {
        // if the number of requests surpass the window size
        Worker worker = request.worker;
        requestTimestampMap.put(request.uuid, new Timestamp(System.currentTimeMillis(), 0));
        Deque<String> queue = workerRequestMap.get(worker);

        if (queue.size() == windowSize) {
            String firstuuid = queue.getFirst();
            ResponseTime responseTime = workerResponseTimeMap.get(worker);
            responseTime.requestTimestampSum -= requestTimestampMap.get(firstuuid).requestTimestamp;
            responseTime.responseTimestampSum -= requestTimestampMap.get(firstuuid).responseTimestamp;

            if(requestTimestampMap.get(firstuuid).requestTimestamp == 0) {
                responseTime.unrespondedCnt -= 1;
            }

            queue.removeFirst();
            requestTimestampMap.remove(firstuuid);
        }

        queue.addLast(request.uuid);
        workerResponseTimeMap.get(worker).requestTimestampSum += requestTimestampMap.get(request.uuid).requestTimestamp;
        workerResponseTimeMap.get(worker).unrespondedCnt += 1;
    }

    @Override
    public void onResponse(Response response) {
        requestTimestampMap.get(response.uuid).responseTimestamp = System.currentTimeMillis();
        ResponseTime responseTime = workerResponseTimeMap.get(response.worker);
        responseTime.responseTimestampSum += System.currentTimeMillis();
        responseTime.unrespondedCnt -= 1;
    }
}