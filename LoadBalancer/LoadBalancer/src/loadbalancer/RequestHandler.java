package loadbalancer;

import algorithm.LBAlgorithm;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import network.Request;
import network.Response;
import network.Worker;
import subscriber.Subscriber;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
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

        String uuid = UUID.randomUUID().toString();

        Request request = new Request(uuid, httpExchange);

        // TODO
        // handle potential data race
        for (Subscriber sub: this.subscribers) {
            sub.onRequest(request);
        }

        // Select the worker to handle the request
        request.worker = algorithm.choose();
        System.out.println("Chosen server: " + request.worker.ip + ":" + request.worker.port);
        String uri = "http://" + request.worker.ip + ":" + request.worker.port + "?" + httpExchange.getRequestURI().getRawQuery();

        // Notify algorithm about the request and which server is chosen to handle it
        algorithm.onRequest(request);

        // Forward request to the server
        try {
            var client = HttpClient.newHttpClient();
            var workerRequest = HttpRequest.newBuilder(URI.create(uri)).timeout(Duration.ofSeconds(2)).build(); // timeout 2 seconds
            var responseFuture = client.sendAsync(workerRequest, HttpResponse.BodyHandlers.ofString());
            var workerResponse = responseFuture.get();

            Response response = new Response(request.uuid, request.worker, workerResponse);

            // TODO
            // handle potential data race
            for (Subscriber sub : this.subscribers) {
                sub.onResponse(response);
            }

            // Notify algorithm about the response and which server returned it
            algorithm.onResponse(response);

            // Forward the response to the client
            httpExchange.sendResponseHeaders(200, workerResponse.body().length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(workerResponse.body().getBytes());
            os.close();
        } catch (Exception e) {
            for (Subscriber sub: this.subscribers) {
                sub.onRequestFail(request);
            }
            algorithm.onRequestFail(request);
        }
    }
}