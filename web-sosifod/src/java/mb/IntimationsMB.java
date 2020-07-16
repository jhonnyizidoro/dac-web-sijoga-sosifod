package mb;


import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import model.Intimation;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HashUtil;
import util.HibernateUtil;
import util.RequestUtil;
import util.SessionUtil;


@ManagedBean
@ViewScoped
public class IntimationsMB implements Serializable {
    private User officer = new User();
    private Intimation intimation = new Intimation();
    private List<Intimation> intimations;
    private List<User> officers;
    
    @PostConstruct
    public void init() {
        User currentUser = (User) SessionUtil.getItem("user");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query intimationsQuery = session.createQuery("FROM Intimation WHERE officer = :user");
        intimationsQuery.setParameter("user", currentUser);
        Query officersQuery = session.createQuery("FROM User WHERE profile = 1");
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
        this.officer.setPassword(HashUtil.hash("123456"));
        this.officer.setProfile(1);
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.officer);
        session.getTransaction().commit();
        this.init();
    }
    
    public void insertIntimation() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        this.intimation.setCreatedAt(new Date());
        session.save(this.intimation);
        session.getTransaction().commit();
        this.init();
    }
    
    public void concludeIntimation(Intimation intimation) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        intimation.setConcludedAt(new Date());
        session.update(intimation);
        session.getTransaction().commit();
        
        if (intimation.getOrigin() == 1) {
            Client client = ClientBuilder.newClient();
            String res = client.target("http://localhost:8080/web-sijoga/webresources/intimations?name=" + RequestUtil.encodeURL(intimation.getName()) + "&id=" + RequestUtil.encodeURL(intimation.getLawsuit()))
                    .request(MediaType.TEXT_PLAIN)
                    .get(String.class);
        }
        
        this.init();
    }
    
}
