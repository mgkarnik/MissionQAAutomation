package mission.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        String path = System.getProperty("user.dir")
                + "/src/test/java/TestData/TestData.properties";
        try (FileInputStream input = new FileInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Could not load TestData.properties from: " + path, e);
        }
    }

    public static String getProperty(String key) {
        // Check environment variable first
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);

        if (envValue != null && !envValue.isEmpty()) {
            return envValue;
        }

        String value = properties.getProperty(key);
        if (value == null) {
            throw new RuntimeException("Property '" + key + "' not found in TestData.properties or environment variables");
        }
        return value;
    }
}
