package com.sevensixfour.ee.webserver.handler.check;

import com.sevensixfour.ee.file.message.MessageHelper;
import com.sevensixfour.ee.webserver.handler.WebserverHandlerCheck;
import com.sun.net.httpserver.HttpExchange;

import java.util.HashMap;
import java.util.Map;

/**
 * RequiredParamCheck is a HandlerCheck that verifies if a specific URL query parameter is set
 * E.g. https://127.0.0.1:8080/player/kill?username=j10max
 * Checks whether username is set
 */
public class RequiredParamCheck implements WebserverHandlerCheck {

    private final String parameter;

    public RequiredParamCheck(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public boolean performCheck(HttpExchange exchange) {
        Map<String, String> params = this.queryToMap(exchange.getRequestURI().getQuery());
        if (params != null && params.size() > 0) {
            for (String inputParam : params.keySet()) {
                if (inputParam.trim().equalsIgnoreCase(this.parameter)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getErrorResponse() {
        return MessageHelper.getMessage("webserver_error_required_check", new HashMap<String, String>() {
            {
                put("PARAMETER", parameter);
            }
        });
    }

    private Map<String, String> queryToMap(String query) {
        if (query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

}
