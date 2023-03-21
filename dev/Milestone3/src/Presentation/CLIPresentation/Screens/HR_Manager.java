package Presentation.CLIPresentation.Screens;

import Globals.util.HumanInteraction;

public class HR_Manager extends Employee {
    private static final String[] extraMenuOptions = {
            "Exit"              //9
    };

    public HR_Manager(Screen caller, Domain.Service.Objects.Employee.HR_Manager sEmployee) {
        super(caller, sEmployee, extraMenuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Management Menu of " + name + " the HR Manager!");
        int option = 0;
        while (option != 9) {
            option = runMenu();
            try {
                if (option <= 8)
                    handleBaseOptions(option);
                else if (option == 9)
                    endRun();
            } catch (HumanInteraction.OperationCancelledException ignored) {
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }
}