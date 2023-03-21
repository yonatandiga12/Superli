package Presentation.CLIPresentation.Screens;

import Domain.Service.Objects.Shift.Shift;
import Globals.Enums.Certifications;
import Globals.util.HumanInteraction;
import Globals.util.ShiftComparator;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Globals.util.HumanInteraction.*;

public abstract class Employee extends Screen {
    private static final String[] menuOptions = {
            "Print employment conditions",  //1
            "Update name",                  //2
            "Update bank details",          //3
            "Update salary per shift",      //4
            "Update certifications",        //5
            "Calculate Salary",             //6
            "Manage work constraints",      //7
            "Print upcoming shifts"         //8
    };

     protected final String id;
     protected String name;

     protected String bankDetails;
     protected int salary;
     protected String employmentConditions;
     protected final LocalDate startingDate;
     protected Set<Certifications> certifications;

    public Employee(Screen caller, Domain.Service.Objects.Employee.Employee sEmployee, String[] extraMenuOptions) {
        super(caller, Stream.concat(Arrays.stream(menuOptions), Arrays.stream(extraMenuOptions)).toArray(String[]::new));
        id = sEmployee.id;
        name = sEmployee.name;
        bankDetails = sEmployee.bankDetails;
        salary = sEmployee.salary;
        employmentConditions = sEmployee.employmentConditions;
        startingDate = sEmployee.startingDate;
        certifications = new HashSet<>(sEmployee.certifications);
    }

    protected void handleBaseOptions(int option) throws Exception {
        switch (option) {
            case 1 : System.out.println(employmentConditions);
            break;
            case 2 : updateName();
            break;
            case 3 : updateBankDetails();
            break;
            case 4 : updateSalary();
            break;
            case 5 : updateCertifications();
            break;
            case 6 : calculateSalary();
            break;
            case 7 : manageConstraints();
            break;
            case 8 : printUpcomingShifts();
        }
    }

    protected void setName(String name) throws Exception {
        controller.editEmployeeName(getID(), name);
        this.name = name;
        updateEmploymentConditions();
    }

    protected void setBankDetails(String bankDetails) throws Exception {
        controller.editEmployeeBankDetails(getID(), bankDetails);
        this.bankDetails = bankDetails;
    }

    protected void setSalary(int salary) throws Exception {
        controller.editEmployeeSalary(getID(), salary);
        this.salary = salary;
        updateEmploymentConditions();
    }

    protected void setCertifications(Set<Certifications> certifications) throws Exception {
        controller.editEmployeeCertifications(getID(), certifications);
        this.certifications = certifications;
    }

    private void updateEmploymentConditions() throws Exception {
        this.employmentConditions = this.controller.getEmploymentConditionsOf(id);
    }

