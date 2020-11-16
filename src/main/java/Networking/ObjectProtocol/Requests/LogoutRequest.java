package Networking.ObjectProtocol.Requests;

import Domain.IEmployee;


public class LogoutRequest implements Request {

    private String username;

    public LogoutRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
