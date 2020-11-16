import Server.Service;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContext context=new ClassPathXmlApplicationContext("BugReportSpring.xml");

        Service service = context.getBean(Service.class);

        /*ScreenSwitcher mainController = new ScreenSwitcher();

        mainController.loadScreen(ScreenFramework.screenLogin, ScreenFramework.screenLoginFile, service);
        mainController.loadScreen(ScreenFramework.screenMain, ScreenFramework.screenMainFile, service);

        mainController.setScreen(ScreenFramework.screenLogin);
        //mainController.setId("pane");
        //mainController.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        mainController.getStylesheets().addAll("style.css");


        Group root = new Group();
        root.getChildren().addAll(mainController);
        Scene scene = new Scene(root,700,600);
        //scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Problema 5");
        stage.show();*/
    }

    public static void main(String[] args) {
        launch(args);
    }



}
