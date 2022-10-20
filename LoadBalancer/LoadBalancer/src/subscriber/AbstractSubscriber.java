package subscriber;

import network.Request;
import network.Response;
import network.Worker;

public abstract class AbstractSubscriber implements Subscriber {

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
