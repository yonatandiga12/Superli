package Presentation.WebPresentation.Screens.ViewModels.Transport.Document;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Transport_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;
import Presentation.WebPresentation.Screens.ViewModels.Transport.TransportMainMenu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class DocumentManagementMenu extends Screen {
    private static final String greet = "DocumentManagement Menu";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));

    public DocumentManagementMenu() {
        super(greet, ALLOWED);
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp,new String[] {"Document ID"},new String[]{"Document ID"},new String[]{"Transport Document Print", "Destination Document Print","Exit"});
        String info = getParamVal(req,"info");
        if(info!=null){
            PrintWriter out = resp.getWriter();
            for (String val:split(info)) {
                out.println("<h4>");
                out.println(val);
                out.println("</h4>");
            }
        }
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if(isButtonPressed(req,"Transport Document Print")){
            try {
                int id = Integer.parseInt(req.getParameter("Document ID"));
                String info =controller.getTransportDocument(id).webDisplay();
                refresh(req,resp,new String[]{"info"},new String[]{info});
            }
            catch (NumberFormatException e){
                setError("please enter valid number");
            } catch (Exception e) {
                setError(e.getMessage());
                refresh(req,resp);
            }
        }
        else{
            if(isButtonPressed(req,"Destination Document Print")){
                try {
                    int id = Integer.parseInt(req.getParameter("Document ID"));
                    String info =controller.getDestinationDocument(id).webDisplay();
                    refresh(req,resp,new String[]{"info"},new String[]{info});
                }
                catch (NumberFormatException e){
                    setError("please enter valid number");
                } catch (Exception e) {
                    setError(e.getMessage());
                    refresh(req,resp);
                }
            }
            else{
                if(isButtonPressed(req,"Exit")){
                    redirect(resp, TransportMainMenu.class);
                }
                else {
                    refresh(req,resp);
                }
            }
        }
    }
    public List<String> split(String s){
        List<String> l = new ArrayList<>();
        while (s.indexOf("/")!=-1){
            int index = s.indexOf("/");
            l.add(s.substring(0,index));
            s=s.substring(index+1);
        }
        return l;
    }
}
