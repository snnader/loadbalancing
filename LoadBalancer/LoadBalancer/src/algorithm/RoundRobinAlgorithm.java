package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.List;

public class RoundRobinAlgorithm extends AbstractLBAlgorithm {
    private List<Worker> workers;
    private int index = 0;

    public RoundRobinAlgorithm(List<Worker> workers) {
        this.workers = workers;
    }

    public Worker choose(Request request) {
        Worker worker = workers.get(index % workers.size());
        this.index += 1;
        return worker;
    }

    @Override
    public void onRequest(Request request) {
    }

    @Override
    public void onResponse(Response response) {

    }
}