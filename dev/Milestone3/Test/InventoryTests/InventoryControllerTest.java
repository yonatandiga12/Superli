package InventoryTests;

import Domain.Business.Controllers.InventoryController;
import Domain.Business.Controllers.SupplierController;
import Domain.Business.Objects.Inventory.Product;
import Domain.DAL.Abstract.DAO;
import Globals.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.max;
import static org.junit.jupiter.api.Assertions.*;

//@NotThreadSafe
class InventoryControllerTest {
    private static final InventoryController is = InventoryController.getInventoryController();
    private static SupplierController sc;
    private static int maxStoreCount;
    private static List<Integer> stores;

    @BeforeAll
    public synchronized static void getMaxStoreCount() {
        DAO.setDBForTests(InventoryControllerTest.class);
        stores = new ArrayList<>();
        sc = new SupplierController();
        sc.setInventoryController(is);
        sc.loadSuppliersData();
        maxStoreCount = max(is.getStoreIDs());
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(InventoryControllerTest.class);
    }

    //integration test
    @Test
    void orderArrived() throws Exception {
        //setup
        int cat = is.addCategory("TestCategory", 0).getID();
        Product prod1 = is.newProduct("TestProduct", cat,0,2,"testManu");
        Product prod2 = is.newProduct("TestProduct", cat,0,2,"testManu");
        int supplier = sc.addSupplier("Test-OrderArrived", 2, "address", "Agreement", new ArrayList<>(), new ArrayList<>());
        sc.addAgreement(supplier,1, "1 2 3 4 5 6 7");
        //    public void addItemToAgreement(int supplierId, int itemId, int idBySupplier, String itemManu, float itemPrice, Map<Integer, Integer> bulkPrices) throws Exception {
        sc.addItemToAgreement(supplier, prod1.getId(), 1, "",  3, new HashMap<>());
        sc.addItemToAgreement(supplier, prod2.getId(), 2, "",  3,  new HashMap<>());
        int store = is.addStore();
        stores.add(store);
        is.addProductToStore(store,Arrays.asList(1),Arrays.asList(1),prod1.getId(),100,200);
        is.addProductToStore(store,Arrays.asList(1),Arrays.asList(1),prod2.getId(),100,200);
        int order = sc.addNewOrder(supplier, store);
        //check for error
        Map<Integer, Map<Integer, Pair<Pair<Integer, Integer>, String>>> reports = new HashMap<>();
        assertThrows(Exception.class, ()->is.transportArrived(0,reports));
        //check preconditions
        assertEquals(0,prod1.getTotalInStore(store));
        assertEquals(0,prod2.getTotalInStore(store));
        sc.addItemToOrder(supplier,order, prod1.getId(), 200);
        prod1.getStockReport(store).setInDelivery(200);
        sc.addItemToOrder(supplier,order, prod2.getId(), 200);
        prod2.getStockReport(store).setInDelivery(200);
        //check post conditions
//        reportOfOrder.put(prod1.getId(), new Pair<>(new Pair<>(0,2),"2 items were defective"));
//        assertDoesNotThrow(()->is.transportArrived(order, reportOfOrder));
//        assertEquals(198,prod1.getTotalInStore(store));
//        assertEquals(200,prod2.getTotalInStore(store));

    }

//    Order arrivedOrder = supplierController.orderHasArrived(orderID, supplierID);
//    int orderStoreID = arrivedOrder.getStoreID();
//        for (OrderItem orderItem : arrivedOrder.getOrderItems()) {
//        getProduct(orderItem.getProductId()).addItems(orderStoreID, orderItem.getQuantity());
//    }

