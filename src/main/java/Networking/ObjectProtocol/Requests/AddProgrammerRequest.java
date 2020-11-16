package Networking.ObjectProtocol.Requests;

import Domain.Programmer;

public class AddProgrammerRequest implements Request {
    private Programmer programmer;

    public AddProgrammerRequest(Programmer programmer) {
        this.programmer = programmer;
    }

    public Programmer getProgrammer() {
        return programmer;
    }
}
