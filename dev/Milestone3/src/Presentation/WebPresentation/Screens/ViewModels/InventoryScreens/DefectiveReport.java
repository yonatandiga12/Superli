package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.Objects.InventoryObjects.DefectiveItemReport;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DefectiveReport extends Screen {
    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(0);

    private static final String greet = "Report";

    public DefectiveReport() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
//        String defectiveOrDamagedOrExpired = "defective";//getParamVal(req, "defective or damaged or expired");
//        String byStoreOrByCategoryOrByProduct = "by product";//getParamVal(req, "by store or by category or by product");
//        LocalDate startDate = LocalDate.parse("2020-01-01");//LocalDate.parse(getParamVal(req, "start date"));
//        LocalDate endDate = LocalDate.parse("2023-12-31");//LocalDate.parse(getParamVal(req, "end date"));
//        List<Integer> IDs = new ArrayList<>();//(Arrays.asList(getParamVal(req, "IDs").split(","))).stream().map(Integer::parseInt).collect(Collectors.toList());
//        IDs.add(1);
        String defectiveOrDamagedOrExpired = getParamVal(req, "defective or damaged or expired");
        String byStoreOrByCategoryOrByProduct = getParamVal(req, "by store or by category or by product");
        LocalDate startDate =LocalDate.parse(getParamVal(req, "start date"));
        LocalDate endDate = LocalDate.parse(getParamVal(req, "end date"));
        List<Integer> IDs = (Arrays.asList(getParamVal(req, "IDs").split(","))).stream().map(Integer::parseInt).collect(Collectors.toList());
        List<DefectiveItemReport> defectiveItemReports = null;
        switch (defectiveOrDamagedOrExpired + " " + byStoreOrByCategoryOrByProduct) {
            case "defective by store":
                defectiveItemReports = controller.getDefectiveItemsByStore(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "defective by category":
                defectiveItemReports = controller.getDefectiveItemsByCategory(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "defective by product":
                defectiveItemReports = controller.getDefectiveItemsByProduct(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "damaged by store":
                defectiveItemReports = controller.getDamagedItemsByStore(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "damaged by category":
                defectiveItemReports = controller.getDamagedItemsByCategory(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "damaged by product":
                defectiveItemReports = controller.getDamagedItemsByProduct(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "expired by store":
                defectiveItemReports = controller.getExpiredItemsByStore(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "expired by category":
                defectiveItemReports = controller.getExpiredItemsByCategory(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
            case "expired by product":
                defectiveItemReports = controller.getExpiredItemsByProduct(startDate, endDate, IDs).isOk() ? controller.getDefectiveItemsByStore(startDate, endDate, IDs).getValue() : null;
                break;
        }
        printReport(resp, defectiveItemReports);
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
    }

    private void printReport(HttpServletResponse resp, List<DefectiveItemReport> defectiveItemReports) {
        try {
            PrintWriter out = resp.getWriter();
            if (defectiveItemReports==null)
            {
                out.println("Check arguments");
                return;
            }
            for (DefectiveItemReport dir: defectiveItemReports) {
                out.println(dir.toString() + "<br>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
