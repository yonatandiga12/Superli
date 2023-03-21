package Presentation.CLIPresentation.Screens.InventoryScreens;


import Domain.Service.util.Result;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Product extends Screen {
    private static final String[] menuOptions = {
            "Update Name",                  //1
            "Update Min Amount",          //2
            "Update Target Amount",      //3
            "Add To Store",        //4
            "Remove From Store",             //5
            "Change Category",      //6
            "Move From Warehouse to Store",         //7
            "Change Price",         //8
            "Delete Product",         //9
            "exit" //10
    };

    private final int id;
    private String name;
    private int categoryID;
    private double originalPrice;
    private double currentPrice;
    private String weight;
    private String manufacturer;

    public Product(Screen caller, String[] extraMenuOptions, Domain.Service.Objects.InventoryObjects.Product sProduct) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
        this.id = sProduct.getId();
        this.name = sProduct.getName();
        this.categoryID = sProduct.getCategoryID();
        this.originalPrice = sProduct.getOriginalPrice();
        this.currentPrice = sProduct.getCurrentPrice();
        this.weight = sProduct.getWeight();
        this.manufacturer = sProduct.getManufacturer();
    }

    public void run() {
        System.out.println("\nWelcome to the Management Menu of " + name + "!");
        int option = 0;
        while (option != 10 && option!=9) {
            option = runMenu();
            try {
                if (option <= 9)
                    handleBaseOptions(option);
                if (option==9 || option == 10)
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
                changeProductName();
                break;
            case 2:
                changeProductMin();
                break;
            case 3:
                changeProductTarget();
                break;
            case 4:
                addProductToStore();
                break;
            case 5:
                removeProductFromStore();
                break;
            case 6:
                changeProductCategory();
                break;
            case 7:
                moveItems();
                break;
            case 8:
                changeProductPrice();
                break;
            case 9:
                deleteProduct();
                break;
            case 10:
                endRun();
        }
    }

    private String printProduct(Domain.Service.Objects.InventoryObjects.Product p) {
        return "Product ID: " + p.getId() + "\nProduct Name: " + p.getName() + "\nProduct's Category ID: " + p.getCategoryID() + "\nOriginal Price: " + p.getOriginalPrice() + "\nCurrent Price: " + p.getCurrentPrice() + "\nProduct Weight: " + p.getWeight() + "\nProduct's Manufacturer: " + p.getManufacturer();
    }

    private void changeProductMin() {
        int store = getStoreID();
        System.out.println("What would you like the new min amount to be?");
        int min = scanner.nextInt();
        scanner.nextLine(); //to remove extra \n
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.changeProductMin(store, id, min);
        if (r.isError()) {
            System.out.println(r.getError());
        }
        else {
            System.out.println("Min changed successfully");
            //System.out.println(printProduct(r.getValue()));
        }
    }

    private void changeProductTarget() {
        int store = getStoreID();
        System.out.println("What would you like the new target amount to be?");
        int target = scanner.nextInt();
        scanner.nextLine(); //to remove extra \n
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.changeProductTarget(store, id, target);
        if (r.isError()) {
            System.out.println(r.getError());
        }
        else {
            System.out.println("Target changed successfully");
            //System.out.println(printProduct(r.getValue()));
        }
    }

    private void addProductToStore() {
        int store = getStoreID();
        scanner.nextLine(); //to remove extra \n
        System.out.println("What will be the product's shelves in the store? (please insert shelf numbers, separated by commas without spaces)");
        List<Integer> inStore = Arrays.asList(scanner.nextLine().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        System.out.println("What will be the product's shelves in the warehouse? (please insert shelf numbers, separated by commas without spaces)");
        List<Integer> inWareHouse = Arrays.asList(scanner.nextLine().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        System.out.println("What will be the min amount in the store?");
        int min = scanner.nextInt();
        System.out.println("What will be the target amount in the store?");
        int target = scanner.nextInt();
        scanner.nextLine(); //to remove extra \n
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.addProductToStore(store, inStore, inWareHouse, id, min, target);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Product added");
            //System.out.println(printProduct(r.getValue()));
        }
    }

    private void removeProductFromStore() {
        int store = getStoreID();
        scanner.nextLine(); //to remove extra \n
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.removeProductFromStore(store, id);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Product removed");
            //System.out.println(printProduct(r.getValue()));
        }
    }

    private void changeProductCategory() {
        System.out.println("To which category would you like to move it? (insert ID)");
        int category = scanner.nextInt();
        scanner.nextLine(); //to remove extra \n
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.moveProductToCategory(id, category);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("product successfully moved to new category");
            Domain.Service.Objects.InventoryObjects.Product p = r.getValue();
            System.out.println(printProduct(p));
            this.categoryID=p.getCategoryID();
        }
    }

    private void moveItems() {
        int store = getStoreID();
        System.out.println("How much is being moved?");
        int amount = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result r = controller.moveItems(store, id, amount);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Moving of items successful");
        }
    }

    private void deleteProduct() {
        Result r = controller.deleteProduct(id);
        if (r.isError())
            System.out.println(r.getError());
        else {
            System.out.println("Product removed");
        }
    }

    private void changeProductPrice() {
        System.out.println("Please insert new product price");
        double price = scanner.nextDouble();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.editProductPrice(id, price);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Domain.Service.Objects.InventoryObjects.Product p = r.getValue();
            originalPrice = p.getOriginalPrice();
            System.out.println(printProduct(p));
        }
    }

    private void changeProductName() {
        System.out.println("Please insert new product name");
        String name = scanner.nextLine();
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.editProductName(id, name);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Domain.Service.Objects.InventoryObjects.Product p = r.getValue();
            System.out.println(printProduct(p));
            this.name=p.getName();
        }
    }


}
