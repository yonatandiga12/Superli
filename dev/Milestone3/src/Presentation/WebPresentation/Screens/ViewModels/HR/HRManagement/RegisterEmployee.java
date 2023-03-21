package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RegisterEmployee extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Employee Registration";

    public RegisterEmployee() {
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
        out.println("<form method=\"post\">");
        out.println("<p>Enter ID: </p>");
        out.println(String.format("<input type=\"text\" name=\"eid\" placeholder=\"Employee ID\"><br><br>"));
        out.println("<p>Enter name: </p>");
        out.println(String.format("<input type=\"text\" name=\"name\" placeholder=\"Employee Name\"><br><br>"));
        out.println("<p>Enter bank details: </p>");
        out.println(String.format("<input type=\"text\" name=\"bDetails\" placeholder=\"Bank Details\"><br><br>"));
        out.println("<p>Enter job title: </p>");
        out.println("<select name=\"title\">");
        for (JobTitles title : JobTitles.values())
            out.println(String.format("<option value=\"%s\">%s</option>", title, title.toString().replaceAll("_"," ")));
        out.println("</select>");
        out.println("<p>Enter starting date: </p>");
        out.println(String.format("<input type=\"date\" name=\"startDate\" value=\"" + LocalDate.now() + "\"><br><br>"));
        out.println("<p>Enter salary per shift: </p>");
        out.println(String.format("<input type=\"text\" name=\"salary\" placeholder=\"Employee salary\"><br><br>"));
        out.println("<p>Enter certification: </p>");
        out.println("<select name=\"cert\">");
        out.println(String.format("<option value=\"None\">None</option>"));
        for (Certifications cert : Certifications.values())
            out.println(String.format("<option value=\"%s\">%s</option>", cert, cert.toString().replaceAll("_", " ")));
        out.println("</select><br><br>");
        out.println(String.format("<input type=\"submit\" name=\"reg\" value=\"Register\"><br><br>"));
        out.println("</form>");

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, "reg")){
            try {
                String id = getParamVal(req, "eid");
                controller.checkUnusedEmployeeID(id);
                String name = getParamVal(req, "name");
                String bDetails = getParamVal(req, "bDetails");
                JobTitles title = JobTitles.valueOf(getParamVal(req, "title"));
                LocalDate startDate = LocalDate.parse(getParamVal(req, "startDate"));
                int salary = Integer.parseInt(getParamVal(req, "salary"));
                Set<Certifications> certifications = new HashSet<>();
                if (!getParamVal(req, "cert").equals("None"))
                    certifications.add(Certifications.valueOf(getParamVal(req, "cert")));
                controller.addEmployee(title, id, name, bDetails, salary, startDate, certifications);
                if (title.equals(JobTitles.Carrier))
                    setError(String.format("Successfully added %s(ID - %s) as a %s to the system. Add Licenses through the management page.", name, id, title));
                else
                    setError(String.format("Successfully added %s(ID - %s) as a %s to the system", name, id, title));
            } catch (NumberFormatException e) {
                setError("Error occurred: Enter numerical value as salary");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp);
        }
    }
}
