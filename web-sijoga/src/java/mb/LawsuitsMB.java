package mb;

import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Lawsuit;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HashUtil;
import util.HibernateUtil;
import util.SessionUtil;

@ManagedBean
@ViewScoped
public class LawsuitsMB {
    private List<Lawsuit> lawsuits;
    private List<User> parts;
    private Lawsuit Lawsuit = new Lawsuit();
    private User part = new User();
    
    @PostConstruct
    public void init() {
        User currentUser = (User) SessionUtil.getItem("user");
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query lawsuitsQuery = session.createQuery("FROM Lawsuit WHERE promoted = :user OR promoting = :user OR promotedLawier = :user OR promotingLawier = :user OR judge = :user");
        lawsuitsQuery.setParameter("user", currentUser);
        Query partsQuery = session.createQuery("FROM User WHERE profile = 1");
        
        this.parts = partsQuery.list();
        this.lawsuits = lawsuitsQuery.list();
        session.getTransaction().commit();
    }

    public List<Lawsuit> getLawsuits() {
        return lawsuits;
    }

    public void setLawsuits(List<Lawsuit> lawsuits) {
        this.lawsuits = lawsuits;
    }

    public List<User> getParts() {
        return parts;
    }

    public void setParts(List<User> parts) {
        this.parts = parts;
    }

    public Lawsuit getLawsuit() {
        return Lawsuit;
    }

    public void setLawsuit(Lawsuit Lawsuit) {
        this.Lawsuit = Lawsuit;
    }

    public User getPart() {
        return part;
    }

    public void setPart(User part) {
        this.part = part;
    }
    
    public void insertPart() {
        this.part.setPassword(HashUtil.hash("123456"));
        this.part.setProfile(1);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.part);
        session.getTransaction().commit();
        this.init();
    }
    
    public void insertLawsuit() {
        User promotingLawier = (User) SessionUtil.getItem("user");
        this.Lawsuit.setPromotingLawier(promotingLawier);
        this.Lawsuit.setCreatedAt(new Date());
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query promotedLawierQuery = session.createQuery("FROM User WHERE profile = 2 AND id <> :id");
        Query judgeQuery = session.createQuery("FROM User WHERE profile = 3");
        promotedLawierQuery.setParameter("id", promotingLawier.getId());
        
        User promotedLawier = (User) promotedLawierQuery.list().get(0);
        User judge = (User) judgeQuery.list().get(0);
        this.Lawsuit.setPromotedLawier(promotedLawier);
        this.Lawsuit.setJudge(judge);
        
        session.save(this.Lawsuit);
        session.getTransaction().commit();
        
        this.init();
    }
    
}
