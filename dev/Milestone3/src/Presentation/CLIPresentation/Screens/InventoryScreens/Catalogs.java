package Presentation.CLIPresentation.Screens.InventoryScreens;

import Domain.Service.util.Result;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.Arrays;
import java.util.Formatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Catalogs extends Screen {

    private static final String[] menuOptions = {
            "View Products",  //1
            "View Products By Category",  //2
            "Manage Products", //3
            "View Categories",                  //4
            "Manage Categories", //5
            "Add new Category", //6
            "Add new Product", //7
            "exit"        //8
    };

    public Catalogs(Screen caller, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
    }
    private int option;
    public void run() {
        System.out.println("\nWelcome to the Catalog!");
        option=0;
        List<Integer> leadToNextScreen = Arrays.asList(new Integer[]{3, 5, 8});
        while (!leadToNextScreen.contains(option)) {
            option = runMenu();
            try {
                if (option <= 8)
                    handleBaseOptions(option);
                else if (option == 6)
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
                listProducts();
                break;
            case 2:
                listProductsByCategory();
                break;
            case 3:
                manageProducts();
                break;
            case 4:
                listCategories();
                break;
            case 5:
                manageCategories();
                break;
            case 6:
                addCategory();
                break;
            case 7:
                addProduct();
                break;
            case 8:
                endRun();
        }
    }

    private void addProduct() {
        System.out.println("Please insert product name, categoryID, weight, price, and manufacturer. Separated by commas, no spaces");
        String[] info = scanner.nextLine().split(",");
        Result<Domain.Service.Objects.InventoryObjects.Product> r = controller.newProduct(info[0],Integer.parseInt(info[1]), Double.parseDouble(info[2]), Double.parseDouble(info[3]), info[4]);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Domain.Service.Objects.InventoryObjects.Product p = r.getValue();
            System.out.println(p);
        }
    }

    private void addCategory() {
        System.out.println("Please insert category name");
        String name = scanner.nextLine();
        System.out.println("Please insert parent category ID, or 0 if there is none");
        int parent = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result<Domain.Service.Objects.InventoryObjects.Category> r = controller.addNewCategory(name, parent);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Domain.Service.Objects.InventoryObjects.Category c = r.getValue();
            System.out.println(c);
        }
    }

    private void manageCategories() {
        System.out.println("Please insert category ID you would like to edit");
        int category = scanner.nextInt();
        scanner.nextLine();
        Result<Domain.Service.Objects.InventoryObjects.Category> c = controller.getCategory(category);
        if (c.isOk())
            new Thread(new Category(this, new String[]{}, c.getValue())).start();
        else if (c.isError()) {
            System.out.println("Error occured:\n"+c.getError());
            System.out.println("Please try again");
            option=0;
        }
    }

    private void manageProducts() {
        System.out.println("Please insert Product ID you would like to edit");
        int product = scanner.nextInt();
        scanner.nextLine();
        Result<Domain.Service.Objects.InventoryObjects.Product> p = controller.getProduct(product);
        if (p.isOk())
            new Thread(new Product(this, new String[]{}, p.getValue())).start();
        else if (p.isError()) {
            System.out.println("Error occured:\n"+p.getError());
            System.out.println("Please try again");
            option=0;
        }
    }

    private void listProducts() {
        Result<List<Domain.Service.Objects.InventoryObjects.Product>> r = controller.getProducts();
        if (r.isError())
            System.out.println(r.getError());
        else {
            Formatter fmt = new Formatter();
            fmt.format("%20s %20s %17s %20s %19s %12s %25s\n", "Product ID", "Product's Name", "Category ID", "Original Price", "Current Price", "Weight", "Manufacturer's Name");
            List<Domain.Service.Objects.InventoryObjects.Product> productList = r.getValue();
            for (Domain.Service.Objects.InventoryObjects.Product p : productList) {
                fmt.format("%20s %20s %17s %20s %19s %12s %25s\n", p.getId(), p.getName(), p.getCategoryID(), p.getOriginalPrice(), p.getCurrentPrice(), p.getWeight(), p.getManufacturer());
            }
            if (productList.isEmpty())
                System.out.println("there are no products in the system");
            else
                System.out.println(fmt);
        }
    }

    private void listProductsByCategory() {
        System.out.println("Which categories would you like to examine? (Please insert categories IDs separated by ',' without spaces)");
        listCategoryIDs();
        List<Integer> categories = Arrays.asList(scanner.nextLine().split(",")).stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
        Result<List<Domain.Service.Objects.InventoryObjects.Product>> r = controller.getProductsFromCategory(categories);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Formatter fmt = new Formatter();
            fmt.format("%20s %20s %17s %20s %19s %12s %25s\n", "Product ID", "Product's Name", "Category ID", "Original Price", "Current Price", "Weight", "Manufacturer's Name");
            List<Domain.Service.Objects.InventoryObjects.Product> productList = r.getValue();
            for (Domain.Service.Objects.InventoryObjects.Product p : productList)
                fmt.format("%20s %20s %17s %20s %19s %12s %25s\n", p.getId(), p.getName(), p.getCategoryID(), p.getOriginalPrice(), p.getCurrentPrice(), p.getWeight(), p.getManufacturer());
            if (productList.isEmpty())
                System.out.println("there are no products in specified categories");
            else
                System.out.println(fmt);
        }
    }

    private void listCategories() {
        Result<List<Domain.Service.Objects.InventoryObjects.Category>> r = controller.getCategories();
        if (r.isError())
            System.out.println(r.getError());
        else {
            Formatter fmt = new Formatter();
            fmt.format("%20s %21s %26s\n", "Category ID", "Category's Name", "Parent Category Name");
            List<Domain.Service.Objects.InventoryObjects.Category> categoryList = r.getValue();
            for (Domain.Service.Objects.InventoryObjects.Category c : categoryList)
                fmt.format("%20s %21s %26s\n", c.getID(), c.getName(), c.getParentCategory());
            if (categoryList.isEmpty())
                System.out.println("there are no categories in the system");
            else
                System.out.println(fmt);
        }
    }
}
