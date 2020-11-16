package Client.Controller;

import Domain.*;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ResourceBundle;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ControllerManagerScreen implements ControlledScreen, Initializable {
    @FXML
    public TableView<Programmer> tableProgrammers;
    @FXML
    public TableView<Tester> tableTesters;
    @FXML
    public TextField textName, textUsername, textPassword, textSalary;
    @FXML
    public ComboBox<String> positionBox;
    @FXML
    public Button buttonAddProgrammer;
    @FXML
    public Button buttonAddTester;

    private ScreenSwitcher screenSwitcher;
    private IService serv;
    private IEmployee employee;
    public IEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(IEmployee employee) {
        this.employee = (Manager) employee;
    }

    @Override
    public void loadTableData() {
        loadProgrammersTable();
        loadTestersTable();
    }


    private void loadProgrammersTable(){
        tableProgrammers.getItems().clear();
        (serv.getProgrammers()).forEach(p -> tableProgrammers.getItems().add(p));
    }

    private void loadTestersTable(){
        tableTesters.getItems().clear();
        (serv.getTesters()).forEach(t -> tableTesters.getItems().add(t));
    }


    @Override
    public void setScreenParent(ScreenSwitcher screenPage) {
        screenSwitcher = screenPage;
    }

    public void setService(IService service) {
        this.serv = service;
    }

    @Override
    @FXML
    public void initialize(URL url, ResourceBundle rb){
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                logout(new ActionEvent());
            }
        });


        tableProgrammers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableTesters.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        buttonAddProgrammer.setDisable(true);
        buttonAddTester.setDisable(true);


        textName.textProperty().addListener((obs, oldv, newv)->{
            if(!textName.getText().equals("") &&
                    !textPassword.getText().equals("") &&
                    !textSalary.getText().equals("") &&
                    !textUsername.getText().equals("") &&
                    positionBox.getSelectionModel().getSelectedItem() != null &&
                    !newv.equals("")) {
                if (positionBox.getSelectionModel().getSelectedItem().equals("Tester"))
                    buttonAddTester.setDisable(false);
                else if(positionBox.getSelectionModel().getSelectedItem().equals("Programmer"))
                    buttonAddProgrammer.setDisable(false);
            }
            else {
                buttonAddProgrammer.setDisable(true);
                buttonAddTester.setDisable(true);
            }
        });


        textUsername.textProperty().addListener((obs, oldv, newv)->{
            if(!textName.getText().equals("") &&
                    !textPassword.getText().equals("") &&
                    !textSalary.getText().equals("") &&
                    !textUsername.getText().equals("") &&
                    positionBox.getSelectionModel().getSelectedItem() != null &&
                    !newv.equals("")) {
                if (positionBox.getSelectionModel().getSelectedItem().equals("Tester"))
                    buttonAddTester.setDisable(false);
                else if(positionBox.getSelectionModel().getSelectedItem().equals("Programmer"))
                    buttonAddProgrammer.setDisable(false);
            }
            else {
                buttonAddProgrammer.setDisable(true);
                buttonAddTester.setDisable(true);
            }
        });


        textSalary.textProperty().addListener((obs, oldv, newv)->{
            if(!textName.getText().equals("") &&
                    !textPassword.getText().equals("") &&
                    !textSalary.getText().equals("") &&
                    !textUsername.getText().equals("") &&
                    positionBox.getSelectionModel().getSelectedItem() != null &&
                    !newv.equals("")) {
                if (positionBox.getSelectionModel().getSelectedItem().equals("Tester"))
                    buttonAddTester.setDisable(false);
                else if(positionBox.getSelectionModel().getSelectedItem().equals("Programmer"))
                    buttonAddProgrammer.setDisable(false);
            }
            else {
                buttonAddProgrammer.setDisable(true);
                buttonAddTester.setDisable(true);
            }
        });

        textPassword.textProperty().addListener((obs, oldv, newv)->{
            if(!textName.getText().equals("") &&
                    !textPassword.getText().equals("") &&
                    !textSalary.getText().equals("") &&
                    !textUsername.getText().equals("") &&
                    positionBox.getSelectionModel().getSelectedItem() != null &&
                    !newv.equals("")) {
                if (positionBox.getSelectionModel().getSelectedItem().equals("Tester"))
                    buttonAddTester.setDisable(false);
                else if(positionBox.getSelectionModel().getSelectedItem().equals("Programmer"))
                    buttonAddProgrammer.setDisable(false);
            }
            else {
                buttonAddProgrammer.setDisable(true);
                buttonAddTester.setDisable(true);
            }
        });


        positionBox.setOnAction(ev->{
            String proba = positionBox.getSelectionModel().getSelectedItem();
            if (proba != null &&
                    !textName.getText().equals("") &&
                    !textPassword.getText().equals("") &&
                    !textSalary.getText().equals("") &&
                    !textUsername.getText().equals("")
            ){
                if (positionBox.getSelectionModel().getSelectedItem().equals("Tester")){
                    buttonAddTester.setDisable(false);
                    buttonAddProgrammer.setDisable(true);

                }

                else if(positionBox.getSelectionModel().getSelectedItem().equals("Programmer")){
                    buttonAddProgrammer.setDisable(false);
                    buttonAddTester.setDisable(true);

                }
            }
        });


    }

    public void logout(ActionEvent actionEvent) {
        try {
            serv.logout(((Manager)employee).getUsername(),this);
            screenSwitcher.loadScreen(ScreenFramework.screenLogin,ScreenFramework.screenLoginFile,serv,null);
            screenSwitcher.setScreen(ScreenFramework.screenLogin);
            screenSwitcher.unloadScreen(ScreenFramework.screenManager);
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());;
        }

    }

    public void addProgrammer(ActionEvent actionEvent) {
        String name = textName.getText();
        String username = textUsername.getText();
        String password = textUsername.getText();
        Integer salary = Integer.parseInt(textSalary.getText());
        try {
            Programmer programmer = new Programmer(null,name,username,password,salary,0);
            serv.addProgrammer(programmer);
            tableProgrammers.getItems().add(programmer);
            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setContentText("Programmer added!");
            message.showAndWait();
            clearFields();

        } catch (ServiceException e) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setContentText(e.getMessage());
            message.showAndWait();
            clearFields();
        }
    }

    public void addTester(ActionEvent actionEvent) {
        String name = textName.getText();
        String username = textUsername.getText();
        String password = textUsername.getText();
        Integer salary = Integer.parseInt(textSalary.getText());
        try {
            Tester tester = new Tester(null,name,username,password,salary,0);
            serv.addTester(tester);
            tableTesters.getItems().add(tester);
            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setContentText("Tester added!");
            message.showAndWait();
            clearFields();

        } catch (ServiceException e) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setContentText(e.getMessage());
            message.showAndWait();
            clearFields();
        }
    }

    private void clearFields(){
        textName.setText("");
        textPassword.setText("");
        textUsername.setText("");
        textSalary.setText("");
        positionBox.getItems().clear();
    }


    @Override
    public void bugAdded(Bug bug) {

    }

    @Override
    public void bugSolved(Bug bug) {

    }
}
