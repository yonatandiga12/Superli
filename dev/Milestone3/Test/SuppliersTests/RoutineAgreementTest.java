package SuppliersTests;

import Domain.Business.Objects.Supplier.Agreement.RoutineAgreement;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.AgreementController;
import net.jcip.annotations.NotThreadSafe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NotThreadSafe
public class RoutineAgreementTest {

    private RoutineAgreement agreement;
    private List<Integer> days;
    private AgreementController dao;
    private int supplierId = 1000;




    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(RoutineAgreementTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(RoutineAgreementTest.class);
    }


    @BeforeEach
    public void setUp(){
        days = new ArrayList<>();
        days.add(1); days.add(2); days.add(3); days.add(4); days.add(5); days.add(6); days.add(7);
        agreement = new RoutineAgreement(days);
        dao = new AgreementController();
    }

    @Test
    public void test_isTransporting(){
        assertTrue(agreement.isTransporting());
        removeSup();

    }

    @Test
    public void test_getDaysOfDelivery(){
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3); list.add(4); list.add(5); list.add(6); list.add(7);

        assertEquals(list, agreement.getDaysOfDelivery());

        removeSup();

    }

    @Test
    public void test_setDaysOfDelivery(){
        String s = "1 2 3 8";
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3);

        try{
            agreement.setDaysOfDelivery(s, supplierId, dao);

            assertEquals(list, agreement.getDaysOfDelivery());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        finally {
            removeSup();
        }

    }

    @Test
    public void test_addDaysOfDelivery(){
        String s = "5 6";
        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(5); list.add(6);

        try{
            agreement.setDaysOfDelivery("1 2", supplierId, dao);

            agreement.addDaysOfDelivery(supplierId, s, dao);

            assertEquals(list, agreement.getDaysOfDelivery());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        finally {
            removeSup();
        }

    }

    @Test
    public void test_removeDayOfDelivery(){
        agreement.removeDayOfDelivery(5);

        List<Integer> list = new ArrayList<>();
        list.add(1); list.add(2); list.add(3); list.add(4); list.add(6); list.add(7);

        assertEquals(list, agreement.getDaysOfDelivery());

        removeSup();

    }

    void removeSup(){
        try {
            dao.removeSupplier(supplierId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
