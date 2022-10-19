package algorithm;

import network.Worker;
import network.Request;
import network.Response;

public interface LBAlgorithm {
    Worker choose();
    void onRequest(Request request);
    void onResponse(Response response);
    void onRequestFail(Request request);
}
