package algorithm;

import network.Worker;
import network.Request;
import network.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LeastUsageFirst extends AbstractLBAlgorithm {

    private List<Worker> workers;
    private HashMap<Worker, Integer> workerRequestCnt;
    private HashMap<Worker, Double> CPUUsage;
    private HashMap<Worker, Double> MemUsage;

    private HashMap<Worker, Double> CPUUsagePerRequest;
    private HashMap<Worker, Double> MemUsagePerRequest;

    public LeastUsageFirst(List<Worker> workers) {
        this.workers = workers;
        workerRequestCnt = new HashMap<>();
        CPUUsage = new HashMap<>();
        MemUsage = new HashMap<>();
        CPUUsagePerRequest = new HashMap<>();
        MemUsagePerRequest = new HashMap<>();

        for (Worker w: workers) {
            CPUUsage.put(w, 0.0);
            MemUsage.put(w, 0.0);
            CPUUsagePerRequest.put(w, 0.1);
            MemUsagePerRequest.put(w, 0.1);
            workerRequestCnt.put(w, 0);
        }

        // Periodically poll performance report from each server
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Worker w : workers) {
                        var client = HttpClient.newHttpClient();
                        String uri = "http://" + w.ip + ":8005/monitor";
                        var workerRequest = HttpRequest.newBuilder(URI.create(uri)).build(); // timeout 2 seconds
                        client.sendAsync(workerRequest, HttpResponse.BodyHandlers.ofString())
                                .thenApply(workerResponse -> workerResponse.body())
                                .thenAccept(body -> {
                                    double cpuUsage = Double.valueOf(body.split(",")[0].substring(0, 5));
                                    double memUsage = Double.valueOf(body.split(",")[1].substring(0, 5));
                                    System.out.println("[" + w.ip + "]" + " CPU: " + cpuUsage + " MEM: " + memUsage);
                                    CPUUsage.put(w, cpuUsage);
                                    MemUsage.put(w, memUsage);
                                    if (workerRequestCnt.get(w) != 0) {
                                        CPUUsagePerRequest.put(w, cpuUsage / workerRequestCnt.get(w));
                                        MemUsagePerRequest.put(w, memUsage / workerRequestCnt.get(w));
                                    }
                                });
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public Worker choose(Request request) {
        Worker lowestUsageWorker = workers.get(0);
        Double lowestUsage = Math.max(CPUUsage.get(lowestUsageWorker), MemUsage.get(lowestUsageWorker));
        for (Worker w : workers) {
//            System.out.println(w.ip + " CPU " + CPUUsage.get(w) + " MEM " + MemUsage.get(w) + " " + CPUUsagePerRequest.get(w) + " " + MemUsage.get(w));
            if (Math.max(CPUUsage.get(w), MemUsage.get(w)) < lowestUsage) {
                lowestUsageWorker = w;
                lowestUsage = Math.max(CPUUsage.get(w), MemUsage.get(w));
            }
        }

        return lowestUsageWorker;
    }

    @Override
    public synchronized void onRequest(Request request) {
            Worker worker = request.worker;
        try {
            workerRequestCnt.put(worker, workerRequestCnt.getOrDefault(worker, 0) + 1);
            CPUUsage.put(worker, CPUUsage.get(worker) + CPUUsagePerRequest.get(worker));
            MemUsage.put(worker, MemUsage.get(worker) + MemUsagePerRequest.get(worker));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Override
    public synchronized void onResponse(Response response) {
        Worker worker = response.worker;
        workerRequestCnt.put(worker, workerRequestCnt.get(worker) - 1);
        CPUUsage.put(worker, CPUUsage.get(worker) - CPUUsagePerRequest.get(worker));
        MemUsage.put(worker, MemUsage.get(worker) - MemUsagePerRequest.get(worker));
    }

    @Override
    public synchronized void onRequestFail(Request request) {
        Worker worker = request.worker;
        workerRequestCnt.put(worker, workerRequestCnt.get(worker) - 1);
        CPUUsage.put(worker, CPUUsage.get(worker) - CPUUsagePerRequest.get(worker));
        MemUsage.put(worker, MemUsage.get(worker) - MemUsagePerRequest.get(worker));
    }
}
