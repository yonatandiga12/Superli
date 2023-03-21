package Presentation.CLIPresentation.Screens.SupplierScreens;

import Domain.Service.Objects.SupplierObjects.ServiceItemObject;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.*;
import java.util.stream.Stream;

public abstract class ViewAgreement extends Screen {


    protected int supplierID;
    private Screen caller;   //To change the agreement type, need to return where it came from
    private static final String[] menuOptions = {
            "View all items",          //1
            "Add item to Agreement",           //2
            "Remove item from agreement",           //3
            "View item",       //4
            "Change Agreement type",            //5
    };



    public ViewAgreement(Screen caller, int id, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
        this.caller = caller;
        supplierID = id;
    }



    protected void handleBaseOptions(int option) throws Exception {

        switch (option) {
            case 1 :
                viewAllItems();
                break;
            case 2 :
                addItemToAgreement();
                break;
            case 3 :
                removeItemFromAgreement();
                break;
            case 4 :
                viewItem();
                break;
            case 5 :
                changeAgreementType();
                break;
        }
    }

    protected abstract void showInfo();


    private void viewAllItems() {
        List<ServiceItemObject> list = new ArrayList<>();
        try {
            list = controller.itemsFromOneSupplier(supplierID);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if(list.isEmpty()){
            System.out.println("[NO ITEMS ARE IN THE AGREEMENT]");
        }

        for(ServiceItemObject item : list){
            System.out.println(item.toString() + "\n");
        }

        System.out.println("\n\nPress \"Enter\" to return.");
        scanner.nextLine();
    }

    private void addItemToAgreement() {
        boolean _continue = true;
        int productId, input, idBySupplier;
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

            System.out.println("Product ID:");
            productId = getInput();

            if(productId == -1){
                System.out.println("Returning..\n");
                return;
            }

            System.out.println("ID by Supplier:");
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
                if(controller.addItemToAgreement(supplierID, productId, idBySupplier, manufacturer, pricePerUnit, bulkMap)){
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

    private void removeItemFromAgreement() {
        int input;
        boolean correctInput, _continue = true;

        while(_continue){

            correctInput = false;
            System.out.println("Insert the ID of the item you wish to remove:");

            while(!correctInput){

                System.out.println("If you want to go back, please insert \"-1\".\n");

                input = getInput();

                if(input == -1){
                    System.out.println("Returning..\n");
                    return;
                }


                try {
                    if(!controller.deleteItemFromAgreement(supplierID, input)){
                        System.out.println("Something went wrong, please try again.");
                    }
                    else{
                        correctInput = true;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }
            }

            System.out.println("Item was deleted successfully.\n");
            System.out.println("Choose:");
            System.out.println("1) Remove another item");
            System.out.println("2) Return");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                switch(input){
                    case 1 : {
                        correctInput = true;
                        System.out.println("\n\n");
                        break;
                    }
                    case 2: {
                        correctInput = true;
                        _continue = false;
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }
        }
    }

    private void changeAgreementType() {
        int input = -1;
        String days;
        boolean correctInput = false, _continue = true;

        while(!correctInput){
            System.out.println("Choose the type of agreement you wish to change to:");
            System.out.println("1) Routine agreement");
            System.out.println("2) By order agreement");
            System.out.println("3) Self-Transport agreement");
            System.out.println("4) Back");

            input = getInput();

            if(input == 4){
                System.out.println("Returning..\n");
                return;
            }

            if(input == 1 || input == 2 || input == 3){
                if(input == 1){
                    System.out.println("Insert the number of days of delivery, in the following format:");
                    System.out.println("1 2 5");
                    System.out.println("Meaning: the delivery days are in Sunday, Monday and Thursday");
                    System.out.println("Insert:");

                    days = scanner.nextLine();

                    try {
                        if(controller.changeAgreementType(supplierID, input, days)){
                            correctInput = true;
                        }
                        else{
                            System.out.println("Something went wrong, please try again.\n\n");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());;
                    }
                }
                if(input == 2){
                    System.out.println("Insert the number of days until delivery:");

                    days = scanner.nextLine();

                    try {
                        if(controller.changeAgreementType(supplierID, input, days)){
                            correctInput = true;
                        }
                        else{
                            System.out.println("Something went wrong, please try again.\n\n");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());;
                    }
                }
                if(input == 3){

                    try {
                        if(controller.changeAgreementType(supplierID, input, "")){
                            correctInput = true;
                        }
                        else{
                            System.out.println("Something went wrong, please try again.\n\n");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());;
                    }
                }
            }
            else{
                System.out.println("Inserted wrong value, please try again.");
            }
        }
        System.out.println("Agreement type was changed successfully, returning.\n\n");
        if(input == 1)
            new Thread ( new ViewRoutineAgreement(caller, supplierID)).start();
        else if(input == 2)
            new Thread ( new ViewByOrderAgreement(caller, supplierID)).start();
        else if(input == 3)
            new Thread ( new ViewNotTransportingAgreement(caller, supplierID)).start();
    }


    private void viewItem() {
        new Thread ( new ViewItem(this, supplierID)).start();

    }


}
