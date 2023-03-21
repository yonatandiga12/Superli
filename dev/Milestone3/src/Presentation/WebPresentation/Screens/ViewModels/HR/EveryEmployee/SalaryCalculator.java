package Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee;

import Globals.util.HumanInteraction;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SalaryCalculator extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>();

    private static final Set<Class<? extends Employee>> ALLOWED_TO_WATCH_OTHERS
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Salary Calculator";

    public SalaryCalculator() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
            return;
        }

        String givenID = getParamVal(req, "EmpID");
        Employee emp = Login.getLoggedUser(req);
        if (givenID != null) {
            if (givenID.equals(emp.id) || !isAllowed(req, resp, ALLOWED_TO_WATCH_OTHERS)) {
                refresh(req, resp);
                return;
            }
            try {
                emp = new EmployeeFactory().createEmployee(controller.getEmployee(givenID));
            } catch (Exception e) {
                setError(e.getMessage());
            }
        }
        else if (emp instanceof Admin){
            redirect(resp, Login.class);
            return;
        }

        header(resp);
        greet(resp);
        PrintWriter out = resp.getWriter();
        out.println(String.format("<h3>Calculating salary for %s(ID - %s)</h3>", emp.getName(), emp.id));

        String val;
        LocalDate start = LocalDate.now(), end = LocalDate.now();
        boolean startGiven = false, endGiven = false;
        if (startGiven = ((val = getParamVal(req, "start")) != null)) {
            start = LocalDate.parse(val);
        }
        if (endGiven = ((val = getParamVal(req, "end")) != null)) {
            end = LocalDate.parse(val);
        }
        out.println("<form method=\"post\">");
        out.println("<p>Enter start date to calculate from: </p>");
        out.println(String.format("<input type=\"date\" name=\"startIn\" value=\"%s\"><br><br>", start));
        out.println("<p>Enter end date to calculate to: </p>");
        out.println(String.format("<input type=\"date\" name=\"endIn\" value=\"%s\"><br><br>", end));
        out.println("<input type=\"submit\" name=\"calculate\" value=\"calculate\"><br><br>");
        out.println("</form>");

        if (startGiven && endGiven && !isError()) {
            int numOfShifts = 0;
            try {
                numOfShifts = controller.getEmployeeShiftsBetween(emp.id, start, end).size();
                out.println("<p>");
                out.println(String.format("Between %s and %s, %s has done %s shifts",
                        start.format(HumanInteraction.dateFormat), end.format(HumanInteraction.dateFormat), emp.getName(), numOfShifts));
                out.println("<br>");
                out.println(String.format("With a salary of %s per shift", emp.getSalary()));
                out.println("<br>");
                out.println(String.format("Calculated salary between these dates is: %s ", (numOfShifts * emp.getSalary())));
                out.println("</p><br><br>");
            } catch (Exception e) {
                setError(e.getMessage());
            }
        }

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, "calculate"))
            if (getParamVal(req, "EmpID") != null)
                refresh(req, resp, new String[]{"start", "end", "EmpID"}, new String[]{req.getParameter("startIn"), req.getParameter("endIn"), req.getParameter("EmpID")});
            else
                refresh(req, resp, new String[]{"start", "end"}, new String[]{req.getParameter("startIn"), req.getParameter("endIn")});
    }
}
