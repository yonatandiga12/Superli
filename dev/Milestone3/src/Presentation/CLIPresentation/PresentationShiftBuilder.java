package Presentation.CLIPresentation;

import Domain.Service.Objects.Employee.Employee;
import Globals.Enums.Certifications;
import Globals.Enums.ShiftTypes;
import Presentation.BackendController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static Globals.util.HumanInteraction.*;

public class PresentationShiftBuilder {

    private static final String NO_LESS = "There can't be less than %o %s in a shift";
    private static final int MIN_CARRIERS= 1;
    private static final int MIN_CASHIERS = 1;
    private static final int MIN_STOREKEEPERS= 1;
    private static final int MIN_SORTERS = 1;
    private static final int MIN_HR_MANAGERS = 0;
    private static final int MIN_LOGISTICS_MANAGERS = 0;
    private static final int MIN_TRANSPORT_MANAGERS = 0;

    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final BackendController controller;
    private LocalDate date;
    private ShiftTypes type;
    private String managerId;
    private int carrierCount;
    private int cashierCount;
    private int storekeeperCount;
    private int sorterCount;
    private int hr_managerCount;
    private int logistics_managerCount;
    private int transportManagerCount;
    protected final static Scanner scanner = new Scanner(System.in);


    public PresentationShiftBuilder() {
        controller = new BackendController();
        reset();
    }

    private void printExitMassage() {
        System.out.println("Enter details to create shift (enter -1 at any point to stop the process)");

    }

    public void setDate() throws OperationCancelledException {
        //Date
        printExitMassage();
        while (true) {
            System.out.println("\nEnter shift's date");
            date = buildDate();
            System.out.println("Entered date: " + date.format(dateFormat));
            System.out.println("Chosen starting date: " + date.format(dateFormat));
            if (areYouSure())
                return;
        }
    }

    public void setShiftType() throws OperationCancelledException {
        //ShiftType
        printExitMassage();
        while (true) {
            System.out.println("\nEnter shift's type");
            for (int i = 0; i < ShiftTypes.values().length; i++)
                System.out.println((i + 1) + " -- " + ShiftTypes.values()[i]);
            type = ShiftTypes.values()[(getNumber(1, ShiftTypes.values().length)) - 1];
            System.out.println("Chosen shift type: " + type);
            if (areYouSure())
                return;
        }
    }

    public void setCarrierCount() throws OperationCancelledException {
        //Carrier Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many carriers do you need for this shift?");
            carrierCount = getNumber(MIN_CARRIERS, String.format(NO_LESS, MIN_CARRIERS, "carriers"));
            System.out.println("Chosen carrier count: " + carrierCount);
            if (areYouSure())
                return;
        }
    }

    public void setCashierCount() throws OperationCancelledException {
        //Cashier Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many cashiers do you need for this shift?");
            cashierCount = getNumber(MIN_CASHIERS, String.format(NO_LESS, MIN_CASHIERS, "cashiers"));
            System.out.println("Chosen cashier count: " + cashierCount);
            if (areYouSure())
                return;
        }
    }

    public void setStorekeeperCount() throws OperationCancelledException {
        //Storekeeper Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many storekeepers do you need for this shift?");
            storekeeperCount = getNumber(MIN_STOREKEEPERS, String.format(NO_LESS, MIN_STOREKEEPERS, "storekeepers"));
            System.out.println("Chosen storekeeper count: " + storekeeperCount);
            if (areYouSure())
                return;
        }
    }

    public void setSorterCount() throws OperationCancelledException {
        //Sorter Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many sorters do you need for this shift?");
            sorterCount = getNumber(MIN_SORTERS, String.format(NO_LESS, MIN_SORTERS, "sorters"));
            System.out.println("Chosen sorter count: " + sorterCount);
            if (areYouSure())
                return;
        }
    }

    public void setHr_managerCount() throws OperationCancelledException {
        //HR Manager Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many HR managers do you need for this shift?");
            hr_managerCount = getNumber(MIN_HR_MANAGERS, String.format(NO_LESS, MIN_HR_MANAGERS, "HR managers"));
            System.out.println("Chosen HR manager count: " + hr_managerCount);
            if (areYouSure())
                return;
        }
    }


    public void setLogistics_managerCount() throws OperationCancelledException {
        //Logistics Manager Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many logistics managers do you need for this shift?");
            logistics_managerCount = getNumber(MIN_LOGISTICS_MANAGERS, String.format(NO_LESS, MIN_LOGISTICS_MANAGERS, "logistics managers"));
            System.out.println("Chosen logistics manager count: " + logistics_managerCount);
            if (areYouSure())
                return;
        }
    }

    public void setTransportManagerCount() throws OperationCancelledException {
        //Transport Manager Count
        printExitMassage();
        while (true) {
            System.out.println("\nHow many transport managers do you need for this shift?");
            transportManagerCount = getNumber(MIN_TRANSPORT_MANAGERS, String.format(NO_LESS, MIN_TRANSPORT_MANAGERS, "transport managers"));
            System.out.println("Chosen transport manager count: " + transportManagerCount);
            if (areYouSure())
                return;
        }
    }

    public void setShiftManager() throws Exception{
        //Shift Manager
        printExitMassage();
        List<Employee> managers = null;
        managers = controller.getAvailableEmployeesFor(date, type).stream().filter(e -> e.certifications.contains(Certifications.ShiftManagement)).collect(Collectors.toList());
        if (managers.size() == 0) {
            System.out.println("No employee who is certified to manage shifts has filled a possibility to work at this shift.");
            System.out.println("Cannot assign a shift manager");
            operationCancelled();
        }
        while (true) {
            System.out.println("Choose manager for this shift");
            for (int i = 0; i < managers.size(); i++)
                System.out.println((i + 1) + " -- " + managers.get(i).name + ": " + controller.getEmployeeWorkDetailsForCurrentMonth(managers.get(i).id));
            Employee manager = managers.get(getNumber(1, managers.size()) - 1);
            managerId = manager.id;
            System.out.println("Chosen shift manager: " + managerId + " - " + manager.name);
            if (areYouSure())
                return;
        }
    }

    public void buildObject(){
        try {
            if (type == null || date == null){
                throw new RuntimeException(" you should set the date type and manager before ");
            }
            controller.createShift(date, type, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount,transportManagerCount );
            System.out.println("Shift added successfully! Remember to assign employees");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Please try again");
        }
        finally {
            reset();
        }
    }

    public void reset(){
        date = null;
        type = null;
        managerId = null;
        this.carrierCount = MIN_CARRIERS;
        this.cashierCount = MIN_CASHIERS;
        this.storekeeperCount = MIN_STOREKEEPERS;
        this.sorterCount = MIN_SORTERS;
        this.hr_managerCount = MIN_HR_MANAGERS;
        this.logistics_managerCount = MIN_LOGISTICS_MANAGERS;
        this.transportManagerCount = MIN_TRANSPORT_MANAGERS;
    }
}
