import java.lang.management.ManagementFactory;

public class MonitorUtils {

    private static com.sun.management.OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean)ManagementFactory.getOperatingSystemMXBean();

    public static double GetCpuUsage() {
        return bean.getSystemCpuLoad();
    }

    public static double GetMemUsage() {
        double total = bean.getTotalPhysicalMemorySize();
        double free = bean.getFreePhysicalMemorySize();
        return ((total - free) / total);
    }
}
