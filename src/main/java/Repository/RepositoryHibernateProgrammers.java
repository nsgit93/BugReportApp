package Repository;

import Domain.Programmer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import java.util.ArrayList;

public class RepositoryHibernateProgrammers implements IProgrammersRepository {

    private static SessionFactory sessionFactory;

    public RepositoryHibernateProgrammers() {
        initialize();
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
    public void save(Programmer entity) throws RepositoryException {
        try(Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Integer integer, Programmer entity) {

    }

    @Override
    public Programmer findOne(Integer integer) {
        return null;
    }


    @Override
    public ArrayList<Programmer> findAll() {
        ArrayList<Programmer> programmers= new ArrayList<Programmer>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                programmers = (ArrayList<Programmer>) session.createQuery("from Programmer ", Programmer.class).list();
                tx.commit();
            }catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("close");
        return programmers;
    }

    @Override
    public Programmer findOnebyUserName(String userName) {
        ArrayList<Programmer> programmer= new ArrayList<Programmer>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String queryString = "from Programmer p where p.username = :User";
                programmer = (ArrayList<Programmer>) session
                        .createQuery(queryString, Programmer.class)
                        .setParameter("User", userName)
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
        if (programmer.size()==1)
            return programmer.get(0);
        else
            return null;
    }
}
