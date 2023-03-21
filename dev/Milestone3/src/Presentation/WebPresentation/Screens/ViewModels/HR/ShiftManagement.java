package Presentation.WebPresentation.Screens.ViewModels.HR;

import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Screen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ShiftManagement extends Screen {

    private static final String GREET = "Welcome to the Shift Management Menu!";

    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Admin.class));

    public ShiftManagement() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
    }
}
