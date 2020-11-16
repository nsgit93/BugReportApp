package Repository;

import Domain.Bug;
import Domain.Tester;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.ArrayList;

public class RepositoryHibernateBugs implements IBugsRepository {

    private static SessionFactory sessionFactory;

    private static void initialize() {
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

    public RepositoryHibernateBugs(){
        initialize();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Bug entity) throws RepositoryException {
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
    public void update(Integer id, Bug entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                session.createQuery("update Bug b set b.status = :status where b.id = :ID")
                        .setParameter("status",entity.getStatus())
                        .setParameter("ID",id)
                        .executeUpdate();
                tx.commit();

            } catch(RuntimeException ex){
                if (tx!=null)
                    tx.rollback();
            }
        }
    }

    @Override
    public Bug findOne(Integer integer) {
        return null;
    }


    @Override
    public ArrayList<Bug> findAll() {
        ArrayList<Bug> bugs= new ArrayList<Bug>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                bugs = (ArrayList<Bug>) session.createQuery("from Bug", Bug.class).list();
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
        return bugs;
    }

    @Override
    public Bug findLastAdded() {
        ArrayList<Bug> bug = new ArrayList<Bug>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                bug = (ArrayList<Bug>) session
                        .createQuery("from Bug b order by b.id desc", Bug.class)
                        .setMaxResults(1)
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
        return bug.get(0);
    }

    @Override
    public ArrayList<Bug> findBugsByTesterUsername(String username) {
        System.out.println("repo -> find bugs by tester");
        ArrayList<Bug> bugs = new ArrayList<Bug>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                ArrayList<Tester> tester = (ArrayList<Tester>) session
                        .createQuery("from Tester t where t.username=:user",Tester.class)
                        .setParameter("user",username)
                        .list();
                System.out.println("Tester: "+tester.get(0).getName()+" "+tester.get(0).getId());
                tx.commit();
                tx = session.beginTransaction();
                Integer id = tester.get(0).getId();
                bugs = (ArrayList<Bug>) session
                        .createQuery("from Bug b where b.testerID = :ID", Bug.class)
                        .setParameter("ID",id)
                        .list();
                System.out.println("Bugs: "+bugs.size());
                tx.commit();
            }catch(RuntimeException ex){
                System.out.println(ex.getMessage());
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        bugs.forEach(System.out::println);
        return bugs;
    }

    @Override
    public ArrayList<Bug> findBugsByTesterID(Integer id) {
        System.out.println("repo -> find bugs by tester id");
        ArrayList<Bug> bugs = new ArrayList<Bug>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                bugs = (ArrayList<Bug>) session
                        .createQuery("from Bug b where b.testerID = :ID", Bug.class)
                        .setParameter("ID",id)
                        .list();
                System.out.println("Bugs: "+bugs.size());
                tx.commit();
            }catch(RuntimeException ex){
                System.out.println(ex.getMessage());
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        bugs.forEach(System.out::println);
        return bugs;
    }

    @Override
    public ArrayList<Bug> findActiveBugs() {
        ArrayList<Bug> bugs= new ArrayList<Bug>();
        try(Session session = sessionFactory.openSession()){
            Transaction tx=null;
            try{
                tx = session.beginTransaction();
                bugs = (ArrayList<Bug>) session.createQuery("from Bug b where b.status = :status", Bug.class)
                        .setParameter("status","active")
                        .list();
                tx.commit();

            }catch(RuntimeException ex){
                System.out.println(ex.getMessage());
                if (tx!=null)
                    tx.rollback();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        System.out.println("close");
        return bugs;
    }
}
