package Networking.ObjectProtocol.Requests;

import Domain.Bug;

public class SolveBugRequest implements Request {
    private Bug bug;

    public SolveBugRequest(Bug bug){
        this.bug = bug;
    }

    public Bug getBug() {
        return bug;
    }

}
