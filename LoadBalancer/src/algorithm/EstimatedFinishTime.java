package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.UnaryOperator;

public class EstimatedFinishTime extends AbstractLBAlgorithm {

    private final static String NAME = "EstimatedFinishTime";

    private final Worker[] workers;
    private final AtomicReferenceArray<Double> finishTime;
    private final double[] capacity;
    private final ConcurrentHashMap<Worker, ConcurrentHashMap<String, Double>> requestMap;

    public EstimatedFinishTime(List<Worker> workers) {
        this.workers = new Worker[workers.size()];
        this.finishTime = new AtomicReferenceArray<>(workers.size());
        this.capacity = new double[workers.size()];
        this.requestMap = new ConcurrentHashMap<>(workers.size());
        for (int i = 0; i < workers.size(); i++) {
            this.workers[i] = workers.get(i);
            this.finishTime.set(i, 0.0d);
            this.capacity[i] = this.workers[i].capacity;
            this.requestMap.put(this.workers[i], new ConcurrentHashMap<>());
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Worker choose(Request request) {
        int requestLoad = request.getLoad();
        return this.workers[findMinFinishTime(request.uuid, requestLoad)];
    }

    private int findMinFinishTime(String uuid, double requestLoad) {
        // find worker with the least finish time
        int index = 0;
        double min = Double.MAX_VALUE;
        for (int i = 0; i < workers.length; i++) {
            double estimatedFinishTime = this.finishTime.get(i) + requestLoad / this.capacity[i];
            if (estimatedFinishTime < min) {
                index = i;
                min = estimatedFinishTime;
            }
        }
        // update its finish time
        this.finishTime.set(index, min);
        this.requestMap.get(this.workers[index]).put(uuid, requestLoad / this.capacity[index]);
        return index;
    }

    @Override
    public void onRequest(Request request) {

    }

    @Override
    public void onResponse(Response response) {
        int index = -1;
        for (int i = 0; i < this.workers.length; i++) {
            if (this.workers[i] == response.worker) {
                index = i;
                break;
            }
        }
        Double delta = this.requestMap.get(response.worker).remove(response.uuid);
        this.finishTime.getAndUpdate(index, (value) -> value - delta );
    }

    @Override
    public void onRequestFail(Request request) {
        int index = -1;
        for (int i = 0; i < this.workers.length; i++) {
            if (this.workers[i] == request.worker) {
                index = i;
                break;
            }
        }
        Double delta = this.requestMap.get(request.worker).remove(request.uuid);
        this.finishTime.getAndUpdate(index, (value) -> value - delta );
    }

}
