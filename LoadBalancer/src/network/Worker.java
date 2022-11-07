package network;

public class Worker {
    public String ip;
    public String port;
    public double capacity = 4.8;

    public Worker(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public Worker(String ip, String port, double capacity) {
        this.ip = ip;
        this.port = port;
        this.capacity = capacity;
    }
}
