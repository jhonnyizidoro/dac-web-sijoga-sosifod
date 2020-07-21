package mb;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.Part;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import model.Lawsuit;
import model.Phase;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;
import util.RequestUtil;
import util.SessionUtil;

@ManagedBean
@ViewScoped
public class LawsuitMB implements Serializable {
    private Lawsuit lawsuit;
    private Phase phase = new Phase();
    private Part file;
    private String[] officers;
    private Map<String, String> intimation = new HashMap();
    private String note;
    private int winningPart;
    
    @PostConstruct
    public void init() {
        this.loadOfficers();
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

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String[] getOfficers() {
        return officers;
    }

    public void setOfficers(String[] officers) {
        this.officers = officers;
    }

    public Map<String, String> getIntimation() {
        return intimation;
    }

    public void setIntimation(Map<String, String> intimation) {
        this.intimation = intimation;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getWinningPart() {
        return winningPart;
    }

    public void setWinningPart(int winningPart) {
        this.winningPart = winningPart;
    }
  
    public String newPhase() {
        User currentUser = (User) SessionUtil.getItem("user");
        Lawsuit phaseLawsuit = (Lawsuit) SessionUtil.getItem("lawsuit");
        
        this.phase.setCreatedAt(new Date());
        this.phase.setUser(currentUser);
        this.phase.setLawsuit(phaseLawsuit);
        this.phase.setAttachment(RequestUtil.uploadFile(this.file));
        
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
    
    public String intimate() {
        Lawsuit intimationLawsuit = (Lawsuit) SessionUtil.getItem("lawsuit");
        User currentUser = (User) SessionUtil.getItem("user");
        
        Client client = ClientBuilder.newClient();
        String res = client.target("http://localhost:8080/web-sosifod/webresources/intimations/save?name=" + RequestUtil.encodeURL(this.intimation.get("name")) + "&cpf=" + RequestUtil.encodeURL(this.intimation.get("cpf")) + "&address=" + RequestUtil.encodeURL(this.intimation.get("address")) + "&officer=" + RequestUtil.encodeURL(this.intimation.get("officer")) + "&lawsuit=" + intimationLawsuit.getId())
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        
        Phase intimationPhase = new Phase();
        intimationPhase.setCreatedAt(new Date());
        intimationPhase.setUser(currentUser);
        intimationPhase.setLawsuit(intimationLawsuit);
        intimationPhase.setTitle("Intimação realizada pelo Juiz");
        intimationPhase.setDescription("Nome: "+this.intimation.get("name")+", CPF: "+this.intimation.get("cpf")+", Endereço: "+this.intimation.get("address"));
        intimationPhase.setType(1);
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(intimationPhase);
        session.getTransaction().commit();
        
        return "lawsuits";
    }
    
    public void loadOfficers() {
        Client client = ClientBuilder.newClient();
        String res = client.target("http://localhost:8080/web-sosifod/webresources/intimations")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        this.officers = res.split(",");
    }
    
    public String endLawsuit() {
        Lawsuit currentLawsuit = (Lawsuit) SessionUtil.getItem("lawsuit");
        
        currentLawsuit.setStatus(this.winningPart);
        currentLawsuit.setNote(this.note);
        
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        session.merge(currentLawsuit);
        session.getTransaction().commit();
        return "lawsuits";
    }
    
}
