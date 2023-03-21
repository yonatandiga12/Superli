package Presentation.WebPresentation.Screens.ViewModels.Transport.Transport;

import Domain.Service.Objects.Shift.Shift;
import Globals.Enums.ShiftTypes;
import Globals.Pair;
import Globals.util.ShiftComparator;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Transport_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class CreateTransport extends Screen {
    private static final String greet = "Create new transport:";
    private static final String NO_SHIFTS = "There are no shifts in the coming month!";
    private static final String SUCCESS_MSG = "The transport was created successfully!";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    private String success = null;
    private static HashMap<String, Shift> shifts;
    public CreateTransport() {
        super(greet, ALLOWED);
        getShiftInTheCloseMonth();
    }
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp, new String[]{"Create", "Cancel"});
        handleError(resp);
        handleSuccess(resp);
    }

    private void handleSuccess(HttpServletResponse resp) throws IOException {
        if (!isSuccess())
            return;
        PrintWriter out = resp.getWriter();
        out.println(String.format("<p style=\"color:green\">%s</p><br><br>", getSuccess()));
        cleanSuccess();
    }

    private void cleanSuccess() {
        success = null;
    }

    private String getSuccess() {
        return success;
    }

    private boolean isSuccess() {
        return success != null;
    }

    private static void printForm(HttpServletResponse resp, String[] buttons) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h3>Choose shift to transport from the list:</h3>");
        out.println("<form method=\"post\">\n");
        out.println(String.format("<input list=\"Shifts\" name=\"shift\" id=\"shift\">"));
        out.println(String.format("<datalist id=\"Shifts\">"));
        if(shifts.isEmpty()){
            out.println(String.format("<option value=\"%s\">", NO_SHIFTS));
        }
        else{
            for(String shift: shifts.keySet()){
                out.println(String.format("<option value=\"%s\">", shift));
            }
        }
        out.println(String.format("</datalist><br><br>"));
        for (String button : buttons)
            out.println(String.format("<input type=\"submit\" name=\"%s\" value=\"%s\">", button, button));
        out.println("<br><br></form>");
    }
    private void getShiftInTheCloseMonth() {
        shifts = new LinkedHashMap();
        try {
            LocalDate today = LocalDate.now();
            LocalDate nextMonth = today.plusMonths(2);
            for(Shift shift: controller.getShiftsBetween(today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList())){
                shifts.put(shift.toString(), shift);
            }
        } catch (Exception e) {
        }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, "Create")){
            try {
                Shift shift = getShift(req);
                int transportID = controller.createNewTransport(new Pair<LocalDate, ShiftTypes>(shift.date, shift.getType()));
                setSuccess(SUCCESS_MSG + "<br>Transport SN: " +  transportID);
                refresh(req, resp);
            } catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
        else if(isButtonPressed(req, "Cancel")) {
            redirect(resp, TransportManagementMenu.class);
        }
    }

    private void setSuccess(String success) {
        this.success = success;
    }

    private static void showSuccessMsg(HttpServletResponse resp, String msg) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(String.format("<p style=\"color:green\">%s</p><br>", msg));
    }
    private Shift getShift(HttpServletRequest req) throws Exception {
        String shift = getParamVal(req, "shift");
        if(shifts.isEmpty()){
            throw new Exception(NO_SHIFTS);
        } else if (shifts.containsKey(shift)) {
            return shifts.get(shift);
        }
        else{
            throw new Exception("Please choose shift from the list!");
        }
    }
}