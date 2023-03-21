package Presentation.CLIPresentation.Screens;

import Domain.Service.Objects.InventoryObjects.Category;
import Presentation.BackendController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static Globals.util.HumanInteraction.OperationCancelledException;
import static Globals.util.HumanInteraction.getNumber;

public abstract class Screen implements Runnable{
    protected final static Scanner scanner = new Scanner(System.in);
    protected final BackendController controller;
    private final String[] menuOptions;
    private final Screen caller;


    public Screen(BackendController controller, String[] menuOptions){
        this.controller = controller;
        this.menuOptions = menuOptions;
        caller = null;
    }

    public Screen(Screen caller, String[] menuOptions){
        this.controller = caller.controller;
        this.menuOptions = menuOptions;
        this.caller = caller;
    }

    protected void printMenu(){
        for (int i = 0 ; i < menuOptions.length; i++)
            System.out.println((i + 1) + " -- " + menuOptions[i]);
    }

    protected int runMenu(){
        System.out.println("\nWhat would you like to do?");
        printMenu();
        int option = 0;
        try {
            option = getNumber(1, menuOptions.length);
        } catch (OperationCancelledException ignored) {
        }
        return option;
    }

    protected void endRun(){
        if (caller != null)
            new Thread(caller).start();
        else
            System.out.println("Thanks for using software by Group_L!\nBye bye!");
    }

    //Input returns only for int!
    protected int getInput(){
        boolean stopWait = true;
        int input = -1;

        while(stopWait){
            if(scanner.hasNextInt()) {
                input = scanner.nextInt();
                stopWait = false;
            }
            else{
                System.out.println("Enter an integer please!");
                scanner.nextLine();

            }
        }
        scanner.nextLine();

        return input;
    }

    //Input returns only for float!
    protected float getInputFloat(){
        boolean stopWait = true;
        float input = -1;

        while(stopWait){
            if(scanner.hasNextFloat()) {
                input = scanner.nextFloat();
                stopWait = false;
            }
            else{
                System.out.println("Enter an integer please!");
                scanner.nextLine();

            }
        }
        scanner.nextLine();

        return input;
    }

    protected LocalDate getDate() {
        while (true) {
            try {
                System.out.println("Please insert date in format: DD/MM/YYYY");
                String dateInput = scanner.nextLine();
                if (dateInput.equals("c")) {
                    System.out.println("Cancelling command");
                    return null;
                }
                String[] date = dateInput.split("/");
                return LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
            } catch (Exception p) {
                System.out.println("Date in wrong format! please try again. c to cancel command");
            }
        }
    }

    protected int getStoreID() {
        System.out.println("Please insert store ID of store you are interested in.");
        System.out.println("Current store IDs are:");
        System.out.println(controller.getStoreIDs().getValue());
        return scanner.nextInt();
    }

    protected void listCategoryIDs() {
        List<Integer> cIDs = getCatIDs();
        System.out.println("Current category IDs are:");
        System.out.println(cIDs);
    }

    protected List<Integer> getCatIDs() {
        List<Integer> cIDs = new ArrayList<>();
        List<Category> c = controller.getCategories().getValue();
        for (Category cat: c) {
            cIDs.add(cat.getID());
        }
        return cIDs;
    }

    protected double round(double price) {
        price = (int)(price*100);
        return price/100;
    }
}