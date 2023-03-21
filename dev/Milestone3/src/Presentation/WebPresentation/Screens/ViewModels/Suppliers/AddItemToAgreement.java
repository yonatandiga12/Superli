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
import java.util.*;


public class AddItemToAgreement extends Screen {


    private static final String greet = "Add Item";

    private static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(Arrays.asList(Admin.class, Storekeeper.class));


    public AddItemToAgreement() {
        super(greet,ALLOWED);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!isAllowed(req, resp)){
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);

        //int supId = getSupplierId( req, resp);

        printForm(resp, new String[] {"productId", "idBySupplier", "manufacturer", "pricePerUnit", "bulkPrices"}
        , new String[]{"Product ID", "ID by Supplier", "Manufacturer", "Price Per Unit", "Bulk Prices"}, new String[]{"Add Item To Agreement"});


        handleError(resp);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handleHeader(req, resp);
        if (isButtonPressed(req, "Add Item To Agreement")) {
            try {
                int supId = getSupplierId( req);
                int productId = Integer.parseInt(req.getParameter("productId"));
                int idBySupplier = Integer.parseInt(req.getParameter("idBySupplier"));
                String manufacturer = req.getParameter("manufacturer");
                float ppu = Float.parseFloat(req.getParameter("pricePerUnit"));
                String bulkString = req.getParameter("bulkPrices");

                String[] bulkArr = bulkString.replaceAll("\\s+","").split(",");
                if(bulkArr.length % 2 != 0){
                    setError("Inserted wrong or not complete values, please try again");
                    refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
                }
                Map<Integer, Integer> bulkMap = new HashMap<>();
                for(int i=0; i<bulkArr.length; i++){
                    if(i+1 >= bulkArr.length) {
                        setError("Missing info in bulkMap!");
                        //refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
                        return;
                    }
                    else{
                        bulkMap.put(Integer.parseInt(bulkArr[i]), Integer.parseInt(bulkArr[i+1]));
                        i++;
                    }
                }
                if(controller.addItemToAgreement(supId, productId, idBySupplier, manufacturer, ppu, bulkMap)) {

                    setError(String.format("Added New Item."));
                    refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
                }
                else{
                    setError("Item wasn't added!");
                    refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(supId)});
                }
            } catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"supId"}, new String[]{String.valueOf(getSupplierId(req))});
            }

        }
    }



    private int getSupplierId(HttpServletRequest req) throws IOException {
        return Integer.parseInt(getParamVal(req,"supId"));
    }

}
