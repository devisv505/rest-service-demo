package com.devisv.rest.config;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import static java.util.Objects.isNull;

/**
 * Configuration of app
 */
@Singleton
public class Config {

    private static final String SERVER_PORT = "server.port";

    private static final String DB_URL = "db.url";

    private static final String DB_USER = "db.user";

    private static final String DB_PASSWORD = "db.password";

    private final Properties properties = new Properties();

    public Config() throws IOException {
        properties.load(Config.class.getClassLoader().getResourceAsStream("config.properties"));
        checkPaths();
    }

    /**
     * Return port of the web server
     *
     * @return
     */
    public int getServerPort() {
        return Integer.parseInt(properties.getProperty(SERVER_PORT));
    }

    /**
     * Return database url
     *
     * @return
     */
    public String getDBUrl() {
        return properties.getProperty(DB_URL);
    }

    /**
     * Return database user
     *
     * @return
     */
    public String getDBUser() {
        return properties.getProperty(DB_USER);
    }

    /**
     * Return database password
     *
     * @return
     */
    public String getDBPassword() {
        return properties.getProperty(DB_PASSWORD);
    }

    /**
     * Calls all methods of configuration for checking that they all are filled before application start
     */
    private void checkPaths() {
        Arrays.stream(this.getClass().getMethods())
              .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
              .forEach(method -> {
                  try {
                      Object property = method.invoke(this);

                      if (isNull(property)) {
                          throw new NullPointerException("Not found required property");
                      }
                  } catch (Throwable e) {
                      throw new RuntimeException(
                              String.format("Incorrect configuration of application (property: %s)", method.getName()),
                              e
                      );
                  }
              });

    }
}
