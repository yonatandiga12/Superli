package InventoryTests;

import Domain.Business.Objects.Inventory.Category;
import Domain.DAL.Abstract.DAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedList;

//@NotThreadSafe
public class CategoryTests {

    @BeforeAll
    public synchronized static void setData() {
        DAO.setDBForTests(CategoryTests.class);
    }

    @AfterAll
    public static void removeData() {
        DAO.deleteTestDB(CategoryTests.class);
    }

    @Test
    public void testChangeParentCategory(){
        Category category1 = new Category(1,"Test1", new HashSet<>(), new LinkedList<>(), null);
        Category category2 = new Category(2,"Test2", new HashSet<>(), new LinkedList<>(), category1);
        Category category3 = new Category(3,"Test3", new HashSet<>(), new LinkedList<>(), category1);
        Category category4 = new Category(4,"Test4", new HashSet<>(), new LinkedList<>(), category2);
        //1-2-4, 1-3.
        Assertions.assertFalse(category1.getSubcategories().contains(category1));
        Assertions.assertTrue(category1.getSubcategories().contains(category2));
        Assertions.assertTrue(category1.getSubcategories().contains(category3));
        Assertions.assertFalse(category1.getSubcategories().contains(category4));
        Assertions.assertFalse(category2.getSubcategories().contains(category1));
        Assertions.assertFalse(category2.getSubcategories().contains(category2));
        Assertions.assertFalse(category2.getSubcategories().contains(category3));
        Assertions.assertTrue(category2.getSubcategories().contains(category4));
        Assertions.assertFalse(category3.getSubcategories().contains(category1));
        Assertions.assertFalse(category3.getSubcategories().contains(category2));
        Assertions.assertFalse(category3.getSubcategories().contains(category3));
        Assertions.assertFalse(category3.getSubcategories().contains(category4));
        Assertions.assertFalse(category4.getSubcategories().contains(category1));
        Assertions.assertFalse(category4.getSubcategories().contains(category2));
        Assertions.assertFalse(category4.getSubcategories().contains(category3));
        Assertions.assertFalse(category4.getSubcategories().contains(category4));

        Assertions.assertTrue(category1.getParentCategory()==null);
        Assertions.assertTrue(category2.getParentCategory()==category1);
        Assertions.assertTrue(category3.getParentCategory()==category1);
        Assertions.assertTrue(category4.getParentCategory()==category2);

        category2.changeParentCategory(null);
        //1-3, 2-4.
        Assertions.assertFalse(category1.getSubcategories().contains(category1));
        Assertions.assertFalse(category1.getSubcategories().contains(category2));
        Assertions.assertTrue(category1.getSubcategories().contains(category3));
        Assertions.assertFalse(category1.getSubcategories().contains(category4));
        Assertions.assertFalse(category2.getSubcategories().contains(category1));
        Assertions.assertFalse(category2.getSubcategories().contains(category2));
        Assertions.assertFalse(category2.getSubcategories().contains(category3));
        Assertions.assertTrue(category2.getSubcategories().contains(category4));
        Assertions.assertFalse(category3.getSubcategories().contains(category1));
        Assertions.assertFalse(category3.getSubcategories().contains(category2));
        Assertions.assertFalse(category3.getSubcategories().contains(category3));
        Assertions.assertFalse(category3.getSubcategories().contains(category4));
        Assertions.assertFalse(category4.getSubcategories().contains(category1));
        Assertions.assertFalse(category4.getSubcategories().contains(category2));
        Assertions.assertFalse(category4.getSubcategories().contains(category3));
        Assertions.assertFalse(category4.getSubcategories().contains(category4));

        Assertions.assertTrue(category1.getParentCategory()==null);
        Assertions.assertTrue(category2.getParentCategory()==null);
        Assertions.assertTrue(category3.getParentCategory()==category1);
        Assertions.assertTrue(category4.getParentCategory()==category2);

        category4.changeParentCategory(category1);
        //1-3, 1-4, 2.
        Assertions.assertFalse(category1.getSubcategories().contains(category1));
        Assertions.assertFalse(category1.getSubcategories().contains(category2));
        Assertions.assertTrue(category1.getSubcategories().contains(category3));
        Assertions.assertTrue(category1.getSubcategories().contains(category4));
        Assertions.assertFalse(category2.getSubcategories().contains(category1));
        Assertions.assertFalse(category2.getSubcategories().contains(category2));
        Assertions.assertFalse(category2.getSubcategories().contains(category3));
        Assertions.assertFalse(category2.getSubcategories().contains(category4));
        Assertions.assertFalse(category3.getSubcategories().contains(category1));
        Assertions.assertFalse(category3.getSubcategories().contains(category2));
        Assertions.assertFalse(category3.getSubcategories().contains(category3));
        Assertions.assertFalse(category3.getSubcategories().contains(category4));
        Assertions.assertFalse(category4.getSubcategories().contains(category1));
        Assertions.assertFalse(category4.getSubcategories().contains(category2));
        Assertions.assertFalse(category4.getSubcategories().contains(category3));
        Assertions.assertFalse(category4.getSubcategories().contains(category4));

        Assertions.assertTrue(category1.getParentCategory()==null);
        Assertions.assertTrue(category2.getParentCategory()==null);
        Assertions.assertTrue(category3.getParentCategory()==category1);
        Assertions.assertTrue(category4.getParentCategory()==category1);
    }
}
