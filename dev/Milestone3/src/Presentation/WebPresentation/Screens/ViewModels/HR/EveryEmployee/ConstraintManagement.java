package Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee;

import Domain.Service.Objects.Shift.Shift;
import Globals.util.ShiftComparator;
import Presentation.WebPresentation.Screens.Models.HR.*;
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

public class ConstraintManagement extends Screen {


    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Carrier.class, Cashier.class, HR_Manager.class, Logistics_Manager.class,
            Sorter.class, Storekeeper.class, Storekeeper.class, Transport_Manager.class));

    private static final String GREET = "Welcome to the Constraint Management Page";


    public ConstraintManagement() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
            return;
        }
        Employee employee = Login.getLoggedUser(req);
        PrintWriter out = resp.getWriter();
        header(resp);
        greet(resp);
        out.println(String.format("<h3>Current user: %s</p><br>", employee.getName()));
        printForm(resp, new String[0], new String[0], new String[]{"Add Constraints", "Remove Constraints"});
        String addVal = getParamVal(req, "add");
        String remVal = getParamVal(req, "remove");
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        if (addVal != null && addVal.equals("true")) {
            try {
                List<Shift> availableShifts = controller.getShiftsBetween(today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
                out.println("<p>When can you work? (Choose available shifts)</p><br>");
                printMenu(resp, availableShifts.stream().map(Object::toString).toArray(String[]::new));
            } catch (Exception e) {
                setError(e.getMessage());
            }
        } else if (remVal != null && remVal.equals("true")) {
            try {
                List<Shift> registeredShifts = controller.getEmployeeConstraintsBetween(employee.id, today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
                out.println("<p>Current constraints (choose a constraint to remove it):</p><br>");
                printMenu(resp, registeredShifts.stream().map(Object::toString).toArray(String[]::new));
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
        if (isButtonPressed(req, "Add Constraints")){
            refresh(req, resp, new String[]{"add"}, new String[]{"true"});
            return;
        }
        if (isButtonPressed(req, "Remove Constraints")){
            refresh(req, resp, new String[]{"remove"}, new String[]{"true"});
            return;
        }

        Employee employee = Login.getLoggedUser(req);
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        int i = getIndexOfButtonPressed(req);
        String addVal = getParamVal(req, "add");
        String remVal = getParamVal(req, "remove");
        if (addVal != null && addVal.equals("true")) {
            try {
                List<Shift> availableShifts = controller.getShiftsBetween(today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
                controller.registerToConstraint(employee.id, availableShifts.get(i));
                setError("Successfully added constraint for " + availableShifts.get(i));
            } catch (Exception e) {
                setError(e.getMessage());
            }
        } else if (remVal != null && remVal.equals("true")) {
            try {
                List<Shift> registeredShifts = controller.getEmployeeConstraintsBetween(employee.id, today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
                controller.unregisterFromConstraint(employee.id, registeredShifts.get(i));
                setError("Successfully removed constraint for " + registeredShifts);
            } catch (Exception e) {
                setError(e.getMessage());
            }
        }
        refresh(req, resp);
    }
}
