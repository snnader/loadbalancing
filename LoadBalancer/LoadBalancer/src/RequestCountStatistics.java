public class RequestCountStatistics implements Subscriber {
    public int count = 0;

    public void onRequest() {
        this.count += 1;
        System.out.println("total request: " + this.count);
    }

    public void onResponse() {
    }
}
