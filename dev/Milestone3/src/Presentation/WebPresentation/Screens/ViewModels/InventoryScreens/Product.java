package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.util.Result;
import Presentation.WebPresentation.Screens.Models.HR.*;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Product extends Screen {

    private static final String greet = "Product";
    private static final String setPriceButton = "Set price";
    private static final String setMinButton = "Set min";
    private static final String setTargetButton = "Set target";
    private static final String setNameButton = "Set name";
    private static final String changeCategoryButton = "Change category";

    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(0);

    private int productID;

    public Product() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        String s = getParamVal(req, "ProductID");
        if (s!=null) {
            productID = Integer.parseInt(s);
            Result<Domain.Service.Objects.InventoryObjects.Product> product = controller.getProduct(productID);
            if (product.isOk() && product.getValue() != null) {
                printForm(resp, new String[]{"new price"}, new String[]{"New price"}, new String[]{setPriceButton});
                printForm(resp, new String[]{"new min"}, new String[]{"New min"}, new String[]{setMinButton});
                printForm(resp, new String[]{"new target"}, new String[]{"New target"}, new String[]{setTargetButton});
                printForm(resp, new String[]{"new name"}, new String[]{"New name"}, new String[]{setNameButton});
                printForm(resp, new String[]{"new category id"}, new String[]{"New category ID"}, new String[]{changeCategoryButton});
                printProduct(resp, product.getValue());
                printCategories(resp);
            }
        }
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, setPriceButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Transport_Manager.class, HR_Manager.class, Admin.class)))) {
                setError("You have no permission to set product price");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                return;
            }
            try {
                double newPrice = Double.parseDouble(req.getParameter("new price"));
                if(controller.editProductPrice(productID, newPrice).isOk()) {
                    setError("Changed price of product " + productID + " to " + newPrice);
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
                else{
                    setError("Price hasn't been changed");
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
        }
        else if (isButtonPressed(req, setMinButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Admin.class)))) {
                setError("You have no permission to set product minimum amount");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                return;
            }
            try {
                int newMin = Integer.parseInt(req.getParameter("new min"));
                if(controller.changeProductMin(1, productID, newMin).isOk()) {
                    setError("Changed minimum amount of product " + productID + " to " + newMin);
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
                else{
                    setError("Minimum amount hasn't been changed");
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
        }
        else if (isButtonPressed(req, setTargetButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Admin.class)))) {
                setError("You have no permission to set product target amount");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                return;
            }
            try {
                int newTarget = Integer.parseInt(req.getParameter("new target"));
                if(controller.changeProductTarget(1, productID, newTarget).isOk()) {
                    setError("Changed target amount of product " + productID + " to " + newTarget);
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
                else{
                    setError("Target amount hasn't been changed");
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
        }
        else if (isButtonPressed(req, setNameButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Admin.class)))) {
                setError("You have no permission to set product name");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                return;
            }
            try {
                String newName = req.getParameter("new name");
                if(controller.editProductName(productID, newName).isOk()) {
                    setError("Product name has been changed to: " + newName);
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
                else{
                    setError("Product name hasn't been changed");
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
        }
        else if (isButtonPressed(req, changeCategoryButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Admin.class)))) {
                setError("You have no permission to change product's category");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                return;
            }
            try {
                int newCategoryID = Integer.parseInt(req.getParameter("new category id"));
                if (controller.getCategory(newCategoryID).isError()) {
                    setError("Category with ID: " + newCategoryID + " doesn't exists");
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                    return;
                }
                if(controller.moveProductToCategory(productID, newCategoryID).isOk()) {
                    setError("Changed category of product " + productID + " to " + newCategoryID);
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
                else{
                    setError("Product's category hasn't been changed");
                    refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"ProductID"}, new String[]{Integer.toString(productID)});
            }
        }
    }
    private void printProduct(HttpServletResponse resp, Domain.Service.Objects.InventoryObjects.Product p) {
        try {
            PrintWriter out = resp.getWriter();
            out.println("Product ID: " + p.getId() + "<br>");
            out.println("Product name: " + p.getName() + "<br>");
            out.println("Category: " + controller.getCategory(p.getCategoryID()).getValue().getName() + "<br>");
            out.println("Original price: " + p.getOriginalPrice() + "<br>");
            out.println("Current price: " + p.getCurrentPrice() + "<br>");
            out.println("Weight: " + p.getWeight() + "<br>");
            out.println("Manufacturer: " + p.getManufacturer() + "<br>");
            out.println("Minimum amount: " + controller.getMin(1, p.getId()) + "<br>");
            out.println("Target amount: " + controller.getTarget(1, p.getId()) + "<br><br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void printCategories(HttpServletResponse resp) {
        try {
            List<Domain.Service.Objects.InventoryObjects.Category> categories = controller.getCategories().getValue();
            PrintWriter out = resp.getWriter();
            categories.sort(Comparator.comparingInt(Domain.Service.Objects.InventoryObjects.Category::getID));
            for (Domain.Service.Objects.InventoryObjects.Category c: categories) {
                out.println(c.getName() + ": " + c.getID() + "<br>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
