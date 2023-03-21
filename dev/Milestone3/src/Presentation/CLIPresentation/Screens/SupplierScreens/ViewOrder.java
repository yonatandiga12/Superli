package Presentation.CLIPresentation.Screens.SupplierScreens;

import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Domain.Service.util.Result;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.List;
//import com.sun.org.apache.xpath.internal.operations.Bool;

public class ViewOrder extends Screen {

    private int supplierId;
    private static final String[] menuOptions = {
            "View Order",     //1
            "Make new Order",     //2
            "Edit order",         //3
            "Exit"           //4
    };

    public ViewOrder(Screen caller, int supplierId) {
        super(caller, menuOptions);
        this.supplierId = supplierId;
    }

    @Override
    public void run() {
        System.out.println("\nHere you can view Orders and make new Ones!");
        int option = 0;
        while (option != 4) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        viewOrder();
                        break;
                    case 2:
                        makeOrder();
                        break;
                    case 3:
                        editOrder();
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

    private void makeOrder() {
        int input;
        boolean correctInput = false;

        System.out.println("If you wish to return, please insert \"-1\", otherwise, insert \"1\".");

        input = getInput();

        if(input == -1){
            System.out.println("Returning..\n");
            return;
        }

        int orderId = -1;
        try {
            orderId = controller.order(supplierId, getStoreID());
            System.out.println("Order " + orderId + " added successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        addItemsToOrder(supplierId, orderId);

        System.out.println("Order was placed successfully, returning.\n");
    }

    private void addItemsToOrder(int supplierID, int orderId) {
        boolean _continue = true, correctInput;
        int itemID, quantity, input, idBySupplier;

        System.out.println("Now we shall add items to your new order.");
        System.out.println("PLEASE NOTICE: the order must contain at least one item.\n");

        while (_continue){
            System.out.println("Please insert the following details:");
            System.out.println("Product ID:");

            itemID = getInput();

            System.out.println("Quantity:");

            quantity = getInput();

            boolean r = false;
            try {
                System.out.println("Item added successfully");
                r = controller.addItemToOrder(supplierID, orderId, itemID, quantity);
            } catch (Exception e) {
                System.out.println(e.getMessage());

            }

            if(r){
                System.out.println("Choose:");
                System.out.println("1) Add another item");
                System.out.println("2) Back");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1: {
                            correctInput = true;
                            break;
                        }
                        case 2: {
                            _continue = false;
                            correctInput = true;
                            break;
                        }
                        default: {
                            System.out.println("Wrong value was inserted, please try again.");
                        }
                    }
                }
            }
            else{
                System.out.println("Please try again.\n");
            }


        }
    }

    private void viewOrder() {
        int input;

        try {
            List<Integer> ordersIds = controller.geOrdersID(supplierId);
            System.out.println("Orders available:  " + ordersIds);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Insert the ID of the order you wish to watch.");
        System.out.println("If you want to return, insert \"-1\"");

        input = getInput();

        if (input == -1) {
            System.out.println("Returning..\n");
            return;
        }
        try {
            ServiceOrderObject r = controller.getOrder(supplierId, input);
            if(r != null){
                System.out.println(r.toString());
            }
            else{
                System.out.println("Order doesn't exist!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }


    }


    private void editOrder(){
        boolean _continue = true, correctInput;
        int input, orderId = -1;

        while(_continue){

            correctInput = false;

            System.out.println("Insert the number of order you want to edit.");
            System.out.println("If you wish to return please insert \"-1\"");

            while(!correctInput){
                orderId = getInput();

                if(orderId == -1){
                    return;
                }

                Result<Boolean> r = controller.orderExists(supplierId, orderId);//

                if(r.isOk() && r.getValue()){
                    correctInput = true;
                }
                else{
                    System.out.println(r.getError());
                }
            }

            System.out.println("Choose an option:");
            System.out.println("1) Add an item to the order");
            System.out.println("2) Remove an item from the order");
            System.out.println("3) Change the quantity of an item in the order");
            System.out.println("4) Back");

            correctInput = false;

            while(!correctInput){
                input = getInput();
                switch (input){
                    case 1:
                        correctInput = true;
                        addItemToOrder(supplierId, orderId);
                        break;
                    case 2:
                        correctInput = true;
                        removeItemFromOrder(supplierId, orderId);
                        break;
                    case 3:
                        correctInput = true;
                        changeQuantityOfItem(supplierId, orderId);
                        break;
                    case 4:
                        correctInput = true;
                        _continue = false;
                        break;
                    default:
                        System.out.println("Wrong value, please try again.\n");
                }
            }
        }

        System.out.println("Returning..");
    }

    private void addItemToOrder(int supplierID, int orderId){
        boolean _continue = true, correctInput;
        int itemID, quantity, input, idBySupplier;


        while (_continue){
            System.out.println("Please insert the following details:");
            System.out.println("If you want to return, please insert \"-1\" instead od the ID");
            System.out.println("Product ID:");

            itemID = getInput();

            if(itemID == -1){
                System.out.println("Returning");
                return;
            }


            System.out.println("Quantity:");

            quantity = getInput();

            boolean r = false;
            try {
                r = controller.addItemToOrder(supplierID, orderId, itemID, quantity);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            if(r){
                System.out.println("Item added successfully!\n");
                System.out.println("Choose:");
                System.out.println("1) Add another item");
                System.out.println("2) Back");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1: {
                            correctInput = true;
                            break;
                        }
                        case 2: {
                            _continue = false;
                            correctInput = true;
                            break;
                        }
                        default: {
                            System.out.println("Wrong value was inserted, please try again.");
                        }
                    }
                }
            }
            else{
                System.out.println("Please try again.\n");
            }
        }
    }

    private void removeItemFromOrder(int supplierID, int orderId){
        boolean _continue = true, correctInput;
        int itemID, input;


        while (_continue){
            System.out.println("Please insert the ID of the item to remove:");
            System.out.println("If you want to return, please insert \"-1\" instead od the ID");

            itemID = getInput();

            if(itemID == -1){
                System.out.println("Returning");
                return;
            }

            Result<Boolean> r = controller.removeItemFromOrder(supplierID, orderId, itemID);;

            if(r.isOk()){
                System.out.println("Choose:");
                System.out.println("1) Remove another item");
                System.out.println("2) Back");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1: {
                            correctInput = true;
                            break;
                        }
                        case 2: {
                            _continue = false;
                            correctInput = true;
                            break;
                        }
                        default: {
                            System.out.println("Wrong value was inserted, please try again.");
                        }
                    }
                }
            }
            else{
                System.out.println("Please try again.\n");
            }
        }
    }

    private void changeQuantityOfItem(int supplierID, int orderId){
        int itemID, input, quantity;

        System.out.println("Please insert the ID of the item to change it's quantity:");
        System.out.println("If you want to return, please insert \"-1\" instead od the ID");

        itemID = getInput();

        if(itemID == -1){
            System.out.println("Returning");
            return;
        }

        System.out.println("Quantity:");
        quantity = getInput();

        Result<Boolean> r = controller.updateItemQuantityInOrder(supplierID, orderId, itemID, quantity);

        if(r.isOk()){
            System.out.println("Changes saved.");
        }
        else{
            System.out.println("Something went wrong, please try again.");
        }

        System.out.println("Returning..\n");
    }
}
