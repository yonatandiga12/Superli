package SuppliersTests;

import Domain.Business.Controllers.InventoryController;
import Domain.Business.Objects.Inventory.Category;
import Domain.Business.Objects.Inventory.Product;
import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.Agreement.NotTransportingAgreement;
import Domain.Business.Objects.Supplier.AgreementItem;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.AgreementController;
import Domain.DAL.Controllers.InventoryAndSuppliers.StoreDAO;
import net.jcip.annotations.NotThreadSafe;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@NotThreadSafe
public class AgreementTest {

    private Agreement agreement;
    private HashMap<Integer, Integer> bulkPrices;
    private AgreementController dao;
    private int supId = 1001;
    static Category category0;
    static Product product1;
    static Product product2;
    static Product product3;
    static Product product4;
    static Product product5;
    static Product product6;
    private static final InventoryController is =  InventoryController.getInventoryController();



    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(AgreementTest.class);
        category0 = is.addCategory("Test-Milk",  0);
        product1 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product2 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product3 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product4 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product5 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");
        product6 = is.newProduct("Test-Milk-Tnuva-1L", category0.getID(), 1, 4.5, "18");

    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(AgreementTest.class);
    }


    @BeforeEach
    public void setUp(){
        agreement = new NotTransportingAgreement();
        bulkPrices = new HashMap<>();
        dao = new AgreementController();
        StoreDAO storeDAO = new StoreDAO();
        storeDAO.getAll();
    }

    private List<AgreementItem> makeItemList(){
        List<AgreementItem> list = new ArrayList<>();
        bulkPrices = new HashMap<>();
        bulkPrices.put(5, 20);
        try {
            list.add(new AgreementItem(product1.getId(),101,  "m1", 5.11f,  bulkPrices));
            list.add(new AgreementItem(product2.getId(),102,  "m2", 7.11f, bulkPrices));
            list.add(new AgreementItem(product3.getId(), 103, "m3", 12.876f,  bulkPrices));
            list.add(new AgreementItem(product4.getId(), 104, "m4", 184.2f,  bulkPrices));
            list.add(new AgreementItem(product5.getId(), 105, "m5", 1123f, bulkPrices));
            list.add(new AgreementItem(product6.getId(), 106, "m6", 687248.45621f, bulkPrices));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Test
    public void test_setItems_getItems(){
        List<AgreementItem> list = makeItemList();

        try{
            agreement.setItems(list);
            assertEquals(list.size(), agreement.getItems().size());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_setItemsFromString(){
        List<String> list = new ArrayList<>();
        List<AgreementItem> aiList = makeItemList();
        for(AgreementItem ai : aiList){
            list.add(ai.toString());
        }

        try{
            agreement.setItemsFromString(list, supId, dao);

            assertEquals(aiList.size(),agreement.getItems().size());
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_addItem(){

        try{
            AgreementItem item1 = new AgreementItem(product2.getId(), 117, "man1", 1565165f,  bulkPrices);
            AgreementItem item2 = new AgreementItem(product3.getId(), 118,"man2", 1565165f,  bulkPrices);
            AgreementItem item3 = new AgreementItem(product4.getId(), 119,"man3", 1565165f,  bulkPrices);

            List<AgreementItem> aiList = makeItemList();

            agreement.setItems(aiList);

            agreement.addItem(item1);

            aiList = makeItemList();
            aiList.add(item1);


            assertEquals(item1, agreement.getItem(item1.getProductId()));

            List<AgreementItem> fromAgreement = agreement.getItems();
            fromAgreement = fromAgreement.stream().sorted(new Comparator<AgreementItem>() {
                @Override
                public int compare(AgreementItem o1, AgreementItem o2) {
                    return o1.getProductId()-o2.getProductId();
                }
            }).collect(Collectors.toList());

            AgreementItem t1;
            AgreementItem t2;

            for(int i=0; i<fromAgreement.size(); i++){
                t1 = aiList.get(i);
                t2 = fromAgreement.get(i);
                assertEquals(t1.toString(), t2.toString());
            }


            agreement.addItem(item2);
            agreement.addItem(item3);

            aiList = makeItemList();
            aiList.add(item1);
            aiList.add(item2);
            aiList.add(item3);


            fromAgreement = agreement.getItems();
            fromAgreement = fromAgreement.stream().sorted(new Comparator<AgreementItem>() {
                @Override
                public int compare(AgreementItem o1, AgreementItem o2) {
                    return o1.getProductId()-o2.getProductId();
                }
            }).collect(Collectors.toList());

            assertEquals(item1, agreement.getItem(item1.getProductId()));
            assertEquals(item2, agreement.getItem(item2.getProductId()));
            assertEquals(item3, agreement.getItem(item3.getProductId()));

            for(int i=0; i<fromAgreement.size(); i++){
                t1 = aiList.get(i);
                t2 = fromAgreement.get(i);
                assertEquals(t1.toString(), t2.toString());
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_removeItem(){
        List<AgreementItem> aiList = makeItemList();

        try{
            agreement.setItems(aiList);

            assertEquals(aiList.size(), agreement.getItems().size());
            agreement.removeItem(product1.getId());
            aiList = makeItemList();
            assertEquals(aiList.size() - 1, agreement.getItems().size());


            agreement.removeItem(product1.getId());
            aiList = makeItemList();
            aiList.remove(0);
            aiList.remove(3);

            List<AgreementItem> fromAgreement = agreement.getItems();
            fromAgreement = fromAgreement.stream().sorted(new Comparator<AgreementItem>() {
                @Override
                public int compare(AgreementItem o1, AgreementItem o2) {
                    return o1.getProductId()-o2.getProductId();
                }
            }).collect(Collectors.toList());

            AgreementItem t1;
            AgreementItem t2;

            for(int i=0; i<fromAgreement.size(); i++){
                t1 = aiList.get(i);
                t2 = fromAgreement.get(i);
                assertEquals(t1.toString(), t2.toString());
            }


            assertEquals(aiList.size(), agreement.getItems().size());

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_addAndRemove(){

        try{
            AgreementItem item1 = new AgreementItem(product1.getId(), 117,  "man1", 1565165,  bulkPrices);
            AgreementItem item2 = new AgreementItem(product2.getId(), 118,  "man2", 1565165,  bulkPrices);
            AgreementItem item3 = new AgreementItem(product4.getId(), 119,"man3", 1565165,  bulkPrices);

            List<AgreementItem> aiList = makeItemList();

            agreement.setItems(aiList);

            aiList = makeItemList();

            agreement.addItem(item3);
            aiList.add(item3);
            assertEquals(item3, agreement.getItem(item3.getProductId()));
            assertEquals(aiList.size(), agreement.getItems().size());

            agreement.removeItem(product1.getId());
            aiList.remove(3);
            assertEquals(aiList.size(), agreement.getItems().size());

            agreement.removeItem(product1.getId());
            aiList.remove(1);
            assertEquals(aiList.size(), agreement.getItems().size());

            agreement.addItem(item1);
            aiList.add(item1);
            assertEquals(aiList.size(), agreement.getItems().size());
            assertEquals(item1, agreement.getItem(item1.getProductId()));

            agreement.addItem(item2);
            aiList.add(item2);
            assertEquals(aiList.size(), agreement.getItems().size());
            assertEquals(item2, agreement.getItem(item2.getProductId()));

            agreement.removeItem(product1.getId());
            aiList.remove(1);
            assertEquals(aiList.size() , agreement.getItems().size());

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void test_itemExists(){
        try{
            agreement.setItems(makeItemList());

            assertFalse(agreement.itemExists(10005));
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }



}
