package Repository;

import Domain.Programmer;

public interface IProgrammersRepository extends IRepository<Integer, Programmer> {

    public Programmer findOnebyUserName(String userName);

}
