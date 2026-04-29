package src.java.com.doodle;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties;

    public ConfigLoader(String filePath) {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(filePath));
        } catch (Exception e) {
            System.out.println("Config file not found, using defaults");
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}