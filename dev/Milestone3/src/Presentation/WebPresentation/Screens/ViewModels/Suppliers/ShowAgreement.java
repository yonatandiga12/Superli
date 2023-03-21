package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Domain.Service.Objects.SupplierObjects.ServiceItemObject;
import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Storekeeper;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShowAgreement extends Screen {


    private static final String greet = "Agreement Info";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));


    public ShowAgreement(){
        super(greet,ALLOWED);
    }

    private int getAgreementType(int supId) {
        try {
            if(controller.isRoutineAgreement(supId))
                return 1;
            else if(controller.isByOrderAgreement(supId))
                return 2;
        } catch (Exception e) {
            setError(e.getMessage());
        }
        return 3;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        int supId = getSupplierId(req);
        int agreementType = getAgreementType(supId);

        resp.getWriter().println("<h2>Agreement Information for Supplier" + supId + ".</h2><br>");

        printMenu(resp, new String[]{"Show All Items", "Add item to agreement"});
        printForm(resp, new String[] {"itemId"}, new String[]{"Item ID"}, new String[]{"Remove Item"});
        printForm(resp, new String[] {"itemId2"}, new String[]{"Item ID"}, new String[]{"View Item"});

        // TODO: 11/06/2022 Should we do it? maybe it can cause problems...
        printForm(resp, new String[] {"agreementType", "agreementDays" }, new String[]{"Agreement Type", "Agreement Days"}, new String[]{"Change Agreement Type"});
        printInstructions(resp);

        differentOptionsForAgreement(agreementType, req, resp);
        chooseAction(req, resp, supId);
        handleError(resp);
    }

    private void differentOptionsForAgreement(int agreementType, HttpServletRequest req, HttpServletResponse resp) throws IOException {

        if(agreementType == 1){  //routine
            printForm(resp, new String[] {"agreementDays2" }, new String[]{"Delivery Days"}, new String[]{"Change Delivery Days"});
            printInstructionsDeliveryDays(req, resp);
        }
        else if(agreementType == 2){  //byOrder
            printForm(resp, new String[] {"day" }, new String[]{"Days Until Delivery"}, new String[]{"Change Days Until Delivery"});
            printInstructionsDaysUntilDelivery(resp);
        }

    }

    private void chooseAction(HttpServletRequest req, HttpServletResponse resp, int supId) throws IOException {

        String val;
        if ((val = getParamVal(req,"showItems")) != null && val.equals("true")){
            showAllItems(req, resp, supId);
        }
        //else if ((val = getParamVal(req,"viewItem")) != null && val.equals("true")){
            //int itemId = Integer.parseInt(getCookie("itemId2ShowAgreement", req, resp, 5));
            //viewItem(req, resp, supId);
        //}
    }


    private void printInstructions(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h4>Agreement Type should be 1, 2 or 3 as follows:</h4>");
        out.println("<h5>1) Routine agreement<br>");
        out.println("2) By order agreement<br>");
        out.println("3) Self-Transport agreement</h5>");
        out.println("<h4>Enter Agreement Days with ',' between, like this: 1,3,5,6</h4>");
    }


    private void printInstructionsDaysUntilDelivery(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h4>");
        out.println("Enter a number which will be the days until delivery.<br>");
        out.println("</h4>");
    }

    private void printInstructionsDeliveryDays(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.println("<h4>");
        out.println("Enter delivery days with ',' between, like this: 1,3,5,6 <br>");
        out.println("</h4>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        String supId = String.valueOf(getSupplierId(req));
        if (isButtonPressed(req, "Remove Item")) {
            removeItemFromAgreement(req, resp);
        }
        else if(isButtonPressed(req, "View Item")){
            try {
                //redirect(resp, ShowAgreement.class, new String[]{"viewItem","supId"}, new String[]{"true", supId});
                viewItem(req, resp);
            } catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(supId)});
            }
        }
        else if(isButtonPressed(req, "Change Agreement Type")) {
            changeAgreementType(req, resp);
        }

        else if(isButtonPressed(req, "Change Delivery Days")){
            changeRoutineDays(req, resp);
        }
        else if(isButtonPressed(req, "Change Days Until Delivery")){
            changeByOrderDay(req, resp);
        }


        switch (getIndexOfButtonPressed(req)){
            case 0:
                redirect(resp, ShowAgreement.class, new String[]{"showItems","supId"}, new String[]{"true",supId});
                //resp.sendRedirect("/ShowAgreement?showItems=true");
                break;
            case 1:
                addItemToAgreement(req, resp, supId);
                break;

        }
    }



    private void addItemToAgreement(HttpServletRequest req, HttpServletResponse resp, String supId) throws IOException {
        try {
            redirect(resp, AddItemToAgreement.class, new String[]{"supId"}, new String[]{supId});
        } catch (Exception e) {
            setError("Item is not in the system!, Please enter Product ID!");
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(supId)});
        }
    }

    private void viewItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int itemId = Integer.parseInt(req.getParameter("itemId2"));
            //int itemId = controller.getMatchingProductIdForIdBySupplier(idBySupplier);
            int supId = getSupplierId(req);

            redirect(resp, ShowAgreementItem.class, new String[]{"supId","itemId"},new String[]{String.valueOf(supId), String.valueOf(itemId)});
        } catch (Exception e) {
            setError("Item is not in the system!, Please enter Product ID!");
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void changeByOrderDay(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int day = Integer.parseInt(req.getParameter("day"));
            int supId = getSupplierId(req);
            if(controller.changeDaysUntilDelivery(supId, day)){
                setError(String.format("Days until delivery updated to %s", day));
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(supId)});
            }
            else{
                setError("Day wasn't updated!");
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(supId)});
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void changeRoutineDays(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String days = req.getParameter("agreementDays2");
            int supId = getSupplierId(req);
            if(days.length() > 0 && controller.setDaysOfDelivery(supId, days) ){
                setError(String.format("Days updated to %s", days));
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(supId)});
            }
            else{
                setError("Days had not been updated!");
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void changeAgreementType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int type = Integer.parseInt(req.getParameter("agreementType"));
            String days = req.getParameter("agreementDays");
            int supId = getSupplierId(req);

            if((type == 1 || type == 2 || type == 3) && controller.changeAgreementType(supId, type, days)){
                setError("Agreement type was changed successfully");
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
            }
            else{
                setError("Agreement wasn't changed!");
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
    }


    private void removeItemFromAgreement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int itemId = Integer.parseInt(req.getParameter("itemId"));
            int supId = getSupplierId(req);
            //int itemId = controller.getMatchingProductIdForIdBySupplier(idBySupplier);
            if(controller.deleteItemFromAgreement(supId, itemId) ){
                setError(String.format("Deleted Item %d", itemId));
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
            }
            else{
                setError("Item wasn't deleted!");
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void showAllItems(HttpServletRequest req, HttpServletResponse resp, int supId) throws IOException {

        try {
            List<ServiceItemObject> list = controller.itemsFromOneSupplier(supId);
            if(list.isEmpty()){
                setError("[NO ITEMS ARE IN THE AGREEMENT]");
                refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
                return;
            }
            for(ServiceItemObject orderObject : list) {
                PrintWriter out = resp.getWriter();
                out.println(orderObject.toString() + "<br><br>");
            }

        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"},  new String[]{String.valueOf(getSupplierId(req))});
        }
    }



    private int getSupplierId(HttpServletRequest req) throws IOException {
        return Integer.parseInt(getParamVal(req,"supId"));
    }


}
