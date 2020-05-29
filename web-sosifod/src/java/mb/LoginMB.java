package mb;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

@Named
@SessionScoped
public class LoginMB implements Serializable {
    private String cpf;
    private String password;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }   
    
    public String login() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM User WHERE cpf = :cpf AND password = :password");
        query.setParameter("cpf", this.cpf);
        query.setParameter("password", this.password);
        boolean userExists = !query.list().isEmpty();
        session.getTransaction().commit();
        if (userExists) {
            return "intimations";
        } else {
            return "index";
        }
    }
    
}