    @org.junit.jupiter.api.Test
    void addStore() {
        //empty
        int currStore = max(is.getStoreIDs());
        List<Integer> currStores = new ArrayList<>(is.getStoreIDs());
        assertEquals(currStore+1, is.addStore());
        Integer[] actual = is.getStoreIDs().toArray(new Integer[0]);
        currStores.add(currStore+1);
        stores.add(currStore+1);
        Integer[] expected = currStores.toArray(new Integer[0]);
        assertArrayEquals(actual, expected);
        assertThrows(IllegalArgumentException.class, ()->is.getAmountInStore(currStore+1, 1));

        assertEquals(currStore+2, is.addStore());
        actual = is.getStoreIDs().toArray(new Integer[0]);
        currStores.add(currStore+2);
        stores.add(currStore+2);
        expected = currStores.toArray(new Integer[0]);
        assertArrayEquals(actual, expected);
        assertThrows(IllegalArgumentException.class, ()->is.getAmountInStore(currStore+2, 2));

        is.removeStore(currStore+2);
        assertEquals(currStore+3, is.addStore());
        actual = is.getStoreIDs().toArray(new Integer[0]);
        currStores.remove(Integer.valueOf(currStore+2));
        currStores.add(currStore+3);
        stores.add(currStore+3);
        expected = currStores.toArray(new Integer[0]);
        assertArrayEquals(actual, expected);
        assertThrows(IllegalArgumentException.class, ()->is.getAmountInStore(currStore+2, 1));
        //need to add a cleanup method
    }

    @org.junit.jupiter.api.Test
    void removeStore() {
        //first
        Integer[] start = new ArrayList<Integer>(is.getStoreIDs()).toArray(new Integer[0]);
        int store = is.addStore();
        stores.add(store);
        int cat = is.addCategory("TestCategory", 0).getID();
        int prod = is.newProduct("TestProduct", cat,2,2,"testManu").getId();
        is.addProductToStore(store, Arrays.asList(1), Arrays.asList(1), prod, 5,5);
        assertDoesNotThrow(()->is.getAmountInStore(store, prod));

        is.removeStore(store);
        Integer[] actual = is.getStoreIDs().toArray(new Integer[0]);
        assertArrayEquals(actual, start);
        assertThrows(IllegalArgumentException.class, ()->is.getAmountInStore(store, prod));
        assertThrows(IllegalArgumentException.class, () -> is.removeStore(store));
    }

    @org.junit.jupiter.api.Test
    void getExpiredItemReportsByProductIllegalEntries() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate afterTwoDays = LocalDate.now().plusDays(2);
        List<Integer> pIDs = new ArrayList<>();
        int cat = is.addCategory("TestCategory", 0).getID();
        pIDs.add(is.newProduct("TestProduct", cat,2,2,"testManu").getId());
        //empty
        assertIterableEquals(new ArrayList<>(), is.getExpiredItemReportsByProduct(yesterday, today, pIDs));
        //illegal - future
        assertThrows(IllegalArgumentException.class, () -> is.getExpiredItemReportsByProduct(tomorrow, afterTwoDays, pIDs));
        //illegal - start>end
        assertThrows(IllegalArgumentException.class, () -> is.getExpiredItemReportsByProduct(today, yesterday, pIDs));
    }

    @Test
    void getAmountsForMinOrdersTest() {
        int store1 = is.addStore();
        int store2 = is.addStore();
        stores.add(store1); stores.add(store2);
        int cat = is.addCategory("TestCategory", 0).getID();
        Product prod1 = is.newProduct("TestProduct", cat,2,2,"testManu");
        Product prod2 = is.newProduct("TestProduct", cat,2,2,"testManu");
        assertEquals(new HashMap<>(), is.getAmountsForMinOrders(prod1));
        assertEquals(new HashMap<>(), is.getAmountsForMinOrders(prod2));

        is.addProductToStore(store1,Arrays.asList(1), Arrays.asList(1),prod1.getId(), 100, 200);
        Map<Integer, Integer> singletonMap = new HashMap<>(Collections.singletonMap(store1, 200));
        assertEquals(singletonMap,is.getAmountsForMinOrders(prod1));
        is.addProductToStore(store2,Arrays.asList(1), Arrays.asList(1),prod1.getId(), 200, 300);
        singletonMap.put(store2, 300);
        assertEquals(singletonMap,is.getAmountsForMinOrders(prod1));

        prod1.addDelivery(store1, 100);
        singletonMap.remove(store1);
        assertEquals(singletonMap,is.getAmountsForMinOrders(prod1));
        prod1.addDelivery(store2, 100);
        singletonMap.put(store2, 200);
        assertEquals(singletonMap,is.getAmountsForMinOrders(prod1));
        prod1.addDelivery(store2, 100);
        singletonMap.remove(store2);
        assertEquals(singletonMap,is.getAmountsForMinOrders(prod1));
    }

}