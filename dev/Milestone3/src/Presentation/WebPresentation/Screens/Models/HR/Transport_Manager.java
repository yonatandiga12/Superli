package Presentation.WebPresentation.Screens.Models.HR;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Transport_Manager extends Employee{

    private static final String GREETING = "Welcome Transport Manager ";

    private static final String[] EXTRA_OPTIONS = {};

    protected Transport_Manager(Domain.Service.Objects.Employee.Transport_Manager sTraMan) {
        super(sTraMan, GREETING, EXTRA_OPTIONS);
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