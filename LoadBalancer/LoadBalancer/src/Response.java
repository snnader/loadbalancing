import java.net.http.HttpResponse;

public class Response {

    public String uuid;
    public Worker worker;
    public HttpResponse response;

    public Response(String uuid, Worker worker, HttpResponse response) {
        this.uuid = uuid;
        this.worker = worker;
        this.response = response;
    }
}
