package Networking.Utils;


import Networking.ObjectProtocol.ClientObjectWorker;
import Services.IService;


import java.net.Socket;


public class ObjectConcurrentServer extends AbsConcurrentServer {
    private IService server;
    public ObjectConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("ObjectConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientObjectWorker worker=new ClientObjectWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }


}
