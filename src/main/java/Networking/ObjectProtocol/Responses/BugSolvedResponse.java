package Networking.ObjectProtocol.Responses;

import Domain.Bug;

public class BugSolvedResponse implements UpdateResponse {

    private Bug solvedBug;

    public BugSolvedResponse(Bug bug){
        this.solvedBug = bug;
    }

    public Bug getSolvedBug() {
        return solvedBug;
    }
}
