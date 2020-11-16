package Networking.ObjectProtocol.Responses;


import Domain.Employee;
import Domain.EmployeePosition;

public class SuccessfulLoginResponse implements Response{
    //EmployeePosition employeePosition;
//    public SuccessfulLoginResponse(EmployeePosition employeePosition){
//        this.employeePosition = employeePosition;
//    }
//    public EmployeePosition getEmployeePosition() {
//        return employeePosition;
//    }
    private Employee employee;

    public SuccessfulLoginResponse(Employee employee) {
        this.employee = employee;
    }

    public Employee getEmployee() {
        return employee;
    }
}
