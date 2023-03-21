package Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement;

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
import java.util.Set;

public class HrMessages extends Screen {


    private static final Set<Class<? extends Employee>> ALLOWED
            = new HashSet<>(Arrays.asList(Admin.class, HR_Manager.class));

    private static final String GREET = "HR Important Messages:";

    public HrMessages() {
        super(GREET, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
            return;
        }
        header(resp);
        PrintWriter out = resp.getWriter();
        out.println("<h3>Important messages about shifts:</h3>");
        try {
            for (String message : controller.getImportantHRMessagesShifts())
                out.println(String.format("<p>%s</p><br>", message));
        } catch (Exception e) {
            out.println(String.format("<p style=\"color:red\">%s</p><br><br>", e.getMessage()));
        }
        out.println("<hr><h3>Important messages about transports:</h3>");
        try {
            for (String message : controller.getImportantHRMessagesTransport())
                out.println(String.format("<p>%s</p><br>", message));
        } catch (Exception e) {
            out.println(String.format("<p style=\"color:red\">%s</p><br><br>", e.getMessage()));
        }
        out.println("<hr><h3>Important messages about Inventory:</h3>");
        try {
            for (String message : controller.getImportantHRMessagesInventory())
                out.println(String.format("<p>%s</p><br>", message));
        } catch (Exception e) {
            out.println(String.format("<p style=\"color:red\">%s</p><br><br>", e.getMessage()));
        }
        out.println("<hr><h3>Important messages about Suppliers:</h3>");
        try {
            for (String message : controller.getImportantHRMessagesSuppliers())
                out.println(String.format("<p>%s</p><br>", message));
        } catch (Exception e) {
            out.println(String.format("<p style=\"color:red\">%s</p><br><br>", e.getMessage()));
        }

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
    }
}
