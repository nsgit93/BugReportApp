package Networking.ObjectProtocol.Responses;

import Domain.Bug;

import java.util.ArrayList;

public class GetBugsByTesterUsernameResponse implements Response{
    private ArrayList<Bug> bugs;

    public GetBugsByTesterUsernameResponse(ArrayList<Bug> bugs){
        this.bugs = bugs;
    }

    public ArrayList<Bug> getBugs() {
        return bugs;
    }
}
