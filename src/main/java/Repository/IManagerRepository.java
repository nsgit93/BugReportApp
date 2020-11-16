package Repository;

import Domain.Manager;

public interface IManagerRepository extends IRepository<Integer,Manager>{
    public Manager findOnebyUserName(String username);
}
