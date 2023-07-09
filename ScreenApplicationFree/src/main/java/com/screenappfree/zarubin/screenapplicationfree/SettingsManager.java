package com.screenappfree.zarubin.screenapplicationfree;

import java.io.*;
import java.util.Properties;

public class SettingsManager {
    private static final String SETTINGS_FILE = "settings.properties";

    public static void saveSettings(String folderPath, String hotkey) {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            if (folderPath != null) {
                saveFolderPath(properties, folderPath);
            }
            saveHotkey(properties, hotkey);
            properties.store(output, "Application Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFolderPath(String folderPath) {
        try (OutputStream output = new FileOutputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            properties.setProperty("folderPath", folderPath);
            properties.store(output, "Application Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveFolderPath(Properties properties, String folderPath) {
        properties.setProperty("folderPath", folderPath);
    }

    private static void saveHotkey(Properties properties, String hotkey) {
        properties.setProperty("hotkey", hotkey);
    }

    public static String loadFolderPath() {
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty("folderPath");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String loadHotkey() {
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty("hotkey");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}