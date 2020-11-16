package Client.Controller;

import Domain.IEmployee;
import Services.IService;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;

public class ScreenSwitcher extends StackPane{
    private ControlledScreen controllerScreen;



    private Stage stage;

    private HashMap<String, Node> screens = new HashMap<>();

    public ScreenSwitcher() {
        super();
    }

    public Stage getStage(){
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    //Add the screen to the collection
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    //Returns the Node with the appropriate name
    public Node getScreen(String name) {
        return screens.get(name);
    }

    public ControlledScreen getMainScreenController(){
        return controllerScreen;
    }

    //Loads the fxml file, add the screen to the screens collection and
    //finally injects the screenPane to the controller.
    public boolean loadScreen(String name, String resource, IService service, IEmployee employee) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = myLoader.load();
            controllerScreen = myLoader.getController();
            controllerScreen.setScreenParent(this);
            controllerScreen.setEmployee(employee);
            controllerScreen.setService(service);
            addScreen(name, loadScreen);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //This method tries to displayed the screen with a predefined name.
    //First it makes sure the screen has been already loaded.  Then if there is more than
    //one screen the new screen is been added second, and then the current screen is removed.
    // If there isn't any screen being displayed, the new screen is just added to the root.
    public boolean setScreen(String name) {
        if (screens.get(name) != null) {   //screen loaded
            /*if (name.equals(ScreenFramework.screenMain)) {
                controllerMainScreen.loadTableData();
            }*/
            controllerScreen.loadTableData();
            if (name.equals(ScreenFramework.screenLogin)){
                stage.setTitle("Login Window");
                stage.setHeight(350);
                stage.setWidth(400);
            }
            if (name.equals(ScreenFramework.screenManager)){
                stage.setHeight(640);
                stage.setWidth(780);
            }
            if (name.equals(ScreenFramework.screenProgrammer)){
                stage.setHeight(570);
                stage.setWidth(550);
            }
            if (name.equals(ScreenFramework.screenTester)){
                stage.setHeight(530);
                stage.setWidth(850);
            }
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(250), new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                getChildren().remove(0);                    //remove the displayed screen
                                getChildren().add(0, screens.get(name));     //add the screen
                                Timeline fadeIn = new Timeline(
                                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                        new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                                fadeIn.play();
                            }
                        }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!!! \n");
            return false;
        }
    }

    //This method will remove the screen with the given name from the collection of screens
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }

}
