package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.Objects.InventoryObjects.Category;
import Domain.Service.util.Result;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class StockReport  extends Screen {

    private static final String greet = "Stock Reports";

    private static final String minButton = "Under Min";
    private static final String allButton = "All";
    private static final String catButton = "By Category";

    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>();

    public StockReport() { super(greet, ALLOWED); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        String underMin = getParamVal(req, "underMin");
        String all = getParamVal(req, "all");
        String cats = getParamVal(req, "cats");
        if (underMin!=null) {
            resp.getWriter().println("Showing stock reports of products under min value<br><br>");
            printOptions(resp);
            printReports(req, resp, 0);
        }
        else if (all!=null) {
            resp.getWriter().println("Showing all stock reports<br><br>");
            printOptions(resp);
            printReports(req, resp, 1);
        }
        else if (cats!=null) {
            resp.getWriter().println(String.format("Showing Stock reports of products in categories: %s<br><br>", cats));
            printOptions(resp);
            printReports(req, resp, 2);
        }
        else {
            printOptions(resp);
        }
        handleError(resp);
    }

    private void printOptions(HttpServletResponse resp) throws IOException {
        printMenu(resp, new String[]{minButton, allButton});
        printForm(resp, new String[]{"cats"}, new String[] {"Categories: 1,2,4..."}, new String[]{catButton});
    }

    private void printReports(HttpServletRequest req, HttpServletResponse resp, int filter) throws IOException {
        PrintWriter out = resp.getWriter();
        Result<List<Domain.Service.Objects.InventoryObjects.StockReport>> r;
        switch (filter) {
            case 0:
                r = controller.getMinStockReport();
                if (r.isOk()) {
                    List<Domain.Service.Objects.InventoryObjects.StockReport> reports = r.getValue();
                    for (Domain.Service.Objects.InventoryObjects.StockReport s : reports) {
                        out.println(s);
                        out.println("<br>");
                    }
                } else {
                    setError(r.getError());
                    refresh(req, resp);
                }
                break;
            case 1:
                r = controller.storeStockReport(Arrays.asList(1), getCatIDs());
                if (r.isOk()) {
                    List<Domain.Service.Objects.InventoryObjects.StockReport> reports = r.getValue();
                    for (Domain.Service.Objects.InventoryObjects.StockReport s : reports) {
                        out.println(s);
                        out.println("<br>");
                    }
                } else {
                    setError(r.getError());
                    refresh(req, resp);
                }
                break;
            case 2:
                r = controller.storeStockReport(Arrays.asList(1), Arrays.stream(getParamVal(req, "cats").split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList()));
                if (r.isOk()) {
                    List<Domain.Service.Objects.InventoryObjects.StockReport> reports = r.getValue();
                    for (Domain.Service.Objects.InventoryObjects.StockReport s : reports) {
                        out.println(s);
                        out.println("<br>");
                    }
                } else {
                    setError(r.getError());
                    refresh(req, resp);
                }
                break;
        }

    }

    protected List<Integer> getCatIDs() {
        List<Integer> cIDs = new ArrayList<>();
        List<Domain.Service.Objects.InventoryObjects.Category> c = controller.getCategories().getValue();
        for (Category cat: c) {
            cIDs.add(cat.getID());
        }
        return cIDs;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        switch (getIndexOfButtonPressed(req)) {
            case 0:
                refresh(req, resp, new String[] {"underMin"}, new String[] {"placeHolder"});
                break;
            case 1:
                refresh(req, resp, new String[] {"all"}, new String[] {"placeHolder"});
                break;
        }
        if (isButtonPressed(req, catButton)) {
            try {
                int[] cats = Arrays.stream(getParamVal(req, "cats").split(",")).mapToInt(Integer::parseInt).toArray();
                refresh(req, resp, new String[] {"cats"}, new String[] {getParamVal(req, "cats")});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
    }
}
