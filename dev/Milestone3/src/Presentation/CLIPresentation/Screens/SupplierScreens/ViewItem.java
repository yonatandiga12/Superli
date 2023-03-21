package Presentation.CLIPresentation.Screens.SupplierScreens;

import Domain.Service.Objects.SupplierObjects.ServiceItemObject;
import Presentation.CLIPresentation.Screens.Screen;

public class ViewItem extends Screen {

    protected int itemID = -1;
    protected int supplierID;
    private static final String[] menuOptions = {
            "View Item",                                        //1
            "Change ID",                                        //2
            "Change name",                                      //3
            "Change manufacturer",                              //4
            "Change price per unit",                            //5
            "Add bulk price",                                   //6
            "Remove bulk price",                                //7
            "Change discount for bulk price",                   //8
            "Calculate total price of an order of viewed item", //9
            "Exit"                                              //10

    };


    public ViewItem(Screen caller, int supplierID) {
        super(caller, menuOptions);
        this.supplierID = supplierID;
    }



    @Override
    public void run() {
        System.out.println("\nHere you can change the item's info");
        int option = 0;
        if(itemID == -1)
            setItemId();
        if(itemID == -1) {
            endRun();
            return;
        }
        while (option != 10) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        System.out.println(getItem().toString());
                        break;
                    case 2:
                        itemID = changeID();
                        break;
                    case 3:
                        changeName();
                        break;
                    case 4:
                        changeManufacturer();
                        break;
                    case 5:
                        changePPU();
                        break;
                    case 6:
                        addBulkPrice();
                        break;
                    case 7:
                        removeBulkPrice();
                        break;
                    case 8:
                        changeDiscountForBulkPrice();
                        break;
                    case 9:
                        calculateTotalPrice();
                        break;
                    case 10:
                        System.out.println("Returning..");
                        endRun();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private void calculateTotalPrice() {
        int input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity to order:");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return;
            }

            Double r = null;
            try {
                r = controller.calculatePriceForItemOrder(supplierID, itemID, input);
                correctInput = true;
                System.out.println("\n\nThe total price is: " + r);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Returning...\n\n");
    }

    private void changeDiscountForBulkPrice() {
        int quantity, discount;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity:");
            System.out.println("If you want to go back insert \"-1\".\n");
            quantity = getInput();

            if(quantity == -1){
                System.out.println("Returning\n");
                return;
            }

            System.out.println("Insert discount:");
            discount = getInput();

            try {
                if(controller.editBulkPriceForItem(supplierID, itemID, quantity, discount)){
                    correctInput = true;
                }
                else{
                    System.out.println("\nSomething went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nItem's bulk price was edited successfully.");
    }

    private void removeBulkPrice() {
        int quantity;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity:");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            quantity = getInput();

            if(quantity == -1){
                System.out.println("Returning..\n");
                return;
            }

            try {
                if(controller.removeBulkPriceForItem(supplierID, itemID, quantity)){
                    correctInput = true;
                }
                else{
                    System.out.println("\nSomething went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nItem's bulk price was removed successfully.");
    }

    private void addBulkPrice() {
        int quantity, discount;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity:");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            quantity = getInput();

            if(quantity == -1){
                System.out.println("Returning..\n");
                return;
            }

            System.out.println("Insert discount:");
            discount = getInput();


            try {
                if(controller.addBulkPriceForItem(supplierID, itemID, quantity, discount)){
                    correctInput = true;
                }
                else{
                    System.out.println("\nSomething went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nItem's bulk price was added successfully.");
    }

    private void changePPU() {
        float input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new price:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = getInputFloat();

            if(input == -1f){
                System.out.println("Returning..\n");
                return;
            }

            try {
                if(controller.updatePricePerUnitForItem(supplierID, itemID, input)){
                    correctInput = true;
                }
                else{
                    System.out.println("\nSomething went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nItem's price was changed successfully.");
    }

    private void changeManufacturer() {
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new manufacturer:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = scanner.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            try {
                if(controller.updateItemManufacturer(supplierID, itemID, input)){
                    correctInput = true;
                }
                else{
                    System.out.println("Something went wrong, please try again.\n\n");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("\nItem's manufacturer was changed successfully.");
    }

    private void changeName() {
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new Name:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = scanner.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            try {
                if(true){
                    correctInput = true;
                }
                else{
                    System.out.println("Something went wrong, please try again.\n\n");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\nItem's name was changed successfully.");
    }

    private int changeID() {
        int input, newID = -1;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new ID:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return itemID;
            }

            try {
                if(controller.updateItemId(supplierID, itemID, input)){
                    correctInput = true;
                    newID = input;
                }
                else{
                    System.out.println("\nSomething went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("\nItem's ID was changed successfully.");
        return newID;
    }

    private void setItemId() {
        ServiceItemObject item = getItem();
        if(item != null)
            itemID = item.getId();
    }

    private ServiceItemObject getItem() {
        int input = -1;
        if(itemID == -1){
            System.out.println("Insert the ID of the item you wish to view.");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return null;
            }
        }
        else{
            input = itemID;
        }

        ServiceItemObject r = null;
        try {
            r = controller.getItem(supplierID, input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if(r != null){
            return r;
        }
        else {
            System.out.println("Something went wrong, please try again.\n\n");
        }
        return null;
    }
}
