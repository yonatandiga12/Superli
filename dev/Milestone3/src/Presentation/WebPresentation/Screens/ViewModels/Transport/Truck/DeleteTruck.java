package Presentation.WebPresentation.Screens.ViewModels.Transport.Truck;

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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeleteTruck extends Screen {
    private static final String greet = "Delete Truck:";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    private static final String SUCCESS_MSG = "Truck was successfully removed!";
    private String success = null;
    public DeleteTruck() {
        super(greet, ALLOWED);
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
    private void setSuccess(String success) {
        this.success = success;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp, new String[]{"LN"}, new String[]{"License number"}, new String[]{"Delete", "Cancel"});
        handleError(resp);
        handleSuccess(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, "Delete")){
            try {
                int ln = Integer.parseInt(req.getParameter("LN"));
                controller.removeTruck(ln);
                setSuccess(SUCCESS_MSG);
                refresh(req, resp);
            }catch (NumberFormatException exception){
                setError("Enter a valid license number!");
                refresh(req, resp);
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
        else if(isButtonPressed(req, "Cancel"))
            redirect(resp, TruckManagementMenu.class);
    }

}