package Domain.Service.Objects.InventoryObjects;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final int ID;
    private final String name;
    private final String parentCategory;
    private final List<Category> subCategories;
    private final int numOfProducts;
    public Category(Domain.Business.Objects.Inventory.Category c) {
        this.ID = c.getID();
        this.name = c.getName();
        this.parentCategory = c.getParentCategoryName();
        List<Category> subCats = new ArrayList<>();
        for (Domain.Business.Objects.Inventory.Category ca : c.getSubcategories())
            subCats.add(new Category(ca));
        this.subCategories = subCats;
        this.numOfProducts=c.getAllProductsInCategory().size();
    }
    public int getID() { return ID; }

    public String getName() {
        return name;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public int getNumOfProducts() {
        return numOfProducts;
    }

    @Override
    public String toString() {
        return "Category{" +
                "ID=" + ID + '\'' +
                ", name='" + name + '\'' +
                ", numOfProducts=" + numOfProducts +
                ", parentCategory='" + parentCategory + '\'' +
                ", subCategories=" + subCategories +
                '}';
    }
}
