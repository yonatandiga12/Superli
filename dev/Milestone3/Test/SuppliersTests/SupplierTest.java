package SuppliersTests;

import Domain.Business.Controllers.SupplierController;
import Domain.Business.Objects.Supplier.Contact;
import Domain.Business.Objects.Supplier.Supplier;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.SuppliersDAO;
import net.jcip.annotations.NotThreadSafe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * NOTE: the tests assumes that the DB is empty.
 */

@NotThreadSafe
class SupplierTest {

    private SupplierController supplierController = new SupplierController();
    static Supplier supplier;
    static ArrayList<Contact> contacts;
    static ArrayList<String> manufacturers;
    static SuppliersDAO dao;

    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(SupplierTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(SupplierTest.class);
    }



    @BeforeEach
    public void setUp() throws Exception{
        contacts = new ArrayList<>();
        manufacturers = new ArrayList<>();
        manufacturers.add("Osem");
        manufacturers.add("Elit");
        contacts.add(new Contact("name", "phone"));
        dao = new SuppliersDAO();

        supplier = new Supplier("name",23, "address", "credit", contacts, manufacturers, dao);
    }



    @Test
    void isTransporting() {
        try {
            supplier.addAgreement(1, "2", dao);  //transporting
            assertTrue(supplier.isTransporting());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            supplier.addAgreement(2, "2", dao);  //transporting
            assertTrue(supplier.isTransporting());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            supplier.addAgreement(3, "", dao);   //not transporting
            assertFalse(supplier.isTransporting());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            dao.removeSupplier(supplier.getId());
        }

    }



    @Test
    void newOrder() {
        int orderId = 0;
        try {
            orderId = supplierController.addNewOrder(1, 1);
            assertTrue(orderId != -1);
            boolean res = supplierController.removeOrder(orderId);
            assertTrue(res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    void getPossibleDates(){
        try{
            supplierController.loadSuppliersData();
            List<LocalDate> dates = supplierController.getPossibleDates(1);
            LocalDate now = LocalDate.now().plusDays(75);
            for(int i = 0; i <=7; i++ ){
                assertFalse(dates.contains(now));
                now.plusDays(1);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}