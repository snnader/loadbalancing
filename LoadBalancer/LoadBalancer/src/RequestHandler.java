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

    public void handle(HttpExchange httpExchange) {
        try {
            handleRequest(httpExchange);
        } catch (IOException e) {
            System.out.println(e);
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
            sub.onRequest(new Request(httpExchange));
        }

        // Select the worker to handle the request
        Worker worker = algorithm.choose(new Request(httpExchange));
        System.out.println("Chosen server: " + worker.ip + ":" + worker.port);
        String uri = "http://" + worker.ip + ":" + worker.port + "?" + httpExchange.getRequestURI().getRawQuery();

        // Forward request to the server
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(uri)).build();
        var responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        var response = responseFuture.get();

        // TODO
        // handle potential data race
        for (Subscriber sub: this.subscribers) {
            sub.onResponse(new Response(worker, response));
        }

        // Forward the response to the client
        httpExchange.sendResponseHeaders(200, response.body().length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.body().getBytes());
        os.close();
    }
}