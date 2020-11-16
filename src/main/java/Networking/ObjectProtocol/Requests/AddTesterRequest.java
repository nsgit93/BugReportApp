package Networking.ObjectProtocol.Requests;

import Domain.Tester;

public class AddTesterRequest implements Request{

    private Tester tester;

    public AddTesterRequest(Tester tester) {
        this.tester = tester;
    }

    public Tester getTester() {
        return tester;
    }
}
