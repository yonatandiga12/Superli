package Presentation.CLIPresentation.Screens;

import Domain.Service.Objects.Employee.Employee;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;
import Globals.util.EmployeeComparator;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static Globals.util.HumanInteraction.*;

public class EmployeesMenu extends Screen {
    public static class EmployeesViewer extends Screen {
        private static final String[] menuOptions = {
                "Print all Employees",          //1
                "Print all Cashiers",           //2
                "Print all Carriers",           //3
                "Print all Storekeepers",       //4
                "Print all Sorters",            //5
                "Print all HR Managers",        //6
                "Print all Logistics Managers", //7
                "Print all Transport Managers", //8
                "Exit"                          //9
        };

        public EmployeesViewer(Screen caller) {
            super(caller, menuOptions);
        }

        @Override
        public void run() {
            System.out.println("\nWelcome to the Employee Viewer Menu!");
            int option = 0;
            while (option != 9) {
                option = runMenu();
                try {
                    switch (option) {
                        case 1:
                            System.out.println("\nPrinting all employees:");
                            printEmployeesWithType(controller.getAllEmployees());
                            break;
                        case 2:
                            System.out.println("\nPrinting all cashiers:");
                            printEmployees(controller.getAllCashiers());
                            break;
                        case 3:
                            System.out.println("\nPrinting all carriers:");
                            printEmployees(controller.getAllCarriers());
                            break;
                        case 4:
                            System.out.println("\nPrinting all storekeepers:");
                            printEmployees(controller.getAllStorekeepers());
                            break;
                        case 5:
                            System.out.println("\nPrinting all sorters:");
                            printEmployees(controller.getAllSorters());
                            break;
                        case 6:
                            System.out.println("\nPrinting all HR managers:");
                            printEmployees(controller.getAllHR_Managers());
                            break;
                        case 7:
                            System.out.println("\nPrinting all logistics managers:");
                            printEmployees(controller.getAllLogistics_Managers());
                            break;
                        case 8:
                            System.out.println("\nPrinting all Transport managers:");
                            printEmployees(controller.getAllTransport_Managers());
                            break;
                        case 9:
                            endRun();
                            break;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please try again");
                }
            }
        }

        public static void printEmployees(Set<? extends Employee> employees) {
            for (Employee e : employees.stream().sorted(new EmployeeComparator()).collect(Collectors.toList())) {
                System.out.println("ID: " + e.id + " , Name :" + e.name);
            }
            System.out.println();
        }

        public static void printEmployeesWithType(Set<? extends Employee> employees) {
            for (Employee e : employees.stream().sorted(new EmployeeComparator()).collect(Collectors.toList())) {
                System.out.println("ID: " + e.id + " , Name: " + e.name + ", Job: " + e.getType());
            }
            System.out.println();
        }
    }

    private static final String[] menuOptions = {
            "View Employees",                                       //1
            "Register Employee",                                         //2
            "Manage Employee (this includes managing constraints)", //3
            "Remove Employee",                                      //4
            "Exit"                                                  //5
    };

    private static final ScreenEmployeeFactory factory = new ScreenEmployeeFactory();

    public EmployeesMenu(Screen caller) {
        super(caller, menuOptions);
    }

    @Override
    public void run() {
        System.out.println("\nWelcome to the Employee Management Menu!");
        int option = 0;
        while (option != 1 && option != 3 && option != 5) {
            option = runMenu();
            try {
                switch (option) {
                    case 1:
                        new Thread(new EmployeesViewer(this)).start();
                        break;
                    case 2:
                        addEmployee();
                        break;
                    case 3:
                        manageEmployee();
                        break;
                    case 4:
                        removeEmployee();
                        break;
                    case 5:
                        endRun();
                        break;
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Please try again");
                option =0;
            }
        }
    }

    private void manageEmployee() throws Exception {
        System.out.println("\nEnter ID of the employee you would like to manage:");
        String id = getString();
        new Thread(factory.createScreenEmployee(this, controller.getEmployee(id))).start();
    }

    private void removeEmployee() throws Exception {
        System.out.println("\nYou are choosing to remove an employee from the system. \nBe aware that this process is irreversible");
        boolean success = false;
        while (!success) {
            System.out.println("Please enter ID of the employee you wish to remove (enter -1 to cancel this action)");
            String id = getString();
            Employee toBeRemoved = controller.getEmployee(id);
            System.out.println("Employee " + toBeRemoved.name + ", ID: " + toBeRemoved.id + " is about to be removed");
            if (areYouSure()) {
                controller.removeEmployee(toBeRemoved.id);
                success = true;
                System.out.println(toBeRemoved.name + " has been successfully removed from the system\n");
            }
        }
    }

    private void addEmployee() throws Exception {
        System.out.println("\nLets add a new employee! (you can cancel this action at any point by entering -1)");

        //ID
        String id = null;
        boolean success = false;
        while (!success) {
            System.out.println("\nEnter new employee's ID");
            id = getString();
            try {
                controller.checkUnusedEmployeeID(id);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Chosen ID: " + id);
            success = areYouSure();
        }

        //Name
        String name = null;
        success = false;
        while (!success) {
            System.out.println("\nEnter new employee's name");
                name = getString();
                    System.out.println("Chosen name: " + name);
                    success = areYouSure();
        }

        //Bank Details
        String bankDetails = null;
        success = false;
        while (!success) {
            System.out.println("\nEnter " + name + "'s bank details");
            bankDetails = getString();
            System.out.println("Chosen bank details: " + bankDetails);
            success = areYouSure();
        }

        //Job Title
        JobTitles jobTitle = null;
        success = false;
        while (!success) {
            System.out.println("\nEnter " + name + "'s job");
            for (int i = 0; i < JobTitles.values().length; i++)
                System.out.println((i + 1) + " -- " + JobTitles.values()[i]);
            jobTitle = JobTitles.values()[getNumber(1, JobTitles.values().length) - 1];
            System.out.println("Chosen job title: " + jobTitle);
            success = areYouSure();
        }

        //Starting Date
        LocalDate startingDate = null;
        success = false;
        while (!success) {
            System.out.println("\nEnter " + name + "'s starting date");
            startingDate = buildDate();
            System.out.println("Chosen starting date: " + dateFormat.format(startingDate));
            success = areYouSure();
        }

        //salary
        Integer salary = null;
        success = false;
        while (!success) {
            System.out.println("\nEnter " + name + "'s salary per shift");
            salary = getNumber(0);
            System.out.println("Chosen salary: " + salary);
            success = areYouSure();
        }

        //certifications
        Set<Certifications> certifications = new HashSet<>();
        success = false;
        while (!success) {
            System.out.println("\nEnter " + name + "'s certifications");
            System.out.println("0 -- stop adding certifications");
            for (int i = 0; i < Certifications.values().length; i++)
                System.out.println((i + 1) + " -- " + Certifications.values()[i]);
            int ordinal = -1;
            while (ordinal != 0) {
                ordinal = getNumber(0, Certifications.values().length);
                if (ordinal != 0)
                    certifications.add(Certifications.values()[ordinal - 1]);
            }
            System.out.println("Chosen certifications: " + String.join(", ", certifications.stream().map(Enum::name).collect(Collectors.toSet())));
            success = areYouSure();
        }

        controller.addEmployee(jobTitle, id, name, bankDetails, salary, startingDate, certifications);
        System.out.println(id + " - "  + name + " has been successfully added as " + jobTitle);
    }
}