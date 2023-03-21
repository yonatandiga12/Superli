package Presentation.CLIPresentation.Screens.SupplierScreens;


import Presentation.CLIPresentation.Screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class ViewManufacturers extends Screen {

    private int supplierId;

    private static final String[] menuOptions = {
            "View Manufacturers",           //1
            "Add manufacturer",             //2
            "Remove manufacturer",          //3
            "Exit"                          //4
    };


    public ViewManufacturers(Screen caller, int supplierId) {
        super(caller, menuOptions);
        this.supplierId = supplierId;
    }


    @Override
    public void run() {
        System.out.println("\nHere you can view all the Supplier's Manufacturers info");
        int option = 0;
        while (option != 4) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        viewManufacturer();
                        break;
                    case 2:
                        addManufacturer();
                        break;
                    case 3:
                        removeManufacturer();
                        break;
                    case 4:
                        endRun();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private void removeManufacturer() {
        System.out.println("Insert the manufacturer's name you wish to remove:");
        System.out.println("(If you want to go back, please insert \"-1\".)\n");
        String manufacturer = scanner.nextLine();

        if(manufacturer.equals("-1")){
            System.out.println("Returning..\n");
            return;
        }

        try {
            if(controller.removeManufacturer(supplierId, manufacturer)){
                System.out.println("Manufacturer " + manufacturer + " has been removed successfully");
            }
            else
                System.out.println("Something went wrong!, Try again!");
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private void addManufacturer() {

        String manufacturer;
        System.out.println("Insert the new manufacturer's name:");
        System.out.println("(If you want to go back, please insert \"-1\".)\n");

        manufacturer = scanner.nextLine();

        if(manufacturer.equals("-1")){
            System.out.println("Returning..\n");
            return;
        }
        try {
            if(controller.addSupplierManufacturer(supplierId, manufacturer))
                System.out.println("Manufacturer " + manufacturer + " has been added successfully");
            else
                System.out.println("Something went wrong!, Try again!");

        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }

    private void viewManufacturer() {

        List<String> list = new ArrayList<>();
        try {
            list = controller.getManufacturers(supplierId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if(list.isEmpty()){
            System.out.println("[THERE ARE NO REPRESENTED MANUFACTURERS BY THIS SUPPLIER]");
        }
        else{
            System.out.println("\nManufacturers:");
            for(String s : list){
                System.out.println(s);
            }
        }

    }
}
