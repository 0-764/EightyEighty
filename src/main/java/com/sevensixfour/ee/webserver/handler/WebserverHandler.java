package com.sevensixfour.ee.webserver.handler;

import com.sevensixfour.ee.file.message.MessageHelper;
import com.sevensixfour.ee.logger.LoggerHelper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class WebserverHandler implements HttpHandler {

    private List<WebserverHandlerCheck> checks;

    public WebserverHandler() {
        this.checks = new ArrayList<>();
    }

    public abstract void onHandle(HttpExchange exchange, Map<String, String> urlParams);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean allowCheck = true;
        // Run each handler check
        WebserverHandlerCheck errorCheck = null;
        for (WebserverHandlerCheck check : this.checks) {
            boolean runCheck = check.performCheck(exchange);
            if (!runCheck) {
                allowCheck = false;
                errorCheck = check;
                break;
            }
        }
        if (allowCheck) {
            // Handle HTTP Exchange and send response
            Map<String, String> urlParams = queryToMap(exchange.getRequestURI().getQuery());
            this.onHandle(exchange, urlParams);
        } else {
            // Send error as response
            if (errorCheck != null) {
                LoggerHelper.debug(errorCheck.getErrorResponse());
                WebserverHelper.sendErrorResponse(exchange, errorCheck.getErrorResponse());
            } else {
                // Report that an unknown error has occurred
                String unknownError = MessageHelper.getMessage("webserver_unknown_error_handler_check");
                LoggerHelper.error(unknownError);
                WebserverHelper.sendErrorResponse(exchange, unknownError);
            }
        }
    }

    /**
     * Add a check to the handler
     * E.g. A required URL parameter
     *
     * @param check Required check
     */
    public void registerCheck(WebserverHandlerCheck check) {
        this.checks.add(check);
    }

    public void sendResponse(HttpExchange exchange, int code, String body) {
        OutputStream output = exchange.getResponseBody();
        try {
            exchange.sendResponseHeaders(code, body.length());
            output.write(body.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> queryToMap(String query) {
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

    public List<WebserverHandlerCheck> getChecks() {
        return checks;
    }

}
