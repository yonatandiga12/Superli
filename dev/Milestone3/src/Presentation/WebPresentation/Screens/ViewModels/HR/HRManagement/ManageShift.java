package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

import Domain.Service.Objects.Shift.Shift;
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
import java.util.stream.Collectors;

public class ManageShift extends Screen {

    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "Manage Shift";

    public ManageShift() {
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
        out.println("<label>Enter date: </label>");
        out.println("<input type=\"date\" name=\"date\" value=\"" + LocalDate.now() + "\"><br><br>");
        out.println("<label>Enter type: <label>");
        out.println("<select name=\"type\">");
        for (ShiftTypes type : ShiftTypes.values())
            out.println(String.format("<option value=\"%s\">%s</option>", type, type));
        out.println("</select><br><br>");
        out.println("<input type=\"submit\" name=\"mng\" value=\"Manage\"><br><br>");
        out.println("</form>");

        String givenDate = getParamVal(req, "date");
        String givenType = getParamVal(req, "type");

        if (givenDate != null && givenType != null) {
            LocalDate date = LocalDate.parse(givenDate);
            ShiftTypes type = ShiftTypes.valueOf(givenType);
            try {
                Shift sShift = controller.getShift(date, type);
                out.println("<hr>");
                out.println("<form method=\"post\">");
                //Date
                out.println(String.format("<label>Date: %s</label><br><br>", sShift.date.format(HumanInteraction.dateFormat)));
                //Type
                out.println(String.format("<label>Type: %s</label><br><br>", sShift.getType()));
                //Shift Manager
                if (!sShift.shiftManagerId.equals("-1")) {
                    Domain.Service.Objects.Employee.Employee manager = controller.getEmployee(sShift.shiftManagerId);
                    out.println(String.format("<label>Shift Manager: %s(ID - %s)</label><br>", manager.name, manager.id));
                } else
                    out.println("<label style=\"color:red\">Shift Manager: Not Assigned!</label><br>");
                Set<Domain.Service.Objects.Employee.Employee> availManagers = controller.getAvailableShiftManagersFor(sShift.date, sShift.getType());
                out.println("<label>Set new manager: </label>");
                out.println("<select name=\"newMan\">");
                for (Domain.Service.Objects.Employee.Employee availMan : availManagers)
                    out.println(String.format("<option value=\"%s\">%s(ID - %s)</option>", availMan.id, availMan.name, availMan.id));
                out.println("</select>");
                out.println("<input type=\"submit\" name=\"uManager\" value=\"Set\"><br><br>");
                //Employees
                for (JobTitles title : JobTitles.values()) {
                    out.println(String.format("<label>%ss: %s out of </label>", title.toString().replaceAll("_", " "), sShift.titleToIDs.get(title).size()));
                    out.println(String.format("<input type=\"number\" name=\"%sCount\" placeholder=\"%s Count\" value=\"%s\" min=\"0\">", title, title.toString().replaceAll("_", " "), sShift.titleToCount.get(title)));
                    out.println(String.format("<input type=\"submit\" name=\"u%sCount\" value=\"Set New Count\"><br>", title));
                    Set<Domain.Service.Objects.Employee.Employee> assigned = controller.getAssignedJobTitleFor(sShift.date, sShift.getType(), title);
                    for (Domain.Service.Objects.Employee.Employee sEmp : assigned) {
                        out.println(String.format("<p>%s(ID - %s)</p>", sEmp.name, sEmp.id));
                    }
                    Set<Domain.Service.Objects.Employee.Employee> available = controller.getAvailableJobTitleFor(sShift.date, sShift.getType(), title);
                    out.println(String.format("<select name=\"add%sID\">", title));
                    for (Domain.Service.Objects.Employee.Employee employee : available)
                        out.println(String.format("<option value=\"%s\">%s(ID - %s). %s</option>", employee.id, employee.name, employee.id, controller.getEmployeeWorkDetailsForCurrentMonth(employee.id)));
                    out.println("</select>");
                    out.println(String.format("<input type=\"submit\" name=\"add%s\" value=\"Add to shift\"><br>", title));
                    out.println(String.format("<select name=\"rem%sID\">", title));
                    for (Domain.Service.Objects.Employee.Employee employee : assigned)
                        out.println(String.format("<option value=\"%s\">%s(ID - %s). %s</option>", employee.id, employee.name, employee.id, controller.getEmployeeWorkDetailsForCurrentMonth(employee.id)));
                    out.println("</select>");
                    out.println(String.format("<input type=\"submit\" name=\"rem%s\" value=\"Remove From shift\"><br><br>", title));
                }

                //MISC Buttons
                out.println("<input type=\"submit\" name=\"delete\" value=\"Remove Shift\"><br><br>");
                if (isButtonPressed(req, "deleted")) {
                    out.println("<p>Are you sure you want to remove this shift from the system?</p>");
                    out.println("<p style=\"color:red\">Mind you that this process is irreversible!</p>");
                    out.println("<input type=\"submit\" name=\"sure\" value=\"Yes!\"><br><br>");
                }

                out.println("<form>");
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
        else if (isButtonPressed(req, "uManager")) {
            try {
                controller.editShiftManagerID(LocalDate.parse(getParamVal(req, "date")), ShiftTypes.valueOf(getParamVal(req, "type")), getParamVal(req, "newMan"));
                setError("Shift manager successfully updated");
            } catch (Exception e) {
                setError(e.getMessage());
            }
            refresh(req, resp, new String[]{"date", "type"}, new String[]{getParamVal(req, "date"), getParamVal(req, "type")});
        } else if (isButtonPressed(req, "delete")) {
            refresh(req, resp, new String[]{"date", "type", "delete"}, new String[]{getParamVal(req, "date"), getParamVal(req, "type"), "true"});
        } else if (isButtonPressed(req, "sure")) {
            try {
                controller.removeShift(LocalDate.parse(getParamVal(req, "date")), ShiftTypes.valueOf(getParamVal(req, "type")));
                setError("Shift deleted successfully");
                refresh(req, resp);
            } catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"date", "type"}, new String[]{getParamVal(req, "date"), getParamVal(req, "type")});
            }
        } else {
            for (JobTitles title : JobTitles.values())
                if (isButtonPressed(req, String.format("u%sCount", title))) {
                    try {
                        int newCount = Integer.parseInt(getParamVal(req, String.format("%sCount", title)));
                        controller.editShiftJobtitleCount(LocalDate.parse(getParamVal(req, "date")), ShiftTypes.valueOf(getParamVal(req, "type")), newCount, title);
                        setError(String.format("Successfully updated %s count to %s", title.toString().replaceAll("_", " "), newCount));
                    } catch (NumberFormatException e) {
                        setError("Error occurred: Enter numerical value as count");
                    } catch (Exception e) {
                        setError(e.getMessage());
                    }
                    refresh(req, resp, new String[]{"date", "type"}, new String[]{getParamVal(req, "date"), getParamVal(req, "type")});
                    return;
                } else if (isButtonPressed(req, String.format("add%s", title))) {
                    try {
                        LocalDate date = LocalDate.parse(getParamVal(req, "date"));
                        ShiftTypes type = ShiftTypes.valueOf(getParamVal(req, "type"));
                        Set<String> newAssigned = controller.getAssignedJobTitleFor(date, type, title).stream().map(e -> e.id).collect(Collectors.toSet());
                        newAssigned.add(getParamVal(req, String.format("add%sID", title)));
                        controller.editShiftJobTitleIDs(date, type, newAssigned, title);
                        setError("Successfully added");
                    } catch (Exception e) {
                        setError(e.getMessage());
                    }
                    refresh(req, resp, new String[]{"date", "type"}, new String[]{getParamVal(req, "date"), getParamVal(req, "type")});
                    return;
                } else if (isButtonPressed(req, String.format("rem%s", title))) {
                    try {
                        LocalDate date = LocalDate.parse(getParamVal(req, "date"));
                        ShiftTypes type = ShiftTypes.valueOf(getParamVal(req, "type"));
                        Set<String> newAssigned = controller.getAssignedJobTitleFor(date, type, title).stream().map(e -> e.id).collect(Collectors.toSet());
                        newAssigned.remove(getParamVal(req, String.format("rem%sID", title)));
                        controller.editShiftJobTitleIDs(date, type, newAssigned, title);
                        setError("Successfully removed");
                    } catch (Exception e) {
                        setError(e.getMessage());
                    }
                    refresh(req, resp, new String[]{"date", "type"}, new String[]{getParamVal(req, "date"), getParamVal(req, "type")});
                    return;
                }
        }
    }
}