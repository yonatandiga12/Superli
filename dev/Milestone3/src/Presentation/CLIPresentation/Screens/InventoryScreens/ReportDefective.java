package Presentation.CLIPresentation.Screens.InventoryScreens;
import Domain.Service.Objects.InventoryObjects.DefectiveItemReport;
import Domain.Service.util.Result;
import Globals.Pair;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.Arrays;
import java.util.stream.Stream;

public class ReportDefective extends Screen {
    private static final String[] menuOptions = {
            "Report Damaged Items",  //1
            "Report Expired Items",  //2
            "exit"        //3
    };

    public ReportDefective(Screen caller, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
    }

    public void run() {
        System.out.println("\nPlease choose action");
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
                reportDamaged();
                break;
            case 2:
                reportExpired();
                break;
            case 3:
                endRun();
        }
    }

    private String printDefective(DefectiveItemReport d) {
        return "Product ID: " + d.getProductID() + "\nStore ID: " + d.getStoreID() + "\nAmount Thrown: " + d.getAmount() + "\nEmployee ID: " + d.getEmployeeID() + "\nLocation: " + ((d.getInWarehouse()) ? ("Warehouse") : ("Store")) + "\nDescription: " + d.getDescription() + "\nDate: " + d.getDate() + "\nDefect: " + d.getDefect() + "\n";
    }

    private void reportDamaged() {
        int store = getStoreID();
        System.out.println("Please enter 0 for report in warehouse or any other number for report in store");
        int inWarehouse = scanner.nextInt();
        System.out.println("Which product is damaged? (insert ID)");
        int productID = scanner.nextInt();
        System.out.println("How much of the product is damaged?");
        int amount = scanner.nextInt();
        System.out.println("Please enter your ID");
        int employeeID = scanner.nextInt();
        System.out.println("Please describe the damage");
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        String description = scanner.nextLine();
        Result<Pair<DefectiveItemReport, String>> r = controller.reportDamaged(store, productID, amount, employeeID, description, inWarehouse==0);
        if (r.isError())
            System.out.println(r.getError());
        else {
            DefectiveItemReport dir = r.getValue().getLeft();
            System.out.println(printDefective(dir));
            if (r.getValue().getRight()!=null)
                System.out.println(r.getValue().getRight());
        }
    }

    private void reportExpired() {
        int store = getStoreID();
        System.out.println("Please enter 0 for report in warehouse or any other number for report in store");
        int inWarehouse = scanner.nextInt();
        System.out.println("Which product is expired? (insert ID)");
        int productID = scanner.nextInt();
        System.out.println("How much of the product is expired?");
        int amount = scanner.nextInt();
        System.out.println("Please enter your ID");
        int employeeID = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        System.out.println("Please add a description (not mandatory)");
        String description = scanner.nextLine();
        Result<Pair<DefectiveItemReport, String>> r = controller.reportExpired(store, productID, amount, employeeID, description, inWarehouse==0);
        if (r.isError())
            System.out.println(r.getError());
        else {
            DefectiveItemReport eir = r.getValue().getLeft();
            System.out.println(printDefective(eir));
            if (r.getValue().getRight()!=null)
                System.out.println(r.getValue().getRight());
        }
    }
}
