package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Globals.Enums.JobTitles;
import Globals.Enums.ShiftTypes;
import Globals.util.HumanInteraction;
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

public class AddShifts extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Add new shift";


    public AddShifts() {
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
        out.println("<p>Enter Date: </p>");
        out.println("<input type=\"date\" name=\"date\" value=\"" + LocalDate.now() + "\"><br><br>");
        out.println("<p>Enter Type: </p>");
        out.println("<select name=\"type\">");
        for (ShiftTypes type: ShiftTypes.values())
            out.println(String.format("<option value=\"%s\">%s</option>", type, type));
        out.println("</select><br><br>");
        for (JobTitles title: JobTitles.values()) {
            out.println(String.format("<p>Enter %s count: </p>", title.toString().replaceAll("_", " ")));
            out.println(String.format("<input type=\"number\" name=\"%sCount\" placeholder=\"%s Count\" value=\"0\" min=\"0\"><br><br>", title.toString().toLowerCase(), title));
        }
        out.println("<input type=\"submit\" name=\"add\" value=\"add\"><br><br>");
        out.println("</form>");

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, "add")){
            try {
                LocalDate date = LocalDate.parse(getParamVal(req, "date"));
                ShiftTypes type = ShiftTypes.valueOf(getParamVal(req, "type"));
                int carrierCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.Carrier.toString().toLowerCase())));
                int cashierCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.Cashier.toString().toLowerCase())));
                int sorterCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.Sorter.toString().toLowerCase())));
                int storekeeperCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.Storekeeper.toString().toLowerCase())));
                int hrManCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.HR_Manager.toString().toLowerCase())));
                int logManCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.Logistics_Manager.toString().toLowerCase())));
                int traManCount = Integer.parseInt(getParamVal(req, String.format("%sCount", JobTitles.Transport_Manager.toString().toLowerCase())));
                controller.createShift(date, type, carrierCount, cashierCount, storekeeperCount, sorterCount, hrManCount, logManCount, traManCount);
                setError(String.format("Successfully added a %s shift for %s", type, date.format(HumanInteraction.dateFormat)));
            } catch (NumberFormatException e) {
                setError("Error occurred: Enter numerical value");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp);
        }
    }
}
