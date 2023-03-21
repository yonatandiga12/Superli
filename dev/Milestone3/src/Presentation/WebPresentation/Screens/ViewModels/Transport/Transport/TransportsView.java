package Presentation.WebPresentation.Screens.ViewModels.Transport.Transport;


import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Transport_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Transport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TransportsView extends Screen {
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    public TransportsView() {
        super("", ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        String val;
        if((val = getParamVal(req,"Pending"))!=null && val.equals("true")){
            PrintWriter out = resp.getWriter();
            out.println("<h1> View Pending Transports </h1><br><br>");
            try{
                Set<Transport> pending = controller.getPendingTransports();
                printTransport(pending,resp);
            }
            catch (Exception e){
                setError("failure");
            }
        }
        else{
            if((val = getParamVal(req,"In Progress"))!=null && val.equals("true")){
                PrintWriter out = resp.getWriter();
                out.println("<h1> View In Progress Transports </h1><br><br>");
                try{
                    Set<Transport> inProgress = controller.getInProgressTransports();
                    printTransport(inProgress,resp);
                }
                catch (Exception e){
                    setError("failure");
                }

            }
            else{
                if((val = getParamVal(req,"Done"))!=null && val.equals("true")){
                    PrintWriter out = resp.getWriter();
                    out.println("<h1> View Done Transports </h1><br><br>");
                    try{
                        Set<Transport> inProgress = controller.getCompleteTransports();
                        printTransport(inProgress,resp);
                    }
                    catch (Exception e){
                        setError("failure");
                    }
                }
                else{
                    setError("invalid request");
                }
            }
        }
        printMenu(resp,new String[]{"Exit"} );
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req,resp);
        if(getIndexOfButtonPressed(req) == 0){
            redirect(resp,TransportManagementMenu.class);
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
    public void printTransport(Set<Transport> lst,HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        if (lst.size()==0){
            out.println("<h2>" + "There is no transports in this status" + "</h2>");
        }
        List<String> all = new ArrayList<>();
        for (Transport t: lst){
            all.add(t.displayWeb());
        }
        for(String s1 : all){
            for(String s2 :split(s1)){
                out.println("<h4>" + s2 + "</h4>");
            }
            out.println("<br>");
        }

    }
}
