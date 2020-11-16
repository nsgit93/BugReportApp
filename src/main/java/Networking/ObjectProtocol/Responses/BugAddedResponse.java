package Networking.ObjectProtocol.Responses;

import Domain.Bug;

public class BugAddedResponse implements UpdateResponse {

    private Bug addedBug;

    public BugAddedResponse(Bug bug){
        this.addedBug = bug;
    }

    public Bug getAddedBug() {
        return addedBug;
    }
}
