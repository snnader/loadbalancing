package subscriber;

import network.Request;
import network.Response;
import subscriber.AbstractSubscriber;

public class RequestCountStatistics extends AbstractSubscriber {

    public int count = 0;

    public synchronized void onRequest(Request request) {
        this.count += 1;
        System.out.println("total request: " + this.count);
    }

    public synchronized void onResponse(Response response) {
    }
}
