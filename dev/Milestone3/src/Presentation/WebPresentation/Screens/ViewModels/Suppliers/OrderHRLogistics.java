package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.HR_Manager;
import Presentation.WebPresentation.Screens.Models.HR.Logistics_Manager;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class OrderHRLogistics extends RemoveViewOrder {


    private static final String greet = "Remove & View Order";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Logistics_Manager.class, HR_Manager.class));


    public OrderHRLogistics() {
        super(greet, ALLOWED);
    }


    // TODO: Do we need it here?

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);


        printOrderIds(resp);
        printForm(resp, new String[] {"orderId1"}, new String[]{"Order ID"}, new String[]{"Remove Order"});
        printForm(resp, new String[] {"orderId3"}, new String[]{"Order ID"}, new String[]{"View Order"});

        String val;
        if ((val = getParamVal(req,"viewOrder")) != null && val.equals("true")){
            String orderId = getParamVal(req,"orderId");
            if(orderId != null )
                printOrder(req, resp, orderId);
        }
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        if(isButtonPressed(req, "Remove Order")){
            removeOrder(req, resp);
        }

        else if(isButtonPressed(req, "View Order")){
            String orderId = req.getParameter("orderId3");
            redirect(resp, OrderHRLogistics.class, new String[]{"viewOrder", "orderId"}, new String[]{"true", orderId});
        }

    }



}
