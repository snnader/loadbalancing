import com.sun.net.httpserver.HttpExchange;

public class RequestCountStatistics implements Subscriber {
    public int count = 0;

    public void onRequest(Request request) {
        this.count += 1;
        System.out.println("total request: " + this.count);
    }

    public void onResponse(Response response) {
    }
}
