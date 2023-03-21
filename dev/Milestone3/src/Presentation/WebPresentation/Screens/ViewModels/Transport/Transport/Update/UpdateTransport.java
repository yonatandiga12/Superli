package Presentation.WebPresentation.Screens.ViewModels.Transport.Transport.Update;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Transport_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Transport.TransportManagementMenu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UpdateTransport extends Screen {
    private static final String greet = "Update Transport Menu";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    private static final String[] forumOptions = {
            "Place truck",                  //1
            "Place carrier",                //2
            "Start transport",              //3
            "View orders",                  //4
            "Add order",                    //5
            "Exit"                          //6
    };
    public UpdateTransport() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp,new String[] {"Transport ID"},new String[] {"Transport ID"},forumOptions);
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        String id = getParamVal(req, "Transport ID");
        if (isButtonPressed(req, "Exit")) {
            redirect(resp,TransportManagementMenu.class);
        }
        else if (isButtonPressed(req, "View orders")) {
            redirect(resp, ViewPendingOrders.class);
        }
        else if (!is_number(id)) {
            refresh(req, resp);
        }
        else  if (isButtonPressed(req, "Place truck")) {
            redirect(resp, PlaceTruck.class, new String[]{"ID"}, new String[]{id});
        }
        else if (isButtonPressed(req, "Place carrier")) {
            redirect(resp,PlaceCarrier.class, new String[]{"ID"}, new String[]{id});
        }
        else if (isButtonPressed(req, "Start transport")) {
            handelStart(id, req, resp);
        }
        else if (isButtonPressed(req, "View orders")) {
            if (is_number(id)) {
                redirect(resp, ViewPendingOrders.class, new String[]{"ID"}, new String[]{id});
            }
        }
        else if (isButtonPressed(req, "Add order")) {
            if (is_number(id)) {
                redirect(resp, AddOrderToTransport.class, new String[]{"ID"}, new String[]{id});
            } else {
                refresh(req, resp);
            }
        }
        else {
            setError("Failure!");
        }


    }


    public void handelStart(String id,HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            if(is_number(id)){
                int transportId= Integer.parseInt(id);
                controller.startTransport(transportId);
                setError("Transport " + id + " started successfully");
            }
        } catch (Exception e) {
            setError(e.getMessage());
        }
        refresh(req,resp);
    }
    public boolean is_number(String id){
        try{
            int transportId = Integer.parseInt(id);
            return true;
        }
        catch (NumberFormatException e){
            setError("Please Enter a valid id");
            return false;
        }
    }
}
