package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.*;

public class EstimatedFinishTime extends AbstractLBAlgorithm {
    private final PriorityQueue<TimeWorkerPair> finishTimePQ;

    public EstimatedFinishTime(List<Worker> workers) {
        this.finishTimePQ = new PriorityQueue<>(workers.size(), Comparator.comparingInt(o -> o.finishTime));
        for (Worker worker : workers) {
            this.finishTimePQ.add(new TimeWorkerPair(worker));
        }
    }

    @Override
    public Worker choose(Request request) {
        int requestLoad = request.getLoad();
        TimeWorkerPair selected = updateFinishTime(requestLoad);
        // reduce estimated finish times of all workers by the same amount to prevent overflow
        if (selected.finishTime > 2000000000) {
            for (TimeWorkerPair pair : this.finishTimePQ) {
                pair.finishTime -= selected.finishTime;
            }
        }
        return selected.worker;
    }

    private synchronized TimeWorkerPair updateFinishTime(int load) {
        // select worker with the least finish time and remove it from queue
        TimeWorkerPair selected = this.finishTimePQ.remove();
        // update its finish time and re-add it to the queue
        selected.finishTime += load;
        this.finishTimePQ.add(selected);
        return selected;
    }

    private static class TimeWorkerPair {
        public int finishTime;
        public Worker worker;

        public TimeWorkerPair(Worker worker) {
            this.finishTime = 0;
            this.worker = worker;
        }
    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onRequestFail(Request request) {

    }

}
