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
    public void onRequest(Request request) {
        totalRequest += 1;
        Logger.log("FailureRate", "[" + ((System.currentTimeMillis()-beginTime)/1000) + "] requests: " + totalRequest + " responses: " + totalResponse);
    }

    @Override
    public void onResponse(Response response) {
        totalResponse += 1;
        Logger.log("FailureRate", "[" + ((System.currentTimeMillis()-beginTime)/1000) + "] requests: " + totalRequest + " responses: " + totalResponse);
    }

    @Override
    public void onRequestFail(Request request) {
        failed += 1;
    }
}
