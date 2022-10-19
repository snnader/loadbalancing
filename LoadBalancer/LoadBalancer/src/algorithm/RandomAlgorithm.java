package algorithm;

import network.Request;
import network.Response;
import network.Worker;
import subscriber.AbstractSubscriber;

import java.util.List;
import java.util.Random;

public class RandomAlgorithm extends AbstractLBAlgorithm {
    private List<Worker> workers;
    private Random rand = new Random();

    public RandomAlgorithm(List<Worker> workers) {
        this.workers = workers;
    }
    @Override
    public Worker choose() {
        return workers.get(rand.nextInt(workers.size()));
    }

    @Override
    public void onRequest(Request request) {

    }

    @Override
    public void onResponse(Response response) {

    }
}
