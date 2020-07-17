package servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@WebServlet(name = "report", urlPatterns = {"/report"})
public class ReportServlet extends HttpServlet {
    
    private static String formatDate(String date) {
        String[] arr = date.split("/");
        return arr[2] + "-" + arr[1] + "-" + arr[0];
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        Connection con = null;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sijoga?useSSL=false","root", "root");
            String jasper = request.getContextPath() + "/report.jasper";
            String host = "http://" + request.getServerName() + ":" + request.getServerPort();
            URL jasperURL = new URL(host + jasper);
            HashMap params = new HashMap();
                        
            Date dateFrom = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("from"));
            Date dateUntil = new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("until"));
            int status =  Integer.valueOf(request.getParameter("status"));

            if (status == 0) {
                params.put("statusFrom", 0);
                params.put("statusUntil", 0);
            } else {
                params.put("statusFrom", 1);
                params.put("statusUntil", 9999999);
            }
              
            params.put("dateFrom", dateFrom); 
            params.put("dateUntil", dateUntil);
            
            byte[] bytes = JasperRunManager.runReportToPdf(jasperURL.openStream(), params, con);
            
            if (bytes != null) {
                response.setContentType("application/pdf");
                OutputStream ops = response.getOutputStream();
                ops.write(bytes);
            }
        } catch (ClassNotFoundException e) {
            request.setAttribute("mensagem", "Driver BD não encontrado : " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        } catch(SQLException e) {
            request.setAttribute("mensagem", "Erro de conexão ou query: " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        } catch(IOException | JRException e) {
            request.setAttribute("mensagem", "Erro no Jasper : " + e.getMessage());
            request.getRequestDispatcher("erro.jsp").forward(request, response);
        } catch (ParseException ex) {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (con!=null) {
                try {
                    con.close();
                } catch(SQLException e) {
                    System.out.println("Erro ao fechar a connection!");
                }
            }
        }
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
