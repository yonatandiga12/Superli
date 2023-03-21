package Presentation.WebPresentation.Screens.Models.HR;

import Presentation.WebPresentation.Screens.ViewModels.Suppliers.OrderHRLogistics;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Logistics_Manager extends Employee{

    private static final String GREETING = "Welcome Logistics Manager ";

    private static final String[] EXTRA_OPTIONS = {
            "Cancel Order"
    };

    protected Logistics_Manager(Domain.Service.Objects.Employee.Logistics_Manager sLogMan) {
        super(sLogMan, GREETING, EXTRA_OPTIONS);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        int index = getIndexOfButtonPressed(req) - BASE_OPTIONS_COUNT;
        switch (index) {
            case 0:
                redirect(resp, OrderHRLogistics.class);
        }
    }

    @Override
    protected void updateGreet() {
        setGreeting(GREETING + getName());
    }
}