package Services;


import Domain.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;

public interface IService {


    public ArrayList<Programmer> getProgrammers();

    public ArrayList<Tester> getTesters();
    
    public ArrayList<Bug> getActiveBugs();
    
    public ArrayList<Bug> getBugsByTesterUsername(String username);

    public ArrayList<Bug> getBugsByTesterID(Integer id);

    public void addProgrammer(Programmer programmer) throws ServiceException;

    public void addTester(Tester tester) throws ServiceException;
    
    public void addBug(Bug bug) throws ServiceException;
    
    public void solveBug(Bug bug) throws ServiceException;

    public void logout(String username, IObserver client) throws ServiceException;

    public Employee login(String username, String password, IObserver client) throws ServiceException;
}
