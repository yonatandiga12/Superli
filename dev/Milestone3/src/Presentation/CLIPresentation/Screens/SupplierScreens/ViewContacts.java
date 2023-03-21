package Presentation.CLIPresentation.Screens.SupplierScreens;

import Presentation.CLIPresentation.Screens.Screen;

import java.util.ArrayList;
import java.util.List;

public class ViewContacts extends Screen {

    private int supplierID;

    private static final String[] menuOptions = {
            "Add Contact",              //1
            "Remove Contact",           //2
            "View contacts",            //3
            "Exit"                      //4
    };


    public ViewContacts(Screen caller, int id) {
        super(caller, menuOptions);
        supplierID = id;
    }

    @Override
    public void run() {
        System.out.println("\nHere you can view all the Supplier's info");
        int option = 0;
        while (option != 4) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        addContact();
                        break;
                    case 2:
                        removeContact();
                        break;
                    case 3:
                        printContacts();
                        break;
                    case 4:
                        endRun();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }


    private void addContact() {
        int input;
        boolean _continue = true, correctInput;
        String name, phone;

        while(_continue){
            System.out.println("Please insert the following details:");
            System.out.println("(If you want to go back, please insert \"-1\".)\n");
            System.out.println("Name:");
            name = scanner.nextLine();

            if(name.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }


            System.out.println("Phone-Number:");
            phone = scanner.nextLine();

            boolean result = false;
            try {
                result = controller.addSupplierContact(supplierID, name, phone);
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
            if(result){
                System.out.println("\nThe new contact has been added.\n");
                System.out.println("Choose:");
                System.out.println("1) Add another contact");
                System.out.println("2) Return");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1 : {
                            System.out.println("Contact was added successfully.\n\n");
                            correctInput = true;
                            break;
                        }
                        case 2 : {
                            _continue = false;
                            correctInput = true;
                            break;
                        }
                        default : System.out.println("You inserted wrong value, please try again.");
                    }
                }
            }
            else{
                System.out.println("Error occurred");
            }
        }

        System.out.println("\n\n");
    }


    private void removeContact() {
        int input;
        boolean _continue = true, correctInput;
        String contact;

        while(_continue){
            correctInput = false;

            System.out.println("Insert the name of the contact you want to remove:");
            System.out.println("(If you want to go back, please insert \"-1\".)\n");
            contact = scanner.nextLine();

            if(contact.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            boolean result = false;
            try {
                result = controller.removeContact(supplierID, contact);
            } catch (Exception e) {
                System.out.println(e.getMessage());;
            }
            if(result){
                System.out.println("The contact was removed successfully.\n\n");
                System.out.println("Choose:");
                System.out.println("1) Remove another contact");
                System.out.println("2) Return");

                while(!correctInput){
                    input = getInput();

                    switch(input){
                        case 1 : {
                            System.out.println("\n\n");
                            correctInput = true;
                            break;
                        }
                        case 2 : {
                            System.out.println("Returning..\n\n");
                            correctInput = true;
                            _continue = false;
                            break;
                        }
                        default : System.out.println("You inserted wrong value, please try again.");
                    }
                }
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }
    }


    private void printContacts() {
        List<String> contacts = new ArrayList<>();
        try {
            contacts = controller.getAllContacts(supplierID);
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }

        if(contacts.size() > 0){
            System.out.println("");
            for(String s : contacts){
                System.out.println(s);
            }
        }
        else{
            System.out.println("[NO CONTACTS]\n");
        }
    }
}
