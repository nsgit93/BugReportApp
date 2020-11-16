package Repository;

import Domain.Programmer;
import Domain.Tester;

import java.util.ArrayList;

public interface ITestersRepository extends IRepository<Integer, Tester> {
    public Tester findOnebyUserName(String userName);

}
