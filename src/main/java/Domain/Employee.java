package Domain;

import java.io.Serializable;

public class Employee implements IEmployee, Serializable {
    EmployeePosition position;
    Integer id;

    public Employee() {
    }

    public Employee(EmployeePosition position, Integer id) {
        this.position = position;
        this.id = id;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
