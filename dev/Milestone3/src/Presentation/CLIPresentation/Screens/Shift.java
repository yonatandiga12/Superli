package Presentation.CLIPresentation.Screens;

import Domain.Service.Objects.Employee.Employee;
import Globals.Enums.JobTitles;
import Globals.Enums.ShiftTypes;
import Globals.util.Supplier;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Globals.util.HumanInteraction.*;

public abstract class Shift extends Screen {

    private static final String[] menuOptions = {
            "Print shift details",              //1
            "Update employee count(s)",         //2
            "Assign employees",                 //3
            "Remove employees from assignment"  //4
    };

    private final Map<JobTitles, Supplier<Set<Employee>>> getAssignedByType = Stream.of(
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Sorter, () -> controller.getAssignedSortersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Storekeeper, () -> controller.getAssignedStorekeepersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Carrier, () -> controller.getAssignedCarriersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Cashier, () -> controller.getAssignedCashiersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.HR_Manager, () -> controller.getAssignedHR_ManagersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Logistics_Manager, () -> controller.getAssignedLogistics_ManagersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Transport_Manager, () -> controller.getAssignedTransports_ManagersFor(getDate(), getType()))
    ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    private final Map<JobTitles, Supplier<Set<Employee>>> getAvailableByType = Stream.of(
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Sorter, () -> controller.getAvailableSortersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Storekeeper, () -> controller.getAvailableStorekeepersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Carrier, () -> controller.getAvailableCarriersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Cashier, () -> controller.getAvailableCashiersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.HR_Manager, () -> controller.getAvailableHR_ManagersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Logistics_Manager, () -> controller.getAvailableLogistics_ManagersFor(getDate(), getType())),
            new AbstractMap.SimpleEntry<JobTitles, Supplier<Set<Employee>>>(JobTitles.Transport_Manager, () -> controller.getAvailableTransports_ManagersFor(getDate(), getType()))
    ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));

    protected final LocalDate date;
    protected String shiftManagerId;

    private final Map<JobTitles, Integer> getCountByType = new HashMap<>();

    public Shift(Screen caller, Domain.Service.Objects.Shift.Shift sShift, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
        date = sShift.date;

        shiftManagerId = sShift.shiftManagerId;
        getCountByType.put(JobTitles.Carrier, sShift.carrierCount);
        getCountByType.put(JobTitles.Cashier ,sShift.cashierCount);
        getCountByType.put(JobTitles.Storekeeper ,sShift.storekeeperCount);
        getCountByType.put(JobTitles.Sorter ,sShift.sorterCount);
        getCountByType.put(JobTitles.HR_Manager ,sShift.hr_managersCount);
        getCountByType.put(JobTitles.Logistics_Manager ,sShift.logistics_managersCount);
        getCountByType.put(JobTitles.Transport_Manager ,sShift.transport_managersCount);
    }

    protected void handleBaseOptions(int option) throws Exception {
        switch (option) {
            case 1:
                printDetails();
                break;
            case 2:
                updateCount();
                break;
            case 3:
                assignEmployees();
                break;
            case 4:
                removeFromAssignment();
                break;
        }
    }

    private void removeFromAssignment() throws Exception {
        System.out.println("Which type of employee would you like remove from assign for this shift?");
        for (int i = 0; i < JobTitles.values().length; i++)
            System.out.println((i + 1) + " -- " + JobTitles.values()[i]);
        JobTitles type = JobTitles.values()[getNumber(1, JobTitles.values().length) - 1];
        Set<Employee> assigned = getAssignedByType.get(type).get();
        Set<String> assignedIDs = assigned.stream().map(e -> e.id).collect(Collectors.toSet());
        System.out.println("\nCurrent " + type + "s assigned to this shift:");
        for (Employee employee : assigned)
            System.out.println(employee.id + " - " + employee.name + " : " + controller.getEmployeeWorkDetailsForCurrentMonth(employee.id));
        System.out.println();
        if (assignedIDs.size() == 0) {
            System.out.println("Can't remove more " + type + "s - 0 assigned.");
            return;
        }
        System.out.println("\nEnter ONLY the IDs of the wanted employees to be removed!");
        boolean success = false;
        while (!success) {
            String id = getString();
            if (!assignedIDs.contains(id))
                System.out.println(id + " is not an ID out of the assigned employee list. \nPlease try again");
            else {
                System.out.println(id + " successfully removed");
                assignedIDs.remove(id);
                if (assignedIDs.size() == 0)
                    System.out.println("Removed all");
                System.out.println("Do you want to save?");
                success = yesOrNo();
                if (!success && assignedIDs.size() == getCountByType.get(type))
                    operationCancelled();
            }
        }

        switch (type) {
            case Sorter:
                controller.editShiftSorterIDs(getDate(), getType(), assignedIDs);
                break;
            case Storekeeper:
                controller.editShiftStorekeeperIDs(getDate(), getType(), assignedIDs);
                break;
            case Carrier:
                controller.editShiftCarrierIDs(getDate(), getType(), assignedIDs);
                break;
            case Cashier:
                controller.editShiftCashierIDs(getDate(), getType(), assignedIDs);
                break;
            case HR_Manager:
                controller.editShiftHR_ManagerIDs(getDate(), getType(), assignedIDs);
                break;
            case Logistics_Manager:
                controller.editShiftLogistics_ManagerIDs(getDate(), getType(), assignedIDs);
                break;
            case Transport_Manager:
                controller.editShiftTransport_ManagerIDs(getDate(), getType(), assignedIDs);
                break;
        }
    }

    private void assignEmployees() throws Exception {
        System.out.println("Which type of employee would you like to assign for this shift?");
        System.out.println("1 -- ShiftManager");
        for (int i = 0; i < JobTitles.values().length; i++)
            System.out.println((i + 2) + " -- " + JobTitles.values()[i]);
        int ordinal = getNumber(1, JobTitles.values().length + 1);
        if (ordinal == 1) {
            assignShiftManager();
        } else {
            assignEmployee(JobTitles.values()[ordinal - 2]);
        }
    }

    private void assignEmployee(JobTitles type) throws Exception {
        Set<Employee> assigned = getAssignedByType.get(type).get();
        Set<String> assignedIDs = assigned.stream().map(e -> e.id).collect(Collectors.toSet());
        System.out.println("\nCurrent " + type + "s assigned to this shift:");
        for (Employee employee : assigned)
            System.out.println(employee.id + " - " + employee.name + " : " + controller.getEmployeeWorkDetailsForCurrentMonth(employee.id));
        if (assignedIDs.size() == getCountByType.get(type)){
            System.out.println("Can't assign more " + type +"s - Max number of " + type + "s has been reached.");
            return;
        }
        Set<Employee> available = getAvailableByType.get(type).get();
        Set<String> availableIDs = available.stream().map(e -> e.id).collect(Collectors.toSet());
        System.out.println("Please add up to " + (getCountByType.get(type) - assignedIDs.size()) + " " + type + "s out of the following available employees");
        for (Employee employee : available)
            System.out.println(employee.id + " - " + employee.name + " : " + controller.getEmployeeWorkDetailsForCurrentMonth(employee.id));
        System.out.println("\nEnter ONLY the ID of the wanted employees!");
        boolean success = false;
        while (!success) {
            String id = getString();
            if (id.equals("-1"))
                operationCancelled();
            if (!availableIDs.contains(id))
                System.out.println(id + " is not an ID out of the available employee list. \nPlease try again");
            else {
                System.out.println(id + " successfully added");
                assignedIDs.add(id);
                availableIDs.remove(id);
                if (assignedIDs.size() == getCountByType.get(type))
                    System.out.println("Max number has been reached");
                System.out.println("Do you want to save?");
                success = yesOrNo();
                if (!success && assignedIDs.size() == getCountByType.get(type))
                    operationCancelled();
            }
        }

        switch (type) {
            case Sorter:
                controller.editShiftSorterIDs(getDate(), getType(), assignedIDs);
                break;
            case Storekeeper:
                controller.editShiftStorekeeperIDs(getDate(), getType(), assignedIDs);
                break;
            case Carrier:
                controller.editShiftCarrierIDs(getDate(), getType(), assignedIDs);
                break;
            case Cashier:
                controller.editShiftCashierIDs(getDate(), getType(), assignedIDs);
                break;
            case HR_Manager:
                controller.editShiftHR_ManagerIDs(getDate(), getType(), assignedIDs);
                break;
            case Logistics_Manager:
                controller.editShiftLogistics_ManagerIDs(getDate(), getType(), assignedIDs);
                break;
            case Transport_Manager:
                controller.editShiftTransport_ManagerIDs(getDate(), getType(), assignedIDs);
                break;
        }
        System.out.println("Chosen group saved successfully");
        if (assignedIDs.size() < getCountByType.get(type)) {
            System.out.println("Notice that the number of assigned employees doesn't meet the requirement of " + getCountByType.get(type) + " " + type + "s");
            System.out.println("Please make sure more employees register to the constraint of this shift");
        }
    }

    private void assignShiftManager() throws Exception {
        if (hasShiftManager()) {
            Employee currManager = controller.getEmployee(shiftManagerId);
            System.out.println("\nCurrent shift manager: " + currManager.name + ", ID: " + currManager.id);
        }
        else
            System.out.println("\nCurrent shift manager: NO MANAGER ASSIGNED!");
        List<Employee> availableManagers = new ArrayList<>(controller.getAvailableShiftManagersFor(getDate(), getType()));
        if (availableManagers.size() == 0) {
            System.out.println("No employee who is certified to manage shifts has filled a possibility to work at this shift.");
            System.out.println("Cannot assign a shift manager");
            operationCancelled();
        }
        System.out.println("Choose manager for this shift");
        for (int i = 0; i < availableManagers.size(); i++)
            System.out.println((i + 1) + " -- " + availableManagers.get(i).name + ": " + controller.getEmployeeWorkDetailsForCurrentMonth(availableManagers.get(i).id));

        Employee manager = null;
        boolean success = false;
        while (!success) {
            manager = availableManagers.get(getNumber(1, availableManagers.size()) - 1);
            System.out.println("\nEntered manager: " + manager.name);
            success = areYouSure();
        }
        setShiftManagerId(manager.id);
        System.out.println("Successfully assigned " + manager.name + " as manager of this shift");
    }

    private void updateCount() throws Exception {
        System.out.println("\nWhich type of count would you like to update?");
        for (int i = 0; i < JobTitles.values().length; i++)
            System.out.println((i + 1) + " -- " + JobTitles.values()[i]);
        JobTitles type = JobTitles.values()[getNumber(1, JobTitles.values().length) - 1];
        System.out.println("\nCurrent " + type + " count: " + getCountByType.get(type));

        System.out.println("How many " + type + "s do you need for this shift?");
        int newCount = getNumber(0);

        switch (type) {
            case Carrier:
                setCarrierCount(newCount);
                break;
            case Cashier:
                setCashierCount(newCount);
                break;
            case Storekeeper:
                setStorekeeperCount(newCount);
                break;
            case Sorter:
                setSorterCount(newCount);
                break;
            case HR_Manager:
                setHr_managersCount(newCount);
                break;
            case Logistics_Manager:
                setLogistics_managersCount(newCount);
                break;
            case Transport_Manager:
                setTransport_managersCount(newCount);
                break;
        }
        System.out.println(type + " count successfully set to " + newCount);
    }

    protected void setShiftManagerId(String shiftManagerId) throws Exception {
        controller.editShiftManagerID(getDate(), getType(), shiftManagerId);
        this.shiftManagerId = shiftManagerId;
    }

    protected void setCarrierCount(int newCount) throws Exception {
        controller.editShiftCarrierCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.Carrier ,newCount);
    }

    protected void setCashierCount(int newCount) throws Exception {
        controller.editShiftCashierCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.Cashier ,newCount);
    }

    protected void setStorekeeperCount(int newCount) throws Exception {
        controller.editShiftStorekeeperCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.Storekeeper ,newCount);
    }

    protected void setSorterCount(int newCount) throws Exception {
        controller.editShiftSorterCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.Sorter ,newCount);
    }

    protected void setHr_managersCount(int newCount) throws Exception {
        controller.editShiftHR_ManagerCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.HR_Manager ,newCount);
    }

    protected void setLogistics_managersCount(int newCount) throws Exception {
        controller.editShiftLogistics_ManagerCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.Logistics_Manager ,newCount);
    }

    private void setTransport_managersCount(int newCount) throws Exception {
        controller.editShiftTransport_ManagerCount(getDate(), getType(), newCount);
        getCountByType.put(JobTitles.Transport_Manager,newCount);
    }

    protected void printEmployeesByType(JobTitles type) throws Exception {
        Set<Employee> assigned = getAssignedByType.get(type).get();
        System.out.println(type + "s (" + assigned.size() + " out of " + getCountByType.get(type) + ")");
        EmployeesMenu.EmployeesViewer.printEmployees(getAssignedByType.get(type).get());
    }

    public abstract ShiftTypes getType();

    protected void printDetails() throws Exception {
        System.out.println("\n" + getType() + " shift");
        System.out.println("Date: " + date.format(dateFormat));
        if (hasShiftManager())
            System.out.println("Shift Manager: " + shiftManagerId + " - " + controller.getEmployee(shiftManagerId).name);
        else
            System.out.println("Shift Manager: " + "NO MANAGER ASSIGNED!");
        for (JobTitles type : JobTitles.values()) {
            printEmployeesByType(type);
            System.out.println();
        }
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    protected int runMenu() {
        if (!hasShiftManager())
            System.out.println("CRITICAL - This shift doesn't have a ShiftManager, please assign one ASAP!");
        return super.runMenu();
    }

    protected boolean hasShiftManager() {
        return !shiftManagerId.equals("-1");
    }
}