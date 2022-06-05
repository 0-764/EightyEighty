package com.sevensixfour.ee;

import com.sevensixfour.ee.features.Feature;
import com.sevensixfour.ee.features.player.action.BurnPlayer;
import com.sevensixfour.ee.file.config.ConfigHelper;
import com.sevensixfour.ee.logger.LoggerHelper;
import com.sevensixfour.ee.file.message.MessageHelper;
import com.sevensixfour.ee.util.StartupMsg;
import com.sevensixfour.ee.webserver.Webserver;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EightyEighty is a plugin that enables a Webserver API for Developers wanting to interact with the Spigot server.
 * By default, the webserver utilises the port 8080, hence the name.
 *
 * @author 0764
 */
public class EightyEighty extends JavaPlugin {

    public static boolean DEBUG_MODE = true;

    public static EightyEighty instance;

    private Webserver webserver;

    private List<Feature> features = new ArrayList<>();

    @Override
    public void onEnable() {
        // Set the instance as the EightyEighty class is a singleton
        instance = EightyEighty.getPlugin(EightyEighty.class);

        // Messages are sent to the Server Owner through the console and to end users through the Webserver API
        // These messages can be changed through the Messages.yml file
        MessageHelper.loadMessagesFile(this);

        // Add standard prefixes which will be used throughout the EightyEighty plugin to the MessagesHelper
        MessageHelper.addPREFIX("PLUGIN_NAME", this.getName());
        MessageHelper.addPREFIX("PLUGIN_DESC", this.getDescription().getDescription());
        MessageHelper.addPREFIX("PLUGIN_VERSION", this.getDescription().getVersion());
        MessageHelper.addPREFIX("PLUGIN_API_VERSION", this.getDescription().getAPIVersion());
        MessageHelper.addPREFIX("PLUGIN_AUTHORS", this.getDescription().getAuthors().toString());
        MessageHelper.addPREFIX("PLUGIN_WEBSITE", this.getDescription().getWebsite());
        MessageHelper.addPREFIX("PLUGIN_PREFIX", this.getDescription().getPrefix());
        MessageHelper.addPREFIX("PLUGIN_MAIN", this.getDescription().getMain());

        // On plugin launch, the Config.yml file is read and loaded into EightyEighty.
        // This defines the functionality of how EightyEighty works
        // Whether the plugin is in DEBUG mode is determined in the config.yml
        ConfigHelper.loadPluginConfigFile(this);

        // Declare the details by which the Webserver is accessible
        String hostname = ConfigHelper.getString("webserver.hostname", "127.0.0.1");
        int port = ConfigHelper.getInt("webserver.port", 8080);

        // Create HTTP Webserver and start under hostname & port
        this.webserver = new Webserver(hostname, port);
        // Create a list of variables that are passable from the Webserver to the Logger
        Map<String, String> msgVariables = new HashMap<String, String>() {
            {
                put("HOSTNAME", webserver.getHostname());
                put("PORT", String.format("%s", webserver.getPort()));
            }
        };

        // Attempt to Create & Start the Webserver
        try {
            // Load permissions
            this.webserver.loadPermissionUsers();

            // Create & start webserver
            this.webserver.createAndStartWebServer();
            LoggerHelper.success(MessageHelper.getMessage("webserver_listening", msgVariables));
        } catch (IOException e) {
            // Webserver cannot be created/started
            // This is acknowledged as an error and alerted to the Console
            LoggerHelper.error(MessageHelper.getMessage("webserver_cannot_start", msgVariables));

            // Disable plugin as fundamental function is not working aka webserver
            this.getPluginLoader().disablePlugin(this);
        }

        this.features();

        // Log startup
        LoggerHelper.raw(StartupMsg.STARTUP);
    }

    private void features() {
        this.registerFeature(new BurnPlayer());
    }

    private void registerFeature(Feature feature) {
        boolean enabledFeature = ConfigHelper.getBoolean(String.format("features.%s.enabled", feature.ymlPrefix()), true);
        if (enabledFeature) {
            this.features.add(feature);
            feature.enableFeature();
            LoggerHelper.debug(MessageHelper.getMessage("feature_set_enabled", new HashMap<String, String>() {
                {
                    put("FEATURE_CLASS", feature.getClass().toGenericString());
                    put("FEATURE_CONFIG", feature.ymlPrefix());
                }
            }));
        } else {
            LoggerHelper.debug(MessageHelper.getMessage("feature_set_disabled", new HashMap<String, String>() {
                {
                    put("FEATURE_CLASS", feature.getClass().toGenericString());
                    put("FEATURE_CONFIG", feature.ymlPrefix());
                }
            }));
        }
    }

    @Override
    public void onDisable() {
        for (Feature feature : this.features) {
            feature.disableFeature();
        }
    }

    public Webserver getWebserver() {
        return webserver;
    }

}
