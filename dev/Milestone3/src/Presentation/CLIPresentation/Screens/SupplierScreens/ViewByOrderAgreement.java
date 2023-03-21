package Presentation.CLIPresentation.Screens.SupplierScreens;


import Presentation.CLIPresentation.Screens.Screen;

public class ViewByOrderAgreement extends ViewAgreement {

    private static String[] extraMenuOptions  = {
            "Change days until delivery",  //6
            "Exit"                         //7
    };



    public ViewByOrderAgreement(Screen caller, int supplierId) {
        super(caller, supplierId, extraMenuOptions);
    }


    @Override
    public void run() {
        System.out.println("\nWelcome to the Management Menu of By Order Agreement.");
        int option = 0;

        while (option != 4 && option != 5 && option != 7) {
            showInfo();
            option = runMenu();
            try {
                if (option <= 5)
                    handleBaseOptions(option);
                else if (option == 6)
                    changeDaysUntillDelivery();
                else if (option == 7)
                    endRun();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    protected void showInfo() {

        try {
            System.out.println("Transporting: Yes");
            System.out.println("By Order Agreement");
            System.out.println("Days to next delivery: " + controller.getDeliveryDays(supplierID));
        } catch (Exception e) {
            System.out.println(e.getMessage());;

        }
    }

    private void changeDaysUntillDelivery() {
        int input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the number of days until delivery:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return;
            }

            try {
                if(controller.changeDaysUntilDelivery(supplierID, input)){
                    correctInput = true;
                }
                else{
                    System.out.println("Something went wrong, please try again.\n\n");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Days until delivery was changed successfully, returning.\n\n");
    }
}
