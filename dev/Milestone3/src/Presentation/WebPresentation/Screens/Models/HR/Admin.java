package Presentation.WebPresentation.Screens.Models.HR;

import Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement.EmployeesMenu;
import Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement.HrMessages;
import Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement.ShiftMenu;
import Presentation.WebPresentation.Screens.ViewModels.InventoryScreens.InventoryMainMenu;
import Presentation.WebPresentation.Screens.ViewModels.Suppliers.SupplierMainMenuStoreManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Admin extends Employee{

    private static final String GREET = "Admin Page";

    private static final String[] MENU_OPTIONS = {
            "Suppliers Menu",           //0
            "Employees Menu",           //1
            "Shifts Menu",              //2
            "HR Important Messages",    //3
            "Inventory Main Menu"       //4
    };

    public Admin() {
        super(GREET, MENU_OPTIONS);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        switch (getIndexOfButtonPressed(req)){
            case 0:
                redirect(resp, SupplierMainMenuStoreManager.class);
                break;
            case 1:
                redirect(resp, EmployeesMenu.class);
                break;
            case 2:
                redirect(resp, ShiftMenu.class);
                break;
            case 3:
                redirect(resp, HrMessages.class);
                break;
            case 4:
                redirect(resp, InventoryMainMenu.class);
                break;
        }
    }

    @Override
    protected void updateGreet() {
       setGreeting(GREET);
    }
}
