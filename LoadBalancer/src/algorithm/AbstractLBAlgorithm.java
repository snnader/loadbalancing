package algorithm;

import network.Request;
import network.Response;
import network.Worker;

public class AbstractLBAlgorithm implements LBAlgorithm {

    @Override
    public String getName() {
        return "Undefined";
    }

    @Override
    public Worker choose(Request request) {
        throw new UnsupportedOperationException("Must implement choose() manually");
    }

    @Override
    public void onRequest(Request request) {
        throw new UnsupportedOperationException("Must implement onRequest() manually");
    }

    @Override
    public void onResponse(Response response) {
        throw new UnsupportedOperationException("Must implement onResponse() manually");
    }

    @Override
    public void onRequestFail(Request request) {
    }
}
