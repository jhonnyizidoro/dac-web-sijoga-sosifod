package mb;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import model.User;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HashUtil;
import util.HibernateUtil;
import util.SessionUtil;

@Named
@SessionScoped
public class LoginMB implements Serializable {
    private User user = new User();

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
   
    public String login() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM User WHERE cpf = :cpf AND password = :password");
        query.setParameter("cpf", this.user.getCpf());
        query.setParameter("password", HashUtil.hash(this.user.getPassword()));
        List<User> users = query.list();
        boolean userExists = !users.isEmpty();
        session.getTransaction().commit();
        if (userExists) {
            this.user = users.get(0);
            SessionUtil.setItem("user", this.user);
            return "lawsuits";
        } else {
            return "index";
        }
    }
    
}
