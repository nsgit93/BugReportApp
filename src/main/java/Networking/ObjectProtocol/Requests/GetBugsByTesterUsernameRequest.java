package Networking.ObjectProtocol.Requests;

import Domain.Bug;

import java.util.ArrayList;

public class GetBugsByTesterUsernameRequest implements Request {
    private String username;

    public GetBugsByTesterUsernameRequest(String username){
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
