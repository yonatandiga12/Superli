package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmployeesMenu extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Welcome to the Employees Management Menu";

    private static final String[] MENU_OPTIONS = {
            "View Employees",       //0
            "Register Employee",    //1
            "Manage Employee",      //2
            "Remove Employee"       //3
    };

    public EmployeesMenu() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
            return;
        }
        header(resp);
        greet(resp);
        printMenu(resp, MENU_OPTIONS);
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        switch (getIndexOfButtonPressed(req)){
            case 0:
                redirect(resp, ViewEmployee.class);
                break;
            case 1:
                redirect(resp, RegisterEmployee.class);
                break;
            case 2:
                redirect(resp, ManageEmployee.class);
                break;
            case 3:
                redirect(resp, RemoveEmployee.class);
                break;
        }
    }
}
