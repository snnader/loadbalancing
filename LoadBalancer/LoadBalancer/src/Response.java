import java.net.http.HttpResponse;

public class Response {

    public Worker worker;
    public HttpResponse response;

    public Response(Worker worker, HttpResponse response) {
        this.worker = worker;
        this.response = response;
    }
}
