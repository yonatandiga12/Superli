package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Storekeeper;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class EditCard extends Screen {


    private static final String greet = "Edit Card";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));


    public EditCard() {
        super(greet, ALLOWED);
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        int supId = getSupplierId(req);

        resp.getWriter().println("<h2>Edit card for Supplier " + supId + ".</h2><br>");


        printForm(resp, new String[]{"bankNumber"}, new String[]{"Bank Number"}, new String[]{"Update Bank Number"});
        printForm(resp, new String[]{"address"}, new String[]{"Address"}, new String[]{"Update Address"});
        printForm(resp, new String[]{"name"}, new String[]{"name"}, new String[]{"Update Name"});
        printForm(resp, new String[]{"payingAgreement"}, new String[]{"Paying Agreement"}, new String[]{"Update Paying Agreement"});


        handleError(resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        if (isButtonPressed(req, "Update Bank Number")) {
            updateBankNumber(req,resp);
        }
        else if(isButtonPressed(req, "Update Address")){
            updateAddress(req,resp);
        }
        else if(isButtonPressed(req, "Update Name")){
            updateName(req,resp);
        }
        else if(isButtonPressed(req, "Update Paying Agreement")){
            updatePayingAgreement(req,resp);
        }
    }

    private void updateBankNumber(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int num = Integer.parseInt(req.getParameter("bankNumber"));
            int supplierId = getSupplierId(req);
            if(controller.updateSupplierBankNumber(supplierId, num) ){
                setError(String.format("Bank number updated to %d", num));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
            else{
                setError("Bank number wasn't updated!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});        }
    }

    private void updateAddress(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("address");
            int supplierId = getSupplierId(req);
            if(!name.equals("") && controller.updateSupplierAddress(supplierId, name) ){
                setError(String.format("Address updated to %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
            else{
                setError("Address wasn't updated!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void updateName(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            int supplierId = getSupplierId(req);
            if(!name.equals("") && controller.updateSupplierName(supplierId, name) ){
                setError(String.format("Name updated to %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
            else{
                setError("Name wasn't updated!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void updatePayingAgreement(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("payingAgreement");
            int supplierId = getSupplierId(req);
            if(!name.equals("") && controller.updateSupplierPayingAgreement(supplierId, name) ){
                setError(String.format("Paying Agreement updated to %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
            else{
                setError("Paying Agreement wasn't updated!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supplierId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }



    private int getSupplierId(HttpServletRequest req) {
        return Integer.parseInt(getParamVal(req,"supId"));

    }

}
