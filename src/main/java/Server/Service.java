package Server;

import Domain.*;
import Repository.*;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;
import Validator.IValidator;
import Validator.ValidationException;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IProgrammersRepository repoProgrammers;
    private ITestersRepository repoTesters;
    private IManagerRepository repoManager;
    private IBugsRepository repoBugs;
    private IValidator<Programmer> validatorProgrammer;
    private IValidator<Tester> validatorTester;
    private IValidator<Bug> validatorBug;
    private volatile boolean finished;


    private Map<Integer, IObserver> loggedEmployees;

    private final int defaultThreadsNo=20;

    public Service(IProgrammersRepository repoProgrammers, ITestersRepository repoTesters, IManagerRepository repoManager,IBugsRepository repoBugs){
        this.repoProgrammers = repoProgrammers;
        this.repoBugs = repoBugs;
        this.repoTesters = repoTesters;
        this.repoManager = repoManager;
        this.loggedEmployees = new ConcurrentHashMap<>();
        finished = false;
    }

    public void setValidatorProgrammer(IValidator<Programmer> validatorProgrammer){
        this.validatorProgrammer = validatorProgrammer;
    }

    public void setValidatorTester(IValidator<Tester> validatorTester) {
        this.validatorTester = validatorTester;
    }

    public void setValidatorBug(IValidator<Bug> validatorBug) {
        this.validatorBug = validatorBug;
    }

    @Override
    public synchronized Employee login(String username, String password, IObserver client) throws ServiceException {
        Programmer programmer = repoProgrammers.findOnebyUserName(username);
        if(programmer == null){
            Tester tester = repoTesters.findOnebyUserName(username);
            if(tester == null){
                Manager manager = repoManager.findOnebyUserName(username);
                if (manager == null)
                    throw new ServiceException("User does not exist!");
                else {
                    if(loggedEmployees.get(manager.getId())!=null)
                        throw new ServiceException("You are logged already!");
                    if(!password.equals(manager.getPassword()))
                        throw new ServiceException("Incorrect password!");
                    loggedEmployees.put(manager.getId(), client);
                    return new Employee(EmployeePosition.manager,manager.getId());
                }
            }
            else {
                if (loggedEmployees.get(tester.getId()) != null)
                    throw new ServiceException("You are logged already!");
                if(!password.equals(tester.getPassword()))
                    throw new ServiceException("Incorrect password!");
                loggedEmployees.put(tester.getId(), client);
                return new Employee(EmployeePosition.tester,tester.getId());
            }
        }
        else {
            if (loggedEmployees.get(programmer.getId()) != null)
                throw new ServiceException("You are logged already!");
            if(!password.equals(programmer.getPassword()))
                throw new ServiceException("Incorrect password!");
            loggedEmployees.put(programmer.getId(), client);
            return new Employee(EmployeePosition.programmer,programmer.getId());
        }
    }

    @Override
    public ArrayList<Programmer> getProgrammers() {
        return repoProgrammers.findAll();
    }

    @Override
    public ArrayList<Tester> getTesters() {
        return repoTesters.findAll();
    }

    @Override
    public ArrayList<Bug> getActiveBugs() {
        return repoBugs.findActiveBugs();
    }

    @Override
    public ArrayList<Bug> getBugsByTesterUsername(String username) {
        return repoBugs.findBugsByTesterUsername(username);
    }

    @Override
    public ArrayList<Bug> getBugsByTesterID(Integer id) {
        return repoBugs.findBugsByTesterID(id);
    }

    @Override
    public void addProgrammer(Programmer programmer) throws ServiceException {
        try{
            validatorProgrammer.validate(programmer);
            repoProgrammers.save(programmer);
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void addTester(Tester tester) throws ServiceException {
        try{
            validatorTester.validate(tester);
            repoTesters.save(tester);
        } catch (ValidationException | RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addBug(Bug bug) throws ServiceException {
        try{
            validatorBug.validate(bug);
            repoBugs.save(bug);

            ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

            for (IObserver client : loggedEmployees.values()) {
                System.out.println("Notifying employee --->" + client + " that a bug was added.");
                if (client != null)
                    executor.execute(() -> {
                        client.bugAdded(repoBugs.findLastAdded());
                    });
            }

            executor.shutdown();

        }
        catch (ValidationException | RepositoryException e){
            throw new ServiceException(e.getMessage());
        }

    }

    @Override
    public void solveBug(Bug bug) throws ServiceException {
        bug.setStatus("solved");
        repoBugs.update(bug.getId(),bug);
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

        for (IObserver client : loggedEmployees.values()) {
            System.out.println("Notifying employee --->" + client + " that a bug was solved.");
            if (client != null)
                executor.execute(() -> {
                    client.bugSolved(bug);
                });
        }
    }

    @Override
    public synchronized void logout(String username, IObserver client) throws ServiceException {
        Manager manager = repoManager.findOnebyUserName(username);
        Programmer programmer = repoProgrammers.findOnebyUserName(username);
        Tester tester = repoTesters.findOnebyUserName(username);
        if (manager != null)
            loggedEmployees.remove(manager.getId());
        else if (programmer != null)
            loggedEmployees.remove(programmer.getId());
        else if (tester != null)
            loggedEmployees.remove(tester.getId());
    }


}
