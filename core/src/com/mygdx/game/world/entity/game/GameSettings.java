package com.mygdx.game.world.entity.game;

import java.io.*;
import java.util.Properties;

public class GameSettings extends Properties {

    public GameSettings() {
        // First try loading from the current directory
        InputStream is = loadFromCurrentDir();

        // Try loading from classpath
        if (is == null) {
            is = loadFromClasspath(is);
        }

        if (is == null) {
            createPropertyFileWithDefaults();
        } else {
            try {
                // Try loading properties from the file (if found)
                load(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private InputStream loadFromClasspath(InputStream is) {
        is = getClass().getResourceAsStream("config.properties");
        return is;
    }

    private InputStream loadFromCurrentDir() {
        InputStream is;
        try {
            File f = new File("config.properties");
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }
        return is;
    }

    private void createPropertyFileWithDefaults() {
        try {
            Properties props = new Properties();
            props.setProperty("customMapWidth", "1000");
            props.setProperty("customMapHeight", "1000");
            File f = new File("config.properties");
            OutputStream out = new FileOutputStream(f);
            props.store(out, "sdd");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getProperty(String key) {
        return super.getProperty(key);
    }
}
