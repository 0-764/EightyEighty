package com.sevensixfour.ee.file.message;

import com.sevensixfour.ee.logger.LoggerHelper;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MessageHelper {

    private static FileConfiguration MESSAGES;
    private static Map<String, String> PREFIXES;

    private final static String PREFIX_FORMAT = "${%s}";

    static {
        PREFIXES = new HashMap<>();
    }

    public static void loadMessagesFile(JavaPlugin plugin) {
        // The local messages.yml file in the server directory:
        final File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        // Check to see if the local file exists in the server directory.
        // If not, copy the messages.yml file from the EightyEighty.jar and put it into the
        // plugin data folder when the plugin is first loaded.
        if (!messagesFile.exists()) {
            // Create the necessary folders that store the messages.yml file within the server directory
            messagesFile.getParentFile().mkdirs();
            // Save messages.yml file (do not replace)
            plugin.saveResource("messages.yml", false);
        }
        // Load the messages.yml file and parse into JavaPlugin as a Yaml configuration
        MESSAGES = new YamlConfiguration();
        try {
            MESSAGES.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            // TODO: get message
        }
    }

    public static String getMessage(String ymlNode, Map<String, String> additionalPrefixes) {
        return parsePREFIXESInString(MessageHelper.getMessage(ymlNode), additionalPrefixes);
    }


    /**
     * Return the message that is configured within the Messages.yml file.
     *
     * @param ymlNode The location of the message within the Messages.yml file
     * @return The value of the message found in yml
     */
    public static String getMessage(String ymlNode) {
        // Determine whether the message exists in the Messages.yml file
        boolean ymlNodeExists = MessageHelper.MESSAGES.contains(ymlNode);
        if (ymlNodeExists) {
            String value = MessageHelper.MESSAGES.getString(ymlNode);
            return MessageHelper.parsePREFIXESInString(value);
        } else {
            // Prepare additional prefixes for warning message
            // Log warning message to console
            String warningMessage = MESSAGES.getString("message_not_found", ChatColor.RESET + "Message ${MESSAGE} cannot be found in Messages.yml.");
            LoggerHelper.warning(MessageHelper.parsePREFIXESInString(warningMessage, new HashMap<String, String>() {
                {
                    put("MESSAGE", ymlNode);
                }
            }));
            // Send backup message if it is not found within the Messages.yml
            // TODO: Return support page.
            return MessageHelper.parsePREFIXESInString(String.format("[Error] Message %s cannot be found in Messages.yml.", ymlNode));
        }
    }

    public static String parsePREFIXESInString(String str) {
        for (String PREFIX : PREFIXES.keySet()) {
            String format = String.format(PREFIX_FORMAT, PREFIX);
            str = str.replace(format, PREFIXES.get(PREFIX));
        }
        return str;
    }

    public static String parsePREFIXESInString(String str, Map<String, String> additionalPrefixes) {
        // Replace prefixes with new string
        str = MessageHelper.parsePREFIXESInString(str);
        // Replace additional prefixes with new string
        for (String PREFIX : additionalPrefixes.keySet()) {
            String format = String.format(PREFIX_FORMAT, PREFIX);
            str = str.replace(format, additionalPrefixes.get(PREFIX));
        }
        return str;
    }

    public static void addPREFIX(String PREFIX, String VALUE) {
        MessageHelper.PREFIXES.put(PREFIX, VALUE);
    }

    private static YamlConfiguration BACKUP_MESSAGES;


}
