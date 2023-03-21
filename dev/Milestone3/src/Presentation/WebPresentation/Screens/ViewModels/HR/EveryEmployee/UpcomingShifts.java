package Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee;

import Domain.Service.Objects.Shift.Shift;
import Globals.util.HumanInteraction;
import Globals.util.ShiftComparator;
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
import java.util.*;

public class UpcomingShifts extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>();

    private static final Set<Class<? extends Employee>> ALLOWED_TO_WATCH_OTHERS
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    public UpcomingShifts() {
        super("", ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
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
        PrintWriter out = resp.getWriter();
        out.println(String.format("<h1>Upcoming shifts in the next 30 days for %s</h1>", emp.getName()));
        try {
            List<Shift> upcoming = new ArrayList<>(controller.getEmployeeShiftsBetween(emp.id, LocalDate.now(), LocalDate.now().plusDays(30)));
            upcoming.sort(new ShiftComparator());
            for (Shift shift : upcoming)
                if (emp.id.equals(shift.shiftManagerId))
                    out.println(String.format("<p>%s - %s shift - As shift manager</p>", shift.date.format(HumanInteraction.dateFormat), shift.getType()));
                else
                    out.println(String.format("<p>%s - %s shift</p>", shift.date.format(HumanInteraction.dateFormat), shift.getType()));
        } catch (Exception e) {
            setError(e.getMessage());
        }
        out.println("<br>");
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
    }
}
