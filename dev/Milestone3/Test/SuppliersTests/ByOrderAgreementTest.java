package SuppliersTests;

import Domain.Business.Objects.Supplier.Agreement.ByOrderAgreement;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.AgreementController;
import net.jcip.annotations.NotThreadSafe;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@NotThreadSafe
public class ByOrderAgreementTest {
    private ByOrderAgreement agreement = new ByOrderAgreement(22);;
    private AgreementController controller;




    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(ByOrderAgreementTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(ByOrderAgreementTest.class);
    }


    @BeforeEach
    public void setUp(){
        //agreement = new ByOrderAgreement(22);
        controller = new AgreementController();
    }

    @Test
    public void test_isTransporting(){
        assertTrue(agreement.isTransporting());
    }

    @Test
    public void test_daysToDelivery(){
        assertEquals(22, agreement.daysToDelivery());
    }


}
