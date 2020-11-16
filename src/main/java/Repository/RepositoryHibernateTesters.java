package Repository;

import Domain.Tester;
import org.apache.logging.log4j.core.config.plugins.util.ResolverUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class RepositoryHibernateTesters implements ITestersRepository {

    private static SessionFactory sessionFactory;


    public RepositoryHibernateTesters(){
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
    public void save(Tester entity) throws RepositoryException {
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
    public void update(Integer integer, Tester entity) {

    }

    @Override
    public Tester findOne(Integer integer) {
        return null;
    }

    @Override
    public ArrayList<Tester> findAll() {
        ArrayList<Tester> testers= new ArrayList<Tester>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                testers = (ArrayList<Tester>) session.createQuery("from Tester ", Tester.class).list();
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
        return testers;
    }

    @Override
    public Tester findOnebyUserName(String userName) {
        ArrayList<Tester> tester= new ArrayList<Tester>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                String queryString = "from Tester t where t.username = :User";
                tester = (ArrayList<Tester>) session
                        .createQuery(queryString, Tester.class)
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
        if (tester.size()==1)
            return tester.get(0);
        else
            return null;
    }
}
