package com.sevensixfour.ee.webserver.handler.check;

import com.sevensixfour.ee.file.message.MessageHelper;
import com.sevensixfour.ee.webserver.handler.WebserverHandlerCheck;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;

public class RequiredPermissionCheck implements WebserverHandlerCheck {

    private final String permission;

    public RequiredPermissionCheck(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean performCheck(HttpExchange exchange) {
        return false;
    }

    @Override
    public String getErrorResponse() {
        return MessageHelper.getMessage("webserver_error_perm_check", new HashMap<String, String>() {
            {
                put("PERMISSION", permission);
            }
        });
    }

}
