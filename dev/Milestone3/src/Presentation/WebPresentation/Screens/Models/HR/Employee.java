package Presentation.WebPresentation.Screens.Models.HR;

import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.ConstraintManagement;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.EmploymentConds;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.SalaryCalculator;
import Presentation.WebPresentation.Screens.ViewModels.HR.EveryEmployee.UpcomingShifts;
import Presentation.WebPresentation.Screens.ViewModels.InventoryScreens.InventoryMainMenu;
import Presentation.WebPresentation.Screens.ViewModels.Suppliers.SupplierMainMenuStorekeeper;
import Presentation.WebPresentation.Screens.ViewModels.Transport.TransportMainMenu;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public abstract class Employee extends Screen {

    private static final String[] BASE_OPTIONS = {
            "View Upcoming shifts",         //0
            "Manage Constraints",           //1
            "Calculate Salary",             //2
            "Print Employment Conditions",  //3
            "Transport Main Menu",          //4
            "Suppliers Main Menu",          //5
            "Inventory Main Menu"           //6
    };

    protected static final int BASE_OPTIONS_COUNT = BASE_OPTIONS.length;

    private final String[] menuOption;

    public final String id;
    private String name;
    private int salary;

    protected Employee(Domain.Service.Objects.Employee.Employee sEmployee, String greeting, String[] extraMenuOptions) {
        super(greeting + sEmployee.name); //greeting is of structure "Welcome <type> "
        this.menuOption = Stream.concat(Arrays.stream(BASE_OPTIONS), Arrays.stream(extraMenuOptions)).toArray(String[]::new);
        id = sEmployee.id;
        name = sEmployee.name;
        salary = sEmployee.salary;
    }

    //for admin purposes
    protected Employee(String greeting, String[] menuOption) {
        super(greeting);
        this.menuOption = menuOption;
        this.id = null;
        this.name = null;
        this.salary = 0;
    }

    @Override
    public void greet(HttpServletResponse resp) throws IOException {
        super.greet(resp);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        header(resp);
        greet(resp);
        printMenu(resp, menuOption);
        handleError(resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        switch (getIndexOfButtonPressed(req)){
            case 0:
                redirect(resp, UpcomingShifts.class);
                break;
            case 1:
                redirect(resp, ConstraintManagement.class);
                break;
            case 2:
                redirect(resp, SalaryCalculator.class);
                break;
            case 3:
                redirect(resp, EmploymentConds.class);
                break;
            case 4:
                redirect(resp, TransportMainMenu.class);
                break;
            case 5:
                redirect(resp, SupplierMainMenuStorekeeper.class);
                break;
            case 6:
                redirect(resp, InventoryMainMenu.class);
                break;
        }
    }

    public void updateName() throws Exception {
        name = controller.getEmployee(id).name;
        updateGreet();
    }

    protected abstract void updateGreet();

    public void updateSalary() throws Exception {
        salary = controller.getEmployee(id).salary;
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }
}
