package com.sevensixfour.ee.webserver.handler;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class WebserverHelper {
    public static void sendSuccessResponse(HttpExchange exchange, String message) {
        JsonObject obj = new JsonObject();
        obj.addProperty("message", message);
        sendResponse(exchange, 200, obj.toString());
    }

    public static void sendErrorResponse(HttpExchange exchange, String error) {
        JsonObject obj = new JsonObject();
        obj.addProperty("error", error);
        sendResponse(exchange, 400, obj.toString());
    }

    public static void sendResponse(HttpExchange exchange, int code, String body) {
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
}
