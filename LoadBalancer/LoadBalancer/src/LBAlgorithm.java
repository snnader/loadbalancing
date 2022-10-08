public interface LBAlgorithm extends Subscriber {
    Worker choose(Request request);
}
