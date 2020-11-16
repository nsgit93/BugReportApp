package Networking.ObjectProtocol.Responses;

import Domain.Bug;

import java.util.ArrayList;

public class GetActiveBugsResponse implements Response {

    private ArrayList<Bug> activeBugs;

    public GetActiveBugsResponse(ArrayList<Bug> bugs){
        this.activeBugs = bugs;
    }

    public ArrayList<Bug> getActiveBugs() {
        return activeBugs;
    }
}
