package InventoryTests;

import Domain.Business.Controllers.InventoryController;
import Domain.Business.Objects.Inventory.Category;
import Domain.Business.Objects.Inventory.Product;
import Domain.Business.Objects.Inventory.SaleToCustomer;
import Domain.DAL.Abstract.DAO;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.max;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

//@NotThreadSafe
public class ProductTests {
    static Category category0;
    static Product product;
    static Product product0;
    static Product product1;
    private static final InventoryController is =  InventoryController.getInventoryController();
    private static List<Integer> stores;
    private static int maxStoreCount;

    @BeforeAll
    public synchronized static void getMaxStoreCount() {
        DAO.setDBForTests(ProductTests.class);
        stores=new ArrayList<>();
        maxStoreCount = max(is.getStoreIDs());
        stores.add(is.addStore());
        stores.add(is.addStore());
        stores.add(is.addStore());
    }
    @BeforeEach
    void addLocations() {
        category0 = is.addCategory("Test-Milk",  0);
        product = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product0 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product1 = is.newProduct("Test-Milk-Tara-1L", category0.getID(), 1.0, 4, "25");
        List<Integer> shelves1 = new LinkedList<Integer>();
        List<Integer> shelves2 = new LinkedList<Integer>();
        List<Integer> shelves3 = new LinkedList<Integer>();
        List<Integer> shelves4 = new LinkedList<Integer>();
        List<Integer> shelves5 = new LinkedList<Integer>();
        List<Integer> shelves6 = new LinkedList<Integer>();
        shelves1.add(8);
        shelves1.add(9);
        shelves2.add(2);
        shelves3.add(40);
        shelves3.add(39);
        shelves3.add(41);
        shelves4.add(9);
        shelves5.add(10);
        shelves6.add(40);
        shelves6.add(39);
        product0.addLocation(maxStoreCount+1, shelves1, shelves2, 80, 300);
        product0.addLocation(maxStoreCount+2, shelves3, shelves4, 65, 250);
        product1.addLocation(maxStoreCount+1, shelves5, shelves6, 40, 150);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(ProductTests.class);
    }

    @Test
    void addDelivery() {
        assertThrows(Exception.class, () -> product.addDelivery(0, 200));
        product.addLocation(maxStoreCount+3, Arrays.asList(1), Arrays.asList(2), 100, 200);
        assertDoesNotThrow(() -> product.addDelivery(maxStoreCount+3, 200));
    }

    @Test
    public void testAddItems() {
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        product0.addItems(maxStoreCount+1, 100, 20, "20 items were missing");
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(80, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        product0.addItems(maxStoreCount+1, 40,0,"");
        product0.addItems(maxStoreCount+2, 30,0,"");
        product1.addItems(maxStoreCount+1, 99,0,"");
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(120, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(30, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(99, product1.getInWarehouse(maxStoreCount+1));
    }
    @Test
    public void testMoveItems() {
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        product0.addItems(maxStoreCount+1, 100,0,"");
        product0.moveItems(maxStoreCount+1, 58);
        Assertions.assertEquals(58, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(42, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        product0.addItems(maxStoreCount+1, 40,0,"");
        product0.addItems(maxStoreCount+2, 30,0,"");
        product1.addItems(maxStoreCount+1, 99,0,"");
        product0.moveItems(maxStoreCount+1, 40);
        product0.moveItems(maxStoreCount+2, 25);
        product1.moveItems(maxStoreCount+1, 18);
        Assertions.assertEquals(98, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(42, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(25, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(5, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(18, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(81, product1.getInWarehouse(maxStoreCount+1));
    }
    @Test
    public void testRemoveItems() {
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        product0.addItems(maxStoreCount+1, 100,0,"");
        product0.moveItems(maxStoreCount+1, 58);
        product0.removeItems(maxStoreCount+1, 37, false);
        Assertions.assertEquals(21, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(42, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        product0.addItems(maxStoreCount+1, 40,0,"");
        product0.addItems(maxStoreCount+2, 30,0,"");
        product1.addItems(maxStoreCount+1, 99,0,"");
        product0.moveItems(maxStoreCount+1, 40);
        product0.moveItems(maxStoreCount+2, 25);
        product1.moveItems(maxStoreCount+1, 18);
        product0.removeItems(maxStoreCount+1, 15, false);
        product0.removeItems(maxStoreCount+2, 25, false);
        product1.removeItems(maxStoreCount+1, 0, false);
        Assertions.assertEquals(46, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(42, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(5, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(18, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(81, product1.getInWarehouse(maxStoreCount+1));
    }
    @Test
    public void testReturnItems() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate beforeTwoDays = LocalDate.now().minusDays(2);
        LocalDate beforeThreeDays = LocalDate.now().minusDays(3);
        LocalDate beforeFourDays = LocalDate.now().minusDays(4);
        LocalDate beforeFiveDays = LocalDate.now().minusDays(5);
        LocalDate beforeSixDays = LocalDate.now().minusDays(6);

        SaleToCustomer sale0 = new SaleToCustomer(0, beforeFourDays, beforeTwoDays, 30, new LinkedList<>(), new ArrayList<>());
        product0.addSale(sale0);
        List<Integer> categories = new ArrayList<>();
        categories.add(0);
        SaleToCustomer sale1 = new SaleToCustomer(0, beforeFiveDays, beforeFourDays, 50, new LinkedList<>(), new ArrayList<>());
        SaleToCustomer sale2 = new SaleToCustomer(0, beforeFourDays, beforeTwoDays, 40, categories, new ArrayList<>());
        SaleToCustomer sale3 = new SaleToCustomer(0, beforeTwoDays, yesterday, 80, new LinkedList<>(), new ArrayList<>());
        product0.addSale(sale1);
        category0.addSale(sale2);
        category0.addSale(sale3);

        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        double returnedValue = product0.returnItems(maxStoreCount+1, 100, beforeThreeDays);
        Assertions.assertEquals(100, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(0, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(270 ,returnedValue);
        returnedValue = product0.returnItems(maxStoreCount+1, 40, beforeFourDays);
        Assertions.assertEquals(90 ,returnedValue);
        returnedValue = product0.returnItems(maxStoreCount+2, 30, beforeTwoDays);
        Assertions.assertEquals(27 ,returnedValue);
        returnedValue = product1.returnItems(maxStoreCount+1, 99, beforeSixDays);
        Assertions.assertEquals(396 ,returnedValue);
        returnedValue = product1.returnItems(maxStoreCount+1, 5, beforeTwoDays);
        Assertions.assertEquals(4 ,returnedValue);
        returnedValue = product0.returnItems(maxStoreCount+1, 99, beforeSixDays);
        Assertions.assertEquals(445.5 ,returnedValue);
        Assertions.assertEquals(239, product0.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+1));
        Assertions.assertEquals(30, product0.getInStore(maxStoreCount+2));
        Assertions.assertEquals(0, product0.getInWarehouse(maxStoreCount+2));
        Assertions.assertEquals(104, product1.getInStore(maxStoreCount+1));
        Assertions.assertEquals(0, product1.getInWarehouse(maxStoreCount+1));
    }
}
