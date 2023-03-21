package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

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
import java.util.NoSuchElementException;
import java.util.Set;

public class AddOrderItem extends Screen {


    private static final String greet = "Add Items to order";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Storekeeper.class));


    public AddOrderItem() {
        super(greet,ALLOWED);

    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        int supId = getSupplierId(req);
        int orderId = getOrderId(req);

        printForm(resp, new String[] {"itemId", "quantity"}, new String[]{"Product ID", "Quantity"}, new String[]{"Add Item"});


        handleError(resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        int supplierId = getSupplierId(req);
        int orderId = getOrderId(req);

        if (isButtonPressed(req, "Add Item")) {
            try {
                int itemId = Integer.parseInt(req.getParameter("itemId"));
                int quantity = Integer.parseInt(req.getParameter("quantity"));

                if(controller.orderItemExistsInOrder(supplierId, orderId, itemId)){
                    setError(String.format("Item %d already exists in Order %d!. If you want to add, use Update quantity", itemId, orderId));
                    refresh(req, resp, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
                }
                else if(quantity < 0) {
                    setError(String.format("Quantity should be POSITIVE!", itemId, orderId));
                    refresh(req, resp, new String[]{"supId", "orderId"}, new String[]{String.valueOf(supplierId), String.valueOf(orderId)});
                }
                else {
                    if (controller.addItemToOrder(supplierId, orderId, itemId, quantity)) {
                        setError(String.format("Item %d added to Order %d!", itemId, orderId));
                        refresh(req, resp, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
                    } else {
                        setError("Item wasn't added!");
                        refresh(req, resp, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
                    }
                }

            } catch(NoSuchElementException e) {
                setError("Something went wrong, please try again");
                refresh(req, resp, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
            } catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"supId","orderId"}, new String[]{String.valueOf(supplierId) ,String.valueOf(orderId)});
            }
        }

    }

    private int getOrderId(HttpServletRequest req) {
        return Integer.parseInt(getParamVal(req,"orderId"));

    }

    private int getSupplierId(HttpServletRequest req) {
        return Integer.parseInt(getParamVal(req,"supId"));

    }


}
