package Presentation.CLIPresentation.Screens;

import Domain.Service.Objects.Shift.Shift;
import Globals.Enums.ShiftTypes;
import Globals.util.ShiftComparator;
import Presentation.BackendController;
import Presentation.CLIPresentation.PresentationShiftBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static Globals.util.HumanInteraction.*;


public class ShiftsMenu extends Screen {
    private static final String[] menuOptions = {
            "View incomplete shifts for the next seven days",   //1
            "View existing shifts",                             //2
            "Add shifts",                                       //3
            "Remove shifts",                                    //4
            "Manage existing shifts",                           //5
            "Exit"                                              //6
    };

    private static final ScreenShiftFactory factory = new ScreenShiftFactory();
    private static final PresentationShiftBuilder presentationShiftBuilder = new PresentationShiftBuilder();

    public ShiftsMenu(Screen caller) {
        super(caller, menuOptions);
    }

    public ShiftsMenu(BackendController controller){
        super(controller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Shift Management Menu!");
        int option = 0;
        while (option != 5 && option != 6) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        incompleteShifts();
                        break;
                    case 2:
                        viewShifts();
                        break;
                    case 3:
                        addShifts();
                        break;
                    case 4:
                        removeShifts();
                        break;
                    case 5:
                        manageShift();
                        break;
                    case 6:
                        endRun();
                        break;
                }
            } catch (OperationCancelledException ignored) {
            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
                option = 0;
            }
        }
    }

    private void incompleteShifts() throws Exception {
        List<Shift> shifts = controller.getIncompleteShiftsBetween(LocalDate.now(), LocalDate.now().plusDays(7))
                .stream().sorted(new ShiftComparator()).collect(Collectors.toList());
        System.out.println("\nThe following shifts are incomplete:");
        for (Shift shift : shifts){
            if (shift.shiftManagerId.equals("-1"))
                System.out.println(shift.getType() + ": " + shift.date.format(dateFormat) + " - NO SHIFT MANAGER ASSIGNED YET!");
            else
                System.out.println(shift.getType() + ": " + shift.date.format(dateFormat));
        }
    }

    private void viewShifts() throws Exception {
        System.out.println("Enter first and last dates to see shifts between the dates");
        System.out.println("Enter first date");
        LocalDate start = buildDate();
        System.out.println("Enter ending date");
        LocalDate end = buildDate();
        List<Shift> shifts = controller.getShiftsBetween(start, end).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
        for (Shift shift : shifts)
            System.out.println(shift);
    }

    private void addShifts() throws Exception {
        presentationShiftBuilder.reset();
        presentationShiftBuilder.setDate();
        presentationShiftBuilder.setShiftType();
     //   presentationShiftBuilder.setShiftManager();
        presentationShiftBuilder.setCarrierCount();
        presentationShiftBuilder.setCashierCount();
        presentationShiftBuilder.setStorekeeperCount();
        presentationShiftBuilder.setSorterCount();
        presentationShiftBuilder.setHr_managerCount();
        presentationShiftBuilder.setLogistics_managerCount();
        presentationShiftBuilder.setTransportManagerCount();
        presentationShiftBuilder.buildObject();
    }

    private void removeShifts() throws Exception {
        while (true) {
            System.out.println("Enter details of the shift you want to remove (enter -1 at any point to stop the process)");
            System.out.println("Be aware that this process is irreversible");

            //Date
            LocalDate date = null;
            boolean success = false;
            while (!success) {
                System.out.println("\nEnter shift's date");
                date = buildDate();
                System.out.println("Chosen date: " + date.format(dateFormat));
                success = areYouSure();
                if (!date.isAfter(LocalDate.now())){
                    System.out.println("Can only delete shifts in the future");
                    success = false;
                }
            }

            //ShiftType
            ShiftTypes type = null;
            success = false;
            while (!success) {
                System.out.println("\nEnter shift's type");
                for (int i = 0; i < ShiftTypes.values().length; i++)
                    System.out.println((i + 1) + " -- " + ShiftTypes.values()[i]);
                type = ShiftTypes.values()[getNumber(1, ShiftTypes.values().length) - 1];
                System.out.println("Chosen shift type: " + type);
                success = areYouSure();
            }

            Shift shift = controller.getShift(date, type);
            System.out.println("We are about to delete");
            System.out.println(shift);
            if (areYouSure())
                controller.removeShift(date, type);
        }
    }

    private void manageShift() throws Exception {
        System.out.println("\nEnter details of the shift you want to manage:");

        //Date
        System.out.println("\nEnter shift's date");
        LocalDate date = buildDate();

        //ShiftType
        System.out.println("\nEnter shift's type");
        for (int i = 0; i < ShiftTypes.values().length; i++)
            System.out.println((i + 1) + " -- " + ShiftTypes.values()[i]);
        ShiftTypes type = ShiftTypes.values()[getNumber(1, ShiftTypes.values().length) - 1];

        new Thread(factory.createScreenShift(this, controller.getShift(date, type))).start();
    }
}