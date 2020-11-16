package Services;


import Domain.Bug;
import Domain.Programmer;
import Domain.Tester;

public interface IObserver {
    void bugAdded(Bug bug);
    void bugSolved(Bug bug);
}
