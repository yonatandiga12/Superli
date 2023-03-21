package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Domain.Service.Objects.SupplierObjects.ServiceOrderItemObject;
import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Domain.Service.Objects.SupplierObjects.ServiceSupplierObject;
import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Storekeeper;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;


public class ViewSupplier extends Screen {

    private static final String greet = "View Supplier";
    private static final String addAgreement = "Add New Agreement";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));

    public ViewSupplier() {
        super(greet,ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        try {
            int supId = getSupplierId(req, resp);

            resp.getWriter().println("<h2>Watching Supplier " + supId + ".</h2><br>");

            printMenu(resp, new String[]{"Show Supplier Info", "Show Contacts", "Show Manufacturers", "Show Agreement", "Show all Orders", "Show all discount items", "Edit Card"});
            printForm(resp, new String[]{"agreementType", "agreementDays"}, new String[]{"Agreement Type", "Agreement Days"}, new String[]{addAgreement});
            printInstructions(resp);

            String val;

            if ((val = getParamVal(req, "showInfo")) != null && val.equals("true")) {
                showSupplierInfo(req, resp, supId);
            } else if ((val = getParamVal(req, "showAllOrders")) != null && val.equals("true")) {
                showAllOrders(req, resp, supId);
            } else if ((val = getParamVal(req, "showAllDiscountItems")) != null && val.equals("true")) {
                showAllDiscountItems(req, resp, supId);
            }
        } catch (IOException e) {
            setError("Something went Wrong!");
            //            refreshPage(req,resp);
        } catch (NumberFormatException e) {
            setError("Enter a number please!");
            //            refreshPage(req,resp);
        }
        handleError(resp);
    }



    private void printInstructions(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h4>Agreement Type should be 1, 2 or 3 as follows:</h4>");
        out.println("<h5>1) Routine agreement<br>");
        out.println("2) By order agreement<br>");
        out.println("3) Self-Transport agreement</h5>");
        out.println("<h4>Enter Agreement Days with ',' between, like this: 1,3,5,6</h4>");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        if (isButtonPressed(req, addAgreement)){
            addAgreement(req, resp);
        }

        //Every time I open a new instance of this window I need to send all the info I was given...
        String supId = getParamVal(req,"supId");
        switch (getIndexOfButtonPressed(req)){
            case 0:
                redirect(resp, ViewSupplier.class, new String[]{"showInfo","supId"}, new String[]{"true",supId});
                //resp.sendRedirect("/ViewSupplier?showInfo=true");
                break;
            case 1:
                manageContacts(req, resp, supId);
                break;
            case 2:
                manageManufacturers(req,resp, supId);
                break;
            case 3:
                showAgreement(req, resp);
                break;
            case 4:
                redirect(resp, ViewSupplier.class, new String[]{"showAllOrders","supId"}, new String[]{"true", supId});
                //resp.sendRedirect("/ViewSupplier?showAllOrders=true");
                break;
            case 5:
                redirect(resp, ViewSupplier.class, new String[]{"showAllDiscountItems","supId"}, new String[]{"true", supId});
                //resp.sendRedirect("/ViewSupplier?showAllDiscountItems=true");
                break;
            case 6:
                redirect(resp, EditCard.class, new String[]{"supId"}, new String[]{supId});
                break;
        }
    }


    private void manageContacts(HttpServletRequest req, HttpServletResponse resp, String supId) throws IOException {
        try {
            redirect(resp, ManageContacts.class, new String[]{"supId"},  new String[]{supId});
        } catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }

    private void manageManufacturers(HttpServletRequest req, HttpServletResponse resp, String supId) throws IOException {
        try {
            redirect(resp, ManageManufacturers.class, new String[]{"supId"},  new String[]{supId});
        } catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }

    private void showAgreement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int supId = getSupplierId(req, resp);
            if(!controller.hasAgreement(supId)){
                setError("No agreement with this supplier");
                refreshPage(req,resp);
            }
            else{
                redirect(resp, ShowAgreement.class, new String[]{"supId"},  new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }


    private void showAllDiscountItems(HttpServletRequest req, HttpServletResponse resp, int supId) throws IOException {
        try{
            PrintWriter out = resp.getWriter();
            ArrayList<ServiceOrderItemObject> r = controller.getAllOrdersItemsInDiscounts(supId);
            if(r != null && r.size() > 0){
                out.println("<h4>");
                for(ServiceOrderItemObject orderItemObject : r){
                    float originalPrice = orderItemObject.getQuantity() * orderItemObject.getPricePerUnit();
                    out.println(orderItemObject.toStringDiscount(originalPrice) + "<br>");
                }
                out.println("</h4>");
            }
            else{
                setError("No order Items available!");
                refreshPage(req,resp);
            }


        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refreshPage(req,resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }

    private void showAllOrders(HttpServletRequest req, HttpServletResponse resp, int supId) throws IOException {
        try {
            ArrayList<ServiceOrderObject> r = controller.getAllOrdersForSupplier(supId);
            if(r != null && r.size() > 0){
                PrintWriter out = resp.getWriter();
                for(ServiceOrderObject orderObject : r){
                    out.println(orderObject.toString() + "<br><br>");
                }
            }
            else{
                setError("No orders available!");
                refreshPage(req,resp);
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refreshPage(req,resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }

    private void addAgreement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int supId = getSupplierId(req, resp);
            int agreementType = Integer.parseInt(req.getParameter("agreementType"));
            String agreementDays = req.getParameter("agreementDays");
            if (!controller.hasAgreement(supId)) {
                if(agreementType == 1 || agreementType == 2 || agreementType == 3) {
                    if (controller.addAgreement(supId, agreementType, agreementDays)) {
                        redirect(resp, AddItemToAgreement.class, new String[]{"supId"}, new String[]{ String.valueOf(supId)});
                    } else {
                        setError("A problem has occurred, please try again later");
                        refreshPage(req,resp);
                    }
                } else {
                    setError("Wrong number!, enter 1, 2 or 3");
                    refreshPage(req,resp);
                }
            }
            else{
                setError("Agreement Already Exists!, if you want to change it, go to Show Agreement Window");
                refreshPage(req,resp);
            }

        }catch (NumberFormatException e1){
            setError("Please enter a number!");
            refreshPage(req,resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }


    private void showSupplierInfo(HttpServletRequest req, HttpServletResponse resp, int supId2) throws IOException {
        try {
            int supId = Integer.parseInt(getParamVal(req,"supId"));
            ServiceSupplierObject result = controller.getSupplierInfo(supId);
            if(result != null){
                PrintWriter out = resp.getWriter();
                out.println("<h4>");
                out.println(result.toString("<br>"));
                out.println("</h4>");
            }
            else{
                setError("Something went wrong!");
                refreshPage(req,resp);
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refreshPage(req,resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            refreshPage(req,resp);
        }
    }


    private int getSupplierId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        return Integer.parseInt(getParamVal(req,"supId"));

    }

        /*
    private String getCookie(String name, HttpServletRequest req, HttpServletResponse resp, int time) throws IOException {
        String cookie = "";
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(name)) {
                cookie = c.getValue();
            }
            c.setMaxAge((int) TimeUnit.MINUTES.toSeconds(time)); //time of life of the cookie, if bot listed its infinite
            resp.addCookie(c);
        }
        return cookie;
    }

     */

    private String getCookie(String name, HttpServletRequest req, HttpServletResponse resp, int time) throws IOException {
        String cookie = "";
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(name)) {
                c.setMaxAge((int) TimeUnit.MINUTES.toSeconds(time)); //time of life of the cookie, if bot listed its infinite
                resp.addCookie(c);
                return c.getValue();
            }
        }
        return cookie;
    }


    private void addCookie(String value, String nameOfCookie, HttpServletResponse resp, int time) {
        Cookie c = new Cookie(nameOfCookie, value);
        c.setMaxAge((int) TimeUnit.MINUTES.toSeconds(time));
        resp.addCookie(c);
    }

    private void refreshPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        refresh(req, resp, new String[]{"supId"},new String[]{String.valueOf(getSupplierId(req, resp))});
    }


}
