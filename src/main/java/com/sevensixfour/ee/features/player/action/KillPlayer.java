package com.sevensixfour.ee.features.player.action;

import com.sevensixfour.ee.EightyEighty;
import com.sevensixfour.ee.features.Feature;
import com.sevensixfour.ee.file.message.MessageHelper;
import com.sevensixfour.ee.webserver.handler.WebserverHandler;
import com.sevensixfour.ee.webserver.handler.WebserverHelper;
import com.sevensixfour.ee.webserver.handler.check.RequiredEitherOrParamCheck;
import com.sevensixfour.ee.webserver.handler.check.RequiredPermissionCheck;
import com.sun.net.httpserver.HttpExchange;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * This feature enables the Developer to burn a specific Player
 */
public class KillPlayer implements Feature {

    @Override
    public void enableFeature() {
        EightyEighty.instance.getWebserver().createContext(this, new KillPlayerHandler());
    }

    @Override
    public void disableFeature() {

    }

    @Override
    public String ymlPrefix() {
        return "features.burn_player";
    }

    private class KillPlayerHandler extends WebserverHandler {
        public KillPlayerHandler() {
            super();
            this.registerCheck(new RequiredPermissionCheck("player.action.kill"));
            this.registerCheck(new RequiredEitherOrParamCheck("username", "uuid"));
        }

        @Override
        public void onHandle(HttpExchange exchange, Map<String, String> urlParams) {
            // Set default values
            Player player = null;
            String identifier = "No Username/UUID";
            // Find the online player by username or uuid
            if (urlParams.containsKey("username")) {
                String playerUsername = urlParams.get("username");
                // Retrieve player by username
                player = EightyEighty.instance.getServer().getPlayer(playerUsername);
                identifier = playerUsername;
            } else if (urlParams.containsKey("uuid")) {
                String playerUUID = urlParams.get("uuid");
                // Retrieve player by UUID
                player = EightyEighty.instance.getServer().getPlayer(UUID.fromString(playerUUID));
                identifier = playerUUID;
            }
            // Prepare response message and whether to kill player
            final String ident = identifier;
            if (player == null) {
                WebserverHelper.sendErrorResponse(exchange, MessageHelper.getMessage("feature_player_cannot_be_found", new HashMap<String, String>() {
                    {
                        put("PLAYER", ident);
                    }
                }));
            } else {
                // Set Player On Fire for how long
                int fireTicks = 20 * 5;
                player.setFireTicks(fireTicks);
                // Send success response
                WebserverHelper.sendSuccessResponse(exchange, MessageHelper.getMessage("feature_burn_player", new HashMap<String, String>() {
                    {
                        put("PLAYER", ident);
                    }
                }));
            }
        }
    }


}
