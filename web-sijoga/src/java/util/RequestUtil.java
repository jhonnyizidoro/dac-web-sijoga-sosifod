package util;

import java.util.Map;
import javax.faces.context.FacesContext;

public class RequestUtil {
    
    public static String getParam(String key){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        String param = paramMap.get(key);
        return param;
    }
    
}
