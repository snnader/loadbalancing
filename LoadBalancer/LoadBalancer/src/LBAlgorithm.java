public interface LBAlgorithm {
    Worker choose(Request request);
    void onRequest(Worker worker, Request request);
    void onResponse(Response response);
}
