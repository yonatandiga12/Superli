package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Domain.Service.Objects.Shift.Shift;
import Globals.util.HumanInteraction;
import Globals.util.ShiftComparator;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewShifts extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "View Shifts";

    public ViewShifts() {
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
        String val;
        LocalDate start = LocalDate.now(), end = LocalDate.now();
        boolean startGiven = false, endGiven = false;
        if (startGiven = ((val = getParamVal(req, "start")) != null)) {
            start = LocalDate.parse(val);
        }
        if (endGiven = ((val = getParamVal(req, "end")) != null)) {
            end = LocalDate.parse(val);
        }
        out.println("<form>");
        out.println("<p>Enter start date to filter from: </p>");
        out.println(String.format("<input type=\"date\" name=\"start\" value=\"%s\"><br><br>", start));
        out.println("<p>Enter end date to filter to: </p>");
        out.println(String.format("<input type=\"date\" name=\"end\" value=\"%s\"><br><br>", end));
        out.println("<input type=\"submit\" name=\"view\" value=\"View shifts\"><br><br>");
        out.println("</form>");

        if (startGiven && endGiven && !isError()) {
            int numOfShifts = 0;
            try {
                List<Shift> shifts = controller.getShiftsBetween(start, end).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
                out.println("<hr>");
                out.println("<h3>Click on a shift in order to manage it</h3>");
                printMenu(resp, shifts.stream().map(s -> String.format("%s - %s shift", s.date.format(HumanInteraction.dateFormat), s.getType())).toArray(String[]::new));
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
            LocalDate start = LocalDate.parse(getParamVal(req, "start")), end = LocalDate.parse(getParamVal(req, "end"));
            Shift shift = controller.getShiftsBetween(start, end).stream().sorted(new ShiftComparator()).collect(Collectors.toList()).get(getIndexOfButtonPressed(req));
            redirect(resp, ManageShift.class, new String[]{"date", "type"},
                    new String[]{shift.date.toString(), shift.getType().toString()});
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }
    }
}
