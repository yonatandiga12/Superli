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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewIncompleteShifts extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "View Shifts Incomplete Shifts (7 days)";

    public ViewIncompleteShifts() {
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

        try {
            printMenu(resp, controller.getIncompleteShiftsBetween(LocalDate.now(), LocalDate.now().plusWeeks(1)).stream().sorted(new ShiftComparator()).map(s -> String.format("%s - %s shift", s.date.format(HumanInteraction.dateFormat), s.getType())).toArray(String[]::new));
        } catch (Exception e) {
            setError(e.getMessage());
        }

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        try {
            Shift shift = controller.getIncompleteShiftsBetween(LocalDate.now(), LocalDate.now().plusWeeks(1)).stream().sorted(new ShiftComparator()).collect(Collectors.toList()).get(getIndexOfButtonPressed(req));
            redirect(resp, ManageShift.class, new String[]{"date", "type"},
                    new String[]{shift.date.toString(), shift.getType().toString()});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
