package Presentation.WebPresentation.Screens.Models.HR;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Storekeeper extends Employee{

    private static final String GREETING = "Welcome Storekeeper ";

    private static final String[] EXTRA_OPTIONS = {

    };

    protected Storekeeper(Domain.Service.Objects.Employee.Storekeeper sStorekeeper) {
        super(sStorekeeper, GREETING, EXTRA_OPTIONS);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        int index = getIndexOfButtonPressed(req) - BASE_OPTIONS_COUNT;
        switch (index) {

        }
    }

    @Override
    protected void updateGreet() {
        setGreeting(GREETING + getName());
    }
}