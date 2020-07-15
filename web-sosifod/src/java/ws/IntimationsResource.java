package ws;

import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import model.Intimation;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

@Path("intimations")
public class IntimationsResource {

    @GET
    @Path("save")
    @Produces("text/plain")
    public String saveIntimation(
            @QueryParam("name") String name,
            @QueryParam("cpf") String cpf,
            @QueryParam("address") String address,
            @QueryParam("officer") String officer,
            @QueryParam("lawsuit") String lawsuit) {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("From User where name = :officer");
        query.setString("officer", officer);
        User intimationOfficer = (User) query.list().get(0);
                
        Intimation intimation = new Intimation();
        intimation.setName(name);
        intimation.setCpf(cpf);
        intimation.setAddress(address);
        intimation.setLawsuit(lawsuit);
        intimation.setCreatedAt(new Date());
        intimation.setOfficer(intimationOfficer);
        intimation.setOrigin(1);
        
        session.save(intimation);
        session.getTransaction().commit();
        
        return "true";
    }
    
    @GET
    @Produces("text/plain")
    public String listOfficers() {
        String res = "";
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("From User where profile = 1");
        List<User> officers = query.list();
        session.getTransaction().commit();
        for (User user : officers) {
            res += user.getName() + ",";
        }
        res = res.substring(0, res.length() - 1);
        return res;
    }
}
