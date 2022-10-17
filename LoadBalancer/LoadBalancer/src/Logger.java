
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Logger {
    private static HashMap<String, FileWriter> fileWriters = new HashMap<>();

    public static void log(String logName, String content) {
        try {
            String fileLocation = System.getProperty("user.dir") + "/logs/" + logName + ".log";
            // delete old log file
            if (!fileWriters.containsKey(logName)) {
                Files.deleteIfExists(Path.of(fileLocation));
            }
            // create directory if it does not exist
            Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/logs"));

            fileWriters.putIfAbsent(logName, new FileWriter(fileLocation, true));
            FileWriter writer = fileWriters.get(logName);
            writer.write(content + "\n");
            writer.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
