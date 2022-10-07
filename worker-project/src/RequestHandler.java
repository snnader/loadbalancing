import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class RequestHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Task requestTask = null;
        if ("GET".equals(exchange.getRequestMethod())) {
            requestTask = handleGetRequest(exchange);
            try {
                requestTask.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        handleResponse(exchange, requestTask);
    }

    private Task handleGetRequest(HttpExchange exchange) {
        String[] args = exchange.getRequestURI().toString().split("\\?")[1].split("&");
        HashMap<String, Integer> params = new HashMap<>();
        for (String arg: args) {
            String[] pair = arg.split("=");
            params.put(pair[0], Integer.parseInt(pair[1]));
        }
        return new Task(params.get("cpu"), params.get("mem"), params.get("io"), params.get("dur"));
    }

    private void handleResponse(HttpExchange exchange, Task requestTask) throws IOException {
        OutputStream outputStream = exchange.getResponseBody();
        String htmlResponse = "<html><body>" + requestTask + "</body></html>";
        exchange.sendResponseHeaders(200, htmlResponse.length());
        outputStream.write(htmlResponse.getBytes());
        outputStream.flush();
        outputStream.close();
        System.out.println("Handled request: " + requestTask);
    }
}
