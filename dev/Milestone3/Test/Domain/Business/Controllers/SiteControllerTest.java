package Domain.Business.Controllers;

import Domain.Business.Controllers.Transport.SiteController;
import Domain.Business.Objects.Site.Address;
import Domain.Business.Objects.Site.Destination;
import Domain.Business.Objects.Site.Source;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.TransportMudel.DestinationsDAO;
import Domain.DAL.Controllers.TransportMudel.SourcesDAO;
import Globals.Enums.ShippingAreas;
import InventoryTests.CategoryTests;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;

public class SiteControllerTest {
    static SiteController controller = new SiteController();
    static SourcesDAO sourcesDAO = new SourcesDAO();
    static DestinationsDAO destinationsDAO = new DestinationsDAO();
    @BeforeAll
    public static synchronized void setData() {
        DAO.setDBForTests(SiteControllerTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(SiteControllerTest.class);
    }

    @BeforeEach
    public void setUp() throws Exception {
        sourcesDAO.save(new Source(303, new Address(ShippingAreas.North, "a"), "1", "050"));
        sourcesDAO.save(new Destination(105 ,new Address(ShippingAreas.North, "a"), "1", "050"));
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void getSourceID() {
        try {
            assertEquals(303, controller.getSource(303).getId());
        } catch (Exception e) {

        }
    }

    @Test
    public void getSource() {
        try {
            assertEquals(303, controller.getSource(303).getId());
        } catch (Exception e) {

        }
    }

    @Test
    public void getDestination() {
        try {
            assertEquals(105, controller.getDestination(105).getId());
        } catch (Exception e) {

        }
    }
}