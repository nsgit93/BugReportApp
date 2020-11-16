package Networking.ObjectProtocol;

import Domain.Bug;
import Domain.Employee;
import Domain.EmployeePosition;
import Networking.ObjectProtocol.Requests.*;
import Networking.ObjectProtocol.Responses.*;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class ClientObjectWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;


    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                System.out.println("Object Worker: received request");
                Object response = handleRequest((Request)request);
                if (response!=null){
                   sendResponse((Response) response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Object Worker: Error "+e);
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("Object Worker: sending response "+response);
        output.writeObject(response);
        System.out.println("Object Worker: response sent: "+response);
        output.flush();
    }

    private Response handleRequest(Request request) {

        if (request instanceof LoginRequest) {
            System.out.println("Object Worker: Login request ...");
            LoginRequest logReq = (LoginRequest) request;
            String username = logReq.getUsername();
            String password = logReq.getPassword();
            try {
                System.out.println("Object Worker: Client " + this + " wants to log in.");
                Employee employee = server.login(username,password,this);
                return new SuccessfulLoginResponse(employee);
            } catch (ServiceException e) {
                connected = false;
                return new ErrorResponse(e.getMessage());
            }
        } else if (request instanceof LogoutRequest) {
            System.out.println("Object Worker: Logout request");
            LogoutRequest logReq = (LogoutRequest) request;
            String username = logReq.getUsername();
            try {
                System.out.println("Object Worker: Client " + username + " wants to log out.");
                server.logout(username, this);
                connected = false;
                return new OkResponse();

            } catch (ServiceException e) {
                return new ErrorResponse(e.getMessage());
            }
        } else if (request instanceof AddBugRequest) {
            System.out.println("Object Worker: Get AddBugRequest ...");
            try {
                server.addBug(((AddBugRequest) request).getBug());
                return new OkResponse();
            } catch (ServiceException se) {
                return new ErrorResponse(se.getMessage());
            }
        } else if (request instanceof SolveBugRequest) {
            System.out.println("Object Worker: Get SolveBugRequest ...");
            try {
                Bug bug = ((SolveBugRequest) request).getBug();
                server.solveBug(bug);
                return new OkResponse();
            } catch (ServiceException se) {
                return new ErrorResponse(se.getMessage());
            }
        } else if (request instanceof GetActiveBugsRequest){
            System.out.println("Object Worker: GetActiveBugsRequest ...");
            return new GetActiveBugsResponse(server.getActiveBugs());
        } else if (request instanceof GetBugsByTesterUsernameRequest){
            System.out.println("Object Worker: GetBugsByTesterUsernameRequest ...");
            return new GetBugsByTesterUsernameResponse(server.getBugsByTesterUsername(((GetBugsByTesterUsernameRequest)request).getUsername()));
        }else if (request instanceof GetBugsByTesterIDRequest){
            System.out.println("Object Worker: GetBugsByTesterIDRequest ...");
            System.out.println("Tester ID: "+((GetBugsByTesterIDRequest)request).getId());
            return new GetBugsByTesterIDResponse(server.getBugsByTesterID(((GetBugsByTesterIDRequest)request).getId()));
        }else if (request instanceof GetProgrammersRequest){
            System.out.println("Object Worker: GetProgrammersRequest ...");
            return new GetProgrammersResponse(server.getProgrammers());
        }else if (request instanceof GetTestersRequest){
            System.out.println("Object Worker: GetTestersRequest ...");
            return new GetTestersResponse(server.getTesters());
        }else if (request instanceof AddProgrammerRequest) {
            System.out.println("Object Worker: Get AddProgrammerRequest ...");
            try {
                server.addProgrammer(((AddProgrammerRequest) request).getProgrammer());
                return new OkResponse();
            } catch (ServiceException se) {
                return new ErrorResponse(se.getMessage());
            }
        }else if (request instanceof AddTesterRequest) {
            System.out.println("Object Worker: Get AddTesterRequest ...");
            try {
                server.addTester(((AddTesterRequest) request).getTester());
                return new OkResponse();
            } catch (ServiceException se) {
                return new ErrorResponse(se.getMessage());
            }
        }

        return null;
    }

    @Override
    public void bugAdded(Bug bug) {
        System.out.println("Object Worker: Bug added: "+bug);
        try {
            sendResponse(new BugAddedResponse(bug));
        } catch (IOException e) {
            System.out.println("Object Worker: Sending error: "+e);
        }
    }

    @Override
    public void bugSolved(Bug bug) {
        System.out.println("Object Worker: Bug solved: "+bug.getId());
        try {
            sendResponse(new BugSolvedResponse(bug));
        } catch (IOException e) {
            System.out.println("Object Worker: Sending error: "+e);
        }
    }
}
