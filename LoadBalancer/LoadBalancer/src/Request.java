import com.sun.net.httpserver.HttpExchange;

public class Request {

    public HttpExchange request;

    public Request(HttpExchange request) {
        this.request = request;
    }
}
