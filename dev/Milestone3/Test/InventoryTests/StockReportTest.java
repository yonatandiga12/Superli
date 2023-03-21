package InventoryTests;

import Domain.Business.Controllers.InventoryController;
import Domain.Business.Objects.Inventory.Product;
import Domain.Business.Objects.Inventory.StockReport;
import Domain.DAL.Abstract.DAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//@NotThreadSafe
class StockReportTest {
    Product product;
    private static final InventoryController is  =  InventoryController.getInventoryController();
    private static int maxStoreCount;
    int store;
    static int cat;
    StockReport stockReport;
    private static List<Integer> stores;

    @BeforeAll
    public synchronized static void setup() {
        DAO.setDBForTests(StockReportTest.class);
        stores=new ArrayList<>();
        maxStoreCount = 0; //max(is.getStoreIDs());
        cat = is.addCategory("TestCategory", 0).getID();
    }

    @BeforeEach
    public void refresh() {
        store = is.addStore();
        stores.add(store);
        product = is.newProduct("TestProduct", cat,2,2,"testManu");
        product.addLocation(store, Arrays.asList(1), Arrays.asList(2), 100, 200);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(StockReportTest.class);
    }

    @Test
    void addDelivery() {
        stockReport = product.getStockReport(store);
        assertThrows(Exception.class, ()->product.addDelivery(store+1,200));
        assertDoesNotThrow(()->product.addDelivery(store,200));
        stockReport = product.getStockReport(store);
        assertEquals(200, stockReport.getAmountInDeliveries());
        assertEquals(0,stockReport.getAmountInStore());
        assertEquals(0,stockReport.getAmountInWarehouse());
        product.addDelivery(store,200);
        stockReport = product.getStockReport(store);
        assertEquals(400, stockReport.getAmountInDeliveries());
        assertEquals(0,stockReport.getAmountInStore());
        assertEquals(0,stockReport.getAmountInWarehouse());
    }

    @Test
    void changeMinAndIsLow() {
        assertThrows(Exception.class, ()->product.changeProductMin(store+1,200));
        stockReport = product.getStockReport(store);
        assertTrue(stockReport.isLow());
        assertEquals(100, stockReport.getMinAmountInStore());
        assertDoesNotThrow(()->product.changeProductMin(store,150));
        stockReport = product.getStockReport(store);
        assertEquals(150, stockReport.getMinAmountInStore());
        assertTrue(stockReport.isLow());
        stockReport.returnItems(100);
        assertTrue(stockReport.isLow());
        stockReport.changeMin(50);
        assertFalse(stockReport.isLow());
        stockReport.changeMin(100);
        assertFalse(stockReport.isLow());
    }
}