package Presentation.CLIPresentation.Screens.SupplierScreens;

import Globals.Pair;
import Presentation.CLIPresentation.Screens.Screen;

import java.util.ArrayList;
import java.util.Objects;

public class ManageSupplier extends Screen {

    private static final String[] menuOptions = {
            "Add Supplier",         //1
            "Remove supplier",      //2
            "Exit"                  //3
    };

    public ManageSupplier(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {

        System.out.println("\nSuppliers Management");
        int option = 0;
        while (option != 3) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        addSupplier();
                        break;
                    case 2:
                        removeSupplier();
                        break;
                    case 3:
                        endRun();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }


    private void addSupplier() {
        int input;
        boolean _continue = true;

        boolean correctInput;
        int bankNumber;
        String name, address, payingAgreement, contact, manufacturer;
        ArrayList<Pair<String, String>> contacts;
        boolean done;
        String[] splitedContact;
        ArrayList<String> manufacturers;

        System.out.println("For each detail you insert, please press \"Enter\" after the insertion to continue.\n");
        System.out.println("If you want to go back, please insert \"-1\" instead of the ID.\n");

        correctInput = false;
        contacts = new ArrayList<>();
        done = false;
        manufacturers = new ArrayList<>();

        /*
        System.out.println("ID: ");
        id = getInput();

        if(id == -1){
            System.out.println("Returning..\n");
            return;
        }
         */

        System.out.println("Name: ");
        name = scanner.nextLine();
        System.out.println("Bank number: ");
        bankNumber = getInput();
        System.out.println("Address: ");
        address = scanner.nextLine();
        System.out.println("Paying agreement: ");
        payingAgreement = scanner.nextLine();

        System.out.println("Now, please insert contacts in the following format, then press \"Enter\".\nTo end the insertion, write \"Done\" and then press \"Enter\":\n");
        System.out.println("Name, phone-number\nFor example:\nIsrael, 0591234567\n");

        while(!done){
            contact = scanner.nextLine();

            if(Objects.equals(contact, "Done")){
                done = true;
            }
            else{
                if(contact.length() > 12 && contact.contains(",")){ //12 is the minimal length according to the format
                    splitedContact = contact.split(",");
                    contacts.add(new Pair<>(splitedContact[0], splitedContact[1]));
                }
            }
        }

        System.out.println("\nAt last, please enter the names of the manufacturers represented by the supplier. To end the insertion, write \"Done\" and press \"Enter\":\n");

        done = false;

        while(!done){
            manufacturer = scanner.nextLine();

            if(Objects.equals(manufacturer, "Done")){
                done = true;
            }
            else{
                if(!manufacturer.isEmpty()){
                    manufacturers.add(manufacturer);
                }
            }
        }

        try {
            int supplierId = controller.addSupplier(name, bankNumber, address, payingAgreement, contacts, manufacturers);
            if(supplierId != -1){
                System.out.println("\nSupplier " + supplierId + " was added successfully to the data base.\n");
            }
            else{
                System.out.println("Supplier wasn't added!");
                return;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    private void removeSupplier() {
        int input;


        System.out.println("Insert the ID of the supplier you wish to delete.");
        System.out.println("WARNING! this action is finite!\n");
        System.out.println("If you want to go back, please insert \"-1\" instead of the ID.\n");

        input = getInput();
        if(input == -1){
            System.out.println("Returning..\n");
            return;
        }
        try {
            if(controller.removeSupplier(input)){
                System.out.println("\nSupplier " + input + " removed successfully");
            }
            else{
                System.out.println("Supplier wasn't removed!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }



}
