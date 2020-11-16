package Client.Controller;

import Domain.Bug;
import Domain.IEmployee;
import Domain.Tester;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ControllerTesterScreen implements ControlledScreen, Initializable {

    private ScreenSwitcher screenSwitcher;
    private IService serv;
    private IEmployee employee;
    public IEmployee getEmployee() {
        return employee;
    }

    @FXML
    public TableView<Bug> tableReportedBugs;

    @FXML
    public TextField textName;

    @FXML
    public TextArea textDescription;

    @FXML
    public Button addBugButton;



    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    @Override
    public void setScreenParent(ScreenSwitcher screenPage) {
        screenSwitcher = screenPage;
    }

    public void setService(IService service) {
        this.serv = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                logout(new ActionEvent());
            }
        });

        tableReportedBugs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        addBugButton.setDisable(true);
        textName.textProperty().addListener((obs, oldv, newv)->{
            if(!textName.getText().equals("") &&
                    !textDescription.getText().equals("") &&
                    !newv.equals("")) {
                addBugButton.setDisable(false);
            }
            else
                addBugButton.setDisable(true);
        });
        textDescription.textProperty().addListener((obs, oldv, newv)->{
            if(!textDescription.getText().equals("") &&
                    !textName.getText().equals("") &&
                    !newv.equals("")) {
                addBugButton.setDisable(false);
            }
            else
                addBugButton.setDisable(true);
        });
    }

    public void loadTableData(){
        System.out.println("Load table data");
        tableReportedBugs.getItems().clear();
        System.out.println("Tester id: "+((Tester)employee).getId());
        (serv.getBugsByTesterID(((Tester)employee).getId())).forEach(p -> tableReportedBugs.getItems().add(p));
    }


    public void logout(ActionEvent actionEvent) {
        try {
            serv.logout(((Tester)employee).getUsername(),this);
            screenSwitcher.loadScreen(ScreenFramework.screenLogin,ScreenFramework.screenLoginFile,serv,null);
            screenSwitcher.setScreen(ScreenFramework.screenLogin);
            screenSwitcher.unloadScreen(ScreenFramework.screenProgrammer);
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());;
        }
    }

    public void addBug(ActionEvent actionEvent) {
        String name = textName.getText();
        String description = textDescription.getText();
        Integer id = ((Tester)employee).getId();
        try {
            serv.addBug(new Bug(null,name,description,id,"active"));
            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setContentText("Bug added!");
            message.showAndWait();
            clearFields();

        } catch (ServiceException e) {
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setContentText(e.getMessage());
            message.showAndWait();
            clearFields();
        }

    }

    @Override
    public void bugAdded(Bug bug) {
        System.out.println("Controller tester -> bug added!");
        System.out.println("Bug -> tester id: "+bug.getTesterID());
        if (bug.getTesterID().equals(((Tester)employee).getId())){
            tableReportedBugs.getItems().add(bug);
        }

    }

    @Override
    public void bugSolved(Bug bug) {
        if (bug.getTesterID().equals(((Tester)employee).getId())){
            Bug updated = bug;
            updated.setStatus("solved");
            for (int i=0; i<tableReportedBugs.getItems().size();i++)
                if (tableReportedBugs.getItems().get(i).getId().equals(updated.getId())){
                    tableReportedBugs.getItems().remove(i);
                    tableReportedBugs.getItems().add(i,updated);
                    break;
                }
        }
    }

    private void clearFields(){
        textName.setText("");
        textDescription.setText("");
    }

}
