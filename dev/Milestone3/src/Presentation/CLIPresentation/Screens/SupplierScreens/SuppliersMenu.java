package Presentation.CLIPresentation.Screens.SupplierScreens;

import Presentation.BackendController;
import Presentation.CLIPresentation.Screens.Screen;

public class SuppliersMenu extends Screen {

    private static final String[] menuOptions = {
            "View supplier's card",         //1
            "Add/Remove supplier",          //2
            "Exit"                          //3
    };


    public SuppliersMenu(BackendController controller) {
        super(controller, menuOptions);
    }

    public SuppliersMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Suppliers Menu!");
        switch (runMenu()) {
            case 1:
                new Thread(new ViewSuppliersMenu(this)).start();
                break;
            case 2:
                new Thread(new ManageSupplier(this)).start();
                break;
            case 3:
                endRun();
        }
    }




}
