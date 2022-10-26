package algorithm;

import network.Request;
import network.Response;
import network.Worker;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ConsistentHashAlgorithm extends AbstractLBAlgorithm {

  // FNVI_32_HASH Algorithm
  private static final long FNV_32_INIT = 2166136261L;
  private static final int FNV_32_PRIME = 16777619;
  private TreeMap<Integer, Worker> treeMapHash;
  private List<Worker> workers;

  public ConsistentHashAlgorithm(List<Worker> workers) {
    this.workers = workers;
    // Build Hash Ring
    buildHash(new TreeMap<Integer, Worker>());

    // Add workers
     for (int i = 0; i < workers.size(); i++) {
       addServer(workers.get(i));
     }
}

  public int getHashCode(String origin) {

    final int p = FNV_32_PRIME;
    int hash = (int) FNV_32_INIT;
    for (int i = 0; i < origin.length(); i++) {
      hash = (hash ^ origin.charAt(i)) * p;
    }
    hash += hash << 13;
    hash ^= hash >> 7;
    hash += hash << 3;
    hash ^= hash >> 17;
    hash += hash << 5;
    hash = Math.abs(hash);
    System.out.println("hash:" + hash);
    return hash;
  }

  // Build Hash Ring
  public SortedMap<Integer, Worker> buildHash(TreeMap<Integer, Worker> treeMap) {
    this.treeMapHash = treeMap;
    System.out.println(treeMap);
    return treeMapHash;
  }

  public void addServer(Worker worker) {
    int hash = getHashCode(worker.port);
    treeMapHash.put(hash, worker);
  }

  public void delServer(Worker worker) {
    int hash = getHashCode(worker.port);
    treeMapHash.remove(hash);
  }

  @Override
  public Worker choose(Request request) {
    int hash = getHashCode(request.uuid + System.currentTimeMillis());
    // find first key to right
    Map.Entry<Integer, Worker> subEntry = treeMapHash.ceilingEntry(hash);
    // set ring and choose first node if over tail
    subEntry = subEntry == null ? treeMapHash.firstEntry() : subEntry;
    return  subEntry.getValue();
  }

  @Override
  public void onRequest(Request request) {
    
    
  }

  @Override
  public void onResponse(Response response) {

  }

}
