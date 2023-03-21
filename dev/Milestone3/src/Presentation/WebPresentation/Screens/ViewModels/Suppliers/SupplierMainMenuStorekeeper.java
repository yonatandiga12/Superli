package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Storekeeper;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SupplierMainMenuStorekeeper extends SupplierMainMenu {

    private static final String greet = "Supplier's Main Menu";
    private static final String button = "View Supplier";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Storekeeper.class));

    public SupplierMainMenuStorekeeper() {
        super(greet,ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        printMenu(resp, new String[]{"Manage Suppliers", "Manage Orders"});

        printSupplierIds(resp, req);
        printForm(resp, new String[] {"ID"}, new String[]{"Supplier ID"}, new String[]{button});

        handleError(resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, button)){
            viewSupplier(req, resp);
        }

        switch (getIndexOfButtonPressed(req)){
            case 0:
                redirect(resp, ManageSuppliers.class);
                break;
            case 1:
                redirect(resp, ManageOrders.class);
                break;

        }
    }


}
