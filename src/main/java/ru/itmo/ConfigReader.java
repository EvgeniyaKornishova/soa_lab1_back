package ru.itmo;


import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
public class ConfigReader {
    InputStream inputStream;
    String user;
    String db_url;
    String pass;

    public ConfigReader() throws IOException {
        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            user = prop.getProperty("user");
            db_url = prop.getProperty("db_url");
            pass = prop.getProperty("pass");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

}
