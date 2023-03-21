package Domain.Business.Controllers;

import Domain.Business.Controllers.Transport.DocumentController;
import Domain.Business.Controllers.Transport.TruckController;
import Domain.Business.Objects.Document.DestinationDocument;
import Domain.Business.Objects.Document.TransportDocument;
import Domain.Business.Objects.Truck;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.TransportMudel.DestinationDocumentDAO;
import Domain.DAL.Controllers.TransportMudel.TransportDocumentDataMapper;
import Globals.Enums.TruckModel;
import InventoryTests.CategoryTests;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DocumentControllerTest extends TestCase{
    private static final InventoryController is =  InventoryController.getInventoryController();
    static DestinationDocument dDoc  =  new DestinationDocument(303, 1, new ArrayList<>());
    static TransportDocument tDoc  =  new TransportDocument(202, "18/01/2002", 123, "Chai");
    private static final DocumentController dc =  new DocumentController();
    static DestinationDocumentDAO destinationDocumentDAO = new DestinationDocumentDAO();
    static TransportDocumentDataMapper transportDocument = new TransportDocumentDataMapper();
    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(DocumentControllerTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(DocumentControllerTest.class);
    }

    @Override
    public void tearDown() throws Exception {
        try{

        }catch(Exception e){

        }
    }

    @BeforeEach
    public void setUp(){
        try {
            dc.uploadDestinationDocument(dDoc);
            dc.uploadTransportDocument(tDoc);
        } catch (Exception e) {

        }
    }


    @Test
    public void testUploadDestinationDocument() {
        try {
            dc.uploadDestinationDocument(dDoc);
            dDoc = dc.getDestinationDocument(303);
            assertEquals(303, dDoc.getID());
        } catch (Exception e) {

        }
    }
    @Test
    public void testGetDestinationDocument() {
        try {
            dDoc = dc.getDestinationDocument(303);
            assertEquals(303, dDoc.getID());
        } catch (Exception e) {

        }

    }
    @Test
    public void testUploadTransportDocument() {
        try {
            dc.uploadTransportDocument(tDoc);
            tDoc = dc.getTransportDocument(202);
            assertEquals(202, tDoc.getTransportID());
        } catch (Exception e) {

        }

    }
    @Test
    public void testGetTransportDocument() {
        try {
            tDoc = dc.getTransportDocument(202);
            assertEquals(202, tDoc.getTransportID());
        } catch (Exception e) {

        }

    }
}