import java.util.List;
import java.util.Random;

public class RandomAlgorithm implements LBAlgorithm {
    private List<Worker> workers;
    private Random rand = new Random();

    public RandomAlgorithm(List<Worker> workers) {
        this.workers = workers;
    }
    @Override
    public Worker choose(Request request) {
        return workers.get(rand.nextInt(workers.size()));
    }

    @Override
    public void onRequest(Worker worker, Request request) {

    }

    @Override
    public void onResponse(Response response) {

    }
}
