package mb;


import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Intimation;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;


@Named
@RequestScoped
public class IntimationsMB {
    private User officer = new User();
    private Intimation intimation = new Intimation();
    private List<Intimation> intimations;
    private List<User> officers;
    
    @PostConstruct
    public void init() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query intimationsQuery = session.createQuery("FROM Intimation");
        Query officersQuery = session.createQuery("FROM User WHERE profile = 0");
        this.intimations = intimationsQuery.list();
        this.officers = officersQuery.list();
        session.getTransaction().commit();
    }

    public User getOfficer() {
        return officer;
    }

    public void setOfficer(User officer) {
        this.officer = officer;
    }

    public Intimation getIntimation() {
        return intimation;
    }

    public void setIntimation(Intimation intimation) {
        this.intimation = intimation;
    }

    public List<Intimation> getIntimations() {
        return intimations;
    }

    public void setIntimations(List<Intimation> intimations) {
        this.intimations = intimations;
    }

    public List<User> getOfficers() {
        return officers;
    }

    public void setOfficers(List<User> officers) {
        this.officers = officers;
    }

    public void insertOfficer() {
        this.officer.setPassword("123456");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.officer);
        session.getTransaction().commit();
    }
    
    public void insertIntimation() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        this.intimation.setCreatedAt(new Date());
        session.save(this.intimation);
        session.getTransaction().commit();
    }
    
        public void concludeIntimation(Intimation intimation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        intimation.setConcludedAt(new Date());
        session.update(intimation);
        session.getTransaction().commit();
    }
    
}
