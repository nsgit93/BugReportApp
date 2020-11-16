package Networking.ObjectProtocol.Responses;

import Domain.Bug;

import java.util.ArrayList;

public class GetBugsByTesterIDResponse implements Response {

    private ArrayList<Bug> bugs;

    public GetBugsByTesterIDResponse(ArrayList<Bug> bugs){
        this.bugs = bugs;
    }

    public ArrayList<Bug> getBugs() {
        return bugs;
    }
}
