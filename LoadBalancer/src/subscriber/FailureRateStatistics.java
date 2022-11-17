package subscriber;

import network.Request;
import network.Response;
import util.Logger;

public class FailureRateStatistics extends AbstractSubscriber {

    public int totalRequest = 0;
    public int totalResponse = 0;

    public int failed = 0;

    public long beginTime = 0;

    public FailureRateStatistics() {
        Logger.log("FailureRate", "[0] Started");
        beginTime = System.currentTimeMillis();
    }

    @Override
    public synchronized void onRequest(Request request) {
        totalRequest += 1;
        String info = "[" + ((System.currentTimeMillis()-beginTime)/1000) + "]"
                + " requests: " + totalRequest
                + " responses: " + totalResponse
                + " failed: " + failed;
        Logger.log("FailureRate", info);
    }

    @Override
    public synchronized void onResponse(Response response) {
        totalResponse += 1;
        String info = "[" + ((System.currentTimeMillis()-beginTime)/1000) + "]"
                + " requests: " + totalRequest
                + " responses: " + totalResponse
                + " failed: " + failed;
        Logger.log("FailureRate", info);
    }

    @Override
    public synchronized void onRequestFail(Request request) {
        failed += 1;
        String info = "[" + ((System.currentTimeMillis()-beginTime)/1000) + "]"
                + " requests: " + totalRequest
                + " responses: " + totalResponse
                + " failed: " + failed;
        Logger.log("FailureRate", info);
    }
}
