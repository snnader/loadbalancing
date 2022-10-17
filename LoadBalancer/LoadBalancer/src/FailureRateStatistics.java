
public class FailureRateStatistics implements Subscriber {

    public int totalRequest = 0;
    public int totalResponse = 0;

    public long beginTime = 0;

    public FailureRateStatistics() {
        beginTime = System.currentTimeMillis();
    }

    public void onRequest(Request request) {
        totalRequest += 1;
        Logger.log("FailureRate", "[" + ((System.currentTimeMillis()-beginTime)/1000) + "] requests: " + totalRequest + " responses: " + totalResponse);
    }

    public void onResponse(Response response) {
        totalResponse += 1;
        Logger.log("FailureRate", "[" + ((System.currentTimeMillis()-beginTime)/1000) + "] requests: " + totalRequest + " responses: " + totalResponse);
    }
}
