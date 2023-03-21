package Presentation.CLIPresentation.Screens.InventoryScreens;
import Domain.Service.util.Result;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Category extends Screen {

    private static final String[] menuOptions = {
            "Update name",                  //1
            "Update Parent Category",          //2
            "Delete Category",      //3
            "exit", //4
    };

    private int id;
    private String name;
    private String parentCategory;
    private List<Domain.Service.Objects.InventoryObjects.Category> subCategories;
    private int numOfProducts;

    public Category(Screen caller, String[] extraMenuOptions, Domain.Service.Objects.InventoryObjects.Category category) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
        id = category.getID();
        name = category.getName();
        parentCategory = category.getParentCategory();
        subCategories = category.getSubCategories();
        numOfProducts = category.getNumOfProducts();
    }

    public void run() {
        System.out.println("\nWelcome to the Management Menu of the category: " + name);
        int option = 0;
        while (option!=3 && option != 4) {
            option = runMenu();
            try {
                if (option <= 3)
                    handleBaseOptions(option);
                if (option == 3 || option==4)
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
                changeCatName();
                break;
            case 2:
                changeCatParent();
                break;
            case 3:
                deleteCategory();
                endRun();
                break;
            case 4:
                endRun();
        }
    }

    private String printCategory(Domain.Service.Objects.InventoryObjects.Category c) {
        String str = "Category ID: " + c.getID() + "\nCateogry Name: " + c.getName() + "\nParent Category Name: " + c.getParentCategory() + "\nSubcategories IDs: [";
        for (Domain.Service.Objects.InventoryObjects.Category subcat: c.getSubCategories()) {
            str += subcat.getID() + ",";
        }
        if (str.endsWith(","))
            str = str.substring(0, str.length()-1);
        return str + "]";
    }

    private void changeCatParent() {
        System.out.println("Please insert new category parent ID (0 for no parent)");
        int parent = scanner.nextInt();
        scanner.nextLine(); //without this line the next scanner will be passed without the user's input.
        Result<Domain.Service.Objects.InventoryObjects.Category> r = controller.changeCategoryParent(id, parent);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Domain.Service.Objects.InventoryObjects.Category c = r.getValue();
            System.out.println(printCategory(c));
        }
    }

    private void changeCatName() {
        System.out.println("Please insert new category name");
        String name = scanner.nextLine();
        Result<Domain.Service.Objects.InventoryObjects.Category> r = controller.editCategoryName(id, name);
        if (r.isError())
            System.out.println(r.getError());
        else {
            Domain.Service.Objects.InventoryObjects.Category c = r.getValue();
            System.out.println(printCategory(c));
        }
    }

    private void deleteCategory() {
        Result r = controller.deleteCategory(id);
        if (r.isError()) {
            System.out.println(r.getError());
        }
        else
            System.out.println("Category deleted successfully");
    }
}
