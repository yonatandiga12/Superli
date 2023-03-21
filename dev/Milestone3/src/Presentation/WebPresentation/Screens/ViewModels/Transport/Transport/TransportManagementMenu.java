package Presentation.WebPresentation.Screens.ViewModels.Transport.Transport;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Transport_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Transport.Update.UpdateTransport;
import Presentation.WebPresentation.Screens.ViewModels.Transport.TransportMainMenu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TransportManagementMenu extends Screen {
    private static final String greet = "Transport Management Menu";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    private static final String[] menuOptions = {
            "Create new transport",         //1
            "Update transport",             //2
            "Get pending transport",        //3
            "Get in progress transport",    //4
            "Get complete transport",       //5
            "Exit"                          //6
    };
    public TransportManagementMenu() {
        super(greet, ALLOWED);
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printMenu(resp, menuOptions);
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        switch (getIndexOfButtonPressed(req)) {
            case 0:
                redirect(resp, CreateTransport.class);
                break;
            case 1:
                redirect(resp, UpdateTransport.class);
                break;
            case 2:
                redirect(resp,TransportsView.class,new String[]{"Pending"},new String[]{"true"});
                break;
            case 3:
                redirect(resp,TransportsView.class,new String[]{"In Progress"},new String[]{"true"});
                break;
            case 4:
                redirect(resp,TransportsView.class,new String[]{"Done"},new String[]{"true"});
                break;
            case 5://Exit
                redirect(resp, TransportMainMenu.class);
                break;
            default:
                refresh(req,resp);
        }
    }


}
