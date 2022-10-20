package network;

import com.sun.net.httpserver.HttpExchange;

public class Request {

    public String uuid;
    public HttpExchange request;
    public Worker worker;
    public Request(Worker worker, String uuid, HttpExchange request) {
        this.uuid = uuid;
        this.request = request;
    }

    public Request(String uuid, HttpExchange request) {
        this.uuid = uuid;
        this.request = request;
    }
}
