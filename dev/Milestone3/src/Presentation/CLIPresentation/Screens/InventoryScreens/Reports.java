package Presentation.CLIPresentation.Screens.InventoryScreens;


import Presentation.CLIPresentation.Screens.Screen;

import java.util.Arrays;
import java.util.stream.Stream;

public class Reports extends Screen {

    private static final String[] menuOptions = {
            "View Stock Reports",  //1
            "View Defective Item Reports",                  //2
            "Report Defective Items",          //3
            //"View History of purchases from suppliers",      //4
            "exit" //4
    };

    public Reports(Screen caller, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
    }

    public void run() {
        System.out.println("\nWelcome to the Reports Menu");
        switch (runMenu()) {
            case 1:
                new Thread(new StockReports(this, new String[]{})).start();
                break;
            case 2:
                new Thread(new DefectiveItemReports(this, new String[]{})).start();
                break;
            case 3:
                new Thread(new ReportDefective(this, new String[]{})).start();
                break;
            case 4:
                endRun();
        }
    }

//    private void getDiscountFromSupplierHistory() {
//        System.out.println("Please insert product ID for which you would like to see history");
//        int id = scanner.nextInt();
//        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
//        Result<List<PurchaseFromSupplierReport>> r = controller.getDiscountFromSupplierHistory(id);
//        if (r.isError())
//            System.out.println(r.getError());
//        else {
//            List<PurchaseFromSupplierReport> purchaseFromSupplierReports = r.getValue();
//            for (PurchaseFromSupplierReport dr : purchaseFromSupplierReports)
//                System.out.println(dr);
//            if (purchaseFromSupplierReports.isEmpty())
//                System.out.println("there are no discounts from suppliers for this product in the system");
//        }
//    }

//    private void getPurchaseFromSupplierHistory() {
//        System.out.println("Please insert product ID for which you would like to see history");
//        int id = scanner.nextInt();
//        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
//        Result<List<PurchaseFromSupplierReport>> r = controller.getPurchaseFromSupplierHistory(id);
//        if (r.isError())
//            System.out.println(r.getError());
//        else {
//            List<PurchaseFromSupplierReport> purchaseFromSupplierReports = r.getValue();
//            for (PurchaseFromSupplierReport dr : purchaseFromSupplierReports)
//                System.out.println(dr);
//            if (purchaseFromSupplierReports.isEmpty())
//                System.out.println("there are no purchases from suppliers for this product in the system");
//        }
//    }
}
