package mb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import model.Lawsuit;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HashUtil;
import util.HibernateUtil;
import util.RequestUtil;
import util.SessionUtil;

@ManagedBean
@ViewScoped
public class LawsuitsMB implements Serializable {
    private List<Lawsuit> lawsuits;
    private List<User> parts;
    private Lawsuit Lawsuit = new Lawsuit();
    private User part = new User();
    private int searchType;
    private int reportStatus;
    private String reportDateFrom;
    private String reportDateUntil;
    
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

    public int getSearchType() {
        return searchType;
    }

    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }

    public int getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(int reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getReportDateFrom() {
        return reportDateFrom;
    }

    public void setReportDateFrom(String reportDateFrom) {
        this.reportDateFrom = reportDateFrom;
    }

    public String getReportDateUntil() {
        return reportDateUntil;
    }

    public void setReportDateUntil(String reportDateUntil) {
        this.reportDateUntil = reportDateUntil;
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
    
    public void search() {
        String lawsuitsQueryString = "FROM Lawsuit WHERE (promoted = :user OR promoting = :user OR promotedLawier = :user OR promotingLawier = :user OR judge = :user) ";
        
        User currentUser = (User) SessionUtil.getItem("user");
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        Query lawsuitsQuery;
        switch (this.searchType) {
            case 1:
                lawsuitsQuery = session.createQuery(lawsuitsQueryString);
                lawsuitsQuery.setParameter("user", currentUser);
                break;
            case 2:
                lawsuitsQuery = session.createQuery(lawsuitsQueryString + "AND status = 0");
                lawsuitsQuery.setParameter("user", currentUser);
                break;
            case 3:
                lawsuitsQuery = session.createQuery(lawsuitsQueryString + "AND status != 0");
                lawsuitsQuery.setParameter("user", currentUser);
                break;
            case 4:
            case 6:
            case 7:
                lawsuitsQuery = session.createQuery(lawsuitsQueryString + "AND (promoting = :user OR promotingLawier = :user)");
                lawsuitsQuery.setParameter("user", currentUser);
                break;
            case 5:
            case 8:
            case 9:
                lawsuitsQuery = session.createQuery(lawsuitsQueryString + "AND (promoted = :user OR promotedLawier = :user)");
                lawsuitsQuery.setParameter("user", currentUser);
                break;
            default:
                lawsuitsQuery = session.createQuery(lawsuitsQueryString);
                lawsuitsQuery.setParameter("user", currentUser);
        }
        
        this.lawsuits = lawsuitsQuery.list();
        session.getTransaction().commit();
    }
    
    public void generateReport() {
        User currentUser = (User) SessionUtil.getItem("user");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("report?status=" + this.reportStatus + "&user=" + currentUser.getId() + "&from=" + RequestUtil.encodeURL(this.reportDateFrom) + "&until=" + RequestUtil.encodeURL(this.reportDateUntil));
        } catch (IOException ex) {
            System.out.println("Erro ao redirecionar: " + ex.getMessage());
        }
    }
    
}
