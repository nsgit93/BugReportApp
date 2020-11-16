package Networking.ObjectProtocol.Requests;

import java.io.Serializable;

public class GetBugsByTesterIDRequest implements Request {
    private Integer id;

    public GetBugsByTesterIDRequest(Integer id){
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