    private void updateName() throws Exception {
        String name = null;
        boolean success = false;
        while (!success){
            System.out.println("\nEnter employee's new name");
            try {
                name = HumanInteraction.scanner.nextLine();
                if (name.equals("-1")) {
                    operationCancelled();
                }
                else {
                    System.out.println("Entered name: " + name);
                    success = areYouSure();
                }
            }
            catch (Exception ex){
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
        setName(name);
        System.out.println("Updated name: " + name);
    }

    private void updateBankDetails() throws Exception {
        String bankDetails = null;
        boolean success = false;
        while (!success){
            System.out.println("\nEnter " + name +"'s bank details");
            try {
                bankDetails = HumanInteraction.scanner.nextLine();
                if (bankDetails.equals("-1")) {
                    operationCancelled();
                }
                else {
                    System.out.println("Entered bank details: " + bankDetails);
                    success = areYouSure();
                }
            }
            catch (Exception ex){
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
        setBankDetails(bankDetails);
        System.out.println("Updated bank details: " + bankDetails);
    }

    private void updateSalary() throws Exception {
        Integer salary = null;
        boolean success = false;
        while (!success) {
            System.out.println("\nEnter " + name + "'s salary per shift");
            salary = getNumber(0);
            System.out.println("Entered salary title: " + salary);
            success = areYouSure();
        }
        setSalary(salary);
        System.out.println("Updated salary: " + salary);
    }

    private void updateCertifications() throws Exception {
        Set<Certifications> curr = new HashSet<>(certifications);
        System.out.println(name + "'s current certifications: " + String.join(", " , curr.stream().map(Enum::name).collect(Collectors.toSet())));
        System.out.println("\nWould you like to add or remove certifications?");
        System.out.println("1 -- add\n2 -- remove");
        int ans = getNumber(1, 2);

        switch (ans) {
            case 1 : addCertifications(curr);
            break;
            case 2 : removeCertifications(curr);
        }

        System.out.println("New certifications: " + String.join(", " , curr.stream().map(Enum::name).collect(Collectors.toSet())));
        System.out.println("Would you like to save?");
        if (yesOrNo())
            setCertifications(curr);
    }

    private void addCertifications(Set<Certifications> curr) throws OperationCancelledException {
        System.out.println("\nChoose which certifications to add");
        System.out.println("0 -- stop adding certifications");
        for (int i = 0; i < Certifications.values().length; i++)
            System.out.println((i + 1) + " -- " + Certifications.values()[i]);
        int ordinal = -1;
        while (ordinal != 0) {
            ordinal = getNumber(0, Certifications.values().length);
            if (ordinal != 0) {
                System.out.println("Added " + Certifications.values()[ordinal - 1]);
                curr.add(Certifications.values()[ordinal - 1]);
            }
        }
    }

    private void removeCertifications(Set<Certifications> curr) throws OperationCancelledException {
        System.out.println("\nChoose which certifications to remove");
        System.out.println("0 -- stop removing certifications");
        for (int i = 0; i < Certifications.values().length; i++)
            System.out.println((i + 1) + " -- " + Certifications.values()[i]);
        int ordinal = -1;
        while (ordinal != 0) {
            ordinal = getNumber(0, Certifications.values().length);
            if (ordinal != 0) {
                System.out.println("Removed " + Certifications.values()[ordinal - 1]);
                curr.remove(Certifications.values()[ordinal - 1]);
            }
        }
    }

    private void calculateSalary() throws Exception {
        System.out.println("Enter date to start calculating from: ");
        LocalDate start = buildDate();
        System.out.println("Enter last date to calculate: ");
        LocalDate end = buildDate();
        int numOfShifts = controller.getEmployeeShiftsBetween(getID(), start, end).size();
        System.out.println("Between the dates entered " + name + " has done " + numOfShifts + "shifts");
        System.out.println("With a salary of " + salary + " per shift");
        System.out.println("Calculated salary between these date is: " + (numOfShifts * salary));
    }

    private void manageConstraints() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        List<Shift> constraints = controller.getEmployeeConstraintsBetween(getID(), today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
        System.out.println("Current constraints for the following month");
        for (Shift constraint : constraints)
            System.out.println(constraint);

        System.out.println("Would you like to add or remove constraints?");
        System.out.println("1 -- add\n2 -- remove");
        int ans = getNumber(1,2);
        switch (ans) {
            case 1 : addConstraints();
            break;
            case 2 : removeConstraints();
        }
    }

    private void removeConstraints() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        List<Shift> registeredShifts = controller.getEmployeeConstraintsBetween(getID(), today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
        System.out.println("Which constraint would you like to remove?");
        System.out.println("0 -- stop removing constraints");
        for (int i = 0; i < registeredShifts.size(); i++)
            System.out.println((i + 1) + " -- " + registeredShifts.get(i));
        for (int ans = getNumber(0, registeredShifts.size()); ans != 0; ans = getNumber(0, registeredShifts.size())) {
            try {
                controller.unregisterFromConstraint(getID(), registeredShifts.get(ans - 1));
                System.out.println("Successfully removed constraint for " + registeredShifts.get(ans - 1));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Finished adding constraints");
    }

    private void addConstraints() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate nextMonth = today.plusMonths(1);
        List<Shift> availableShifts = controller.getShiftsBetween(today, nextMonth).stream().sorted(new ShiftComparator()).collect(Collectors.toList());
        System.out.println("When can you work? Choose available shifts out of this lists of shifts available for the next month");
        System.out.println("0 -- stop adding constraints");
        for (int i = 0; i < availableShifts.size(); i++)
            System.out.println((i + 1) + " -- " + availableShifts.get(i));
        for (int ans = getNumber(0, availableShifts.size()); ans != 0; ans = getNumber(0, availableShifts.size())) {
            controller.registerToConstraint(getID(), availableShifts.get(ans - 1));
            System.out.println("Successfully added constraint for " + availableShifts.get(ans - 1));
        }
        System.out.println("Finished adding constraints");
    }

    private void printUpcomingShifts() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate nextWeek = today.plusWeeks(1);
        List<Shift> shifts= new ArrayList<>(controller.getEmployeeShiftsBetween(getID(), today, nextWeek));
        shifts.sort(new ShiftComparator());
        System.out.println("Upcoming shift for the following week");
        for (Shift shift : shifts)
            System.out.println(shift);
    }

    public String getID() {
        return id;
    }
}
