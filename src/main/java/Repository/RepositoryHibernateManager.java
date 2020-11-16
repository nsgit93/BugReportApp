package Repository;

import Domain.Manager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import java.util.ArrayList;

public class RepositoryHibernateManager implements IManagerRepository {

    private static SessionFactory sessionFactory;

    public RepositoryHibernateManager() {
    }

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }


    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Manager entity) throws RepositoryException {

    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Manager entity) {

    }

    @Override
    public Manager findOne(Integer integer) {
        return null;
    }


    @Override
    public ArrayList<Manager> findAll() {
        return null;
    }

    @Override
    public Manager findOnebyUserName(String username) {
        initialize();
        ArrayList<Manager> manager= new ArrayList<Manager>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String queryString = "from Manager m where m.username = :User";
                manager = (ArrayList<Manager>) session
                        .createQuery(queryString, Manager.class)
                        .setParameter("User", username)
                        .list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        close();
        if (manager.size()==1)
            return manager.get(0);
        else
            return null;
    }
}
