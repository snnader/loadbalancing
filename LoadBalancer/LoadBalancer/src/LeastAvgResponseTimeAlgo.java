import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;

public class LeastAvgResponseTimeAlgo implements LBAlgorithm {

    private List<Worker> workers;
    private Map<Worker, Deque<Long>> requestTimeMap;
    private Map<Worker, Deque<Long>> responseTimeMap;
    private SortedMap<Worker, Float> avgResponseMap;
    private static Integer windowSize = 1000;

    public LeastAvgResponseTimeAlgo(List<Worker> workers) {
        this.workers = workers;
        requestTimeMap = new HashMap<>();
        responseTimeMap = new HashMap<>();
        avgResponseMap = new TreeMap<>(Comparator);
        workers.forEach(t -> {
            responseTimeMap.put(t, new ArrayDeque<>());
            requestTimeMap.put(t, new ArrayDeque<>());
            avgResponseMap.put(t, 0F);
        });
    }

    public Worker choose(Request request) {
        // compute avg time for each worker
        workers.forEach(worker -> {
            Long totalResponseTime = 
                responseTimeMap.get(worker).stream().reduce(0L, Long::sum) +
                System.currentTimeMillis() * (requestTimeMap.get(worker).size() - responseTimeMap.get(worker).size()) - 
                requestTimeMap.get(worker).stream().reduce(0L, Long::sum);
            avgResponseMap.put(worker, totalResponseTime.floatValue() / requestTimeMap.get(worker).size());
        });
        return avgResponseMap.firstKey();
    }

    public void onRequest(Worker worker, Request request) {
        // if the number of requests surpass the window size
        if (responseTimeMap.get(worker).size() == windowSize) {
            responseTimeMap.get(worker).removeFirst();
            requestTimeMap.get(worker).removeFirst();
        }
        requestTimeMap.get(worker).addLast(System.currentTimeMillis());
    }

    public void onResponse(Response response) {
        responseTimeMap.get(response.worker).addLast(System.currentTimeMillis());
    }
}