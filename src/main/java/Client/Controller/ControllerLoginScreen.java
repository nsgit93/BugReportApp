package Client.Controller;

import Domain.*;
import Networking.ObjectProtocol.ServiceObjectProxy;
import Services.IService;
import Services.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;

public class ControllerLoginScreen implements ControlledScreen{
    private ScreenSwitcher screenSwitcher;
    private IService serv;

    @FXML
    private TextField textUser;
    @FXML
    private PasswordField textPassword;
    @FXML
    private Label lblEroareLogare;
    @FXML
    public Button buttonLogin;


    @Override
    public void setScreenParent(ScreenSwitcher screenPage) {
        screenSwitcher = screenPage;
    }

    public void setService(IService service) {
        this.serv = service;
    }

    @Override
    public void setEmployee(IEmployee employee) {

    }

    @Override
    public void loadTableData() {

    }

    @FXML
    private void initialize(){
        buttonLogin.setDisable(true);
        textPassword.textProperty().addListener((obs, oldv, newv)->{
            if(!textPassword.getText().equals("") &&
                    !textUser.getText().equals("") &&
                    !newv.equals("")) {
                buttonLogin.setDisable(false);
            }
            else
                buttonLogin.setDisable(true);
        });
        textUser.textProperty().addListener((obs, oldv, newv)->{
            if(!textUser.getText().equals("") &&
                    !textPassword.getText().equals("") &&
                    !newv.equals("")) {
                buttonLogin.setDisable(false);
            }
            else
                buttonLogin.setDisable(true);
        });
    }

    public void login(ActionEvent actionEvent) {
        String userName = textUser.getText();
        String password = textPassword.getText();
        try{
            Employee employee = serv.login(userName,password,null);
            System.out.println("Employee id: "+employee.getId());
            if(employee.getPosition().equals(EmployeePosition.manager)) {
                Manager manager = new Manager(employee.getId(),null,userName,password,null);
                screenSwitcher.loadScreen(ScreenFramework.screenManager,ScreenFramework.screenManagerFile,serv,manager);
                ((ServiceObjectProxy)serv).setClient(screenSwitcher.getMainScreenController());
                screenSwitcher.setScreen(ScreenFramework.screenManager);
                screenSwitcher.getStage().setTitle("Main Window for manager: "+userName);
            }
            else if(employee.getPosition().equals(EmployeePosition.programmer)){
                Programmer programmer = new Programmer(employee.getId(),null,userName,password,null,null);
                screenSwitcher.loadScreen(ScreenFramework.screenProgrammer,ScreenFramework.screenProgrammerFile,serv,programmer);
                ((ServiceObjectProxy)serv).setClient(screenSwitcher.getMainScreenController());
                screenSwitcher.setScreen(ScreenFramework.screenProgrammer);
                screenSwitcher.getStage().setTitle("Main Window for programmer: "+userName);
            }
            else if(employee.getPosition().equals(EmployeePosition.tester)) {
                Tester tester = new Tester(employee.getId(),null,userName,password,null,null);
                screenSwitcher.loadScreen(ScreenFramework.screenTester,ScreenFramework.screenTesterFile,serv,tester);
                ((ServiceObjectProxy)serv).setClient(screenSwitcher.getMainScreenController());
                screenSwitcher.setScreen(ScreenFramework.screenTester);
                screenSwitcher.getStage().setTitle("Main Window for tester: "+userName);
            }
            textPassword.setText("");
            textUser.setText("");
        }
        catch (ServiceException ex){
            lblEroareLogare.setText(ex.getMessage());
            lblEroareLogare.setTextFill(Paint.valueOf("red"));
            textUser.setText("");
            textPassword.setText("");
        }
    }

    @Override
    public void bugAdded(Bug bug) {

    }

    @Override
    public void bugSolved(Bug bug) {

    }
}
