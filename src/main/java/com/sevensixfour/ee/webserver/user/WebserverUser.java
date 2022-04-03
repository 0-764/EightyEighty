package com.sevensixfour.ee.webserver.user;

import java.util.List;

public class WebserverUser {

    private String username;
    private String password;
    private boolean admin;
    private List<String> permissions;

    public WebserverUser(String username, String password, boolean admin, List<String> permissions) {
        this.username = username;
        this.password = password;
        this.admin = admin;
        this.permissions = permissions;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }

    public List<String> getPermissions() {
        return permissions;
    }

}
