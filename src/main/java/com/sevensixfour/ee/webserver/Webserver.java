package com.sevensixfour.ee.webserver;

import com.sevensixfour.ee.features.Feature;
import com.sevensixfour.ee.file.config.ConfigHelper;
import com.sevensixfour.ee.webserver.handler.WebserverHandler;
import com.sevensixfour.ee.webserver.handler.WebserverHelper;
import com.sevensixfour.ee.webserver.user.WebserverUser;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Webserver {

    private final String hostname;
    private final int port;

    private HttpServer httpServer;

    private List<WebserverUser> permissionUsers;

    public Webserver(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.permissionUsers = new ArrayList<>();
    }

    public void loadPermissionUsers() {
        // Reduce to 0
        this.permissionUsers.clear();
        // Find users
        final ConfigurationSection section = ConfigHelper.getConfigurationSection("users");
        if (section != null) {
            for (String userKey : section.getKeys(false)) {
                String userUsername = ConfigHelper.getString(String.format("users.%s.user", userKey), null);
                String userPassword = ConfigHelper.getString(String.format("users.%s.password", userKey), null);
                boolean userIsAdmin = ConfigHelper.getBoolean(String.format("users.%s.admin", userKey), false);
                if (userUsername != null & userPassword != null) {
                    List<String> userPerms = (List<String>) ConfigHelper.getList(String.format("users.%s.permissions", userKey));
                    // Add permission user
                    this.permissionUsers.add(new WebserverUser(userUsername, userPassword, userIsAdmin, userPerms));
                }
            }
        } else {
            // TODO: Warning msg for no users
        }
    }

    public void createAndStartWebServer() throws IOException {
        this.createWebServer();
        this.startWebServer();
    }

    public void createWebServer() throws IOException {
        // Create
        this.httpServer = HttpServer.create(new InetSocketAddress(this.hostname, this.port), 0);
        this.createContext("/", new WebserverHandler() {
            @Override
            public void onHandle(HttpExchange exchange, Map<String, String> urlParams) {
                WebserverHelper.sendSuccessResponse(exchange, "hello");
            }
        });
    }

    public void createContext(Feature feature, WebserverHandler handler) {

    }

    /**
     * This method creates an HTTP endpoint within the server and also
     * creates a Config value within the Config.yml so that the endpoint can be enabled/disabled as required.
     */
    public void createContext(String context, WebserverHandler handler) {
        // TODO: Show in console when DEBUG is used
        HttpContext httpContext = this.httpServer.createContext(context, handler);
        httpContext.setAuthenticator(new BasicAuthenticator("get") {
            @Override
            public boolean checkCredentials(String inputUser, String inputPassword) {
                for (WebserverUser user : permissionUsers) {
                    if (user.getUsername().equalsIgnoreCase(inputUser) && user.getPassword().equalsIgnoreCase(inputPassword)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void startWebServer() {
        this.httpServer.start();
    }

    public void closeWebServer() {
        this.httpServer.stop(0);
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public List<WebserverUser> getPermissionUsers() {
        return permissionUsers;
    }

}
