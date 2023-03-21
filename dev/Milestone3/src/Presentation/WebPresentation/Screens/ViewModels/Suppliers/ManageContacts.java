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

public class ManageContacts extends Screen {

    private static final String greet = "Contacts Management";
    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));


    public ManageContacts() {
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
        resp.getWriter().println("<h2>Manage Contacts for Supplier" + supId + ".</h2><br>");


        printMenu(resp, new String[]{"Show Contacts"});
        printForm(resp, new String[] {"nameAdd", "phone" }, new String[]{"Name", "Phone number"}, new String[]{"Add Contact"});
        printForm(resp, new String[] {"nameRemove" }, new String[]{"Name"}, new String[]{"Remove Contact"});


        String val;
        if(((val = getParamVal(req, "showContacts")) != null) && val.equals("true")){
            showContacts(req, resp);
        }
        handleError(resp);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        String supId = getParamVal(req,"supId");
        if (isButtonPressed(req, "Add Contact")) {
            addContact(req, resp);
        }
        else if(isButtonPressed(req, "Remove Contact")){
            removeContact(req, resp);
        }
        if(getIndexOfButtonPressed(req) == 0){
            redirect(resp, ManageContacts.class, new String[]{"showContacts","supId"}, new String[]{"true", supId});
        }

    }

    private void addContact(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("nameAdd");
            String phone = req.getParameter("phone");

            int supId = getSupplierId(req);

            if(controller.addSupplierContact(supId, name, phone) ){

                setError(String.format("Added Contact %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
            else{
                setError("Contact wasn't added!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void removeContact(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("nameRemove");

            int supId = getSupplierId(req);
            if(controller.removeContact(supId, name)){

                setError(String.format("Removed Contact %s", name));
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
            else{
                setError("Contact wasn't removed!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }
    }

    private void showContacts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int supId = getSupplierId(req);
            List<String> contacts = controller.getAllContacts(supId);
            if(contacts.size() > 0){
                PrintWriter out = resp.getWriter();
                out.println("<h4>");
                for(String s : contacts){
                    out.println(s + "<br>");
                }
                out.println("</h4>");
            }
            else{
                setError("[NO CONTACTS!]");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
            }
        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
        }

    }


    private int getSupplierId(HttpServletRequest req) {
        return Integer.parseInt(getParamVal(req,"supId"));
    }


}
