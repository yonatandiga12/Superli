package Presentation.CLIPresentation.Screens;

import Globals.Enums.TruckModel;
import Globals.util.HumanInteraction;

public class TruckMenu extends Screen{
    private static final String[] menuOptions = {
            "Add truck",                //1
            "Delete truck",             //2
            //update truck
            "Exit"                      //3
    };
    public TruckMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Truck Management Menu!");
        int option = 0;
        while (option != 3) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        addTruck();
                        break;
                    case 2:
                        deleteTruck();
                        break;
                    case 3:
                        endRun();
                        break;
                }
            }catch (HumanInteraction.OperationCancelledException ignore){
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }
    }

    private int getLicenseNumber() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter truck license number:");
        return HumanInteraction.getNumber(0);
    }
    private void deleteTruck() throws HumanInteraction.OperationCancelledException {
        //License number:
        int licenseNumber = getLicenseNumber();
        try {
            controller.removeTruck(licenseNumber);
            System.out.println("The truck was successfully removed!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addTruck() throws HumanInteraction.OperationCancelledException {
        //License number:
        int licenseNumber = getLicenseNumber();
        //Truck model:
        TruckModel truckModel = getTruckModel();
        //Net weight:
        int netWeight = getTruckWeight();
        //Max capacity weight:
        int maxCapacityWeight = getMaxCapacityWeight(truckModel);
        try {
            controller.addTruck(licenseNumber, truckModel, netWeight, maxCapacityWeight);
            System.out.println("The truck was successfully added!");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private int getMaxCapacityWeight(TruckModel tm) throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter the weight of the truck:");
        printLegalWeight();
        int maxWeight = 0;
        switch (tm)
        {
            case Van:
                maxWeight = HumanInteraction.getNumber(0, 1000);
                break;
            case SemiTrailer:
                maxWeight = HumanInteraction.getNumber(1001, 5000);
                break;
            case DoubleTrailer:
                maxWeight = HumanInteraction.getNumber(5001, 10000);
                break;
            case FullTrailer:
                maxWeight = HumanInteraction.getNumber(10001, 20000);
                break;
        }
        return maxWeight;
    }
    private void printLegalWeight()
    {
        System.out.println("Van - 0 < weight <= 1000\n" +
                            "SemiTrailer - 1000 < weight <= 5000\n" +
                            "DoubleTrailer - 5000 < weight <= 10000\n" +
                            "FullTrailer - 10000 < weight <= 20000\n");
    }
    private int getTruckWeight() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter the weight of the truck:");
        return HumanInteraction.getNumber(1);
    }
    private TruckModel getTruckModel() throws HumanInteraction.OperationCancelledException {
        System.out.println("Enter truck model:");
        for (int i = 0; i < TruckModel.values().length; i++){
            System.out.println((i + 1) + " -- " + TruckModel.values()[i]);
        }
        return TruckModel.values()[HumanInteraction.getNumber(1, 4) - 1];
    }

}
