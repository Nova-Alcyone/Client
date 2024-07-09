package qc.novaclient.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    private static final String appDataPath = System.getenv("APPDATA");
    private static final String novaLauncherConfigPath = Paths.get(appDataPath, ".novaclient", "data.json").toString();
    private static final String filePath = Paths.get(appDataPath, ".novaclient", "user.stock").toString();

    public static String getRefreshToken() {
        Map<String, String> configMap = readConfigFile();
        return configMap.get("refresh_token");
    }

    public static String getUsername() {
        Map<String, String> configMap = readConfigFile();
        return configMap.get("player_username");
    }

    public static String getRam() {
        Map<String, String> configMap = readConfigFile();
        return configMap.get("ram");
    }

    public static String getVersion() {
        JsonObject novaLauncherConfig = readNovaLauncherConfig();
        JsonElement latestVersionElement = novaLauncherConfig.get("latest_version");

        if (latestVersionElement != null && latestVersionElement.isJsonPrimitive()) {
            return latestVersionElement.getAsString();
        } else {
            return "";
        }

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

    public static void setRam(String ram) {
        Map<String, String> configMap = readConfigFile();
        configMap.put("ram", ram);
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

    public static void removeRam() {
        Map<String, String> configMap = readConfigFile();
        configMap.remove("ram");
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

    private static JsonObject readNovaLauncherConfig() {
        try (Reader reader = new FileReader(novaLauncherConfigPath)) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JsonObject();
    }

    public static boolean getLog() {
        if (getRefreshToken() != null) {
            return true;
        } else {
            return false;
        }
    }
}
