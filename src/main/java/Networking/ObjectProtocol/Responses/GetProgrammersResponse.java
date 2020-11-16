package Networking.ObjectProtocol.Responses;

import Domain.Programmer;
import Domain.Tester;

import java.util.ArrayList;

public class GetProgrammersResponse implements Response {
    private ArrayList<Programmer> programmers;

    public GetProgrammersResponse(ArrayList<Programmer> programmers) {
        this.programmers = programmers;
    }

    public ArrayList<Programmer> getProgrammers() {
        return programmers;
    }

}
