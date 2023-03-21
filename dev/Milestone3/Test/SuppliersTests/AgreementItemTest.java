package SuppliersTests;

import Domain.Business.Controllers.InventoryController;
import Domain.Business.Objects.Inventory.Category;
import Domain.Business.Objects.Inventory.Product;
import Domain.Business.Objects.Supplier.AgreementItem;
import Domain.DAL.Abstract.DAO;
import net.jcip.annotations.NotThreadSafe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@NotThreadSafe
public class AgreementItemTest {

    private AgreementItem item;
    static Category category0;
    static Product product;
    private static final InventoryController is =  InventoryController.getInventoryController();



    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(AgreementItemTest.class);
        category0 = is.addCategory("Test-Milk",  0);
        product = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(AgreementItemTest.class);
    }


    @BeforeEach
    public void setUp(){

        HashMap<Integer, Integer> map = new HashMap<>();
        map.put(20, 15); map.put(50, 20); map.put(100, 30);
        try {
            item = new AgreementItem(product.getId(), 1, "Osem", 8.99f,  map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_setters(){
        try{
            item.setProductId(444);
            //item.setName("Bissli");
            item.setManufacturer("Tnuva");
            item.setPrice(51.367f);
            HashMap<Integer, Integer> map = new HashMap<>();
            map.put(1000, 23);
            item.setBulkPrices(map);

            assertEquals(444, item.getProductId());
            //assertEquals("Bissli", item.getName());
            assertEquals("Tnuva", item.getManufacturer());
            assertEquals(51.367f, item.getPricePerUnit());
            assertEquals(map, item.getBulkPrices());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_addBulkPrice(){
        try{
            item.addBulkPrice(1000, 30);

            assertEquals(30, item.getBulkPrices().get(1000));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_removeBulkPrice(){
        try{
            item.removeBulkPrice(100);
            HashMap<Integer, Integer> map = new HashMap<>();
            map.put(20, 15); map.put(50, 20);

            assertEquals(map, item.getBulkPrices());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_editBulkPrice(){
        try{
            item.editBulkPrice(50, 25);
            HashMap<Integer, Integer> map = new HashMap<>();
            map.put(20, 15); map.put(50, 20); map.put(100, 35);

            assertEquals(25, item.getBulkPrices().get(50));
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_calculateTotalPrice_noBulk(){
        double price = item.calculateTotalPrice(10);

        assertTrue(10*8.99 - price < 0.00001);
    }

    @Test
    public void test_calculateTotalPrice_withBulk(){
        double price = item.calculateTotalPrice(30);

        assertTrue(30*8.99*0.85 - price < 0.0001);

        price = item.calculateTotalPrice(50);
        assertTrue(50*8.99*0.8 - price < 0.0001);

        price = item.calculateTotalPrice(99);
        assertTrue(99*8.99*0.8 - price < 0.0001);
    }

}
