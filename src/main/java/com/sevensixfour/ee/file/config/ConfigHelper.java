package com.sevensixfour.ee.file.config;

import com.sevensixfour.ee.EightyEighty;
import com.sevensixfour.ee.logger.LoggerHelper;
import com.sevensixfour.ee.file.message.MessageHelper;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Helper class for accessing the Config.yml file
 */
public class ConfigHelper {

    private static FileConfiguration CONFIG;

    /**
     * Read the Plugin's config.yml file.
     * Move the file from EightyEighty.jar on first launch.
     *
     * @param plugin EightyEighty instance
     */
    public static void loadPluginConfigFile(JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        ConfigHelper.CONFIG = plugin.getConfig();
        EightyEighty.DEBUG_MODE = ConfigHelper.getBoolean("DEBUG_MODE", false);
    }

    public static ConfigurationSection getConfigurationSection(String path) {
        return CONFIG.getConfigurationSection(path);
    }

    public static List<?> getList(String path) {
        LoggerHelper.debug(String.format("Retrieve Config List: %s", path));
        boolean found = CONFIG.contains(path);
        if (!found) {
            // Log as warning
            LoggerHelper.warning(MessageHelper.getMessage("config_var_not_found", new HashMap<String, String>() {
                {
                    put("PATH", path);
                    put("DEFAULT", "List<Empty>");
                }
            }));
            return new ArrayList<>();
        }
        return CONFIG.getList(path);
    }

    public static int getInt(String path, int def) {
        LoggerHelper.debug(String.format("Retrieve Config integer: %s", path));
        boolean found = CONFIG.contains(path);
        if (!found) {
            // Log as warning
            LoggerHelper.warning(MessageHelper.getMessage("config_var_not_found", new HashMap<String, String>() {
                {
                    put("PATH", path);
                    put("DEFAULT", String.format("%s", def));
                }
            }));
        }
        return CONFIG.getInt(path, def);
    }

    public static String getString(String path, String def) {
        LoggerHelper.debug(String.format("Retrieve Config string: %s", path));
        boolean found = CONFIG.contains(path);
        if (!found) {
            // Log as warning
            LoggerHelper.warning(MessageHelper.getMessage("config_var_not_found", new HashMap<String, String>() {
                {
                    put("PATH", path);
                    put("DEFAULT", String.format("%s", def));
                }
            }));
        }
        return CONFIG.getString(path, def);
    }

    public static boolean getBoolean(String path, boolean def) {
        LoggerHelper.debug(String.format("Retrieve Config boolean: %s", path));
        boolean found = CONFIG.contains(path);
        if (!found) {
            // Log as warning
            LoggerHelper.warning(MessageHelper.getMessage("config_var_not_found", new HashMap<String, String>() {
                {
                    put("PATH", path);
                    put("DEFAULT", String.format("%s", def));
                }
            }));
        }
        return CONFIG.getBoolean(path, def);
    }

}
