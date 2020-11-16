package Client.Controller;

import Domain.Bug;
import Domain.IEmployee;
import Domain.Programmer;
import Services.IObserver;
import Services.IService;
import Services.ServiceException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class ControllerProgrammerScreen implements ControlledScreen, Initializable {
    @FXML
    public TableView<Bug> tableActiveBugs;
    @FXML
    public Button removeBugButton;
    private ScreenSwitcher screenSwitcher;
    private IService serv;
    private IEmployee employee;
    public IEmployee getEmployee() {
        return employee;
    }
    private Bug selectedBug;

    public void setEmployee(IEmployee employee) {
        this.employee = employee;
    }

    @Override
    public void loadTableData() {
        tableActiveBugs.getItems().clear();
        (serv.getActiveBugs()).forEach(p -> tableActiveBugs.getItems().add(p));
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
        tableActiveBugs.setMinWidth(500);

        tableActiveBugs.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        removeBugButton.setDisable(true);
        tableActiveBugs.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableActiveBugs.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV)->{
            if (newV == null){
                removeBugButton.setDisable(true);
            }
            else {
                System.out.println("Selected bug: "+newV.getId());
                removeBugButton.setDisable(false);
                selectedBug = newV;
            }
        });

    }

    public void logout(ActionEvent actionEvent) {
        try {
            serv.logout(((Programmer)employee).getUsername(),this);
            screenSwitcher.loadScreen(ScreenFramework.screenLogin,ScreenFramework.screenLoginFile,serv,null);
            screenSwitcher.setScreen(ScreenFramework.screenLogin);
            screenSwitcher.unloadScreen(ScreenFramework.screenProgrammer);
        }
        catch (ServiceException ex){
            System.out.println(ex.getMessage());;
        }
    }

    public void removeBug(ActionEvent actionEvent) throws ServiceException {
        try{
            serv.solveBug(selectedBug);
            Alert message = new Alert(Alert.AlertType.CONFIRMATION);
            message.setContentText("Bug solved!");
            message.showAndWait();
            //tableActiveBugs.getSelectionModel().clearSelection();
        }
        catch (ServiceException se){
            Alert message = new Alert(Alert.AlertType.WARNING);
            message.setContentText(se.getMessage());
            message.showAndWait();
        }
    }

    @Override
    public void bugAdded(Bug bug) {
        tableActiveBugs.getItems().add(bug);
    }

    @Override
    public void bugSolved(Bug bug) {
        for (int i=0; i<tableActiveBugs.getItems().size();i++)
            if (tableActiveBugs.getItems().get(i).getId().equals(bug.getId())){
                tableActiveBugs.getItems().remove(i);
                break;
            }
    }
}
