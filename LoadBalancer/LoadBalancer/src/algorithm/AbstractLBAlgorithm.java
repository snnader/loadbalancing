package algorithm;

import network.Request;
import network.Response;
import network.Worker;

public class AbstractLBAlgorithm implements LBAlgorithm {

    @Override
    public Worker choose() {
        throw new UnsupportedOperationException("Must implement choose()");
    }

    @Override
    public void onRequest(Request request) {
    }

    @Override
    public void onResponse(Response response) {
    }

    @Override
    public void onRequestFail(Request request) {
    }
}
