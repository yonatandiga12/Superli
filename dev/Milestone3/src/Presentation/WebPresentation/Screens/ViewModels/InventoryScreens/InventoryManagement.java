package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.util.Result;
import Globals.Pair;
import Presentation.WebPresentation.Screens.Models.HR.*;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InventoryManagement extends Screen {
    private static final String greet = "Inventory Management";

    private static final String moveButton = "Move items";
    private static final String returnButton = "Return items";
    private static final String buyButton = "Buy items";
    private static final String reportDefectiveButton = "Report defective items";
    private static final String transportButton = "Transport Arrived";

    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>();

    public InventoryManagement() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp, new String[]{"product ID", "amount"}, new String[]{"Product ID", "Amount"}, new String[]{moveButton});
        printForm(resp, new String[]{"product ID", "amount", "date bought"}, new String[]{"Product ID", "Amount", "Date bought YYYY-MM-DD"}, new String[]{returnButton});
        printForm(resp, new String[]{"product ID", "amount"}, new String[]{"Product ID", "Amount"}, new String[]{buyButton});
        printForm(resp, new String[]{"product ID", "amount", "description", "store or warehouse", "damaged or expired"}, new String[]{"Product ID", "Amount", "Description", "Store or warehouse", "damaged or expired"}, new String[]{reportDefectiveButton});
        printForm(resp, new String[] {"transport"}, new String[]{"Transport ID"}, new String[]{transportButton});
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, moveButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class, Sorter.class)))) {
                setError("You have no permission to move items from warehouse to store");
                refresh(req, resp);
                return;
            }
            try {
                int storeID = 1;
                int productID = Integer.parseInt(req.getParameter("product ID"));
                int amount = Integer.parseInt(req.getParameter("amount"));
                controller.moveItems(storeID, productID, amount);
                setError(amount + " items of product " + productID + " has been moved from the store to the warehouse");
                refresh(req, resp);
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp);
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
        else if (isButtonPressed(req, returnButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Cashier.class, Sorter.class, Admin.class)))) {
                setError("You have no permission to return items to the store");
                refresh(req, resp);
                return;
            }
            try {
                int storeID = 1;
                int productID = Integer.parseInt(req.getParameter("product ID"));
                int amount = Integer.parseInt(req.getParameter("amount"));
                LocalDate dateBought = LocalDate.parse(req.getParameter("date bought"));
                Result<Double> refund = controller.returnItems(storeID, productID, amount, dateBought);
                if(refund.isOk()) {
                    setError(amount + " items of product " + productID + " has been returned to the store with " + refund.getValue() + " refund to the customer");
                    refresh(req, resp);
                }
                else{
                    setError("Items weren't returned");
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
        else if (isButtonPressed(req, buyButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Cashier.class)))) {
                setError("You have no permission to make a purchase");
                refresh(req, resp);
                return;
            }
            try {
                int storeID = 1;
                int productID = Integer.parseInt(req.getParameter("product ID"));
                int amount = Integer.parseInt(req.getParameter("amount"));
                Result<Pair<Double, String>> priceAndMessage = controller.buyItems(storeID, productID, amount);
                if(priceAndMessage.isOk()) {
                    double price = priceAndMessage.getValue().getLeft();
                    String message1 = priceAndMessage.getValue().getRight();
                    String message2 = amount + " items of product " + productID + " were bought in the store at a price of " + price;
                    setError(message2);
                    if (message1!=null)
                        setError(message2 + "<br>" + message1);
                    refresh(req, resp);
                }
                else{
                    setError("Items weren't bought");
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
        else if (isButtonPressed(req, reportDefectiveButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class, Sorter.class, Cashier.class, Admin.class)))) {
                setError("You have no permission to report defective items in the store or in the warehouse");
                refresh(req, resp);
                return;
            }
            try {
                int storeID = 1;
                int productID = Integer.parseInt(req.getParameter("product ID"));
                int amount = Integer.parseInt(req.getParameter("amount"));
                String description = req.getParameter("description");
                String location = req.getParameter("store or warehouse");
                String damagedOrExpired = req.getParameter("damaged or expired");
                int employeeID = Integer.parseInt(Login.getLoggedUser(req).id);
                if(damagedOrExpired.equals("damaged") ? controller.reportDamaged(storeID, productID, amount, employeeID, description, location.equals("warehouse")).isOk() : controller.reportExpired(storeID, productID, amount, employeeID, description, location.equals("warehouse")).isOk()) {
                    setError(amount + " items of product " + productID + " were found " + damagedOrExpired + " in the " + location + " by employee " + employeeID + "<br>Description: " + description);
                    refresh(req, resp);
                }
                else{
                    setError("Report failed");
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
        else if (isButtonPressed(req, transportButton)) {
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class)))) {
                setError("You have no permission to accept a transport");
                refresh(req, resp);
                return;
            }
            try {
                String transportID = getParamVal(req, "transport");
                if (transportID==null || transportID.equals("")) {
                    setError("Please enter transport ID");
                    refresh(req, resp);
                }
                else
                    redirect(resp, TransportArrived.class, new String[] {"transport ID"}, new String[] {transportID});
            } catch (NumberFormatException e1) {
                setError("Please enter transport ID");
                refresh(req, resp);
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
    }
}
