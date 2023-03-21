package Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee;

import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.EmployeeFactory;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EmploymentConds extends Screen {
    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>();

    private static final Set<Class<? extends Employee>> ALLOWED_TO_WATCH_OTHERS
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "";

    public EmploymentConds() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
            return;
        }
        String givenID = getParamVal(req, "EmpID");
        Employee employee = Login.getLoggedUser(req);
        if (givenID != null) {
            if (givenID.equals(employee.id) || !isAllowed(req, resp, ALLOWED_TO_WATCH_OTHERS)) {
                refresh(req, resp);
                return;
            }
            try {
                employee = new EmployeeFactory().createEmployee(controller.getEmployee(givenID));
            } catch (Exception e) {
                setError(e.getMessage());
            }
        }
        else if (employee instanceof Admin){
            redirect(resp, Login.class);
            return;
        }

        header(resp);
        if (!isError()) {
            try {
                PrintWriter out = resp.getWriter();
                out.println(String.format("<h1>%s's Employment Conditions</h1><br><br>", employee.getName()));
                out.println("<p>");
                out.println(controller.getEmploymentConditionsOf(employee.id).replaceAll("\n", "<br>"));
                out.println("</p>");
            } catch (Exception e) {
                setError(e.getMessage());
            }
        }
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
    }
}
