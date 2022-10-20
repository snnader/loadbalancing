package subscriber;

import network.Request;
import network.Response;
import util.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThroughputStatistics extends AbstractSubscriber {
    public int traffic = 0;
    public long startTime = 0; // last start of a time period
    public long beginTime = 0; // log starting time

    public ThroughputStatistics() {
        Logger.log("Throughput", "[0] Started");
        this.startTime = System.currentTimeMillis();
        this.beginTime = System.currentTimeMillis();

        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                System.out.println("traffic: " + totalTraffic + " start: " + startTime + " current: " + System.currentTimeMillis() + " throughput: " + (String.valueOf(1000.0 * totalTraffic / (System.currentTimeMillis() - startTime))));
                long currentTime = System.currentTimeMillis();
                String info = "[" + ((currentTime-beginTime)/1000) + "] " + (String.valueOf(1000.0 * traffic / (currentTime - startTime)) + " B/s");
                Logger.log("Throughput", info);

                // Reset time and traffic
                traffic = 0;
                startTime = currentTime;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void onRequest(Request request) {
        try {
            traffic += request.request.getRequestURI().getRawQuery().getBytes().length;
            traffic += request.request.getRequestBody().toString().getBytes().length;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void onResponse(Response response) {
        traffic += response.response.uri().toString().getBytes().length;
        traffic += response.response.body().toString().getBytes().length;
    }
}