package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Screen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

public abstract class SupplierMainMenu extends Screen {


    public SupplierMainMenu(String greet, Set<Class<? extends Employee>> allowed) {
        super(greet,allowed);
    }


    @Override
    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

    @Override
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;


    protected void viewSupplier(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            //
            int supplierId = Integer.parseInt(req.getParameter("ID"));
            if(controller.doesSupplierExists(supplierId)) {
                redirect(resp, ViewSupplier.class, new String[]{"supId"},new String[]{String.valueOf(supplierId)});
            }
            else{
                setError("No such supplier, please try again.");
                refresh(req, resp);
            }
        }catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }
    }


    protected void printSupplierIds(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        try {
            ArrayList<Integer> supplierIds = controller.getSuppliersID();
            PrintWriter out = resp.getWriter();
            out.println("<h2>");
            out.println("Suppliers available:  " + supplierIds);
            out.println("</h2>");
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }

    }


}
