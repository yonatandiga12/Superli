package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Presentation.WebPresentation.Screens.Models.HR.Admin;
import Presentation.WebPresentation.Screens.Models.HR.Employee;
import Presentation.WebPresentation.Screens.Models.HR.Storekeeper;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManageManufacturers extends Screen {

    private static final String greet = "Manage Manufacturers";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));


    public ManageManufacturers() {
        super(greet,ALLOWED);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        int supId = getSupplierId(req);
        resp.getWriter().println("<h2>Manage Manufacturers for Supplier" + supId + ".</h2><br>");

        printMenu(resp, new String[]{"Show Manufacturers"});
        printForm(resp, new String[] {"nameAdd"}, new String[]{"Name"}, new String[]{"Add Manufacturer"});
        printForm(resp, new String[] {"nameRemove"}, new String[]{"Name"}, new String[]{"Remove Manufacturer"});

        String val;
        if(((val = getParamVal(req, "showManufacturers")) != null) && val.equals("true")){
            showManufacturers(req, resp, supId);
        }
        handleError(resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        String supId = (getParamVal(req,"supId"));
        if (isButtonPressed(req, "Add Manufacturer")) {
            addManufacturer(req, resp);
        }
        else if(isButtonPressed(req, "Remove Manufacturer")){
            removeManufacturer(req, resp);
        }
        if(getIndexOfButtonPressed(req) == 0){
            redirect(resp, ManageManufacturers.class, new String[]{"showManufacturers","supId"}, new String[]{"true",supId});
            //showManufacturers(req, resp);
        }

    }

    private void showManufacturers(HttpServletRequest req, HttpServletResponse resp, int supId) throws IOException {
        try {
            List<String> list = controller.getManufacturers(supId);
            if(list.isEmpty()){
                setError("[THERE ARE NO REPRESENTED MANUFACTURERS BY THIS SUPPLIER]");
                //refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
            else{
                PrintWriter out = resp.getWriter();
                out.println("<h4>");
                for(String s : list){
                    out.println(s + "<br>");
                }
                out.println("</h4>");
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
        }
    }

    private void addManufacturer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("nameAdd");
            int supId = getSupplierId(req);
            if(!name.equals("") && controller.addSupplierManufacturer(supId, name)){

                setError(String.format("Added manufacturer %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
            else{
                setError("Manufacturer wasn't added!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void removeManufacturer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int supId = getSupplierId(req);
            String name = req.getParameter("nameRemove");
            if(!name.equals("") && controller.removeManufacturer(supId, name)){

                setError(String.format("Removed manufacturer %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
            else{
                setError("Manufacturer wasn't removed!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }



    private int getSupplierId(HttpServletRequest req) throws IOException {
        return Integer.parseInt(getParamVal(req,"supId"));
    }


}
