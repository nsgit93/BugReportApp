package Client;

import Client.Controller.*;
import Networking.ObjectProtocol.*;
import Services.IService;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;



public class StartClient extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55556;
    private static String defaultServer = "localhost";


    public void start(Stage stage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new ServiceObjectProxy(serverIP, serverPort);

        ScreenSwitcher screenSwitcher = new ScreenSwitcher();
        screenSwitcher.loadScreen(ScreenFramework.screenLogin, ScreenFramework.screenLoginFile,server,null);

        screenSwitcher.getStylesheets().addAll("style.css");

        screenSwitcher.setStage(stage);
        screenSwitcher.setScreen(ScreenFramework.screenLogin);


        Group root = new Group();
        root.getChildren().addAll(screenSwitcher);
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setWidth(400);
        stage.setHeight(350);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}


