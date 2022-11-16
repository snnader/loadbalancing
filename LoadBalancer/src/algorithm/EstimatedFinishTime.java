package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.*;

public class EstimatedFinishTime extends AbstractLBAlgorithm {

    private String name = "EstimatedFinishTime";

    private final Worker[] workers;
    private final double[] finishTime;
    private final double[] capacity;

    public EstimatedFinishTime(List<Worker> workers) {
        this.workers = new Worker[workers.size()];
        this.finishTime = new double[workers.size()];
        this.capacity = new double[workers.size()];
        for (int i = 0; i < workers.size(); i++) {
            this.workers[i] = workers.get(i);
            this.finishTime[i] = 0;
            this.capacity[i] = this.workers[i].capacity;
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Worker choose(Request request) {
        int requestLoad = request.getLoad();
        return this.workers[findMinFinishTime(requestLoad)];
    }

    private synchronized int findMinFinishTime(double requestLoad) {
        // find worker with the least finish time
        int index = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < workers.length; i++) {
            double estimatedFinishTime = this.finishTime[i] + requestLoad / this.capacity[i];
            if (estimatedFinishTime < min) {
                index = i;
                min = estimatedFinishTime;
            }
        }
        // update its finish time
        this.finishTime[index] += requestLoad / this.capacity[index];
        return index;
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
