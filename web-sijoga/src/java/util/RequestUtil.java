package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

public class RequestUtil {
    
    public static String getParam(String key){
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        String param = paramMap.get(key);
        return param;
    }
    
    public static String uploadFile(Part file) {
        
        try {
            InputStream input = file.getInputStream();
            String fileName = file.getSubmittedFileName();
            File fileToCopy = new File("C:\\Users\\jhonn\\Documents\\NetBeansProjects\\web-sijoga\\web\\resources\\uploads", fileName);
            Files.copy(input, fileToCopy.toPath());
            return fileName;
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        
        return null;
    }
    
    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.toString());
        }
        return null;
    }
        
}
