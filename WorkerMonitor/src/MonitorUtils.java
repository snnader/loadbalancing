import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class MonitorUtils {

    private static com.sun.management.OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

    public static double GetCpuUsage() {
        return bean.getSystemCpuLoad();
    }

    public static double GetMemUsage() {

        String fileName = "/proc/meminfo";

        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Double total = 1.0 * Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]) / (1024 * 1024);
            bufferedReader.readLine();
            Double avail = 1.0 * Integer.valueOf(bufferedReader.readLine().split("\\s+")[1]) / (1024 * 1024);

            System.out.println("total: " + total + " avail: " + avail);

            bufferedReader.close();
            return ((total - avail) / total);
        } catch (IOException e) {
            System.out.println(e);
        }

        return 1.0;
    }
}
