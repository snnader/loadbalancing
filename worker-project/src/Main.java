import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter IP address: ");
        String ipAddress = scanner.nextLine();
        System.out.println("Enter Port: ");
        int port = Integer.parseInt(scanner.nextLine());
        Worker worker = new Worker(new InetSocketAddress(ipAddress, port));
        worker.start();

//        Worker worker1 = new Worker(InetAddress.getByName("localhost"), 8888);
//        worker1.start();
//        Worker worker2 = new Worker(new InetSocketAddress("localhost", 9999));
//        worker2.start();
//        Worker worker3 = new Worker(InetAddress.getByName("localhost"));
//        worker3.start();
//        Worker worker4 = new Worker();
//        worker4.start();
    }
}