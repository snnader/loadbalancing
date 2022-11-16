package algorithm;

import network.Worker;
import network.Request;
import network.Response;

public interface LBAlgorithm {
    String getName();
    Worker choose(Request request);
    void onRequest(Request request);
    void onResponse(Response response);
    void onRequestFail(Request request);
}
