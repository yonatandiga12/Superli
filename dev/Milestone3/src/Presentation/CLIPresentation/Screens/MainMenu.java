package Presentation.CLIPresentation.Screens;

import Presentation.BackendController;
import Presentation.CLIPresentation.Screens.Document.DocumentMenu;
import Presentation.CLIPresentation.Screens.Transport.TransportsMenu;

public class MainMenu extends Screen{

    private static final String[] menuOptions = {
            "Manage Employees",  //1
            "Manage Shifts",     //2
            "Manage Trucks",     //3
            "Manage Transports", //4
            "Manage Documents",  //5
            "Exit"               //6
    };

    public MainMenu(BackendController controller) {
        super(controller, menuOptions);
    }

    public MainMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Main Menu!");
        switch (runMenu()) {
            case 1:
                new Thread(new EmployeesMenu(this)).start();
                break;
            case 2:
                new Thread(new ShiftsMenu(this)).start();
                break;
            case 3:
                new Thread(new TruckMenu(this)).start();
                break;
            case 4:
                new Thread(new TransportsMenu(this)).start();
                break;
            case 5:
                new Thread(new DocumentMenu(this)).start();
                break;
            case 6:
                endRun();
        }
    }
}
