package Networking.ObjectProtocol.Requests;

import Domain.Bug;

public class AddBugRequest implements Request {
    private Bug bug;

    public AddBugRequest(Bug bug){
        this.bug = bug;
    }

    public Bug getBug() {
        return bug;
    }
}
