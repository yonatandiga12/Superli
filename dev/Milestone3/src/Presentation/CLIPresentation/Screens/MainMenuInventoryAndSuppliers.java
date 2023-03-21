package Presentation.CLIPresentation.Screens;

import Presentation.BackendController;
import Presentation.CLIPresentation.Screens.InventoryScreens.InventoryMenu;
import Presentation.CLIPresentation.Screens.SupplierScreens.SuppliersMenu;

public class MainMenuInventoryAndSuppliers extends Screen {

    private static final String[] menuOptions = {
            "Manage Suppliers", //1
            "Manage Inventory",    //2
            "Exit"              //3
    };

    public MainMenuInventoryAndSuppliers(BackendController controller) {
        super(controller, menuOptions);
    }

    public MainMenuInventoryAndSuppliers(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Main Menu!");
        switch (runMenu()) {
            case 1:
                new Thread(new SuppliersMenu(this)).start();
                break;
            case 2:
                new Thread(new InventoryMenu(this)).start();
                break;
            case 3:
                endRun();
        }
    }

}
