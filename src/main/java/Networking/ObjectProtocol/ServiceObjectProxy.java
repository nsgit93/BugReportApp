package Networking.ObjectProtocol;


import Domain.*;
import Networking.ObjectProtocol.Requests.*;
import Networking.ObjectProtocol.Responses.*;
import Services.ServiceException;
import Services.IObserver;
import Services.IService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class ServiceObjectProxy implements IService {
    private String host;
    private int port;

    private IObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws ServiceException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("Proxy: response received " + response);
                    if (response instanceof UpdateResponse) {
                        handleUpdate((UpdateResponse) response);
                    } else {
                        try {
                            System.out.println("Proxy: Response put in qresponses.");
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Proxy: Reading error " + e);
                }
            }
        }
    }


    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request) throws ServiceException {
        try {
            System.out.println("Proxy: Request: " + request);
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ServiceException("Proxy: Error sending object " + e);
        }

    }

    private Response readResponse() throws ServiceException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }


    private void handleUpdate(UpdateResponse update) {
        if (update instanceof BugAddedResponse){
            System.out.println("Proxy: Update response received: "+update);
            BugAddedResponse res = (BugAddedResponse) update;
            client.bugAdded(res.getAddedBug());
        }
        else if (update instanceof BugSolvedResponse){
            System.out.println("Proxy: Update response received: "+update);
            BugSolvedResponse res = (BugSolvedResponse) update;
            client.bugSolved(res.getSolvedBug());
        }

    }


    @Override
    public Employee login(String username, String password, IObserver client) throws ServiceException {
        initializeConnection();
        sendRequest(new LoginRequest(username,password));
        Response response = readResponse();
        if (response instanceof SuccessfulLoginResponse) {
            System.out.println("Proxy: Logged client: " + client);
            return ((SuccessfulLoginResponse) response).getEmployee();
        }
        else if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            closeConnection();
            throw new ServiceException(err.getMessage());
        }
        return null;
    }

    public void setClient(IObserver client){
        this.client = client;
    }


    @Override
    public ArrayList<Programmer> getProgrammers() {
        try {
            System.out.println("Proxy: Get Programmers");
            sendRequest(new GetProgrammersRequest());
            System.out.println("Proxy: sent GetProgrammersRequest");
            Response response = readResponse();
            System.out.println("Proxy: Response received");
            GetProgrammersResponse resp = (GetProgrammersResponse) response;
            return resp.getProgrammers();
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Tester> getTesters() {
        try {
            System.out.println("Proxy: Get Testers");
            sendRequest(new GetTestersRequest());
            System.out.println("Proxy: sent GetTestersRequest");
            Response response = readResponse();
            System.out.println("Proxy: Response received");
            GetTestersResponse resp = (GetTestersResponse) response;
            return resp.getTesters();
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Bug> getActiveBugs() {
        try {
            System.out.println("Proxy: Get ActiveBugs");
            sendRequest(new GetActiveBugsRequest());
            System.out.println("Proxy: sent GetActiveBugsRequest");
            Response response = readResponse();
            System.out.println("Proxy: Response received");
            GetActiveBugsResponse resp = (GetActiveBugsResponse) response;
            return resp.getActiveBugs();
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Bug> getBugsByTesterUsername(String username) {
        try {
            System.out.println("Proxy: GetBugsByTesterUsername");
            sendRequest(new GetBugsByTesterUsernameRequest(username));
            System.out.println("Proxy: sent GetBugsByTesterUsernameRequest");
            Response response = readResponse();
            System.out.println("Proxy: Response received");
            GetBugsByTesterUsernameResponse resp = (GetBugsByTesterUsernameResponse) response;
            return resp.getBugs();
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<Bug> getBugsByTesterID(Integer id) {
        try {
            System.out.println("Proxy: GetBugsByTesterID");
            sendRequest(new GetBugsByTesterIDRequest(id));
            System.out.println("Proxy: sent GetBugsByTesterIDRequest");
            Response response = readResponse();
            System.out.println("Proxy: Response received");
            GetBugsByTesterIDResponse resp = (GetBugsByTesterIDResponse) response;
            return resp.getBugs();
        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addProgrammer(Programmer programmer) throws ServiceException {
        System.out.println("Proxy: Add Programmer");
        sendRequest(new AddProgrammerRequest(programmer));
        System.out.println("Proxy: sent AddProgrammerRequest");
        Response response = readResponse();
        if (response instanceof ErrorResponse)
            throw new ServiceException(((ErrorResponse) response).getMessage());
    }

    @Override
    public void addTester(Tester tester) throws ServiceException {
        System.out.println("Proxy: Add Tester");
        sendRequest(new AddTesterRequest(tester));
        System.out.println("Proxy: sent AddTesterRequest");
        Response response = readResponse();
        if (response instanceof ErrorResponse)
            throw new ServiceException(((ErrorResponse) response).getMessage());
    }

    @Override
    public void addBug(Bug bug) throws ServiceException {
        System.out.println("Proxy: Add Bug");
        sendRequest(new AddBugRequest(bug));
        System.out.println("Proxy: sent AddBugRequest");
        Response response = readResponse();
        if (response instanceof ErrorResponse)
            throw new ServiceException(((ErrorResponse) response).getMessage());
    }

    @Override
    public void solveBug(Bug bug) throws ServiceException {
        System.out.println("Proxy: Solve Bug");
        sendRequest(new SolveBugRequest(bug));
        System.out.println("Proxy: sent SolveBugRequest");
        Response response = readResponse();
        if (response instanceof ErrorResponse)
            throw new ServiceException(((ErrorResponse) response).getMessage());
    }

    @Override
    public void logout(String username, IObserver client) throws ServiceException {
        //EmployeeDTO employeeDTO = DTOUtils.getDTO(employee);
        sendRequest(new LogoutRequest(username));
        Response response = readResponse();
        closeConnection();
        if (response instanceof ErrorResponse) {
            ErrorResponse err = (ErrorResponse) response;
            throw new ServiceException(err.getMessage());
        }
    }

}



