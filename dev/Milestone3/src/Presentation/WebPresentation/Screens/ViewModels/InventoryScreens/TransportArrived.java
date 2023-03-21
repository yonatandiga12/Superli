package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.util.Result;
import Globals.Pair;
import Presentation.WebPresentation.Screens.Models.HR.*;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TransportArrived extends Screen {

    private static final String greet = "Transport Arrived";

    private static final String addReport = "Add Info";
    private static final String doneButton = "Done";

    //Map<OrderId<ProductId , ( (missingAmount,defectiveAmount), description)>>
    private static Map<Integer, Map<Integer, Pair<Pair<Integer, Integer>, String>>> info = new HashMap<>();
    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class));
    private Integer transportID;
    public TransportArrived() { super(greet, ALLOWED); }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        try {
            String s = getParamVal(req, "transport ID");
             if (s!=null) {
                 transportID = Integer.parseInt(s);
                 printForm(resp, new String[] {"order", "product", "missing", "defective", "description"}, new String[]{"Order ID", "Product ID", "Amount Missing", "Amount Defective", "Description"}, new String[]{addReport});
                 printForm(resp, new String[] {},new String[] {}, new String[] {doneButton});
                 printInfo(resp);
             }
        } catch (NumberFormatException e1) {
            setError("Please enter transport ID");
            redirect(resp, InventoryManagement.class);
        }
        catch (Exception e) {
            setError(e.getMessage());
            redirect(resp, InventoryManagement.class);
        }
        handleError(resp);
    }

    private void printInfo(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        for (Integer order : info.keySet()) {
            Map<Integer,Pair<Pair<Integer, Integer>, String>> orderInfo = info.get(order);
            for (Integer product : orderInfo.keySet()) {
                Pair<Pair<Integer, Integer>, String> pInfo = orderInfo.get(product);
                int missing = pInfo.getLeft().getLeft();
                int defective = pInfo.getLeft().getRight();
                String description = pInfo.getRight();
                out.println(String.format("Order: %s, Product: %s, Missing: %s, Defective: %s, Description: %s <br>", order, product, missing, defective, description));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req,addReport)) {
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class)))) {
                setError("You have no permission to report on missing or defective items of a transport");
                refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
                return;
            }
            addReport(req, resp);
        }
        else if (isButtonPressed(req, doneButton)) {
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class)))) {
                setError("You have no permission to accept a transport");
                refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
                return;
            }
            transportArrived(req, resp);
        }
    }

    private void transportArrived(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Storekeeper.class)))) {
            setError("You have no permission to accept order to the warehouse");
            refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
            return;
        }
        try {
            Result r = controller.transportArrived(transportID, info);
            if (r.isError()) {
                setError(r.getError());
                refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
            }
            else {
                //setError("Transport accepted successfully");
                //refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
                redirect(resp, InventoryManagement.class);
            }
        }
        catch (NumberFormatException e1){
            setError("Please enter a number!");
            refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
        }
        catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
        }
    }

    private void addReport(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int order = Integer.parseInt(getParamVal(req, "order"));
        int product = Integer.parseInt(getParamVal(req, "product"));
        int missing = Integer.parseInt(getParamVal(req, "missing"));
        int defective = Integer.parseInt(getParamVal(req, "defective"));
        String description = getParamVal(req, "description");
        if (!info.containsKey(order))
            info.put(order, new HashMap<>());
        Map orderInfo = info.get(order);
        orderInfo.put(product,new Pair<>(new Pair(missing, defective), description));
        refresh(req, resp, new String[]{"transport ID"}, new String[]{Integer.toString(transportID)});
    }
}
