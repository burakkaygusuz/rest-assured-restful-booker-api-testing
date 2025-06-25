package io.github.burakkaygusuz.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PropertyUtil {

  private static final Map<String, PropertyUtil> INSTANCES = new ConcurrentHashMap<>();
  private final Properties properties;

  public static PropertyUtil getInstance(String fileName) {
    return INSTANCES.computeIfAbsent(fileName, PropertyUtil::new);
  }

  private PropertyUtil(String fileName) {
    this.properties = new Properties();
    try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
      if (inputStream == null) {
        throw new IllegalArgumentException("Property file not found: " + fileName);
      }
      properties.load(inputStream);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }
}