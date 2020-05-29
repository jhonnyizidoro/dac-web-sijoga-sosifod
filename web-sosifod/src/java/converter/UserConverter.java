package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.User;
import org.hibernate.Session;
import util.HibernateUtil;

@FacesConverter(value="userConverter", forClass=User.class)
public class UserConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && !value.equals("")) {
            int id = Integer.parseInt(value);
            Session session = HibernateUtil.getSessionFactory().openSession();
            User user = (User) session.get(User.class, id);
            return user;
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof User) {
            User user = (User) value;
            return String.valueOf(user.getId());
        }
        return null;
    }
    
}
