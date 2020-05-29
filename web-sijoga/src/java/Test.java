
import java.util.Date;
import model.Lawsuit;
import model.Phase;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;

public class Test {

    public static void main(String[] args) {
        insertPhase();
    }
    
    private static void insertUsers() {
        User userOne = new User();
        userOne.setName("Usuário Parte 1");
        userOne.setCpf("10655675965");
        userOne.setPassword("123456");
        userOne.setProfile(1);
        
        User userTwo = new User();
        userTwo.setName("Usuário Parte 2");
        userTwo.setCpf("10655675965");
        userTwo.setPassword("123456");
        userTwo.setProfile(1);
        
        User lawierOne = new User();
        lawierOne.setName("Advogado 1");
        lawierOne.setCpf("10655675965");
        lawierOne.setPassword("123456");
        lawierOne.setProfile(2);
        
        User lawierTwo = new User();
        lawierTwo.setName("Advogado 2");
        lawierTwo.setCpf("10655675965");
        lawierTwo.setPassword("123456");
        lawierTwo.setProfile(2);
        
        User judge = new User();
        judge.setName("Juiz");
        judge.setCpf("10655675965");
        judge.setPassword("123456");
        judge.setProfile(3);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(userOne);
        session.save(userTwo);
        session.save(lawierOne);
        session.save(lawierTwo);
        session.save(judge);
        session.getTransaction().commit();
        System.out.println("Success.");
    }
    
    private static void insertLawsuit() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User promoted = (User) session.get(User.class, 1);
        User promoting = (User) session.get(User.class, 2);
        User promotedLawier = (User) session.get(User.class, 3);
        User promotingLawier = (User) session.get(User.class, 4);
        User judge = (User) session.get(User.class, 5);
        
        Lawsuit lawsuit = new Lawsuit();
        lawsuit.setCreatedAt(new Date());
        lawsuit.setStatus(1);
        lawsuit.setPromoted(promoted);
        lawsuit.setPromotedLawier(promotedLawier);
        lawsuit.setPromoting(promoting);
        lawsuit.setPromotingLawier(promotingLawier);
        lawsuit.setJudge(judge);
        session.beginTransaction();
        session.save(lawsuit);
        session.getTransaction().commit();
        System.out.println("Success.");
        
    }
    
    private static void insertPhase() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = (User) session.get(User.class, 1);
        Lawsuit lawsuit = (Lawsuit) session.get(Lawsuit.class, 1);
        
        Phase phase = new Phase();
        phase.setTitle("Lorem ipsum");
        phase.setDescription("HEHEXD Lorem Ipsum Desciption");
        phase.setAttachment("Lorem Ipsum Attachment");
        phase.setCreatedAt(new Date());
        phase.setType(1);
        phase.setStatus(1);
        phase.setUser(user);
        phase.setLawsuit(lawsuit);
        
        session.beginTransaction();
        session.save(phase);
        session.getTransaction().commit();
        System.out.println("Success.");
        
    }
    
}
