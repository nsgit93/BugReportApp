package Domain;

import javax.persistence.Entity;
import java.io.Serializable;

public class Programmer implements Serializable, IEmployee {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private Integer salary;
    private Integer solvedBugsNumber;

    public Programmer(Integer id, String name, String username, String password, Integer salary, Integer solvedBugsNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.salary = salary;
        this.solvedBugsNumber = solvedBugsNumber;
    }

    public Programmer() {
        super();
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }


    public Integer getSolvedBugsNumber() {
        return solvedBugsNumber;
    }

    public void setSolvedBugsNumber(Integer solvedBugsNumber) {
        this.solvedBugsNumber = solvedBugsNumber;
    }


}
