package Presentation.WebPresentation.Screens.ViewModels.InventoryScreens;

import Domain.Service.util.Result;
import Presentation.WebPresentation.Screens.Models.HR.*;
import Presentation.WebPresentation.Screens.Screen;
import Presentation.WebPresentation.Screens.ViewModels.HR.Login;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Category extends Screen{

    private static final String greet = "Category";
    private static final String setParentButton = "Set parent";
    private static final String setNameButton = "Set name";
    public static final Set<Class<? extends Employee>> ALLOWED = new HashSet<>(0);

    private int categoryID;

    public Category() {
        super(greet, ALLOWED);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isAllowed(req, resp)) {
            redirect(resp, Login.class);
        }
        header(resp);
        greet(resp);
        String s = getParamVal(req, "CategoryID");
        if (s!=null) {
            categoryID = Integer.parseInt(s);
            Result<Domain.Service.Objects.InventoryObjects.Category> category = controller.getCategory(categoryID);
            if (category.isOk() && category.getValue() != null) {
                printForm(resp, new String[]{"new parent"}, new String[]{"New parent ID"}, new String[]{setParentButton});
                printForm(resp, new String[]{"new name"}, new String[]{"New name"}, new String[]{setNameButton});
                printCategory(resp, category.getValue());
            }
        }
        handleError(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (handleHeader(req, resp))
            return;
        if (isButtonPressed(req, setParentButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Transport_Manager.class, HR_Manager.class, Admin.class)))) {
                setError("You have no permission to set category parent");
                refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
                return;
            }
            try {
                int newParentID = Integer.parseInt(req.getParameter("new parent"));
                if(controller.changeCategoryParent(categoryID, newParentID).isOk()) {
                    setError("Category parent has beed changed to: " + controller.getCategory(newParentID).getValue().getName());
                    refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
                }
                else{
                    setError("Parent category hasn't been changed");
                    refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
            }
        }
        else if (isButtonPressed(req, setNameButton)){
            if (!isAllowed(req, resp, new HashSet<>(Arrays.asList(Logistics_Manager.class, Admin.class)))) {
                setError("You have no permission to set category name");
                refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
                return;
            }
            try {
                String newName = req.getParameter("new name");
                if(controller.editCategoryName(categoryID, newName).isOk()) {
                    setError("Category name has been changed to: " + newName);
                    refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
                }
                else{
                    setError("Category name hasn't been changed");
                    refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
                }
            }catch (NumberFormatException e1){
                setError("Please enter a number!");
                refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
            }
            catch (Exception e) {
                setError(e.getMessage());
                refresh(req, resp, new String[]{"CategoryID"}, new String[]{Integer.toString(categoryID)});
            }
        }
    }
    private void printCategory(HttpServletResponse resp, Domain.Service.Objects.InventoryObjects.Category c) {
        try {
            PrintWriter out = resp.getWriter();
            out.println("Category ID: " + c.getID() + "<br>");
            out.println("Category name: " + c.getName() + "<br>");
            out.println("Parent category ID: " + (c.getParentCategory().equals("") ? "no parent" : c.getParentCategory()) + "<br>");
            out.println("Number of products: " + c.getNumOfProducts() + "<br>");
            out.println("Sub categories:");
            for (Domain.Service.Objects.InventoryObjects.Category cat: c.getSubCategories()) {
                out.println(" " + cat.getName() + ",");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
