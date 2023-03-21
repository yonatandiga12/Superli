package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.Objects.InventoryObjects.Sale;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SaleHistory extends Screen{

    private static final String greet = "Sales History";
    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(0);

    public SaleHistory() {
        super(greet,ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        String productOrCategory = getParamVal(req, "product or category");
        int ID = Integer.parseInt(getParamVal(req, "ID"));
        Set<Sale> sales = productOrCategory.equals("product") ? controller.getSaleHistoryByProduct(ID).getValue() : controller.getSaleHistoryByCategory(ID).getValue();
        printSales(resp, sales);
        handleError(resp);
    }

    private void printSales(HttpServletResponse resp, Set<Sale> sales) {
        try {
            PrintWriter out = resp.getWriter();
            List<Sale> sortedSalesList = new ArrayList<>(sales);
            sortedSalesList.sort(Comparator.comparingInt(Domain.Service.Objects.InventoryObjects.Sale::getSaleID));
            for (Sale sale : sortedSalesList)
                out.println("Sale ID: " + sale.getSaleID() + "<br>Sale products: " + sale.getProducts() + "<br>Sale categories: " + sale.getCategories() + "<br>Sale percentage: " + sale.getPercent() + "<br>Sale start date: " + sale.getStartDate() + "<br>Sale end date" + sale.getEndDate() + "<br><br>");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
    }
}
