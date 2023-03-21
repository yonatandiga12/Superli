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
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RemoveEmployee extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Employee Removal";

    public RemoveEmployee() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
            return;
        }
        header(resp);
        greet(resp);
        PrintWriter out = resp.getWriter();
        String givenID;
        if ((givenID = getParamVal(req, "eid")) == null)
            givenID = "";
        out.println("<form method=\"post\">");
        out.println("<p>Enter ID of employee to remove: </p>");
        out.println(String.format("<input type=\"text\" name=\"empID\" placeholder=\"Employee ID\" value=\"%s\"><br><br>", givenID));
        out.println("<input type=\"submit\" name=\"remove\" value=\"Remove\"><br><br>");
        if (!givenID.equals("")){
            if (givenID.equals(Login.getLoggedUser(req).id)) {
                setError("Can't remove yourself!");
            }
            else {
                try {
                    Domain.Service.Objects.Employee.Employee sEmp = controller.getEmployee(givenID);
                    out.println(String.format("<p>Are you sure you want to remove %s(ID - %s) from the system?</p>", sEmp.name, sEmp.id));
                    out.println("<p style=\"color:red\">Mind you that this process is irreversible!</p>");
                    out.println("<input type=\"submit\" name=\"sure\" value=\"Yes!\"><br><br>");
                } catch (Exception e) {
                    setError(e.getMessage());
                }
            }
        }
        out.println("</form>");
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, "remove")) {
            refresh(req, resp, new String[]{"eid"}, new String[]{getParamVal(req, "empID")});
            return;
        }
        if (isButtonPressed(req, "sure")) {
            try {
                Domain.Service.Objects.Employee.Employee sEmp = controller.getEmployee(getParamVal(req, "empID"));
                Login.removeUser(sEmp.id);
                controller.removeEmployee(sEmp.id);
                setError(String.format("Successfully removed %s(ID - %s) from the system!", sEmp.name, sEmp.id));
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp);
        }
    }
}
