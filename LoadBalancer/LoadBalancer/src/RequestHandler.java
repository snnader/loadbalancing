import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RequestHandler implements HttpHandler {

    private List<Subscriber> subscribers;
    private LBAlgorithm algorithm;

    public RequestHandler(List<Subscriber> subscribers, LBAlgorithm algorithm) {
        this.subscribers = subscribers;
        this.algorithm = algorithm;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            handleRequest(httpExchange);
        } catch (InterruptedException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            System.out.println(e);
        }
    }

    private void handleRequest(HttpExchange httpExchange) throws IOException, InterruptedException, ExecutionException {

        // TODO
        // handle potential data race
        for (Subscriber sub: this.subscribers) {
            sub.onRequest();
        }

        // Select the worker to handle the request
        Worker worker = algorithm.choose();
        String uri = "http://" + worker.ip + ":" + worker.port + "?" + httpExchange.getRequestURI().getRawQuery();

        // Forward request to the server
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(uri)).build();
        var responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        var response = responseFuture.get().body();

        // TODO
        // handle potential data race
        for (Subscriber sub: this.subscribers) {
            sub.onResponse();
        }

        // Forward the response to the client
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}