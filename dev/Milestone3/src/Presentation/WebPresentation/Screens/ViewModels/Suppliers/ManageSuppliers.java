package Presentation.WebPresentation.Screens.ViewModels.Suppliers;

import Globals.Pair;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ManageSuppliers extends Screen {

    private static final String greet = "Manage Suppliers";
    private static final String removeButton = "Remove Supplier";
    private static final String addButton = "Add Supplier";

    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));



    public ManageSuppliers() {
        super(greet,ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        printForm(resp, new String[] {"ID"}, new String[]{"Supplier ID"}, new String[]{removeButton});
        printForm(resp, new String[] {"name", "bankNumber", "address", "payingAgreement", "contacts", "manufacturers"},
                        new String[]{"Name", "Bank Number", "Address", "Paying agreement", "Contacts", "Manufacturers" }, new String[]{addButton});
        printInstructions(req, resp);

        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);

        if (isButtonPressed(req, removeButton)){
            try {
                int supplierId = Integer.parseInt(req.getParameter("ID"));
                if(controller.removeSupplier(supplierId) ){
                    setError(String.format("Supplier %d removed successfully!", supplierId));
                    refresh(req, resp);
                }
                else{
                    setError("Supplier wasn't removed!");
                    refresh(req, resp);
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp);
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp);
            }
        }
        else if(isButtonPressed(req, addButton)){
            addSupplier(req, resp);
        }

    }


    private void printInstructions(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            PrintWriter out = resp.getWriter();
            out.println("<h4>Enter Contacts like this : Name1, phone-number1, Name2, phone-number2<br>");
            out.println("For example:Israel, 0591234567<br>");
            out.println("Enter manufacturers divided by , like this : Osem, Elit</h4>");

        } catch (Exception e) {
            setError(e.getMessage());
            refresh(req,resp);
        }

    }

    private void addSupplier(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try{
            String name = req.getParameter("name");
            int bankNumber = Integer.parseInt(req.getParameter("bankNumber"));
            String address = req.getParameter("address");
            String payingAgreement = req.getParameter("payingAgreement");

            ArrayList<Pair<String, String>> contacts = splitContatcts(req, resp);

            String manu = req.getParameter("manufacturers");
            ArrayList<String> manufacturers = new ArrayList<>(Arrays.asList(manu.split(",")));
            if(isError()){
                refresh(req, resp);
                return;
            }
            int supplierId = controller.addSupplier(name, bankNumber, address, payingAgreement, contacts, manufacturers);
            if(supplierId != -1){
                setError(String.format("Supplier %d added successfully!", supplierId));
                refresh(req, resp);
            }
            else{
                setError("Supplier wasn't added!");
                refresh(req, resp);
            }
        }
        catch (NumberFormatException e1){
        setError("Please enter a number!");
        refresh(req, resp);
    }
        catch (Exception e){
            setError(e.getMessage());
            refresh(req, resp);
        }
    }

    private ArrayList<Pair<String, String>> splitContatcts(HttpServletRequest req, HttpServletResponse resp) {
        ArrayList<Pair<String, String>> contacts = new ArrayList<>();
        String[] splitContact;
        String contact = req.getParameter("contacts");
        splitContact = contact.split(",");
        for(int i = 0; i < splitContact.length; i += 2){
            if(i+1 >= splitContact.length){
                setError("Missing details in Contacts!");
                return new ArrayList<>();
            }
            contacts.add(new Pair<>(splitContact[i], splitContact[i+1]));
        }
        return contacts;
    }


}
