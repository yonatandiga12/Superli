package Presentation.WebPresentation.Screens.ViewModels.Transport.Truck;

import Globals.Enums.TruckModel;
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

public class AddTruck extends Screen {
    private static final String greet = "Add Truck:";
    private static final String SUCCESS_MSG = "Truck successfully added!";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(HR_Manager.class, Transport_Manager.class, Logistics_Manager.class));
    public AddTruck() {
        super(greet, ALLOWED);
    }
    private String success = null;
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp, ALLOWED)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp, new String[]{"OK", "Cancel"});
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
    private static void printForm(HttpServletResponse resp, String[] buttons) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<form method=\"post\">\n");
        out.println(String.format("<input type=\"text\" name=\"%s\" placeholder=\"%s\"><br><br>", "LN", "License number"));
        out.println(String.format("<input list=\"Truck Models\" name=\"model\" id=\"model\">"));
        out.println(String.format("<datalist id=\"Truck Models\">"));
        out.println(String.format("<option value=\"%s\">", TruckModel.Van.toString()));
        out.println(String.format("<option value=\"%s\">", TruckModel.SemiTrailer.toString()));
        out.println(String.format("<option value=\"%s\">", TruckModel.FullTrailer.toString()));
        out.println(String.format("<option value=\"%s\">", TruckModel.DoubleTrailer.toString()));
        out.println(String.format("</datalist><br><br>"));
        out.println(String.format("<input type=\"text\" name=\"%s\" placeholder=\"%s\"><br><br>", "NetWeight", "Net weight"));
        out.println(String.format("<input type=\"text\" name=\"%s\" placeholder=\"%s\"><br><br>", "MaxCapacityWeight", "Max capacity weight"));
        for (String button : buttons)
            out.println(String.format("<input type=\"submit\" name=\"%s\" value=\"%s\">", button, button));
        out.println("<br><br></form>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, "OK")){
            try {
                int ln = getLicenseNumber(req);
                TruckModel tm = getTransportModel(req);
                int netWeight = getNetWeight(req, tm);
                int maxCapacityWeight = getMaxCapacityWeight(req, tm);
                controller.addTruck(ln, tm, netWeight, maxCapacityWeight);
                setSuccess(SUCCESS_MSG);
                refresh(req, resp);
            } catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
        else if(isButtonPressed(req, "Cancel")) {
            redirect(resp, TruckManagementMenu.class);
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
    private static void showSuccessMsg(HttpServletResponse resp, String msg) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println(String.format("<p style=\"color:green\">%s</p><br><br>", msg));
    }
    private int getNetWeight(HttpServletRequest req, TruckModel tm) throws Exception {
        String error = "<b>Enter valid net truck weight:</b><br>" +
                "Van - 100 < weight <= 200<br>" +
                "SemiTrailer - 200 < weight <= 500<br>" +
                "DoubleTrailer - 1000 < weight <= 2000<br>" +
                "FullTrailer - 3000 < weight <= 5000<br>";
        try {
            int netWeight = Integer.parseInt(req.getParameter("NetWeight"));
            if(isValidNetTruckWeight(tm, netWeight)){
                return netWeight;
            }
        }
        catch (Exception e){
            throw new Exception(error);
        }
        throw new Exception(error);
    }
    private boolean isValidNetTruckWeight(TruckModel tm, int weight){
        boolean ans = false;
        switch (tm){
            case Van:
                ans = weight >= 100 & weight <= 200;
                break;
            case SemiTrailer:
                ans = weight > 200 & weight <= 500;
                break;
            case DoubleTrailer:
                ans = weight > 1000 & weight <= 2000;
                break;
            case FullTrailer:
                ans = weight > 3000 & weight <= 5000;
                break;
        }
        return ans;
    }
    private boolean isValidWeight(TruckModel tm, int weight){
        boolean ans = false;
        switch (tm){
            case Van:
                ans = weight >= 200 & weight <= 1000;
                break;
            case SemiTrailer:
                ans = weight > 1000 & weight <= 5000;
                break;
            case DoubleTrailer:
                ans = weight > 5000 & weight <= 10000;
                break;
            case FullTrailer:
                ans = weight > 10000 & weight <= 20000;
                break;
        }
        return ans;
    }

    private int getMaxCapacityWeight(HttpServletRequest req, TruckModel tm) throws Exception {
        String error = "<b>Enter valid max capacity weight:</b><br>" +
                "Van - 200 < weight <= 1000<br>" +
                "SemiTrailer - 1000 < weight <= 5000<br>" +
                "DoubleTrailer - 5000 < weight <= 10000<br>" +
                "FullTrailer - 10000 < weight <= 20000<br>";
        try {
            int maxCapacityWeight = Integer.parseInt(req.getParameter("MaxCapacityWeight"));
            if(isValidWeight(tm, maxCapacityWeight)){
                return maxCapacityWeight;
            }
        }
        catch (Exception e){
            throw new Exception(error);
        }
        throw new Exception(error);
    }

    private TruckModel getTransportModel(HttpServletRequest req) throws Exception {
        try {
            return TruckModel.valueOf(req.getParameter("model"));
        }
        catch (Exception e){
            throw new Exception("Please choose truck model from the lists!");
        }
    }
}