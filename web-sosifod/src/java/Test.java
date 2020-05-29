
import java.util.Date;
import java.util.List;
import model.Intimation;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

public class Test {

    public static void main(String[] args) {
        selectIntimations();
    }
    
    private static void insertUser() {
        User user = new User();
        user.setName("Jhonny Izidoro Menarim");
        user.setCpf("10655675965");
        user.setEmail("email@admin.com");
        user.setPassword("123456");
        user.setProfile(1);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(user);
        session.getTransaction().commit();
        System.out.println("Success.");
    }
    
    private static void selectUserById() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = (User) session.get(User.class, 1);
        System.out.println(user.getIntimations().get(0).getAddress());
    }
    
    private static void selectUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM User");
        List<User> users = query.list();
        session.getTransaction().commit();
        for (User user : users) {
            System.out.println(user.getName());
        }
    }
    
    private static void insertIntimation() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = (User) session.get(User.class, 1);
        Intimation intimation = new Intimation();
        intimation.setName("Intimado da Silva");
        intimation.setCpf("10655675965");
        intimation.setAddress("Rua da Esquina, 111");
        intimation.setLawsuit("159159");
        intimation.setOfficer(user);
        intimation.setCreatedAt(new Date());
        session.beginTransaction();
        session.save(intimation);
        session.getTransaction().commit();
        System.out.println("Success.");
    }
    
    private static void selectIntimations() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM Intimation");
        List<Intimation> intimations = query.list();
        session.getTransaction().commit();
        System.out.println(intimations);
    }
    
}
