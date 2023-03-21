package Presentation.CLIPresentation.Screens.Transport;

import Domain.Service.Objects.Shift.Shift;
import Globals.Enums.ShiftTypes;
import Globals.Pair;
import Globals.util.HumanInteraction;
import Globals.util.ShiftComparator;
import Presentation.CLIPresentation.Objects.Transport.Transport;
import Presentation.CLIPresentation.Screens.Screen;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static Globals.util.HumanInteraction.OperationCancelledException;
import static Globals.util.HumanInteraction.getNumber;

public class TransportsMenu extends Screen {

    private static final String[] menuOptions = {
            "Add transport order",          //1
            "Create new transport",         //2
            "Update transport",             //3
            "Get pending transport",        //4
            "Get in progress transport",    //5
            "Get complete transport",       //6
            "Exit"                          //7
    };
    public TransportsMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Transport Management Menu!");
        int option = 0;
        while (option != 7 && option != 1 && option != 3) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        new Thread(new TransportOrderMenu(this)).start();
                        break;
                    case 2:
                        createNewTransport();
                        break;
                    case 3:
                        new Thread(new UpdateTransportMenu(this)).start();
                        break;
                    case 4:
                        getPendingTransports();
                        break;
                    case 5:
                        getInProgressTransports();
                        break;
                    case 6:
                        getCompleteTransports();
                        break;
                    case 7:
                        endRun();
                        break;
                }
            } catch (OperationCancelledException ignore){
            }catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
            }
        }

    }

    private LocalDate getShiftDate() throws OperationCancelledException {
        System.out.println("\nEnter shift's date");
        LocalDate date = HumanInteraction.buildDate();
        return date;
    }

    private ShiftTypes getShiftType() throws OperationCancelledException {
        System.out.println("\nEnter shift's type");
        for (int i = 0; i < ShiftTypes.values().length; i++)
            System.out.println((i + 1) + " -- " + ShiftTypes.values()[i]);
        ShiftTypes type = ShiftTypes.values()[getNumber(1, ShiftTypes.values().length) - 1];
        return type;
    }

    private void createNewTransport() throws Exception {
        System.out.println("Create transport:");
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        List<Shift> availableShifts = controller.getShiftsBetween(today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
        System.out.println("\nChoose a shift from the following: ");
        for (int i = 0; i < availableShifts.size(); i++)
            System.out.println((i + 1) + " -- " + availableShifts.get(i));
        System.out.println();
        Shift shift = availableShifts.get(getNumber(1 , availableShifts.size()) - 1);
        controller.createNewTransport(new Pair<LocalDate, ShiftTypes>(shift.date, shift.getType()));
    }


    private void getPendingTransports() throws Exception {
        //displayTransportList("Pending", controller.getPendingTransports());

    }

    private void getInProgressTransports() throws Exception {
        //displayTransportList("In Progress", controller.getInProgressTransports());
    }

    private void getCompleteTransports() throws Exception {
        //displayTransportList("Complete", controller.getCompleteTransports());
    }
    private void displayTransportList(String status, Set<Transport> transports)
    {
        System.out.println(status + " Transports: ");
        for (Transport transport: transports)
        {
            transport.display();
        }
    }
}
