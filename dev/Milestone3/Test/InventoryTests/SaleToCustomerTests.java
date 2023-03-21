package InventoryTests;

import Domain.Business.Objects.Inventory.SaleToCustomer;
import Domain.DAL.Abstract.DAO;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.LinkedList;

//@NotThreadSafe
public class SaleToCustomerTests {

    SaleToCustomer sale1;
    SaleToCustomer sale2;
    SaleToCustomer sale3;
    SaleToCustomer sale4;
    SaleToCustomer sale5;
    SaleToCustomer sale6;
    SaleToCustomer sale7;
    SaleToCustomer sale8;

    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(SaleToCustomerTests.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(SaleToCustomerTests.class);
    }

    @BeforeEach
    void createDatesAndSales() {
        LocalDate today = LocalDate.now();

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate beforeTwoDays = LocalDate.now().minusDays(2);

        LocalDate afterTwoDays = LocalDate.now().plusDays(2);

        sale1 = new SaleToCustomer(0, yesterday, tomorrow, 30, new LinkedList<>(), new LinkedList<>());
        sale2 = new SaleToCustomer(1, today, tomorrow, 30, new LinkedList<>(), new LinkedList<>());
        sale3 = new SaleToCustomer(2, yesterday, today, 30, new LinkedList<>(), new LinkedList<>());
        sale4 = new SaleToCustomer(3, today, today, 30, new LinkedList<>(), new LinkedList<>());
        sale5 = new SaleToCustomer(4, beforeTwoDays, yesterday, 30, new LinkedList<>(), new LinkedList<>());
        sale6 = new SaleToCustomer(5, tomorrow, afterTwoDays, 30, new LinkedList<>(), new LinkedList<>());
        sale7 = new SaleToCustomer(6, yesterday, yesterday, 30, new LinkedList<>(), new LinkedList<>());
        sale8 = new SaleToCustomer(7, tomorrow, tomorrow, 30, new LinkedList<>(), new LinkedList<>());
    }
    @Test
    public void testIsUpcoming() {
        try {
            Assertions.assertFalse(sale1.isUpcoming());
            Assertions.assertFalse(sale2.isUpcoming());
            Assertions.assertFalse(sale3.isUpcoming());
            Assertions.assertFalse(sale4.isUpcoming());
            Assertions.assertFalse(sale5.isUpcoming());
            Assertions.assertTrue(sale6.isUpcoming());
            Assertions.assertFalse(sale7.isUpcoming());
            Assertions.assertTrue(sale8.isUpcoming());
        } catch (Exception e) {
            Assertions.fail("isUpcoming isn't working");
        }
    }
    @Test
    public void testIsPassed() {
        Assertions.assertFalse(sale1.isPassed());
        Assertions.assertFalse(sale2.isPassed());
        Assertions.assertFalse(sale3.isPassed());
        Assertions.assertFalse(sale4.isPassed());
        Assertions.assertTrue(sale5.isPassed());
        Assertions.assertFalse(sale6.isPassed());
        Assertions.assertTrue(sale7.isPassed());
        Assertions.assertFalse(sale8.isPassed());
    }
    @Test
    public void testIsActive() {
        Assertions.assertTrue(sale1.isActive());
        Assertions.assertTrue(sale2.isActive());
        Assertions.assertTrue(sale3.isActive());
        Assertions.assertTrue(sale4.isActive());
        Assertions.assertFalse(sale5.isActive());
        Assertions.assertFalse(sale6.isActive());
        Assertions.assertFalse(sale7.isActive());
        Assertions.assertFalse(sale8.isActive());
    }
}
