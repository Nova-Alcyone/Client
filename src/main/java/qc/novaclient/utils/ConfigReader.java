package qc.novaclient.utils;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    private static final String appDataPath = System.getenv("APPDATA");
    private static final String filePath = Paths.get(appDataPath, ".novaclient", "user.stock").toString();

    public static String getRefreshToken() {
        Map<String, String> configMap = readConfigFile();
        return configMap.get("refresh_token");
    }

    public static String getUsername() {
        Map<String, String> configMap = readConfigFile();
        return configMap.get("player_username");
    }

    public static void setRefreshToken(String refreshToken) {
        Map<String, String> configMap = readConfigFile();
        configMap.put("refresh_token", refreshToken);
        writeConfigFile(configMap);
    }

    public static void setUsername(String username) {
        Map<String, String> configMap = readConfigFile();
        configMap.put("player_username", username);
        writeConfigFile(configMap);
    }

    public static void removeRefreshToken() {
        Map<String, String> configMap = readConfigFile();
        configMap.remove("refresh_token");
        writeConfigFile(configMap);
    }

    public static void removeUsername() {
        Map<String, String> configMap = readConfigFile();
        configMap.remove("player_username");
        writeConfigFile(configMap);
    }

    private static Map<String, String> readConfigFile() {
        Map<String, String> configMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length >= 2) {
                    configMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configMap;
    }

    private static void writeConfigFile(Map<String, String> configMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, String> entry : configMap.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
