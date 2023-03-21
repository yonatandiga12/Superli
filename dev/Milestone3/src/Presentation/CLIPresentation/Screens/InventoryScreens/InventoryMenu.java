package Presentation.CLIPresentation.Screens.InventoryScreens;


import Presentation.CLIPresentation.Screens.Screen;

public class InventoryMenu extends Screen {
    private static final String[] menuOptions = {
            "View/Manage Catalog", //1
            "View/Add Reports",    //2
            "Manage Store (sales, warehouse)",    //3
            "Exit"              //4
    };

    public InventoryMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Inventory Menu!");
        switch (runMenu()) {
            case 1:
                new Thread(new Catalogs(this, new String[]{})).start();
                break;
            case 2:
                new Thread(new Reports(this, new String[]{})).start();
                break;
            case 3:
                new Thread(new Store(this, new String[]{})).start();
                break;
            case 4:
                endRun();
        }
    }
}
