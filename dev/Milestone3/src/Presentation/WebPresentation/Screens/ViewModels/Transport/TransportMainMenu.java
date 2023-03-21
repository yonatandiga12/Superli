package Presentation.WebPresentation.Screens.ViewModels.Transport;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Transport_Manager;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Document.DocumentManagementMenu;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Transport.TransportManagementMenu;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Truck.TruckManagementMenu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TransportMainMenu extends Screen {
    private static final String greet = "Transport's Main Menu";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));

    public TransportMainMenu() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printMenu(resp, new String[]{"Transport Manage", "Truck Management", "Document Management"});
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        switch (getIndexOfButtonPressed(req)) {
            case 0:
                redirect(resp, TransportManagementMenu.class);
                break;
            case 1:
                redirect(resp, TruckManagementMenu.class);
                break;
            case 2:
                redirect(resp, DocumentManagementMenu.class);
                break;
            default:
                redirect(resp, TransportMainMenu.class);
        }
    }
}
