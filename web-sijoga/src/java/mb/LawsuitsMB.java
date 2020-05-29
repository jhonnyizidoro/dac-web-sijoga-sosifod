package mb;

import java.util.Date;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import model.Lawsuit;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

@Named
@RequestScoped
public class LawsuitsMB {
    private List<Lawsuit> lawsuits;
    private List<User> parts;
    private Lawsuit Lawsuit = new Lawsuit();
    private User part = new User();
    
    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query lawsuitsQuery = session.createQuery("FROM Lawsuit");
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
        this.part.setPassword("123456");
        this.part.setProfile(1);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.part);
        session.getTransaction().commit();
    }
    
    public void insertLawsuit() {
//        System.out.println(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user"));
//        this.Lawsuit.setCreatedAt(new Date());
//        this.Lawsuit.setPromotingLawier((User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("authenticatedUser"));
    }
    
}
