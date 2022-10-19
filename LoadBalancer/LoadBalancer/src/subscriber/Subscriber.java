package subscriber;

import network.Request;
import network.Response;

public interface Subscriber {
    void onRequest(Request request);
    void onResponse(Response response);
    void onRequestFail(Request request);
}
