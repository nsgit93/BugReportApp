package Repository;

import Domain.Bug;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface IBugsRepository extends IRepository<Integer, Bug> {
    public Bug findLastAdded();
    public ArrayList<Bug> findBugsByTesterUsername(String username);
    public ArrayList<Bug> findBugsByTesterID(Integer id);
    public ArrayList<Bug> findActiveBugs();
}
