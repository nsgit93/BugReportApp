package Server;

import Networking.Utils.AbstractServer;
import Networking.Utils.ObjectConcurrentServer;
import Networking.Utils.ServerException;
import Repository.RepositoryHibernateBugs;
import Repository.RepositoryHibernateManager;
import Repository.RepositoryHibernateProgrammers;
import Repository.RepositoryHibernateTesters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.Properties;


public class StartServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
       Properties serverProps=new Properties();
        try {
            serverProps.load(StartServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties "+e);
            return;
        }

        ApplicationContext context=new ClassPathXmlApplicationContext("BugReportSpring.xml");

        RepositoryHibernateProgrammers repositoryHibernateProgrammers = context.getBean(RepositoryHibernateProgrammers.class);
        RepositoryHibernateTesters repositoryHibernateTesters = context.getBean(RepositoryHibernateTesters.class);
        RepositoryHibernateManager repositoryHibernateManager = context.getBean(RepositoryHibernateManager.class);

        Service service = context.getBean(Service.class);

        System.out.println(service);

        int chatServerPort=defaultPort;

        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);


        AbstractServer server = new ObjectConcurrentServer(chatServerPort, service);

        try {
                server.start();
        } catch (ServerException e) {
                System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
