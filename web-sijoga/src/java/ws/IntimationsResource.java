package ws;

import java.util.Date;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import model.Lawsuit;
import model.Phase;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;

@Path("intimations")
public class IntimationsResource {

    @GET
    @Produces("text/plain")
    public String cincludeIntimation(
            @QueryParam("id") int id,
            @QueryParam("name") String name) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        Lawsuit lawsuit = (Lawsuit) session.get(Lawsuit.class, id);
        User user = (User) session.get(User.class, 1);
        
        
        Phase intimationPhase = new Phase();
        intimationPhase.setCreatedAt(new Date());
        intimationPhase.setUser(user);
        intimationPhase.setLawsuit(lawsuit);
        intimationPhase.setTitle("Intimação efetivada pelo sistema SOSIFOD!");
        intimationPhase.setDescription("Nome: " + name);
        intimationPhase.setType(1);
        
        session.save(intimationPhase);
        
        session.getTransaction().commit();
        
        return "true";
    }

}
