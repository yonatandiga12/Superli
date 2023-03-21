package Domain.Service.Services;


import Domain.Business.Controllers.InventoryController;
import Domain.Business.Controllers.SupplierController;
import Domain.Business.Objects.Inventory.DefectiveItems;
import Domain.Business.Objects.Inventory.SaleToCustomer;
import Domain.Business.Objects.Supplier.Order;
import Domain.Business.Objects.Supplier.OrderItem;
import Domain.Service.Objects.InventoryObjects.*;
import Domain.Service.Objects.SupplierObjects.ServiceOrderItemObject;
import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Domain.Service.util.Result;
import Globals.Pair;

import java.time.LocalDate;
import java.util.*;

/**
 * Service controller for Inventory management operations
 *
 * All InventoryService's methods return Results detailing the success. encapsulating values/error messages
 */
public class InventoryService {

    private final InventoryController controller;

    public InventoryService(){
        controller = new InventoryController();
    }


    /**
     * gets store ids of existing stores
     *
     * @return Result detailing success of operation, holding list of store ids
     */
    public Result<Collection<Integer>> getStoreIDs(){
        try {
            return Result.makeOk(controller.getStoreIDs());
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * gets List of sales which are current or upcoming
     *
     * @return Result detailing success of operation, holding list of sales
     */
    public Result<List<Sale>> getRemovableSales(){
        try {
            List<SaleToCustomer> removableSales = controller.getRemovableSales();
            List<Sale> result = new ArrayList<>();
            for (SaleToCustomer sale: removableSales) {
                result.add(new Sale(sale));
            }
            return Result.makeOk(result);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Loads data from persistence layer
     *
     * @return Result detailing success of operation
     */
    public Result<Object> loadData(){
        try {
            controller.loadData();
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    /**
     * Adds new product to catalog
     *
     * @return Result detailing success of operation, containing the new Product
     */
    public Result<Product> newProduct(String name, int categoryID, double weight, double price, String manufacturer){
        try {
            Domain.Business.Objects.Inventory.Product p = controller.newProduct(name, categoryID, weight, price, manufacturer);
            return Result.makeOk(new Product(p));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Removes product from catalog
     *
     * @return Result detailing success of operation
     */
    public Result<Boolean> deleteProduct(int id){
        try {
            return Result.makeOk(controller.deleteProduct(id));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * add sale on specified categories and/or items
     *
     * @return Result detailing success of operation, containing the new sale
     */
    public Result<Sale> addSale(List<Integer> categories, List<Integer> products, int percent, LocalDate start, LocalDate end){
        try {
            return Result.makeOk(new Sale(controller.addSale(categories, products, percent, start, end)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * delete an upcoming or current sale
     *
     * @return Result detailing success of operation
     */
    public Result<Object> removeSale(int saleID){
        try {
            controller.removeSale(saleID);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    /**
     * add product to the items the specified store sells
     *
     * @return Result detailing success of operation, containing the specified product
     */
    public Result<Product> addProductToStore(int storeID, List<Integer> shelvesInStore, List<Integer> shelvesInWarehouse, int productID, int minAmount, int targetAmount){
        try {
            Domain.Business.Objects.Inventory.Product p = controller.addProductToStore(storeID, shelvesInStore, shelvesInWarehouse, productID, minAmount, targetAmount);
            return Result.makeOk(new Product(p));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * remove product from items that the specified store sells
     *
     * @return Result detailing success of operation, containing the specified product
     */
    public Result<Product> removeProductFromStore(int storeID, int productID){
        try {
            Domain.Business.Objects.Inventory.Product p = controller.removeProductFromStore(storeID, productID);
            return Result.makeOk(new Product(p));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * add purchase from supplier on specified product to a store
     *
     * @return Result detailing success of operation, containing the info on the purchase
     */
    public Result<Object> transportArrived(int transportID, Map<Integer, Map<Integer, Pair<Pair<Integer, Integer>, String>>> reportOfOrder){
        try {
            controller.transportArrived(transportID, reportOfOrder);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    /**
     * Get History of all sales the store had on a specific product
     *
     * @return Result detailing success of operation, containing the list of the sales
     */
    public Result<Set<Sale>> getSaleHistoryByProduct(int productId){
        try {
            Set<SaleToCustomer> saleToCustomerList = controller.getSaleHistoryByProduct(productId);
            Set<Sale> sales = new HashSet<>();
            for (SaleToCustomer sale : saleToCustomerList) {
                sales.add(new Sale(sale));
            }
            return Result.makeOk(sales);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get History of all sales the store had on a specific category
     *
     * @return Result detailing success of operation, containing the list of the sales
     */
    public Result<Set<Sale>> getSaleHistoryByCategory(int categoryID){
        try {
            Set<SaleToCustomer> saleToCustomerList = controller.getSaleHistoryByCategory(categoryID);
            Set<Sale> saleList = new HashSet<>();
            for (SaleToCustomer s : saleToCustomerList) {
                saleList.add(new Sale(s));
            }
            return Result.makeOk(saleList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get History of reported damaged and expired items, in the time range specified, in specified stores
     *
     * @return Result detailing success of operation, containing the list of the reports
     */
    public Result<List<DefectiveItemReport>> getDefectiveItemsByStore(LocalDate start, LocalDate end, List<Integer> storeIDs){
        try {
            List<DefectiveItems> dirs = controller.getDefectiveItemsByStore(start, end, storeIDs);
            List<DefectiveItemReport> SLdirs = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems dir : dirs)
                SLdirs.add(new DefectiveItemReport(dir));
            return Result.makeOk(SLdirs);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get History of reported damaged and expired items, in the time range specified, in specified categories
     *
     * @return Result detailing success of operation, containing the list of the reports
     */
    public Result<List<DefectiveItemReport>> getDefectiveItemsByCategory(LocalDate start, LocalDate end, List<Integer> categoryIDs){
        try {
            List<DefectiveItems> dirs = controller.getDefectiveItemsByCategory(start, end, categoryIDs);
            List<DefectiveItemReport> SLdirs = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems dir : dirs)
                SLdirs.add(new DefectiveItemReport(dir));
            return Result.makeOk(SLdirs);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get History of reported damaged and expired items, in the time range specified, of specified products
     *
     * @return Result detailing success of operation, containing the list of the reports
     */
    public Result<List<DefectiveItemReport>> getDefectiveItemsByProduct(LocalDate start, LocalDate end, List<Integer> productIDs){
        try {
            List<DefectiveItems> dirs = controller.getDefectiveItemsByProduct(start, end, productIDs);
            List<DefectiveItemReport> SLdirs = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems dir : dirs)
                SLdirs.add(new DefectiveItemReport(dir));
            return Result.makeOk(SLdirs);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get list of all products in catalog
     *
     * @return Result detailing success of operation, containing list of the products
     */
    public Result<List<Product>> getProducts(){
        try {
            Collection<Domain.Business.Objects.Inventory.Product> products = controller.getProducts();
            List<Product> productList = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.Product p : products) {
                productList.add(new Product(p));
            }
            return Result.makeOk(productList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get list of all products in specified categories
     *
     * @return Result detailing success of operation, containing list of the productsv
     */
    public Result<List<Product>> getProductsFromCategory(List<Integer> categoryIDs){
        try {
            Collection<Domain.Business.Objects.Inventory.Product> products = controller.getProductsFromCategory(categoryIDs);
            List<Product> productList = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.Product p : products) {
                productList.add(new Product(p));
            }
            return Result.makeOk(productList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Get list of all Categories in the store
     *
     * @return Result detailing success of operation, containing list of the categories
     */
    public Result<List<Category>> getCategories(){
        try {
            Collection<Domain.Business.Objects.Inventory.Category> categories = controller.getCategories();
            List<Category> categoryList = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.Category c : categories) {
                categoryList.add(new Category(c));
            }
            return Result.makeOk(categoryList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * buy items from specified store
     *
     * @return Result detailing success of operation, containing price of the products
     */
    public Result<Pair<Double, String>> buyItems(int storeID, int productID, int amount){
        try {
            return Result.makeOk(controller.buyItems(storeID, productID, amount));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Stock report of all items under the defined minimum amount per store
     *
     * @return Result detailing success of operation, with list of stockreports
     */
    public Result<List<StockReport>> getMinStockReport(){
        try {
            List<Domain.Business.Objects.Inventory.StockReport> minStock = controller.getMinStockReport();
            List<StockReport> stockReports = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.StockReport s : minStock)
                stockReports.add(new StockReport(s));
            return Result.makeOk(stockReports);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Stock report of all items under the defined minimum amount in specified storeIDs and categories
     *
     * @return Result detailing success of operation, with list of stockreports
     */
    public Result<List<StockReport>> storeStockReport(Collection<Integer> storeIDs, List<Integer> categories){
        try {
            List<Domain.Business.Objects.Inventory.StockReport> stock = controller.getStockReport(storeIDs, categories);
            List<StockReport> stockReports = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.StockReport s : stock)
                stockReports.add(new StockReport(s));
            return Result.makeOk(stockReports);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query as to whether specified item is under defined minimum in store
     *
     * @return Result detailing success of operation, with boolean true if the amount in the store is under the defined minimum
     */
    public Result<Boolean> isUnderMin(int store, int product){
        try {
            return Result.makeOk(controller.isUnderMin(store, product));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Report and remove damage items from the store
     *
     * @return Result detailing success of operation, containing the report details
     */
    public Result<Pair<DefectiveItemReport, String>> reportDamaged(int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse){
        try {
            Pair<DefectiveItems, String> result = controller.reportDamaged(storeID, productID, amount, employeeID, description, inWarehouse);
            return Result.makeOk(new Pair<>(new DefectiveItemReport(result.getLeft()), result.getRight()));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Report and remove expired items from the store
     *
     * @return Result detailing success of operation, containing the report details
     */
    public Result<Pair<DefectiveItemReport, String>> reportExpired(int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse){
        try {
            Pair<DefectiveItems, String> result = controller.reportExpired(storeID, productID, amount, employeeID, description, inWarehouse);
            return Result.makeOk(new Pair<>(new DefectiveItemReport(result.getLeft()), result.getRight()));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query of all damaged items from specified stores in the date range
     *
     * @return Result detailing success of operation, containing a list of the reports
     */
    public Result<List<DefectiveItemReport>> getDamagedItemsReportByStore(LocalDate start, LocalDate end, List<Integer> storeIDs){
        try {
            List<Domain.Business.Objects.Inventory.DefectiveItems> dirs = controller.getDamagedItemReportsByStore(start, end, storeIDs);
            List<DefectiveItemReport> SLdirs = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems dir : dirs)
                SLdirs.add(new DefectiveItemReport(dir));
            return Result.makeOk(SLdirs);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query of all damaged items from specified categories in the date range
     *
     * @return Result detailing success of operation, containing a list of the reports
     */
    public Result<List<DefectiveItemReport>> getDamagedItemsReportByCategory(LocalDate start, LocalDate end, List<Integer> categoryIDs){
        try {
            List<Domain.Business.Objects.Inventory.DefectiveItems> dirs = controller.getDamagedItemReportsByCategory(start, end, categoryIDs);
            List<DefectiveItemReport> SLdirs = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems dir : dirs)
                SLdirs.add(new DefectiveItemReport(dir));
            return Result.makeOk(SLdirs);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query of all damaged items of specified products in the date range
     *
     * @return Result detailing success of operation, containing a list of the reports
     */
    public Result<List<DefectiveItemReport>> getDamagedItemsReportByProduct(LocalDate start, LocalDate end, List<Integer> productIDs){
        try {
            List<Domain.Business.Objects.Inventory.DefectiveItems> dirs = controller.getDamagedItemReportsByProduct(start, end, productIDs);
            List<DefectiveItemReport> SLdirs = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems dir : dirs)
                SLdirs.add(new DefectiveItemReport(dir));
            return Result.makeOk(SLdirs);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query of all expired items from specified stores in the date range
     *
     * @return Result detailing success of operation, containing a list of the reports
     */
    public Result<List<DefectiveItemReport>> getExpiredItemReportsByStore(LocalDate start, LocalDate end, List<Integer> storeIDs){
        try {
            List<Domain.Business.Objects.Inventory.DefectiveItems> expiredItemReports = controller.getExpiredItemReportsByStore(start, end, storeIDs);
            List<DefectiveItemReport> expiredItemReportList = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems eir : expiredItemReports)
                expiredItemReportList.add(new DefectiveItemReport(eir));
            return Result.makeOk(expiredItemReportList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query of all expired items from specified categories in the date range
     *
     * @return Result detailing success of operation, containing a list of the reports
     */
    public Result<List<DefectiveItemReport>> getExpiredItemReportsByCategory(LocalDate start, LocalDate end, List<Integer> categoryIDs){
        try {
            List<Domain.Business.Objects.Inventory.DefectiveItems> expiredItemReports = controller.getExpiredItemReportsByCategory(start, end, categoryIDs);
            List<DefectiveItemReport> expiredItemReportList = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems eir : expiredItemReports)
                expiredItemReportList.add(new DefectiveItemReport(eir));
            return Result.makeOk(expiredItemReportList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Query of all expired items of specified products in the date range
     *
     * @return Result detailing success of operation, containing a list of the reports
     */
    public Result<List<DefectiveItemReport>> getExpiredItemReportsByProduct(LocalDate start, LocalDate end, List<Integer> productIDs){
        try {
            List<Domain.Business.Objects.Inventory.DefectiveItems> expiredItemReports = controller.getExpiredItemReportsByProduct(start, end, productIDs);
            List<DefectiveItemReport> expiredItemReportList = new ArrayList<>();
            for (Domain.Business.Objects.Inventory.DefectiveItems eir : expiredItemReports)
                expiredItemReportList.add(new DefectiveItemReport(eir));
            return Result.makeOk(expiredItemReportList);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Move product from warehouse to store
     *
     * @return Result detailing success of operation
     */
    public Result<Object> moveItems(int storeID, int productID, int amount){
        try {
            controller.moveItems(storeID, productID, amount);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    /**
     * Customer returns items to a store
     *
     * @return Result detailing success of operation, containing the price the customer paid for the items
     */
    public Result<Double> returnItems(int storeID, int productID, int amount, LocalDate dateBought){
        try {
            return Result.makeOk(controller.returnItems(storeID, productID, amount, dateBought));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Add a new store to the system
     *
     * @return Result detailing success of operation
     */
    public Result<Integer> addStore(){
        try {
            return Result.makeOk(controller.addStore());
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Remove a store from the system
     *
     * @return Result detailing success of operation
     */
    public Result<Object> removeStore(int storeID){
        try {
            controller.removeStore(storeID);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    /**
     * Change product Price
     *
     * @return Result detailing success of operation, containing the edited Product
     */
    public Result<Product> editProductPrice(int productID, double newPrice){
        try {
            return Result.makeOk(new Product(controller.editProductPrice(productID, newPrice)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Change product Name
     *
     * @return Result detailing success of operation, containing the edited Product
     */
    public Result<Product> editProductName(int productID, String newName){
        try {
            return Result.makeOk(new Product(controller.editProductName(productID, newName)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Move Product to new Category
     *
     * @return Result detailing success of operation, containing the edited Product
     */
    public Result<Product> moveProductToCategory(int productID, int newCatID){
        try {
            return Result.makeOk(new Product(controller.moveProductToCategory(productID, newCatID)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Change Category Name
     *
     * @return Result detailing success of operation, containing the edited Category
     */
    public Result<Category> editCategoryName(int categoryID, String newName){
        try {
            return Result.makeOk(new Category(controller.editCategoryName(categoryID, newName)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * Change Category parent/super-category
     *
     * @return Result detailing success of operation, containing the edited Category
     */
    public Result<Category> changeCategoryParent(int categoryID, int parentID){
        try {
            return Result.makeOk(new Category(controller.changeParentCategory(categoryID, parentID)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * add new category
     *
     * @return Result detailing success of operation, containing the new Category
     */
    public Result<Category> addNewCategory(String name, int parentCategoryID){
        try {
            return Result.makeOk(new Category(controller.addCategory(name, parentCategoryID)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

//    /**
//     * Add supplier to list of suppliers who sell specified product
//     *
//     * @return Result detailing success of operation, containing the edited Product
//     */
//    public Result<Product> addSupplierToProduct(int productID, int supplierID, int productIDWithSupplier){
//        try {
//            return Result.makeOk(new Product(controller.addSupplierToProduct(productID, supplierID, productIDWithSupplier)));
//        }
//        catch (Exception e){
//            return Result.makeError(e.getMessage());
//        }
//    }

//    /**
//     * Remove supplier from list of suppliers who sell specified product
//     *
//     * @return Result detailing success of operation, containing the edited Product
//     */
//    public Result<Product> removeSupplierFromProduct(int productID, int supplierID){
//        try {
//            return Result.makeOk(new Product(controller.removeSupplierFromProduct(productID, supplierID)));
//        }
//        catch (Exception e){
//            return Result.makeError(e.getMessage());
//        }
//    }

    /**
     * Delete a category
     * @param catID = ID of category to remove
     * @return Result detailing success of operation
     */
    public Result<Boolean> deleteCategory(int catID) {
        try {
            Result.makeOk(controller.deleteCategory(catID));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    /**
     * change the min amount of product in specified store before warnings will occur
     *
     * @return Result detailing success of operation, containing the edited product
     */
    public Result<Product> changeProductMin(int store, int product, int min) {
        try {
            return Result.makeOk(new Product(controller.changeProductMin(store, product, min)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * change the max recommended amount of product in specified store
     *
     * @return Result detailing success of operation, containing the edited product
     */
    public Result<Product> changeProductTarget(int store, int product, int max) {
        try {
            return Result.makeOk(new Product(controller.changeProductTarget(store, product, max)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    /**
     * get a specified category
     *
     * @return Result detailing success of operation, containing the category
     */
    public Result<Category> getCategory(int categoryID) {
        try {
            if (controller.getCategory(categoryID)==null)
                return Result.makeError("Category not found");
            return Result.makeOk(new Category(controller.getCategory(categoryID)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Product> getProduct(int product) {
        try {
            return Result.makeOk(new Product(controller.getProduct(product)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Collection<ServiceOrderObject>> getAvailableOrders() {
        try {
            Collection<Order> orders = controller.getAvailableOrders();
            Collection<ServiceOrderObject> serviceOrders = new ArrayList<>();
            for (Order o : orders) {
                List<ServiceOrderItemObject> oItems = new ArrayList<>();
                for (OrderItem oItem : o.getOrderItems()) {
                    oItems.add(new ServiceOrderItemObject(oItem.getProductId(),oItem.getIdBySupplier(), oItem.getName(), oItem.getQuantity(), oItem.getPricePerUnit(), oItem.getDiscount(), oItem.getFinalPrice(), oItem.getMissingItems(), oItem.getDefectiveItems(), oItem.getDescription(), oItem.getWeight()));
                }
                serviceOrders.add(new ServiceOrderObject(o.getId(), o.getSupplierId(), o.getCreationTime(), o.getArrivaltime(), o.getStoreID(), o.getStatusString(), oItems));
            }
            return Result.makeOk(serviceOrders);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    public List<String> getReadyOrders() {
        try {
            Collection<Order> orders = controller.getReadyOrders();
            Collection<ServiceOrderObject> serviceOrders = new ArrayList<>();
            for (Order o : orders) {
                List<ServiceOrderItemObject> oItems = new ArrayList<>();
                for (OrderItem oItem : o.getOrderItems()) {
                    oItems.add(new ServiceOrderItemObject(oItem.getProductId(),oItem.getIdBySupplier(), oItem.getName(), oItem.getQuantity(), oItem.getPricePerUnit(), oItem.getDiscount(), oItem.getFinalPrice(), oItem.getMissingItems(), oItem.getDefectiveItems(), oItem.getDescription(), oItem.getWeight()));
                }
                serviceOrders.add(new ServiceOrderObject(o.getId(), o.getSupplierId(), o.getCreationTime(), o.getArrivaltime(), o.getStoreID(), o.getStatusString(), oItems));
            }
            List<String> ordersStrings = new ArrayList<>(orders.size());
            for (ServiceOrderObject order: serviceOrders) {
                ordersStrings.add(order.toString());
            }
            return ordersStrings;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void setSupplierController(SupplierController supplierController) {
        controller.setSupplierController(supplierController);
    }

    public int getTarget(int i, int productID) {
        return controller.getTarget(i, productID);
    }
    public int getMin(int i, int productID) {
        return controller.getMin(i, productID);
    }
}
