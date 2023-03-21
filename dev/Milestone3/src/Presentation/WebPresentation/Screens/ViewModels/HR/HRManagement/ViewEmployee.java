package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Globals.Enums.JobTitles;
import Globals.util.EmployeeComparator;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewEmployee extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "View Employees";

    public ViewEmployee() {
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

        PrintWriter out = resp.getWriter();
        out.println("<form>");
        out.println("<label>Choose type of employee to view: </label>");
        out.println("<select name=\"title\">");
        out.println("<option value=\"all\">All</option>");
        for (JobTitles title : JobTitles.values())
            out.println(String.format("<option value=\"%s\">%s</option>", title.toString().toLowerCase(), title.toString().replaceAll("_", " ")));
        out.println("</select><br><br>");
        out.println("<input type=\"submit\" name=\"view\" value=\"View Employees\">");
        out.println("</form><br><br>");

        if (getParamVal(req, "title") != null) {
            List<Domain.Service.Objects.Employee.Employee> sEmps = null;
            try {
                sEmps = getEmployees(getParamVal(req, "title"));
                out.println("<hr>");
                out.println("<h3>Click on an employee in order to manage it</h3>");
                printMenu(resp, sEmps.stream().map(e -> String.format("ID: %s, Name: %s, Title: %s", e.id, e.name, e.getType())).toArray(String[]::new));
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
        try {
            redirect(resp, ManageEmployee.class, new String[]{"EmpID"},
                    new String[]{getEmployees(getParamVal(req, "title")).get(getIndexOfButtonPressed(req)).id});
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }
    }

    private static List<Domain.Service.Objects.Employee.Employee> getEmployees(String type) throws Exception {
        switch (type) {
            case "all":
                return controller.getAllEmployees().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "carrier":
                return controller.getAllCarriers().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "cashier":
                return controller.getAllCashiers().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "storekeeper":
                return controller.getAllStorekeepers().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "sorter":
                return controller.getAllSorters().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "hr_manager":
                return controller.getAllHR_Managers().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "logistics_manager":
                return controller.getAllLogistics_Managers().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
            case "transport_manager":
                return controller.getAllTransport_Managers().stream().sorted(new EmployeeComparator()).collect(Collectors.toList());
        }
        throw  new Exception("Error occurred: illegal title given");
    }
}
