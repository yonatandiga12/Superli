package Presentation.CLIPresentation.Screens.InventoryScreens;

import Domain.Service.util.Result;
import Globals.Pair;
import Presentation.CLIPresentation.Screens.Screen;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Store extends Screen {
    private static final String[] menuOptions = {
            "Manage Sales",  //1
            "Buy Items",                  //2
            "Return Items",          //3
            "Order Arrived",      //4
            "Add Store",        //5
            "Remove Store",             //6
            "exit",      //7
    };

    public Store(Screen caller, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
    }

    public void run() {
        System.out.println("\nWelcome to the Management Menu of Store!");
        int option = 0;
        while (option != 7 && option != 1) {
            option = runMenu();
            try {
                if (option <= 7)
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
                new Thread(new Sales(this, new String[]{})).start();
                break;
            case 2:
                buyItems();
                break;
            case 3:
                returnItems();
                break;
            case 4:
                orderArrived();
                break;
            case 5:
                addStore();
                break;
            case 6:
                removeStore();
                break;
            case 7:
                endRun();
        }
    }
    private void removeStore() {
        System.out.println("Which store would you like remove?");
        int id = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result r = controller.removeStore(id);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Store removed");
        }
    }

    private void addStore() {
        Result<Integer> r = controller.addStore();
        if (r.isError())
            System.out.println(r.getError());
        else {
            int id = r.getValue();
            System.out.println("new store created, ID is: " + id);
        }
    }

    private void orderArrived() {
        System.out.println("Please insert the ID of the arrived transport");
        int transportID = scanner.nextInt();
        Map<Integer, Map<Integer, Pair<Pair<Integer, Integer>, String>>> reports = new HashMap<>();
        boolean anotherOrder = true;
        while (anotherOrder) {
            System.out.println("Please insert the ID of the arrived order");
            int orderID = scanner.nextInt();
            Map<Integer, Pair<Pair<Integer, Integer>, String>> reportOfOrder = new HashMap<>();
            reports.put(orderID, reportOfOrder);
            System.out.println("Please insert the ID of the product you want to report on in the order, or insert 0 to continue");
            int productID = scanner.nextInt();
            int missingItems;
            int defectiveItems;
            String description;
            while (productID != 0) {
                System.out.println("Please insert the number of missing items in the order of product: " + productID);
                missingItems = scanner.nextInt();
                System.out.println("Please insert the number of defective items in the order of product: " + productID);
                defectiveItems = scanner.nextInt();
                System.out.println("Please insert the description of your report");
                description = scanner.nextLine();
                reportOfOrder.put(productID, new Pair<>(new Pair<>(missingItems, defectiveItems), description));
                System.out.println("Please insert the next ID of the product you want to report on in the order, or insert 0 to continue");
                productID = scanner.nextInt();
            }
            System.out.println("is there another order? (yes/no)");
            anotherOrder = scanner.nextLine().equals("yes");
        }
//        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result<Object> r = controller.transportArrived(transportID, reports);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Transport inserted into system successfully");
        }
    }

    private void buyItems() {
        int storeID = getStoreID();
        System.out.println("Please insert product ID of product you would like to buy");
        int productId = scanner.nextInt();
        System.out.println("Please insert amount of product you would like to buy");
        int amount = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result<Pair<Double, String>> r = controller.buyItems(storeID, productId, amount);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Purchase successful! Total price is " + round(r.getValue().getLeft()) + "NIS");
            if (r.getValue().getRight()!=null)
                System.out.println(r.getValue().getRight());
        }
    }

    private void returnItems() {
        int storeID = getStoreID();
        System.out.println("Please insert product ID of the product's items you would like to return");
        int productId = scanner.nextInt();
        System.out.println("Please insert amount of items you would like to return");
        int amount = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        System.out.println("Please insert the date the items were bought");
        LocalDate dateBought = getDate();
        if (dateBought==null)
            return;
        Result<Double> r = controller.returnItems(storeID, productId, amount, dateBought);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Total refund is " + round(r.getValue()) + "NIS");
        }
    }
}
