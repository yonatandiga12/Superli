package Presentation.CLIPresentation.Screens.InventoryScreens;

import Domain.Service.Objects.SupplierObjects.ServiceItemObject;
import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Domain.Service.Objects.SupplierObjects.ServiceSupplierObject;
import Domain.Service.Services.SupplierService;
import Domain.Service.util.Result;
import Globals.Pair;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class CLI {
    private SupplierService service;
    private boolean terminateSystem;
    private static Scanner scan = new Scanner(System.in);


    public CLI(){
        service = null;
        terminateSystem = false;
    }

    public void init(SupplierService service){
        this.service = service;
        boolean terminateSystem = false;

        System.out.println("Welcome to Suppliers Management!\n");
        System.out.println("When you have the ability to choose one out of several options, insert the number of the option and press \"Enter\"\n\n");

        homePage();
    }

    public void init(){
        service = new SupplierService();
        boolean terminateSystem = false;

        System.out.println("Welcome to Suppliers Management!\n");
        System.out.println("When you have the ability to choose one out of several options, insert the number of the option and press \"Enter\"\n\n");

        homePage();
    }

    private int getInput(){
        boolean stopWait = true;
        int input = -1;

        while(stopWait){
            if(scan.hasNextInt()) {
                input = scan.nextInt();
                stopWait = false;
            }
            else{
                System.out.println("Enter an integer please!");
                scan.nextLine();

            }
        }
        scan.nextLine();

        return input;
    }

    private float getInputFloat(){
        boolean stopWait = true;
        float input = -1;

        while(stopWait){
            if(scan.hasNextFloat()) {
                input = scan.nextFloat();
                stopWait = false;
            }
            else{
                System.out.println("Enter an integer please!");
                scan.nextLine();

            }
        }
        scan.nextLine();

        return input;
    }

    private void homePage(){
        int input;
        boolean correctInput;

        while(!terminateSystem){
            System.out.println("What would you like to do?");
            System.out.println("1) View supplier's card");
            System.out.println("2) Add supplier");
            System.out.println("3) Remove supplier");
            System.out.println("4) Exit\n");


            correctInput = false;

           while(!correctInput){
               input = getInput();

               switch (input){ // temporary
                   case 1 : {
                       supplierCard();
                       correctInput = true ;
                       break;
                   }
                   case 2 : {
                       addSupplier();
                       correctInput = true ;
                       break;
                   }
                   case 3 : {
                       removeSupplier();
                       correctInput = true ;
                       break;
                   }
                   case 4 : {
                       terminateSystem = true;
                       correctInput = true ;
                       break;
                   }
                   default : System.out.println("You inserted wrong value, please try again.");
               }
           }

        }

        try{
            System.out.println("\n\n\nSee you next time!");
            TimeUnit.SECONDS.sleep(3);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    private void supplierCard(){
        int input, supplierID = -1;
        boolean correctInput, _continue = true;

        if(service.isSuppliersEmpty().getValue()){
            System.out.println("NO SUPPLIERS ARE AVAILABLE!\nPress \"Enter\" to return.");
            scan.nextLine();
            return;
        }

        while(_continue){

            System.out.println("Insert the supplier ID you wish to view, then press \"Enter\" to continue.\n");
            System.out.println("If you wish to return, type \"-1\" and press \"Enter\"");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                if(input == -1){
                    System.out.println("\n\n");
                    return;
                }

                Result<ServiceSupplierObject> r = service.getSupplierInfo(input);

                if(r.isOk()){
                    System.out.println(r.getValue().toString());
                    correctInput = true;
                    supplierID = input;
                }
                else{
                    System.out.println("No such supplier or your input is incorrect, please try again.");
                }
            }

            System.out.println("\n");

            System.out.println("Choose the next operation: ");

            System.out.println("1) Edit card");
            System.out.println("2) View agreement");
            System.out.println("3) New agreement");
            System.out.println("4) View contacts");
            System.out.println("5) View represented manufacturers");
            System.out.println("6) Make a new order");
            System.out.println("7) View an order");
            System.out.println("8) Back to home page\n");
            correctInput = false;

            while (!correctInput){
                input = getInput();

                switch (input){
                    case 1 : {
                        editCard(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 2 : {
                        viewAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 3 : {
                        newAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 4 : {
                        viewContacts(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 5 : {
                        viewRepresentedManufacturers(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 6 : {
                        makeOrder(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 7 : {
                        viewOrder(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 8 : {
                        correctInput = true;
                        _continue = false;
                        System.out.println("Returning\n\n");
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }

            System.out.println("\n\n");
        }

    }

    private void makeOrder(int supplierID){
        int input;
        boolean correctInput = false;

        System.out.println("Insert the ID of the new order:");
        System.out.println("If you wish to return, please insert \"-1\"");

        input = getInput();

        if(input == -1){
            System.out.println("Returning..\n");
            return;
        }

        Result<Integer> r = service.order(supplierID, 1); // Result<Boolean> r = service.order(supplierID, input);

        if(r.isError()){
            System.out.println(r.getError());
            System.out.println("Returning, please try again.\n");
            return;
        }

        addItemsToOrder(supplierID, input);

        System.out.println("Order was placed successfully, returning.\n");

    }

    private void addItemsToOrder(int supplierID, int orderId){
        boolean _continue = true, correctInput;
        int itemID, quantity, input;

        System.out.println("Now we shall add items to your new order.");
        System.out.println("PLEASE NOTICE: the order must contain at least one item.\n");

        while (_continue){
            System.out.println("Please insert the following details:");
            System.out.println("ID:");

            itemID = getInput();

            System.out.println("Quantity:");

            quantity = getInput();

            Result<Boolean> r = service.addItemToOrder(supplierID, orderId, itemID, quantity);

            if(r.isOk()){
                System.out.println("Choose:");
                System.out.println("1) Add another item");
                System.out.println("2) Back");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1: {
                            correctInput = true;
                            break;
                        }
                        case 2: {
                            _continue = false;
                            correctInput = true;
                            break;
                        }
                        default: {
                            System.out.println("Wrong value was inserted, please try again.");
                        }
                    }
                }
            }
            else{
                System.out.println(r.getError() + "\n");
                System.out.println("Please try again.\n");
            }


        }

    }

    private void viewOrder(int supplierID){
        int input;
        boolean correctInput, _continue = true;

        while(_continue){
            System.out.println("Insert the ID of the order you wish to watch.");
            System.out.println("If you want to return, insert \"-1\"");

            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return;
            }

            Result<ServiceOrderObject> r = service.getOrder(supplierID, input);

            if(r.isOk()){
                ServiceOrderObject order = r.getValue();
                System.out.println(order.toString());
                System.out.println("\n");

                System.out.println("Choose:");
                System.out.println("1) View another order");
                System.out.println("2) Back");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1: {
                            System.out.println("\n");
                            correctInput = true;
                            break;
                        }
                        case 2:{
                            System.out.println("Returning..\n\n");
                            _continue = false;
                            correctInput = true;
                            break;
                        }
                        default:{
                            System.out.println("You inserted wrong value, please try again.");
                        }
                    }
                }
            }
            else{
                System.out.println("You inserted wrong value, please try again.\n");
            }
        }

    }

    private void addSupplier(){
        int input;
        boolean _continue = true;

        boolean correctInput;
        int id, bankNumber;
        String name, address, payingAgreement, contact, manufacturer;
        ArrayList<Pair<String, String>> contacts;
        boolean done;
        String[] splitedContact;
        ArrayList<String> manufacturers;

        System.out.println("For each detail you insert, please press \"Enter\" after the insertion to continue.\n");

        while(_continue){

            System.out.println("If you want to go back, please insert \"-1\" instead of the ID.\n");

            correctInput = false;
            contacts = new ArrayList<>();
            done = false;
            manufacturers = new ArrayList<>();

            System.out.println("ID: ");
            id = getInput();

            if(id == -1){
                System.out.println("Returning..\n\n");
                return;
            }

            System.out.println("Name: ");
            name = scan.nextLine();
            System.out.println("Bank number: ");
            bankNumber = getInput();
            System.out.println("Address: ");
            address = scan.nextLine();
            System.out.println("Paying agreement: ");
            payingAgreement = scan.nextLine();

            System.out.println("Now, please insert contacts in the following format, then press \"Enter\".\nTo end the insertion, write \"Done\" and then press \"Enter\":\n");
            System.out.println("Name, phone-number\nFor example:\nIsrael, 0591234567\n");

            while(!done){
                contact = scan.nextLine();

                if(Objects.equals(contact, "Done")){
                    done = true;
                }
                else{
                    //if(contact.length() > 12 && contact.contains(",")){ //12 is the minimal length according to the format
                        splitedContact = contact.split(",");
                        contacts.add(new Pair<>(splitedContact[0], splitedContact[1]));
                    //}
                }
            }

            System.out.println("\n\n");

            System.out.println("At last, please enter the names of the manufacturers represented by the supplier. To end the insertion, write \"Done\" and press \"Enter\":\n");

            done = false;

            while(!done){
                manufacturer = scan.nextLine();

                if(Objects.equals(manufacturer, "Done")){
                    done = true;
                }
                else{
                    if(!manufacturer.isEmpty()){
                        manufacturers.add(manufacturer);
                    }
                }
            }

            Result<Integer> result = service.addSupplier(name, bankNumber, address, payingAgreement, contacts, manufacturers);

            if(result.isOk()){
                System.out.println("The new supplier was added successfully to the data base.\n\n");
            }
            else{
                //System.out.println("Something went wrong, please try again.\n\n");
                System.out.println(result.getError());
                return;
            }

            System.out.println("Choose your next action: ");
            System.out.println("1) New agreement to supplier");
            System.out.println("2) Add another supplier");
            System.out.println("3) Back\n");

            while(!correctInput){
                input = getInput();

                switch (input){
                    case 1 : {
                        newAgreement(id);
                        correctInput = true;
                        _continue = false;
                        break;
                    }
                    case 2 : {
                        correctInput = true;
                        break;
                    }
                    case 3 : {
                        correctInput = true;
                        System.out.println("Returning\n\n");
                        _continue = false;
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }
        }



    }

    private void removeSupplier(){
        int input;
        boolean _continue = true, done, correctInput;

        while(_continue){
            done = false;
            System.out.println("Insert the ID of the supplier you wish to delete.");
            System.out.println("WARNING! this action is finite!\n");

            System.out.println("If you want to go back, please insert \"-1\" instead of the ID.\n");

            while(!done){
                correctInput = false;
                input = getInput();

                if(input == -1){
                    System.out.println("Returning..\n");
                    return;
                }

                Result<Boolean> r = service.removeSupplier(input);

                if(r.isOk()){
                    done = true;
                    System.out.println("Supplier was removed successfully, please chose:");
                    System.out.println("1) Remove another supplier");
                    System.out.println("2) Back to home Page");

                    while(!correctInput) {
                        input = getInput();

                        switch (input) {
                            case 1 : {
                                correctInput = true;
                                System.out.print("\n\n");
                                break;
                            }
                            case 2 : {
                                correctInput = true;
                                _continue = false;
                                System.out.print("Returning\n\n");
                                break;
                            }
                            default : System.out.println("You inserted wrong value, please try again.");
                        }
                    }
                }
                else{
                    System.out.println(r.getError());
                    //System.out.println("Something went wrong, please try again.\n");
                }
            }
        }
    }

    private void editCard(int supplierID){
        int input;
        boolean correctInput, _continue = true;

        while(_continue){
            correctInput = false;
            System.out.println("Choose what to edit:");
            System.out.println("1) ID\n2) Bank number\n3) Address\n4) Name\n5) Paying agreement\n6) Back\n");

            while(!correctInput){
                input = getInput();

                switch (input){
                    case 1 : {
                        //supplierID = editID(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 2 : {
                        editBunkNumber(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 3 : {
                        editAddress(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 4 : {
                        editName(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 5 : {
                        editPayingAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 6 : {
                        System.out.println("Returning..");
                        return;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }

            System.out.println("Choose: ");
            System.out.println("1) Edit another detail");
            System.out.println("2) Back to Home Page");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                switch(input){
                    case 1 : {
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

        System.out.println("Returning..\n\n");

    }

    /*
    private int editID(int supplierID){
        int input, newID = -1;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new id please.");

            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return supplierID;
            }

            Result<Boolean> r = service.updateSupplierID(supplierID, input);

            if(r.isOk()){
                correctInput = true;
                newID = input;
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }

        System.out.println("ID was changed successfully.\n\n");
        return newID;

    }
     */

    private void editBunkNumber(int supplierID){
        int input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new bunk number please.");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.updateSupplierBankNumber(supplierID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }

        System.out.println("Bunk number was changed successfully.\n\n");
    }

    private void editAddress(int supplierID){
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new address please.");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.updateSupplierAddress(supplierID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }

        System.out.println("Address was changed successfully.\n\n");
    }

    private void editName(int supplierID){
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new name please.");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.updateSupplierName(supplierID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }

        System.out.println("Name was changed successfully.\n\n");
    }

    private void editPayingAgreement(int supplierID){
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new paying agreement please.");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.updateSupplierPayingAgreement(supplierID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }

        System.out.println("The paying agreement was changed successfully.\n\n");
    }

    private void viewAgreement(int supplierID){
        Result<Boolean> r = service.isRoutineAgreement(supplierID);

        if(!service.hasAgreement(supplierID).getValue()){
            System.out.println("No agreement with this supplier, press \"Enter\" to return.");
            scan.nextLine();
            return;
        }

        if(r.isOk() && r.getValue()){
            viewRoutineAgreement(supplierID);
        }
        else{
            r = service.isByOrderAgreement(supplierID);

            if(r.isOk() && r.getValue()){
                viewByOrderAgreement(supplierID);
            }
            else{
                viewNotTransportingAgreement(supplierID);
            }
        }

    }

    private void viewRoutineAgreement(int supplierID){
        int input;
        boolean correctInput, _continue = true;

        while(_continue){
            correctInput = false;
            System.out.println("Transporting: Yes");
            System.out.println("Routine \\ By Order: Routine");
            System.out.println("Days to next delivery: " + service.daysToDelivery(supplierID).getValue());
            System.out.println("Days of delivery: " + listToString(service.getDaysOfDelivery(supplierID).getValue()) + "\n\n");

            System.out.println("Choose:");
            System.out.println("1) Change days of delivery");
            System.out.println("2) Add days of delivery");
            System.out.println("3) Remove day of delivery");
            System.out.println("4) View all items");
            System.out.println("5) Add item to agreement");
            System.out.println("6) Remove item from agreement");
            System.out.println("7) View item");
            System.out.println("8) Change agreement type");
            System.out.println("9) Back");

            while(!correctInput){
                input = getInput();

                switch (input){
                    case 1 : {
                        changeDaysOfDelivery(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 2 : {
                        addDaysOfDelivery(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 3 : {
                        removeDayOfDelivery(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 4 : {
                        viewAllItems(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 5 : {
                        addItemToAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 6 : {
                        removeItemFromAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 7 : {
                        viewItem(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 8 : {
                        changeAgreementType(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 9 : {
                        correctInput = true;
                        _continue = false;
                        System.out.println("Returning..\n\n");
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");

                }
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

    private void changeDaysOfDelivery(int supplierID){
        String input;
        boolean correctInput = false;

        System.out.println("Insert the new days of delivery");

        System.out.println("If you want to go back, please insert \"-1\".\n");

        while(!correctInput){
            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            if(input.length() > 0){
                Result<Boolean> r = service.setDaysOfDelivery(supplierID, input);

                if(r.isOk()){
                    correctInput = true;
                }
                else{
                    System.out.println("Something went wrong, please try again.");
                }
            }
            else{
                System.out.println("Wrong input, please try again.\n");
            }
        }

        System.out.println("Changes saved, returning\n\n");
    }

    private void addDaysOfDelivery(int supplierID){
        boolean correctInput = false;
        String input;

        System.out.println("Insert the new days of delivery you wish to add");

        System.out.println("If you want to go back, please insert \"-1\".\n");

        while(!correctInput){
            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            if(input.length() > 0){
                Result<Boolean> r = service.addDaysOfDelivery(supplierID, input);

                if(r.isOk()){
                    correctInput = true;
                }
                else{
                    System.out.println("Something went wrong, please try again.");
                }
            }
        }

        System.out.println("Changes saved, returning\n\n");
    }

    private void removeDayOfDelivery(int supplierID){
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

            Result<Boolean> r = service.removeDayOfDelivery(supplierID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.");
            }
        }

        System.out.println("Changes saved, returning\n\n");
    }

    private void viewAllItems(int supplierID){
        Result<List<ServiceItemObject>> r = service.itemsFromOneSupplier(supplierID);

        if(r.isError()){
            System.out.println("Something went wrong, returning..\n\n");
        }

        List<ServiceItemObject> list = r.getValue();

        if(list.isEmpty()){
            System.out.println("[NO ITEMS ARE IN THE AGREEMENT]");
        }

        for(ServiceItemObject item : list){
            System.out.println(item.toString() + "\n");
        }

        System.out.println("\n\nPress \"Enter\" to return.");
        scan.nextLine();
    }

    private void addItemToAgreement(int supplierID){
        boolean _continue = true;
        int id, input;
        String name, manufacturer, bulkString;
        float pricePerUnit;
        String[] bulkArr;
        Map<Integer, Integer> bulkMap;
        boolean correctInput;


        while(_continue){
            bulkArr = new String[0];
            bulkMap = new HashMap<>();
            correctInput = false;

            System.out.println("Insert the details, press \"Enter\" to continue.");
            System.out.println("If you want to go back, please insert \"-1\" instead of the ID.\n");

            System.out.println("ID:");
            id = getInput();

            if(id == -1){
                System.out.println("Returning..\n");
                return;
            }

            System.out.println("Name:");
            name = scan.nextLine();
            System.out.println("Manufacturer:");
            manufacturer = scan.nextLine();
            System.out.println("Price per unit:");
            pricePerUnit = getInputFloat();

            System.out.println("Now, let's insert the bulk prices in the following format:");
            System.out.println("quantity, price, quantity, price, quantity, price ...");
            System.out.println("Example:");
            System.out.println("100, 13, 200, 24");
            System.out.println("Meaning: for 100 units there is a 13% discount, for 200 units there is 24% discount.");
            System.out.println("\nWhen you are done, press \"Enter\".\n\n");

            while(!correctInput){
                bulkString = scan.nextLine();

                if(bulkString.isEmpty()){
                    break;
                }

                bulkArr = bulkString.replaceAll("\\s+","").split(",");

                if(bulkArr.length % 2 != 0){
                    System.out.println("Inserted wrong or not complete values, please try again");
                }
                else{
                    correctInput = true;
                }
            }

            for(int i=0; i<bulkArr.length; i++){
                bulkMap.put(Integer.parseInt(bulkArr[i]), Integer.parseInt(bulkArr[i+1]));
                i++;
            }

            Result<Boolean> r = service.addItemToAgreement(supplierID, id, 1,  manufacturer, pricePerUnit, bulkMap);

            if(r.isOk()){
                System.out.println("The new Item was added successfully.");
                System.out.println("Choose:");
                System.out.println("1) Add another item");
                System.out.println("2) Return");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch (input){
                        case 1 : {
                            correctInput = true;
                            System.out.println("\n\n");
                            break;
                        }
                        case 2 : {
                            correctInput = true;
                            _continue = false;
                            System.out.println("Returning..");
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

    private void removeItemFromAgreement(int supplierID){
        int input;
        boolean correctInput, _continue = true;

        while(_continue){

            correctInput = false;
            System.out.println("Insert the ID of the item you wish to remove:");

            while(!correctInput){

                System.out.println("If you want to go back, please insert \"-1\".\n");

                input = getInput();

                if(input == -1){
                    System.out.println("Returning..\n");
                    return;
                }

                Result<Boolean> r = service.deleteItemFromAgreement(supplierID, input);

                if(r.isError()){
                    System.out.println("Something went wrong, please try again.");
                }
                else{
                    correctInput = true;
                }
            }

            System.out.println("Item was deleted successfully.\n");
            System.out.println("Choose:");
            System.out.println("1) Remove another item");
            System.out.println("2) Return");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                switch(input){
                    case 1 : {
                        correctInput = true;
                        System.out.println("\n\n");
                        break;
                    }
                    case 2: {
                        correctInput = true;
                        _continue = false;
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }
        }
    }

    private void viewItem(int supplierID){
        boolean _continue = true, correctInput;
        int input;
        ServiceItemObject item = null;

        while(_continue){
            correctInput = false;
            System.out.println("Insert the ID of the item you wish to view.");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            while(!correctInput){
                input = getInput();

                if(input == -1){
                    System.out.println("Returning..\n");
                    return;
                }

                Result<ServiceItemObject> r = service.getItem(supplierID, input);

                if(r.isOk()){
                    correctInput = true;
                    item = r.getValue();
                }
                else{
                    System.out.println("Something went wrong, please try again.\n\n");
                }

            }

            System.out.println(item.toString());

            System.out.println("Choose:");
            System.out.println("1) Change ID");
            System.out.println("2) Change name");
            System.out.println("3) Change manufacturer");
            System.out.println("4) Change price per unit");
            System.out.println("5) Add bulk price");
            System.out.println("6) Remove bulk price");
            System.out.println("7) Change discount for bulk price");
            System.out.println("8) Calculate total price of an order of viewed item");
            System.out.println("9) View another item");
            System.out.println("10) Return");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                switch (input){
                    case 1 : {
                        correctInput = true;
                        item.setId(changeItemID(supplierID, item.getId()));
                        break;
                    }
                    case 2 : {
                        correctInput = true;
                        changeItemName(supplierID, item.getId());
                        break;
                    }
                    case 3 : {
                        correctInput = true;
                        changeItemManufacturer(supplierID, item.getId());
                        break;
                    }
                    case 4 : {
                        correctInput = true;
                        changeItemPricePerUnit(supplierID, item.getId());
                        break;
                    }
                    case 5 : {
                        correctInput = true;
                        addBulkPrice(supplierID, item.getId());
                        break;
                    }
                    case 6 : {
                        correctInput = true;
                        removeBulkPrice(supplierID, item.getId());
                        break;
                    }
                    case 7 : {
                        correctInput = true;
                        changeDiscountForBulkPrice(supplierID, item.getId());
                        break;
                    }
                    case 8 : {
                        correctInput = true;
                        calculatePrice(supplierID, item.getId());
                        break;
                    }
                    case 9 : {
                        correctInput = true;
                        break;
                    }
                    case 10 : {
                        correctInput = true;
                        _continue = false;
                        System.out.println("Returning..\n\n");
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }
        }
    }

    private int changeItemID(int supID, int itemID){
        int input, newID = -1;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new ID:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return itemID;
            }

            Result<Boolean> r = service.updateItemId(supID, itemID, input);

            if(r.isOk()){
                correctInput = true;
                newID = input;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's ID was changed successfully.\n\n");

        return newID;
    }

    private void changeItemName(int supID, int itemID){
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new Name:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = null;

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's name was changed successfully.\n\n");
    }

    private void changeItemManufacturer(int supID, int itemID){
        String input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new manufacturer:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = scan.nextLine();

            if(input.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.updateItemManufacturer(supID, itemID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's manufacturer was changed successfully.\n\n");
    }

    private void changeItemPricePerUnit(int supID, int itemID){
        float input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert the new price:");
            System.out.println("If you want to go back, please insert \"-1\".\n");

            input = getInputFloat();

            if(input == -1f){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.updatePricePerUnitForItem(supID, itemID, input);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's price was changed successfully.\n\n");
    }

    private void addBulkPrice(int supID, int itemID){
        int quantity, discount;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity:");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            quantity = getInput();

            if(quantity == -1){
                System.out.println("Returning..\n");
                return;
            }

            System.out.println("Insert discount:");
            discount = getInput();

            Result<Boolean> r = service.addBulkPriceForItem(supID, itemID, quantity, discount);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's bulk price was added successfully.\n\n");
    }

    private void changeDiscountForBulkPrice(int supID, int itemID){
        int quantity, discount;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity:");
            System.out.println("If you want to go back insert \"-1\".\n");
            quantity = getInput();

            if(quantity == -1){
                System.out.println("Returning\n");
                return;
            }

            System.out.println("Insert discount:");
            discount = getInput();

            Result<Boolean> r = service.editBulkPriceForItem(supID, itemID, quantity, discount);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's bulk price was edited successfully.\n\n");
    }

    private void calculatePrice(int supID, int itemID){
        int input;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity to order:");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            input = getInput();

            if(input == -1){
                System.out.println("Returning..\n");
                return;
            }

            Result<Double> r = service.calculatePriceForItemOrder(supID, itemID, input);

            if(r.isOk()){
                correctInput = true;
                System.out.println("\n\nThe total price is: " + r.getValue());
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Returning...\n\n");
    }

    private void removeBulkPrice(int supID, int itemID){
        int quantity;
        boolean correctInput = false;

        while(!correctInput){
            System.out.println("Insert quantity:");
            System.out.println("If you want to go back, please insert \"-1\".\n");
            quantity = getInput();

            if(quantity == -1){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> r = service.removeBulkPriceForItem(supID, itemID, quantity);

            if(r.isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Item's bulk price was removed successfully.\n\n");
    }

    private void changeAgreementType(int supplierID){
        int input;
        String days;
        boolean correctInput = false, _continue = true;

        while(!correctInput){
            System.out.println("Choose the type of agreement you wish to change to:");
            System.out.println("1) Routine agreement");
            System.out.println("2) By order agreement");
            System.out.println("3) Self-Transport agreement");
            System.out.println("4) Back");

            input = getInput();

            if(input == 4){
                System.out.println("Returning..\n");
                return;
            }

            if(input == 1 || input == 2 || input == 3){
                if(input == 1){
                    System.out.println("Insert the number of days of delivery, in the following format:");
                    System.out.println("1 2 5");
                    System.out.println("Meaning: the delivery days are in Sunday, Monday and Thursday");
                    System.out.println("Insert:");

                    days = scan.nextLine();

                    if(service.changeAgreementType(supplierID, input, days).isOk()){
                        correctInput = true;
                    }
                    else{
                        System.out.println("Something went wrong, please try again.\n\n");
                    }
                }
                if(input == 2){
                    System.out.println("Insert the number of days until delivery:");

                    days = scan.nextLine();

                    if(service.changeAgreementType(supplierID, input, days).isOk()){
                        correctInput = true;
                    }
                    else{
                        System.out.println("Something went wrong, please try again.\n\n");
                    }
                }
                if(input == 3){

                    if(service.changeAgreementType(supplierID, input, "").isOk()){
                        correctInput = true;
                    }
                    else{
                        System.out.println("Something went wrong, please try again.\n\n");
                    }
                }
            }
            else{
                System.out.println("Inserted wrong value, please try again.");
            }



        }

        System.out.println("Agreement type was changed successfully, returning.\n\n");

    }

    private void viewByOrderAgreement(int supplierID){
        int input;
        boolean correctInput, _continue = true;

        while (_continue) {
            correctInput = false;
            System.out.println("Transporting: Yes");
            System.out.println("Routine \\ By Order: By Order");
            System.out.println("Days to next delivery: " + service.getDeliveryDays(supplierID).getValue());

            System.out.println("Choose:");
            System.out.println("1) Change days until delivery");
            System.out.println("2) View all items");
            System.out.println("3) Add item to agreement");
            System.out.println("4) Remove item from agreement");
            System.out.println("5) View item");
            System.out.println("6) Change agreement type");
            System.out.println("7) Back");

            while (!correctInput) {
                input = getInput();

                switch (input) {
                    case 1 : {
                        changeDaysUntilDelivery(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 2 : {
                        viewAllItems(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 3 : {
                        addItemToAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 4 : {
                        removeItemFromAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 5 : {
                        viewItem(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 6 : {
                        changeAgreementType(supplierID);
                        correctInput = true;
                        return;
                    }
                    case 7 : {
                        correctInput = true;
                        _continue = false;
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");

                }
            }
        }

        System.out.println("Returning..\n\n");
    }

    private void changeDaysUntilDelivery(int supID){
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

            if(service.changeDaysUntilDelivery(supID, input).isOk()){
                correctInput = true;
            }
            else{
                System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("Days until delivery was changed successfully, returning.\n\n");
    }

    private void viewNotTransportingAgreement(int supplierID) {
        int input;
        boolean correctInput, _continue = true;

        while (_continue) {
            correctInput = false;
            System.out.println("Transporting: No");

            System.out.println("Choose:");
            System.out.println("1) View all items");
            System.out.println("2) Add item to agreement");
            System.out.println("3) Remove item from agreement");
            System.out.println("4) View item");
            System.out.println("5) Change agreement type");
            System.out.println("6) Back");

            while (!correctInput) {
                input = getInput();

                switch (input) {
                    case 1 : {
                        viewAllItems(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 2 : {
                        addItemToAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 3 : {
                        removeItemFromAgreement(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 4 : {
                        viewItem(supplierID);
                        correctInput = true;
                        break;
                    }
                    case 5 : {
                        changeAgreementType(supplierID);
                        correctInput = true;
                        return;
                    }
                    case 6 : {
                        correctInput = true;
                        _continue = false;
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");

                }
            }
        }

        System.out.println("Returning..\n\n");
    }

    private void newAgreement(int supID){
        int input = -1;
        boolean correctInput = false;
        String stringInput = "";

        System.out.println("Insert the number of the option you wish to choose, then press \"Enter\"");
        System.out.println("1) Routine delivery");
        System.out.println("2) By order delivery");
        System.out.println("3) Self-transport");
        System.out.println("4) Back");


        while(!correctInput){
            input = getInput();

            if(input == 4){
                System.out.println("Returning..\n\n");
                return;
            }

            if(input == 1 || input == 2 || input == 3){
                correctInput = true;
            }
            else{
                System.out.println("Wrong value was inserted, please try again.\n\n");
            }
        }

        if(input == 1){
            System.out.println("Insert the delivery days as numbers with whitespaces in between. For example:");
            System.out.println("1 2 5");
            System.out.println("Meaning: Sunday, Monday, Thursday");

            stringInput = scan.nextLine();
        }
        if(input == 2){
            System.out.println("Insert the number of days to deliver:");
            stringInput = scan.nextLine();
        }

        if(service.addAgreement(supID, input, stringInput).isError()){
            System.out.println("A problem has occurred, please try again later");
            return;
        }

        System.out.println("Now, let's add the items included in the agreement.");
        addItemToAgreement(supID);

        System.out.println("All is done, returning\n\n");


    }

    private void viewContacts(int supID){
        int input;
        boolean _continue = true, correctInput;
        String name, phone;
        List<String> contacts;

        while(_continue){
            contacts = service.getAllContacts(supID).getValue();

            if(contacts.size() > 0){
                for(String s : contacts){
                    System.out.println(s);
                }
                System.out.println("\n");
            }
            else{
                System.out.println("[NO CONTACTS]\n");
            }

            System.out.println("Choose an option:");
            System.out.println("1) Add contact");
            System.out.println("2) Remove contact");
            System.out.println("3) Return");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                switch(input){
                    case 1 : {
                        correctInput = true;
                        addContact(supID);
                        break;
                    }
                    case 2 : {
                        correctInput = true;
                        removeContact(supID);
                        break;
                    }
                    case 3 : {
                        correctInput = true;
                        _continue = false;
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }
        }
    }

    private void addContact(int supID){
        int input;
        boolean _continue = true, correctInput;
        String name, phone;

        while(_continue){
            System.out.println("Please insert the following details:");
            System.out.println("(If you want to go back, please insert \"-1\".)\n");
            System.out.println("Name:");
            name = scan.nextLine();

            if(name.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }


            System.out.println("Phone-Number:");
            phone = scan.nextLine();

            Result<Boolean> result =  service.addSupplierContact(supID, name, phone);
            if(result.isOk()){
                System.out.println("The new contact has been added.");
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
                System.out.println(result.getError());
                //System.out.println("Something went wrong, please try again.\n\n");
            }
        }

        System.out.println("\n\n");
    }

    private void removeContact(int supID){
        int input;
        boolean _continue = true, correctInput;
        String contact;

        while(_continue){
            correctInput = false;

            System.out.println("Insert the name of the contact you want to remove:");
            System.out.println("(If you want to go back, please insert \"-1\".)\n");
            contact = scan.nextLine();

            if(contact.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            Result<Boolean> result = service.removeContact(supID, contact);
            if(result.isOk()){
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
                System.out.println(result.getError());
                //System.out.println("Something went wrong, please try again.");
            }
        }
    }

    private void viewRepresentedManufacturers(int supID){
        int input;
        boolean _continue = true, correctInput;

        while (_continue){
            Result<List<String>> r = service.getManufacturers(supID);

            if(r.isError()){
                System.out.println("Something went wrong, returning..\n\n");
                return;
            }

            List<String> list = r.getValue();

            if(list.isEmpty()){
                System.out.println("[THERE ARE NO REPRESENTED MANUFACTURERS BY THIS SUPPLIER]");
            }
            else{
                for(String s : list){
                    System.out.println(s);
                }
            }

            System.out.println("\n\n");

            System.out.println("Choose:");
            System.out.println("1) Add manufacturer");
            System.out.println("2) Remove manufacturer");
            System.out.println("3) Return");

            correctInput = false;

            while(!correctInput){
                input = getInput();

                switch(input){
                    case 1 : {
                        correctInput = true;
                        addManufacturer(supID);
                        break;
                    }
                    case 2 : {
                        correctInput = true;
                        removeManufacturer(supID);
                        break;
                    }
                    case 3 : {
                        correctInput = true;
                        _continue = false;
                        System.out.println("Returning..\n\n");
                        break;
                    }
                    default : System.out.println("You inserted wrong value, please try again.");
                }
            }
        }
    }

    public void addManufacturer(int supID){
        int input;
        boolean _continue = true, correctInput;
        String manufacturer;

        while(_continue){
            System.out.println("Insert the new manufacturer's name:");
            System.out.println("(If you want to go back, please insert \"-1\".)\n");

            manufacturer = scan.nextLine();

            if(manufacturer.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            if(service.addSupplierManufacturer(supID, manufacturer).isOk()){
                System.out.println("The new manufacturer was added successfully.\n");
                System.out.println("Choose:");
                System.out.println("1) Add another manufacturer");
                System.out.println("2) Return");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch(input){
                        case 1 : {
                            System.out.println("\n");
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

    public void removeManufacturer(int supID){
        int input;
        boolean _continue = true, correctInput;
        String manufacturer;

        while(_continue){
            System.out.println("Insert the manufacturer's name you wish to remove:");
            System.out.println("(If you want to go back, please insert \"-1\".)\n");

            manufacturer = scan.nextLine();

            if(manufacturer.equals("-1")){
                System.out.println("Returning..\n");
                return;
            }

            if(service.removeManufacturer(supID, manufacturer).isOk()){
                System.out.println("The manufacturer was removed successfully.\n");
                System.out.println("Choose:");
                System.out.println("1) Remove another manufacturer");
                System.out.println("2) Return");

                correctInput = false;

                while(!correctInput){
                    input = getInput();

                    switch(input){
                        case 1 : {
                            System.out.println("\n\n\n");
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


}
