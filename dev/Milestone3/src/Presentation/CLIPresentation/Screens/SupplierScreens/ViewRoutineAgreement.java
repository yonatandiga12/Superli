package Presentation.CLIPresentation.Screens.SupplierScreens;


import Presentation.CLIPresentation.Screens.Screen;

import java.util.List;

public class ViewRoutineAgreement extends ViewAgreement {

    private static String[] extraMenuOptions  = {
            "Change days of delivery",  //6
            "Add days of delivery",     //7
            "Remove day of delivery",   //8
            "Exit"                      //9
    };

    public ViewRoutineAgreement(Screen caller, int supplierId) {
        super(caller, supplierId, extraMenuOptions);
    }


    @Override
    public void run() {
        System.out.println("\nWelcome to the Management Menu of Routine Agreement.");

        int option = 0;
        while (option != 4 && option != 5 && option != 9) {
            showInfo();
            option = runMenu();
            try {
                if (option <= 5)
                    handleBaseOptions(option);
                else if (option == 6)
                    changeDaysOfDelivery();
                else if (option == 7)
                    addDaysOfDelivery();
                else if (option == 8)
                    removeDaysOfDelivery();
                else if (option == 9)
                    endRun();
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private String listToString(List<Integer> list){
        String toReturn = "";
        for(Integer s: list){
            toReturn += (s + " ");
        }

        return toReturn;
    }
    private void removeDaysOfDelivery() {
        boolean correctInput = false;
        int input;

        System.out.println("Insert the number of days you wish to remove");

        System.out.println("If you want to go back, please insert \"-1\".\n");

        while(!correctInput){
            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return;
            }

            try {
                if(controller.removeDayOfDelivery(supplierID, input)){
                    correctInput = true;
                }
                else{
                    System.out.println("Something went wrong, please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
        }

        System.out.println("Changes saved, returning\n\n");
    }

    private void addDaysOfDelivery() {
        boolean correctInput = false;
        String input;

        System.out.println("Insert the new days of delivery you wish to add");

        System.out.println("If you want to go back, please insert \"-1\".\n");

        while(!correctInput){
            input = Screen.scanner.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            if(input.length() > 0){

                try {
                    if(controller.addDaysOfDelivery(supplierID, input)){
                        correctInput = true;
                    }
                    else{
                        System.out.println("Something went wrong, please try again.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());;
                }
            }
        }

        System.out.println("Changes saved, returning\n\n");
    }

    private void changeDaysOfDelivery() {
        String input;
        boolean correctInput = false;

        System.out.println("Insert the new days of delivery");

        System.out.println("If you want to go back, please insert \"-1\".\n");

        while(!correctInput){
            input = Screen.scanner.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            if(input.length() > 0){

                try {
                    if(controller.setDaysOfDelivery(supplierID, input)){
                        correctInput = true;
                    }
                    else{
                        System.out.println("Something went wrong, please try again.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());;
                }
            }
            else{
                System.out.println("Wrong input, please try again.\n");
            }
        }

        System.out.println("Changes saved, returning\n\n");
    }

    @Override
    protected void showInfo() {
        try {
            System.out.println("Transporting: Yes");
            System.out.println("Routine Agreement");
            System.out.println("Days to next delivery: " + controller.daysToDelivery(supplierID));
            System.out.println("Days of delivery: " + listToString(controller.getDaysOfDelivery(supplierID)) + "\n\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
}
