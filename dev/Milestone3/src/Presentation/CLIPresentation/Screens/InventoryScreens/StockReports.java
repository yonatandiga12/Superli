package Presentation.CLIPresentation.Screens.InventoryScreens;
import Domain.Service.Objects.InventoryObjects.StockReport;
import Domain.Service.util.Result;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockReports extends Screen {

    private static final String[] menuOptions = {
            "View General Stock Reports",  //1
            "View Minimum Stock Reports",  //2
            "exit"        //3
    };

    public StockReports(Screen caller, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
    }

    public void run() {
        System.out.println("\nWelcome to the Stock Report Menu");
        int option = 0;
        while (option != 3) {
            option = runMenu();
            try {
                if (option <= 3)
                    handleBaseOptions(option);
                else if (option == 9)
                    endRun();
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private void handleBaseOptions(int option) throws Exception {
        switch (option) {
            case 1:
                storeStockReport();
                break;
            case 2:
                getMinStockReport();
                break;
            case 3:
                endRun();
        }
    }

    private String printStockReport(StockReport s) {
        return "Store ID: " + s.getStoreID() + "\nProduct ID: " + s.getProductID() + "\nAmount In Store: " + s.getAmountInStore() + "\nAmount In Warehouse: " + s.getAmountInWarehouse() + "\nAmount In Deliveries: " + s.getAmountInDeliveries() + "\nMinimum Amount: " + s.getMinAmountInStore() + "\nTarget Amount: " + s.getTargetAmountInStore();
    }

    private void storeStockReport() {
        System.out.println("Please insert store IDs, separated by commas without spaces");
        System.out.println("Current store IDs are:");
        System.out.println(controller.getStoreIDs().getValue());
        System.out.println("If you would like all stores, you can enter a blank list");
        String input = scanner.nextLine();
        Collection<Integer> storeIDs;
        if (input.equals(""))
            storeIDs = controller.getStoreIDs().getValue();
        else
            storeIDs = Arrays.asList(input.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        System.out.println("Please insert category IDs, separated by commas without spaces");
        listCategoryIDs();
        System.out.println("If you would like all categories, you can enter a blank list");
        input = scanner.nextLine();
        List<Integer> categoryIDs;
        if (input.equals(""))
            categoryIDs = getCatIDs();
        else
            categoryIDs = Arrays.asList(input.split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        Result<List<StockReport>> r = controller.storeStockReport(storeIDs, categoryIDs);
        if (r.isError())
            System.out.println(r.getError());
        else {
            List<StockReport> stockReports = r.getValue();
            for (StockReport s : stockReports) {

                System.out.println(printStockReport(s) + "\n");
            }
            if (stockReports.isEmpty())
                System.out.println("the store has no products registered to it in the system or it has been removed");
        }
    }

    private void getMinStockReport() {
        Result<List<StockReport>> r = controller.getMinStockReport();
        if (r.isError())
            System.out.println(r.getError());
        else {
            List<StockReport> stockReports = r.getValue();
            for (StockReport s : stockReports)
                System.out.println(printStockReport(s) + "\n");
            if (stockReports.isEmpty())
                System.out.println("There are no products under the min amount");
        }
    }
}
