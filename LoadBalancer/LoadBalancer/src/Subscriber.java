public interface Subscriber {
    void onRequest(Request request);
    void onResponse(Response response);
}
