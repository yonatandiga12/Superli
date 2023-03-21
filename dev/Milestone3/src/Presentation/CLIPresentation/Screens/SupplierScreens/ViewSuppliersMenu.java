package Presentation.CLIPresentation.Screens.SupplierScreens;

import Domain.Service.Objects.SupplierObjects.ServiceOrderItemObject;
import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Domain.Service.Objects.SupplierObjects.ServiceSupplierObject;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSuppliersMenu extends Screen {


    //FOR TESTING!!
    private int supplierId = -2; // first time in the window

    private static final String[] menuOptions = {
            "View Information",     //1
            "Edit card",          //2
            "View agreement",           //3
            "New agreement",           //4
            "View contacts",       //5
            "View represented manufacturers",            //6
            "View order",                   //7
            "View all orders",              //8
            "View all discount items",      //9
            "Exit"                          //10
    };



    public ViewSuppliersMenu(Screen caller) {
        super(caller, menuOptions);
    }


    @Override
    public void run() {
        boolean correctInput = false, _continue = true;

        System.out.println("\nHere you can view all the Supplier's info\n");

        try {
            if(controller.isSuppliersEmpty()){
                System.out.println("NO SUPPLIERS ARE AVAILABLE!\nPress \"Enter\" to return.");
                scanner.nextLine();
                endRun();
                return;
            }
            else{
                ArrayList<Integer> supplierIds = controller.getSuppliersID();
                System.out.println("Suppliers available:  " + supplierIds);
            }
        } catch (Exception e) {
            System.out.println("Something went wrong, please try again");
        }



        while(!correctInput){

            if(supplierId == -2){
                System.out.println("If you want to return, please insert \"-1\" and then press \"Enter\".\n");
                supplierId = getInput();
            }
            else{
                System.out.println("Supplier number: " + supplierId);

            }


            if(supplierId == -1) {
                endRun();
                return;
            }

            try{
                if(!controller.doesSupplierExists(supplierId))
                    System.out.println("No such supplier, please try again.\n");
                correctInput = true;
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("\n\nPlease try again.\n");
            }

        }

        int option = 0;
        while (_continue) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        printSupplierInfo();
                        break;
                    case 2:
                        new Thread(new EditCardScreen(this, supplierId)).start();
                        _continue = false;
                        break;
                    case 3:
                        _continue = viewAgreement();
                        break;
                    case 4:
                        addNewAgreement(supplierId);
                        break;
                    case 5:
                        new Thread(new ViewContacts(this, supplierId)).start();
                        _continue = false;
                        break;
                    case 6:
                        new Thread( new ViewManufacturers(this, supplierId)).start();
                        _continue = false;
                        break;
                    case 7:
                        new Thread( new ViewOrder(this, supplierId)).start();
                        _continue = false;
                        break;
                    case 8:
                        viewAllOrders();
                        break;
                    case 9:
                        viewOrderItemsDiscount();
                        break;
                    case 10:
                        _continue = false;
                        endRun();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private void viewOrderItemsDiscount() {

        try {
            ArrayList<ServiceOrderItemObject> r = controller.getAllOrdersItemsInDiscounts(supplierId);
            if(r != null){
                for(ServiceOrderItemObject orderItemObject : r){
                    float originalPrice = orderItemObject.getQuantity() * orderItemObject.getPricePerUnit();
                    System.out.println(orderItemObject.toStringDiscount(originalPrice) + "\n");
                }
            }
            else{
                System.out.println("No Order Items available");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private void viewAllOrders() {

        try {
            ArrayList<ServiceOrderObject> r = controller.getAllOrdersForSupplier(supplierId);
            if(r != null){
                for(ServiceOrderObject orderObject : r){
                    System.out.println(orderObject.toString());
                }
            }
            else{
                System.out.println("No Orders available");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    private boolean viewAgreement() {
        try {
            if(!controller.hasAgreement(supplierId)){
                System.out.println("No agreement with this supplier, press \"Enter\" to return.");
                scanner.nextLine();
                return true;
            }
            if(controller.isRoutineAgreement(supplierId)){
                new Thread ( new ViewRoutineAgreement(this, supplierId)).start();
            }
            else{
                if(controller.isByOrderAgreement(supplierId)){
                    new Thread ( new ViewByOrderAgreement(this, supplierId)).start();
                }
                else{
                    new Thread ( new ViewNotTransportingAgreement(this, supplierId)).start();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false;
    }


    private void printSupplierInfo() {
        boolean correctInput = false, _continue = true;
        while(!correctInput){

            if(supplierId == -1){
                System.out.println("\n\n");
                return;
            }

            ServiceSupplierObject r = null;
            try {
                r = controller.getSupplierInfo(supplierId);
            } catch (Exception e) {
                System.out.println("please try again.");
            }

            if(r != null){

                System.out.println("\n" + r.toString());
                correctInput = true;
            }
            else{
                System.out.println("please try again.");
            }
        }

    }

    private void getSupplierId() {
        System.out.println("Insert the supplier ID you wish to view, then press \"Enter\" to continue.");
        System.out.println("If you wish to return, type \"-1\" and press \"Enter\"");
        int input, supplierID = -1;
        boolean correctInput, _continue = true;
        correctInput = false;

        while(!correctInput){
            input = getInput();

            if(input == -1){
                System.out.println("\n\n");
                return;
            }

            boolean r = false;
            try {
                r = controller.doesSupplierExists(input);
            } catch (Exception e) {
                e.getMessage();
            }

            if(r){
                System.out.println("You are now watching suppiler " + input);
                supplierId = input;
                correctInput = true;
                supplierID = input;
            }
            else{
                System.out.println("No such supplier or your input is incorrect, please try again.");
            }
        }
    }

    private void addNewAgreement(int supplierId) {
        int input = -1;
        boolean correctInput = false;
        String stringInput = "";

        System.out.println("Insert the number of the option you wish to choose, then press \"Enter\"");
        System.out.println("1) Routine delivery");
        System.out.println("2) By order delivery");
        System.out.println("3) Self-transport");
        System.out.println("4) Back");


        while(!correctInput){
            input = getInput();

            if(input == 4){
                System.out.println("Returning..\n\n");
                return;
            }

            if(input == 1 || input == 2 || input == 3){
                correctInput = true;
            }
            else{
                System.out.println("Wrong value was inserted, please try again.\n\n");
            }
        }

        if(input == 1){
            System.out.println("Insert the delivery days as numbers with whitespaces in between. For example:");
            System.out.println("1 2 5");
            System.out.println("Meaning: Sunday, Monday, Thursday");

            stringInput = scanner.nextLine();
        }
        if(input == 2){
            System.out.println("Insert the number of days to deliver:");
            stringInput = scanner.nextLine();
        }

        try {
            if(controller.addAgreement(supplierId, input, stringInput)){
                System.out.println("Now, let's add the items included in the agreement.");
                addItemToAgreement();

                System.out.println("All is done, returning\n\n");
            }
            else{
                System.out.println("A problem has occurred, please try again later");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private void addItemToAgreement() {
        boolean _continue = true;
        int id, input, idBySupplier;
        String name, manufacturer, bulkString;
        float pricePerUnit;
        String[] bulkArr;
        Map<Integer, Integer> bulkMap;
        boolean correctInput;


        while(_continue){
            bulkArr = new String[0];
            bulkMap = new HashMap<>();
            correctInput = false;

            System.out.println("Insert the details, press \"Enter\" to continue.");
            System.out.println("If you want to go back, please insert \"-1\" instead of the ID.\n");

            System.out.println("ID:");
            id = getInput();

            if(id == -1){
                System.out.println("Returning..\n");
                return;
            }

            System.out.println("ID by supplier:");
            idBySupplier = getInput();

            System.out.println("Name:");
            name = scanner.nextLine();
            System.out.println("Manufacturer:");
            manufacturer = scanner.nextLine();
            System.out.println("Price per unit:");
            pricePerUnit = getInputFloat();

            System.out.println("Now, let's insert the bulk prices in the following format:");
            System.out.println("quantity, price, quantity, price, quantity, price ...");
            System.out.println("Example:");
            System.out.println("100, 13, 200, 24");
            System.out.println("Meaning: for 100 units there is a 13% discount, for 200 units there is 24% discount.");
            System.out.println("\nWhen you are done, press \"Enter\".\n\n");

            while(!correctInput){
                bulkString = scanner.nextLine();

                if(bulkString.isEmpty()){
                    break;
                }

                bulkArr = bulkString.replaceAll("\\s+","").split(",");

                if(bulkArr.length % 2 != 0){
                    System.out.println("Inserted wrong or not complete values, please try again");
                }
                else{
                    correctInput = true;
                }
            }

            for(int i=0; i<bulkArr.length; i++){
                bulkMap.put(Integer.parseInt(bulkArr[i]), Integer.parseInt(bulkArr[i+1]));
                i++;
            }


            try {
                if(controller.addItemToAgreement(supplierId, id, idBySupplier, manufacturer, pricePerUnit, bulkMap)){
                    System.out.println("The new Item was added successfully.");
                    System.out.println("Choose:");
                    System.out.println("1) Add another item");
                    System.out.println("2) Return");

                    correctInput = false;

                    while(!correctInput){
                        input = getInput();

                        switch (input){
                            case 1 : {
                                correctInput = true;
                                System.out.println("\n\n");
                                break;
                            }
                            case 2 : {
                                correctInput = true;
                                _continue = false;
                                System.out.println("Returning..");
                                break;
                            }
                            default : System.out.println("You inserted wrong value, please try again.");
                        }
                    }
                }
                else{
                    System.out.println("Something went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
