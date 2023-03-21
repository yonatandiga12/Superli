package Presentation.WebPresentation.Screens.Models.HR;

import Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement.EmployeesMenu;
import Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement.HrMessages;
import Presentation.WebPresentation.Screens.ViewModels.HR.HRManagement.ShiftMenu;
import Presentation.WebPresentation.Screens.ViewModels.Suppliers.OrderHRLogistics;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HR_Manager extends Employee{

    private static final String GREETING = "Welcome HR Manager ";

    private static final String[] EXTRA_OPTIONS = {
            "Employees Menu",       //BASE + 0
            "Shifts Menu",          //BASE + 1
            "HR Important Messages", //BASE + 2
            "Cancel Order"           //BASE + 3
    };

    protected HR_Manager(Domain.Service.Objects.Employee.HR_Manager sHRMan) {
        super(sHRMan, GREETING, EXTRA_OPTIONS);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        int index = getIndexOfButtonPressed(req) - BASE_OPTIONS_COUNT;
        switch (index) {
            case 0:
                redirect(resp, EmployeesMenu.class);
                break;
            case 1:
                redirect(resp, ShiftMenu.class);
                break;
            case 2:
                redirect(resp, HrMessages.class);
                break;
            case 3:
                redirect(resp, OrderHRLogistics.class);
                break;
        }
    }

    @Override
    protected void updateGreet() {
        setGreeting(GREETING + getName());
    }
}
