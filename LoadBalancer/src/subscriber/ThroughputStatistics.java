package subscriber;

import network.Request;
import network.Response;
import util.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThroughputStatistics extends AbstractSubscriber {
    public int traffic = 0;
    public int totalTraffic = 0;
    public long startTime = 0; // last start of a time period
    public long beginTime = 0; // log starting time
    private boolean start = false; // only log after receive first request

    public ThroughputStatistics() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                System.out.println("traffic: " + totalTraffic + " start: " + startTime + " current: " + System.currentTimeMillis() + " throughput: " + (String.valueOf(1000.0 * totalTraffic / (System.currentTimeMillis() - startTime))));
                if (start) {
                    long currentTime = System.currentTimeMillis();
                    double currentThroughput = 1000.0 * traffic / (currentTime - startTime);
                    String info = "[" + ((currentTime - beginTime) / 1000) + "] "
                            + (String.valueOf(currentThroughput)) + " B/s"
                            + " avg " + String.format("%.1f", 1000.0 * totalTraffic / (currentTime - beginTime)) + " B/s";
                    Logger.log("Throughput", info);
                    // Reset time and traffic
                    traffic = 0;
                    startTime = currentTime;
                }
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public synchronized void onRequest(Request request) {
        try {
            if (!this.start) {
                // start log from first request
                this.start = true;
                this.startTime = System.currentTimeMillis();
                this.beginTime = System.currentTimeMillis();
            }
            if (request.request.getRequestURI().getRawQuery() != null) {
                traffic += request.request.getRequestURI().getRawQuery().getBytes().length;
                totalTraffic += request.request.getRequestURI().getRawQuery().getBytes().length;
            }
            if (request.request.getRequestBody() != null) {
                traffic += request.request.getRequestBody().toString().getBytes().length;
                totalTraffic += request.request.getRequestBody().toString().getBytes().length;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public synchronized void onResponse(Response response) {
        if (response.response.uri() != null) {
            traffic += response.response.uri().toString().getBytes().length;
            totalTraffic += response.response.uri().toString().getBytes().length;
        }
        if (response.response.body() != null) {
            traffic += response.response.body().toString().getBytes().length;
            totalTraffic += response.response.body().toString().getBytes().length;
        }
    }
}