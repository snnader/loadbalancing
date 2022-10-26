package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LeastRecentlyUsed_LeastConnection extends AbstractLBAlgorithm {
    private List<Worker> workers;
    private List<Worker> temp1;
    private List<Worker> temp2;
    private List<Integer> temp3;

    private HashMap<Worker, Integer> collection;
    private HashMap<Worker, Integer> timeNotUsed;

    private Worker chooseWorker;
    private int longestNotUsed;
    private Lock lock;

    private Random random = new Random();

    public LeastRecentlyUsed_LeastConnection(List<Worker> workers) {
        this.workers = workers;
        longestNotUsed = 0;
        collection = new HashMap<>();
        timeNotUsed = new HashMap<>();
        lock = new ReentrantLock();
        for (Worker worker : workers) {
            collection.put(worker, 0);
            timeNotUsed.put(worker, 0);
        }
        
    }
    @Override
    public Worker choose(Request request){
        lock.lock();
        temp1 = new ArrayList<>();
        temp2 = new ArrayList<>();
        temp3 = new ArrayList<>();

        int minimumConnection = Collections.min(collection.values());
        for (Worker worker : workers) {
            if(collection.get(worker) == minimumConnection){
                temp1.add(worker);
            }
            
        }
        System.out.println(temp1);
        for (Worker worker : workers) {
            System.out.println(worker.ip + ":" + collection.get(worker));
            System.out.println(timeNotUsed.get(worker));
        }
        if(temp1.size() > 1){
            for (Worker worker : temp1) {
                temp3.add(timeNotUsed.get(worker));
            }
            longestNotUsed = Collections.max(temp3);
            for (Worker worker : temp1) {
                if(timeNotUsed.get(worker) == longestNotUsed){
                    temp2.add(worker);
                }
            }
            System.out.println("temp2:"+temp2);
            chooseWorker = temp2.get(random.nextInt(temp2.size()));
        }else{
            chooseWorker = temp1.get(0);
        }
        for (Worker worker : workers) {
            if(timeNotUsed.get(worker) > 3 * workers.size()){
                chooseWorker = worker;
            }
        }
        for (Worker worker : workers) {
            if (worker != chooseWorker) {
                timeNotUsed.put(worker, timeNotUsed.get(worker) + 1);
            } else {
                timeNotUsed.put(worker, 0);
            }
        }
        lock.unlock();
        return chooseWorker;
    }


    @Override
    public void onRequest(Request request){
        collection.put(request.worker, collection.get(request.worker) + 1);
    }

    @Override
    public void onResponse(Response response){
        collection.put(response.worker, collection.get(response.worker) - 1);
    }

    @Override
    public void onRequestFail(Request request){
        collection.put(request.worker, collection.get(request.worker) - 1);
    }
}
