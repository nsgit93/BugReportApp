package Networking.ObjectProtocol.Requests;

import Domain.IEmployee;


public class LoginRequest implements Request {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword(){
        return password;
    }
}
