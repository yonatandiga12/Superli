package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Storekeeper;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class ManageOrders extends Screen {

    private static final String greet = "Manage orders";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Storekeeper.class));


    public ManageOrders() {
        super(greet,ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);


        printOrderIds(resp,req);
        printForm(resp, new String[] {"supplierId","storeId"}, new String[]{"Supplier ID", "Store ID"}, new String[]{"Add Order"});
        printForm(resp, new String[] {"orderId1"}, new String[]{"Order ID"}, new String[]{"Remove Order"});
        printForm(resp, new String[] {"orderId2"}, new String[]{"Order ID"}, new String[]{"Edit Order"});
        printForm(resp, new String[] {"orderId3"}, new String[]{"Order ID"}, new String[]{"View Order"});
        printForm(resp, new String[] {"supplierId"}, new String[]{"Supplier ID"}, new String[]{"View Orders From Supplier"});

        String val;

        if ((val = getParamVal(req, "viewAllOrders")) != null && val.equals("true")) {
            String supId = getParamVal(req, "supId");
            if (supId != null)
                showAllOrders(req, resp, supId);
        } else if ((val = getParamVal(req, "viewOrder")) != null && val.equals("true")) {
            String orderId = getParamVal(req, "orderId");
            if (orderId != null)
                printOrder(req, resp, orderId);
        }
        handleError(resp);
    }

    private void printOrderIds(HttpServletResponse resp, HttpServletRequest req) throws IOException {
        try {
            PrintWriter out = resp.getWriter();
            out.println("<h2>");
            ArrayList<Integer> supplierIds = controller.getSuppliersID();
            for(Integer id : supplierIds){
                List<Integer> orderIds = controller.geOrdersID(id);
                out.print(String.format("Order from Supplier %s  : ", id));
                out.println(orderIds + "<br>");
            }
            out.println("</h2>");
        } catch (Exception e) {
            setError("No orders available!");
            refresh(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        if (isButtonPressed(req, "Add Order")){
            addOrder(req, resp);
        }
        else if(isButtonPressed(req, "Remove Order")){
            removeOrder(req, resp);
        }
        else if(isButtonPressed(req, "Edit Order")){
            editOrder(req, resp);
        }
        else if(isButtonPressed(req, "View Order")){
            String orderId = req.getParameter("orderId3");
            redirect(resp, ManageOrders.class, new String[]{"viewOrder", "orderId"}, new String[]{"true", orderId});
        }
        else if(isButtonPressed(req,"View Orders From Supplier")){
            String supplierId = req.getParameter("supplierId");
            redirect(resp, ManageOrders.class, new String[]{"viewAllOrders","supId"}, new String[]{"true",supplierId});
        }

    }



    private void showAllOrders(HttpServletRequest req, HttpServletResponse resp, String supplierIdString) throws IOException {
        try {
            int supplierId = Integer.parseInt(supplierIdString);
            ArrayList<ServiceOrderObject> r = controller.getAllOrdersForSupplier(supplierId);
            if(r != null && r.size() > 0){
                PrintWriter out = resp.getWriter();
                for(ServiceOrderObject orderObject : r){
                    out.println(orderObject.toString() + "<br><br>");
                }
            }
            else{
                setError("No orders available!");
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

    private void removeOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    private void editOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId2"));
            int supplierId = controller.getSupplierWIthOrderID(orderId);
            if(supplierId != -1)
                redirect(resp, EditOrder.class, new String[]{"supId","orderId"},  new String[]{String.valueOf(supplierId),String.valueOf(orderId) });
            else{
                setError(String.format("Didn't found Supplier with Order number %d", orderId));
                refresh(req, resp);
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp);
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp);
        }
    }

    private void printOrder(HttpServletRequest req, HttpServletResponse resp, String orderIdString) throws IOException {
        try {
            int orderId = Integer.parseInt(orderIdString);
            ServiceOrderObject result = controller.getOrder(orderId);
            if(result != null){
                PrintWriter out = resp.getWriter();
                out.println(result.toString() + "<br><br>");
            }
            else{
                setError("Something went wrong, try again later");
                //refresh(req, resp);
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

    private void addOrder(HttpServletRequest req, HttpServletResponse resp/*, String storeIdString, String supplierIdString*/) throws IOException {
        try {
            int supplierId = Integer.parseInt(req.getParameter("supplierId"));
            int storeId = Integer.parseInt(req.getParameter("storeId"));
            int orderId = controller.order(supplierId, storeId);
            if(orderId != -1){
                redirect(resp, AddOrderItem.class, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
            }
            else{
                setError("Order wasn't added!");
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


}
