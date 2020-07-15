package mb;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import model.Lawsuit;
import model.Phase;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;
import util.RequestUtil;
import util.SessionUtil;

@ManagedBean
@ViewScoped
public class LawsuitMB {
    private Lawsuit lawsuit;
    private Phase phase = new Phase();
    
    @PostConstruct
    public void init() {
        String id = RequestUtil.getParam("id");
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Lawsuit currentLawsuit = (Lawsuit) session.get(Lawsuit.class, Integer.parseInt(id));
        this.lawsuit = currentLawsuit;
        SessionUtil.setItem("lawsuit", currentLawsuit);
        session.getTransaction().commit();
    }
    
    public Lawsuit getLawsuit() {
        return lawsuit;
    }

    public void setLawsuit(Lawsuit lawsuit) {
        this.lawsuit = lawsuit;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
  
    public String newPhase() {
        User currentUser = (User) SessionUtil.getItem("user");
        Lawsuit phaseLawsuit = (Lawsuit) SessionUtil.getItem("lawsuit");
        
        this.phase.setCreatedAt(new Date());
        this.phase.setUser(currentUser);
        this.phase.setLawsuit(phaseLawsuit);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.phase);
        session.getTransaction().commit();
        
        return "lawsuits";
    }
    
    public String acceptPhase() {
        User currentUser = (User) SessionUtil.getItem("user");
        Lawsuit phaseLawsuit = (Lawsuit) SessionUtil.getItem("lawsuit");
               
        this.phase.setCreatedAt(new Date());
        this.phase.setUser(currentUser);
        this.phase.setLawsuit(phaseLawsuit);
        this.phase.setTitle("Pedido aceito pelo Juiz");
        this.phase.setDescription("Pedido aceito pelo Juiz");
        this.phase.setType(1);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.phase);
        session.getTransaction().commit();
        
        return "lawsuits";
    }
    
    public String denyPhase() {
        User currentUser = (User) SessionUtil.getItem("user");
        Lawsuit phaseLawsuit = (Lawsuit) SessionUtil.getItem("lawsuit");
               
        this.phase.setCreatedAt(new Date());
        this.phase.setUser(currentUser);
        this.phase.setLawsuit(phaseLawsuit);
        this.phase.setTitle("Pedido negado pelo Juiz");
        this.phase.setType(1);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(this.phase);
        session.getTransaction().commit();
        
        return "lawsuits";
    }
    
}
