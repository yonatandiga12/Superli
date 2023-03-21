package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Screen;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class RemoveViewOrder extends Screen {



    public RemoveViewOrder(String greet, Set<Class<? extends Employee>> allowed) {
        super(greet, allowed);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    protected void printOrderIds(HttpServletResponse resp) {
        try {
            PrintWriter out = resp.getWriter();
            out.println("<h2>");
            ArrayList<Integer> supplierIds = controller.getSuppliersID();
            for(Integer id : supplierIds){
                List<Integer> orderIds = controller.geOrdersID(id);
                out.print(String.format("Order from Supplier %s  : ", id));
                out.println(orderIds);
            }
            out.println("</h2>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;


    protected void removeOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId1"));
            if(controller.removeOrder(orderId) ){
                setError(String.format("Order %d was removed", orderId));
                refresh(req, resp);
            }
            else{
                setError("Order wasn't removed!");
                refresh(req, resp);
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }
    }



    protected void printOrder(HttpServletRequest req, HttpServletResponse resp, String orderIdString) throws IOException {
        try {
            int orderId = Integer.parseInt(orderIdString);
            ServiceOrderObject result = controller.getOrder(orderId);
            if(result != null){

                PrintWriter out = resp.getWriter();
                out.println(result.toString());
            }
            else{
                setError("Something went wrong, try again later");
                refresh(req, resp);
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            //refresh(req, resp);
        }
        catch (Exception e) {
            setError(e.getMessage());
            //refresh(req, resp);
        }
    }
}
