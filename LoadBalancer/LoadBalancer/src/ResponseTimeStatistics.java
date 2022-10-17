import java.util.HashMap;

public class ResponseTimeStatistics implements Subscriber {

    public int requestCount = 0;
    public long totalTime = 0;
    public HashMap<String, Long> requestTimestamps;
    public long beginTime = 0;

    public ResponseTimeStatistics() {
        this.requestTimestamps = new HashMap();
        this.beginTime = System.currentTimeMillis();
    }

    public void onRequest(Request request) {
        this.requestCount += 1;
        System.out.println("received request " + request.uuid);
        requestTimestamps.put(request.uuid, System.currentTimeMillis());
    }

    public void onResponse(Response response) {
        System.out.println("received response " + response.uuid);

        Long requestTime = requestTimestamps.get(response.uuid);
        Long responseTime = System.currentTimeMillis();
        totalTime += responseTime - requestTime;
        this.requestTimestamps.remove(response.uuid);

        String info = "[" + ((System.currentTimeMillis()-beginTime)/1000) + "] " + "total " + totalTime + "ms avg: " + totalTime / requestCount + " ms";
        Logger.log("ResponseTime", info);
    }
}
