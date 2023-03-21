package Presentation.CLIPresentation.Screens.SupplierScreens;


import Presentation.CLIPresentation.Screens.Screen;

public class ViewNotTransportingAgreement extends ViewAgreement {

    private static String[] extraMenuOptions  = {
            "Exit"      //6
    };



    public ViewNotTransportingAgreement(Screen caller, int supplierId) {
        super(caller, supplierId, extraMenuOptions);

    }


    @Override
    public void run() {
        System.out.println("\nWelcome to the Management Menu of Not Transporting Agreement.");
        int option = 0;
        while (option != 4 && option != 5 && option != 6) {
            showInfo();
            option = runMenu();
            try {
                if (option <= 5)
                    handleBaseOptions(option);
                else if (option == 6)
                    endRun();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    @Override
    protected void showInfo() {
        System.out.println("Transporting: No");
    }
}
