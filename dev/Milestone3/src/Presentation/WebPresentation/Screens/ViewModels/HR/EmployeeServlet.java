package Presentation.WebPresentation.Screens.ViewModels.HR;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Screen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmployeeServlet extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList());

    public EmployeeServlet() {
        super(null, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
            return;
        }
        Employee employee = Login.getLoggedUser(req);
        employee.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        Employee emp = Login.getLoggedUser(req);
        try {
            emp.doPost(req, resp);
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }
    }
}
