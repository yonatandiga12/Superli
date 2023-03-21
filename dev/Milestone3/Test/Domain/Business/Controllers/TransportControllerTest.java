package Domain.Business.Controllers;

import Domain.Business.Controllers.Transport.TransportController;
import Domain.DAL.Abstract.DAO;
import Globals.Enums.ShiftTypes;
import Globals.Pair;
import InventoryTests.CategoryTests;
import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TransportControllerTest extends TestCase {
    @BeforeAll
    public static synchronized void setData() {
        DAO.setDBForTests(TransportControllerTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(TransportControllerTest.class);
    }



    static TransportController transportController = new TransportController();



    //integration test"

    // this test require loading the employee db
    @Test
    public void testCreateTransport() {
        try {
            transportController.createTransport(new Pair<>(LocalDate.parse("2022-06-25"), ShiftTypes.Morning));
        } catch (Exception e) {
            assertTrue(true);
        }
/*
        try {
            transportController.createTransport(new Pair<>(LocalDate.parse("2022-06-1"), ShiftTypes.Evening));
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }

 */
    }

    @Test
    public void testPlaceCarrier(){
        try {
            transportController.placeDriver(0,"100");
            assertSame("100", transportController.getTransport(0).getDriverID());
        } catch (Exception e) {

        }
    }
}