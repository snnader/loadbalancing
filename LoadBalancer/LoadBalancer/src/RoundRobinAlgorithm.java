import java.util.List;

public class RoundRobinAlgorithm implements LBAlgorithm {
    private List<Worker> workers;
    private int index = 0;

    public RoundRobinAlgorithm(List<Worker> workers) {
        this.workers = workers;
    }

    public Worker choose() {
        Worker worker = workers.get(index % workers.size());
        this.index += 1;
        return worker;
    }

    public void onRequest() {

    }

    public void onResponse() {

    }
}