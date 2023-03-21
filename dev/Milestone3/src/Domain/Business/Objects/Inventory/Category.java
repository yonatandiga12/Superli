package Domain.Business.Objects.Inventory;

import Domain.DAL.Controllers.InventoryAndSuppliers.CategoryDataMapper;
import Domain.DAL.Controllers.InventoryAndSuppliers.ProductDataMapper;
import Domain.DAL.Controllers.InventoryAndSuppliers.SalesDataMapper;

import java.time.LocalDate;
import java.util.*;

public class Category {
    private int ID;
    private String name;
    private Set<Category> subcategories;
    private Category parentCategory;
    private List<Product> products;
    private List<SaleToCustomer> sales;
    public final static CategoryDataMapper CATEGORY_DATA_MAPPER = new CategoryDataMapper();
    private final static SalesDataMapper SALE_DATA_MAPPER = SaleToCustomer.SALES_DATA_MAPPER;
    private final static ProductDataMapper PRODUCT_DATA_MAPPER = Product.PRODUCT_DATA_MAPPER;

    public int getID() { return ID; }
    public String getName() { return name; }
    public Set<Category> getSubcategories() {
        return subcategories;
    }
    public Category getParentCategory() {
        return parentCategory;
    }

    public Category(int ID, String name, Set<Category> subcategories, List<Product> products, Category parentCategory) {
        this.ID = ID;
        this.name = name;
        this.subcategories = subcategories;
        for (Category c : subcategories)
            c.changeParentCategory(this);
        this.parentCategory = parentCategory;
        if (parentCategory!=null)
            parentCategory.addSubcategory(this);
        sales = new ArrayList<>();
        this.products = products;
    }

    public void setName(String name) {
        this.name = name;
        CATEGORY_DATA_MAPPER.updateName(getID(), name);
    }
    public void changeParentCategory(Category newParentCategory) {
        if (parentCategory!=null)
            parentCategory.removeSubcategory(this);
        parentCategory = newParentCategory;
        if (parentCategory!= null)
            parentCategory.addSubcategory(this);
        CATEGORY_DATA_MAPPER.updateParentCategory(getID(),newParentCategory==null ? null : newParentCategory.getID());
    }
    public List<Product> getAllProductsInCategory() {
//         output = new ArrayList<>();
        //if (products.isEmpty()) {
        List<Product> output = PRODUCT_DATA_MAPPER.getProductsFromCategory(ID);
        //}
        //else
//        output.addAll(products);
        for (Category c : subcategories) {
            output.addAll(c.getAllProductsInCategory());
        }
        return output;
    }

    private boolean removeSubcategory(Category category) { return subcategories.remove(category); }
    private boolean addSubcategory(Category category) {
        return subcategories.add(category);
    }
    public boolean removeProduct(Product product) {
        return products.remove(product);
    }
    public boolean addProduct(Product product) { return products.add(product); }
    public boolean removeSale(SaleToCustomer sale) {
        return sales.remove(sale);
    }
    public boolean addSale(SaleToCustomer sale) {
        return sales.add(sale);
    }

    public SaleToCustomer findCurrentBestSale(SaleToCustomer currentSale) {
        for (SaleToCustomer sale: sales)
            if ((sale.isActive() && currentSale==null) || (sale.isActive() && currentSale.getPercent()<sale.getPercent()))
                currentSale = sale;
        if (parentCategory!=null)
            currentSale = parentCategory.findCurrentBestSale(currentSale);
        return currentSale;
    }
    public Set<SaleToCustomer> getSaleHistory() {
        Set<SaleToCustomer> result;
        if (parentCategory==null)
             result = new HashSet<>();
        else
            result = parentCategory.getSaleHistory();
        for (SaleToCustomer sale : SALE_DATA_MAPPER.getSalesByCategory(ID)) {
            if (sale.isPassed() || sale.isActive())
                result.add(sale);
        }
        return result;
    }
    public String getParentCategoryName() {
        if (parentCategory==null)
            return "";
        return parentCategory.getName();
    }
    public List<SaleToCustomer> getSalesOnDate(LocalDate date) {
        List<SaleToCustomer> result;
        if (parentCategory==null)
            result = new ArrayList<>();
        else
            result = parentCategory.getSalesOnDate(date);
        for (SaleToCustomer sale : sales) {
            if (sale.wasActive(date))
                result.add(sale);
        }
        return result;
    }

    public Collection<DefectiveItems> getExpiredItemReports(LocalDate start, LocalDate end) {
        //products.addAll(PRODUCT_DATA_MAPPER.getProductsFromCategory(ID));
        List<DefectiveItems> eirList = new ArrayList<>();
        for (Product p : products) {
            eirList.addAll(p.getExpiredItemReports(start, end));
        }
        for (Category c: subcategories) {
            eirList.addAll(c.getExpiredItemReports(start, end));
        }
        return eirList;
    }

    public Collection<DefectiveItems> getDamagedItemReports(LocalDate start, LocalDate end) {
        //products.addAll(PRODUCT_DATA_MAPPER.getProductsFromCategory(ID));
        List<DefectiveItems> dirList = new ArrayList<>();
        for (Product p : products) {
            dirList.addAll(p.getDamagedItemReports(start, end));
        }
        for (Category c: subcategories) {
            dirList.addAll(c.getDamagedItemReports(start, end));
        }
        return dirList;
    }

    public boolean belongsToCategory(Category category) {
        return (parentCategory!=null && (parentCategory==category || parentCategory.belongsToCategory(category)));
    }
}
