package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Domain.Service.util.Result;
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

public class EditOrder extends Screen {


    private static final String greet = "Edit orders";

    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList( Storekeeper.class));


    public EditOrder() {
        super(greet,ALLOWED);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);


        int supplierId = getSupplierId(req);
        int orderId = getOrderId(req);
        resp.getWriter().println("<h2>Edit Order " + orderId + " for Supplier " + supplierId + ".</h2><br>");

        printMenu(resp, new String[]{"Add an item"});
        printForm(resp, new String[] {"orderItemId1"}, new String[]{"Item ID"}, new String[]{"Remove Item"});
        printForm(resp, new String[] {"orderItemId", "quantity"}, new String[]{"Item ID", "Quantity"}, new String[]{"Update Quantity"});

        handleError(resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        if (isButtonPressed(req, "Remove Item")) {
            removeOrderItem(req, resp);
        }
        else if (isButtonPressed(req, "Update Quantity")) {
            updateQuantity(req, resp);
        }
        else if (getIndexOfButtonPressed(req) == 0) {
            addItem(req,resp);
        }
    }

    private void addItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderId = getOrderId(req);
        int supplierId = getSupplierId(req);
        redirect(resp, AddOrderItem.class, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});

    }


    private void updateQuantity(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int supplierId = getSupplierId(req);
            int orderId = getOrderId(req);

            int itemId = Integer.parseInt(req.getParameter("orderItemId"));
            int quantity = Integer.parseInt(req.getParameter("quantity"));
            //int itemId = controller.getMatchingProductIdForIdBySupplier(idBySupplier);

            Result<Boolean> r = controller.updateItemQuantityInOrder(supplierId, orderId, itemId, quantity);
            if(r.isOk()){
                setError(String.format("Item %d updated to quantity %d!", itemId, quantity));
                refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(supplierId),String.valueOf(orderId) });
            }
            else{
                setError(r.getError());
                refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(supplierId),String.valueOf(orderId) });
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(getSupplierId(req)),String.valueOf(getOrderId(req)) });
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(getSupplierId(req)),String.valueOf(getOrderId(req)) });
        }
    }

    private void removeOrderItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            int supplierId = getSupplierId(req);
            int orderId = getOrderId(req);

            int itemId = Integer.parseInt(req.getParameter("orderItemId1"));
            //int itemId = controller.getMatchingProductIdForIdBySupplier(idBySupplier);

            Result<Boolean> r = controller.removeItemFromOrder(supplierId, orderId, itemId);
            if(r.isOk()){
                setError(String.format("Item %d removed from order %d!", itemId , orderId));
                refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(supplierId),String.valueOf(orderId) });
            }
            else{
                setError("Item wasn't removed!");
                refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(supplierId),String.valueOf(orderId) });
            }
        } catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(getSupplierId(req)),String.valueOf(getOrderId(req)) });
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId","orderId"},  new String[]{String.valueOf(getSupplierId(req)),String.valueOf(getOrderId(req)) });
        }

    }

    private int getOrderId(HttpServletRequest req) {
        return Integer.parseInt(getParamVal(req,"orderId"));

    }

    private int getSupplierId(HttpServletRequest req) {
        return Integer.parseInt(getParamVal(req,"supId"));

    }



}
