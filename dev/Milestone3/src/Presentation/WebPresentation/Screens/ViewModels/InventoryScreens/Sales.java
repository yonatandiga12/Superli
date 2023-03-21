package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.Objects.InventoryObjects.Category;
import Domain.Service.Objects.InventoryObjects.Product;
import Domain.Service.Objects.InventoryObjects.Sale;
import Domain.Service.util.Result;
import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Sales extends Screen{

    private static final String greet = "Sales";
    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>();

    private static final String viewHistoryByProductButton = "View Sale History By Product";
    private static final String viewHistoryByCategoryButton = "View Sale History By Category";
    private static final String addButton = "Add Sale";
    private static final String deleteButton = "Delete Sale";

    public Sales() {
        super(greet,ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printForm(resp, new String[] {"ID"}, new String[]{"Product ID"}, new String[]{viewHistoryByProductButton});
        printForm(resp, new String[] {"ID"}, new String[]{"Category ID"}, new String[]{viewHistoryByCategoryButton});
        printForm(resp, new String[] {"ID"}, new String[]{"Sale ID"}, new String[]{deleteButton});
        //categories, products, percent, start, end
        printForm(resp, new String[] {"categories", "products", "percent", "start", "end"},
                new String[]{"Categories (1,2,..7)", "Products (1,2..7)", "Percent", "Start Date (yyyy-mm-dd)", "End Date (yyyy-mm-dd)"}, new String[]{addButton});
        printInstructions(resp);
        printSales(resp);
        printProducts(resp);
        printCategories(resp);
        handleError(resp);
    }

    private void printInstructions(HttpServletResponse resp) {
        try {
            PrintWriter out = resp.getWriter();
            out.println("Enter 0 for no products or no categories<br><br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, deleteButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Admin.class, Logistics_Manager.class)))) {
                setError("You have no permission to delete sale");
                refresh(req, resp);
                return;
            }
            try {
                int saleID = Integer.parseInt(req.getParameter("ID"));
                if(controller.removeSale(saleID).isOk()) {
                    setError("Sale " + saleID + " has been deleted");
                    refresh(req, resp);
                }
                else{
                    setError("Sale ID " + saleID + " doesn't exist!");
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
        else if(isButtonPressed(req, addButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Admin.class)))) {
                setError("You have no permission to add sale");
                refresh(req, resp);
                return;
            }
            try {
                List<Integer> categories = (Arrays.asList(req.getParameter("categories").split(","))).stream().map(Integer::parseInt).collect(Collectors.toList());
                List<Integer> products = (Arrays.asList(req.getParameter("products").split(","))).stream().map(Integer::parseInt).collect(Collectors.toList());
                int percent = Integer.parseInt(req.getParameter("percent"));
                LocalDate startDate = LocalDate.parse(req.getParameter("start"));
                LocalDate endDate = LocalDate.parse(req.getParameter("end"));
                while (categories.contains(new Integer(0)))
                    categories.remove(new Integer(0));
                while (products.contains(new Integer(0)))
                    products.remove(new Integer(0));
                if (categories.isEmpty() && products.isEmpty()) {
                    setError("You must enter at least 1 product or 1 category");
                    refresh(req, resp);
                }
                else if(controller.addSale(categories, products, percent, startDate, endDate).isOk()) {
                    setError("Sale has been added successfully");
                    refresh(req, resp);
                }
                else{
                    setError("Sale wasn't added");
                    refresh(req, resp);
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp);
            }catch (IllegalFormatException e1){
                setError("Please enter a date: yyyy-mm-dd");
                refresh(req, resp);
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
        else if(isButtonPressed(req, viewHistoryByProductButton)){
            if (!isAllowed(req, resp, SaleHistory.ALLOWED)) {
                setError("You have no permission to view sale history");
                refresh(req, resp);
                return;
            }
            try {
                String productIDstr = req.getParameter("ID");
                int productID = Integer.parseInt(productIDstr);
                if (controller.getProduct(productID).isError())
                {
                    setError("Product " + productIDstr + " doesn't exist");
                    refresh(req, resp);
                    return;
                }
                Result<Set<Sale>> sales = controller.getSaleHistoryByProduct(productID);
                if(sales.isOk() && sales.getValue().size()>0)
                    redirect(resp, Presentation.WebPresentation.Screens.ViewModels.InventoryScreens.SaleHistory.class, new String[]{"product or category", "ID"}, new String[]{"product", productIDstr});
                else if (sales.isOk()) {
                    setError("Product " + productIDstr + " had no sales in the past");
                    refresh(req, resp);
                }
                else {
                    setError("Failed to get sales history for product " + productIDstr);
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
        else if(isButtonPressed(req, viewHistoryByCategoryButton)){
            if (!isAllowed(req, resp, SaleHistory.ALLOWED)) {
                setError("You have no permission to view sale history");
                refresh(req, resp);
                return;
            }
            try {
                String categoryIDstr = req.getParameter("ID");
                int categoryID = Integer.parseInt(categoryIDstr);
                if (controller.getCategory(categoryID).isError())
                {
                    setError("Category " + categoryIDstr + " doesn't exist");
                    refresh(req, resp);
                    return;
                }
                Result<Set<Sale>> sales = controller.getSaleHistoryByCategory(categoryID);
                if(sales.isOk() && sales.getValue().size()>0)
                    redirect(resp, Presentation.WebPresentation.Screens.ViewModels.InventoryScreens.SaleHistory.class, new String[]{"product or category", "ID"}, new String[]{"category", categoryIDstr});
                else if (sales.isOk()) {
                    setError("Category " + categoryIDstr + " had no sales in the past");
                    refresh(req, resp);
                }
                else {
                    setError("Failed to get sales history for category " + categoryIDstr);
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
    }

    private void printSales(HttpServletResponse resp) {
        try {
            List<Domain.Service.Objects.InventoryObjects.Sale> sales = controller.getRemovableSales().getValue();
            PrintWriter out = resp.getWriter();
            sales.sort(Comparator.comparingInt(Domain.Service.Objects.InventoryObjects.Sale::getSaleID));
            out.println("REMOVABLE SALES:<br>");
            for (Domain.Service.Objects.InventoryObjects.Sale s: sales) {
                out.println("Sale ID: " + s.getSaleID() + " | Sale Percentage: " + s.getPercent() + " | Sale Start Date: " + s.getStartDate() + " | Sale End Date: " + s.getEndDate() + " | Sale Categories: " + s.getCategories() + " | Sale Products: " + s.getProducts() + "<br>");
            }
            out.println("<br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printProducts(HttpServletResponse resp) {
        try {
            List<Domain.Service.Objects.InventoryObjects.Product> products = controller.getProducts().getValue();
            PrintWriter out = resp.getWriter();
            products.sort(Comparator.comparingInt(Product::getId));
            out.println("PRODUCTS:<br>");
            for (Product p: products) {
                out.println(p.getName() + ": " + p.getId() + "<br>");
            }
            out.println("<br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCategories(HttpServletResponse resp) {
        try {
            List<Domain.Service.Objects.InventoryObjects.Category> categories = controller.getCategories().getValue();
            PrintWriter out = resp.getWriter();
            categories.sort(Comparator.comparingInt(Category::getID));
            out.println("CATEGORIES:<br>");
            for (Domain.Service.Objects.InventoryObjects.Category c: categories) {
                out.println(c.getName() + ": " + c.getID() + "<br>");
            }
            out.println("<br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}