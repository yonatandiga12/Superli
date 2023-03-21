package Presentation.WebPresentation.Screens.ViewModels.Transport.Transport.Update;

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

public class PlaceTruck extends Screen {
    private static final String greet = "Place Truck";
    private static final String SUCCESS_MSG = "A truck was successfully place into transport!";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    private static final int NOT_TRANSPORT = -1;
    private String success = null;
    private int transportSN = NOT_TRANSPORT;
    public PlaceTruck() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        transportSN = getTransportSN(req);
        printForm(resp, new String[]{"LN"}, new String[]{"License number"}, new String[]{"Place", "Cancel"});
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
    private void setSuccess(String success) {
        this.success = success;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, "Place")){
            try {
                if(transportSN != NOT_TRANSPORT){
                    int ln = getLicenseNumber(req);
                    controller.placeTruck(transportSN, ln);
                    setSuccess(SUCCESS_MSG);
                    refresh(req, resp, new String[]{"ID"}, new String[]{String.valueOf(transportSN)});
                }
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"ID"}, new String[]{String.valueOf(transportSN)});
            }
        }
        else if(isButtonPressed(req, "Cancel"))
            redirect(resp, UpdateTransport.class);
    }
    private int getTransportSN(HttpServletRequest req){
        String val;
        if ((val = getParamVal(req,"ID")) != null){
            try
            {
                return Integer.parseInt(val);
            }catch(Exception e){
                setError("Enter a valid transport ID number!");
                return NOT_TRANSPORT;
            }
        }
        else{
            setError("Enter a valid transport ID number!");
            return NOT_TRANSPORT;
        }
    }
    private int getLicenseNumber(HttpServletRequest req) throws Exception {
        try {
            int ln = Integer.parseInt(req.getParameter("LN"));
            if(ln >= 0){
                return ln;
            }
        }
        catch (Exception e){
            throw new Exception("Enter a valid license number!");
        }
        throw new Exception("Enter a valid license number!");
    }

}
