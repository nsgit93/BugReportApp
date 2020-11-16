package Networking.ObjectProtocol.Responses;

import Domain.Tester;

import java.util.ArrayList;

public class GetTestersResponse implements Response {
    private ArrayList<Tester> testers;

    public GetTestersResponse() {
    }

    public GetTestersResponse(ArrayList<Tester> testers) {
        this.testers = testers;
    }

    public ArrayList<Tester> getTesters() {
        return testers;
    }
}
