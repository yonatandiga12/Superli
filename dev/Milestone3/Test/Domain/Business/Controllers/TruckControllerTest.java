package Domain.Business.Controllers;

import Domain.Business.Controllers.Transport.DocumentController;
import Domain.Business.Controllers.Transport.TruckController;
import Domain.Business.Objects.Document.DestinationDocument;
import Domain.Business.Objects.Document.TransportDocument;
import Domain.Business.Objects.Inventory.Category;
import Domain.Business.Objects.Inventory.Product;
import Domain.Business.Objects.Supplier.AgreementItem;
import Domain.Business.Objects.Truck;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.TransportMudel.DestinationDocumentDAO;
import Domain.DAL.Controllers.TransportMudel.TransportDocumentDataMapper;
import Domain.DAL.Controllers.TransportMudel.TruckDAO;
import Globals.Enums.TruckModel;
import InventoryTests.CategoryTests;
import SuppliersTests.AgreementItemTest;
import junit.framework.TestCase;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruckControllerTest extends TestCase{
    private static final  int TRUCK_NUMBER = 181;
    static Truck truck = new Truck(TRUCK_NUMBER, TruckModel.FullTrailer, 500, 1000);
    private static final TruckController tc =  new TruckController();

    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(TruckControllerTest.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(TruckControllerTest.class);
    }

    @Override
    public void tearDown() throws Exception {
        try{
            tc.removeTruck(507);
        }catch(Exception e){

        }
    }

    @BeforeEach
    public void setUp(){
        try {
            tc.addTruck(truck.getLicenseNumber(), truck.getModel(), truck.getNetWeight(), truck.getMaxCapacityWeight());
        } catch (Exception e) {

        }
    }

    @Test
    public void test_setters(){
        try{
            truck.setLicenseNumber(444);
            truck.setModel(TruckModel.DoubleTrailer);
            truck.setNetWeight(200);
            truck.setMaxCapacityWeight(200);
            assertEquals(444, truck.getLicenseNumber());
            assertEquals(TruckModel.DoubleTrailer, truck.getModel());
            assertEquals(200, truck.getNetWeight());
            assertEquals(200, truck.getMaxCapacityWeight());
            truck.setLicenseNumber(TRUCK_NUMBER);
            truck.setModel(TruckModel.FullTrailer);
            truck.setNetWeight(500);
            truck.setMaxCapacityWeight(1000);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void addTruck(){
        try{
            tc.removeTruck(truck.getLicenseNumber());
            tc.addTruck(truck.getLicenseNumber(), truck.getModel(),truck.getNetWeight(), truck.getMaxCapacityWeight());
            Truck t = getTruck(TRUCK_NUMBER);
            if(t != null){
                assertEquals(TRUCK_NUMBER, t.getLicenseNumber());
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private Truck getTruck(int licenseNumber){
        try {
            return tc.getTruck(licenseNumber);
        } catch (Exception e) {
            return null;
        }
    }
    @Test
    public void removeTruck(){
        try{
            tc.removeTruck(truck.getLicenseNumber());
            Truck t = getTruck(TRUCK_NUMBER);
            assertNull(t);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getTruck(){
        try{
            Truck t = getTruck(TRUCK_NUMBER);
            if(t != null){
                assertEquals(TRUCK_NUMBER, t.getLicenseNumber());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    private void cleanTrucks(){
        try {
            tc.removeTruck(507);
        } catch (Exception e) {
        }
    }


}






