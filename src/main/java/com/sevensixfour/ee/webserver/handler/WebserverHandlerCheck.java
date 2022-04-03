package com.sevensixfour.ee.webserver.handler;

import com.sun.net.httpserver.HttpExchange;

public interface WebserverHandlerCheck {

    public boolean performCheck(HttpExchange exchange);

    public String getErrorResponse();

}
