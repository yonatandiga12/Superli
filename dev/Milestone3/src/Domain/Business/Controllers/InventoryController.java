package Domain.Business.Controllers;

import Domain.Business.Controllers.Transport.TransportController;
import Domain.Business.Objects.Inventory.*;
import Domain.Business.Objects.Supplier.Order;
import Domain.Business.Objects.Supplier.OrderItem;
import Domain.DAL.Controllers.InventoryAndSuppliers.CategoryDataMapper;
import Domain.DAL.Controllers.InventoryAndSuppliers.ProductDataMapper;
import Domain.DAL.Controllers.InventoryAndSuppliers.SalesDataMapper;
import Domain.DAL.Controllers.InventoryAndSuppliers.StoreDAO;
import Globals.Pair;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class InventoryController {
    private Collection<Integer> storeIds;
    private Map<Integer, Category> categories;
    private Map<Integer, SaleToCustomer> sales;
    private Map<Integer, Product> products;
    private int saleID;
    private int catID;
    private int productID;
    private int storeID;
    private SupplierController supplierController;
    private TransportController transportController;
    private final static StoreDAO STORE_DAO = new StoreDAO();
    private final static ProductDataMapper PRODUCT_DATA_MAPPER = Product.PRODUCT_DATA_MAPPER;
    private final static CategoryDataMapper CATEGORY_DATA_MAPPER = Category.CATEGORY_DATA_MAPPER;
    private final static SalesDataMapper SALE_DATA_MAPPER = SaleToCustomer.SALES_DATA_MAPPER;
    private List<Order> readyOrders;

    public InventoryController() {
        storeIds = STORE_DAO.getAll();
        categories = CATEGORY_DATA_MAPPER.getIntegerMap();
        sales = SALE_DATA_MAPPER.getIntegerMap();
        products = PRODUCT_DATA_MAPPER.getIntegerMap();
        storeID=STORE_DAO.getIDCount() + 1;
        saleID=SALE_DATA_MAPPER.getIDCount() + 1;
        catID=CATEGORY_DATA_MAPPER.getIDCount() + 1;
        productID=PRODUCT_DATA_MAPPER.getIDCount() + 1;
        readyOrders = new ArrayList<>();
        //supplierController = new SupplierController();
        transportController = new TransportController();
    }

    public void setSupplierController(SupplierController supCont){
        supplierController = supCont;
    }

//    public void setTransportController(TransportController controller) {
//        this.transportController = controller;
//    }
    //for tests
    private static InventoryController instance;
    public static synchronized InventoryController getInventoryController() {
        if (instance==null) {
            instance = new InventoryController();
        }
        return instance;
    }

    public Category getCategory(int categoryID) {
        try {
            if (categoryID==0)
                return null;
            Category output = categories.get(categoryID);
            if (output==null) {
                output = CATEGORY_DATA_MAPPER.get(Integer.toString(categoryID));
                if (output==null) {
                    throw new IllegalArgumentException("Category ID Invalid: " + categoryID);
                }
            }
            return output;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product getProduct(int productID) {
        try {
            Product output = products.get(productID);
            if (output==null) {
                output = PRODUCT_DATA_MAPPER.get(Integer.toString(productID));
                if (output==null) {
                    throw new IllegalArgumentException("Product ID Invalid: " + productID);
                }
            }
            return output;
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Product ID Invalid: " + productID);
        }
    }

    private Collection<SaleToCustomer> getAllSales() {
        try {
            return SALE_DATA_MAPPER.getAll();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    private SaleToCustomer getSale(int saleID) {
        try {
            SaleToCustomer output = sales.get(saleID);
            if (output==null) {
                output = SALE_DATA_MAPPER.get(Integer.toString(saleID));
                if (output==null) {
                    throw new IllegalArgumentException("Sale ID Invalid: " + saleID);
                }
            }
            return output;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SaleToCustomer> getRemovableSales() {
        List<SaleToCustomer> removableSales = new ArrayList<>();
        for (SaleToCustomer sale: getAllSales()) {
            if (!sale.isPassed())
                removableSales.add(sale);
        }
        return removableSales;
    }

    private void redundantCategories (List<Integer> categoriesList) {
        //remove redundant categories
        List<Integer> redundantCategories = new ArrayList<>();
        //search forward
        for (Integer i : categoriesList) {
            Category category1 = getCategory(i);
            for (Integer j :categoriesList) {
                if (!(i.equals(j))) {
                    Category category2 = getCategory(j);
                    if (category2.belongsToCategory(category1)) {
                        redundantCategories.add(j);
                    }
                    if (category1.belongsToCategory(category2)) {
                        redundantCategories.add(i);
                    }
                }
            }
        }
        categoriesList.removeAll(redundantCategories);
    }

    private void redundantProducts(List<Integer> productIDs, List<Integer> categoriesList) {
        //search products
        List<Integer> redundantProducts = new ArrayList<>();
        for (Integer i : productIDs) {
            Product product = getProduct(i);
            for (Integer c : categoriesList) {
                Category category = getCategory(c);
                if (product.belongsToCategory(category)) {
                    redundantProducts.add(i);
                }
            }
        }
        productIDs.removeAll(redundantProducts);
    }
    public SaleToCustomer addSale(List<Integer> categoriesList, List<Integer> productIDs, int percent, LocalDate start, LocalDate end) {
        /*if (!start.before(end)) //could add more restrictions regarding adding past sales but would be problematic for tests
            throw new IllegalArgumentException("Illegal dates. start must be before end");
        if (!(percent>0 && percent<100))
            throw new IllegalArgumentException("Percent sale must be between 1 and 99. Received: " + percent);*/
        if (start.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Can't create new sale that start in the past");
        if (categoriesList.isEmpty()&&productIDs.isEmpty())
            throw new IllegalArgumentException("Must specify categories and/or products for the sale to apply to");
        redundantCategories(categoriesList);
        redundantProducts(productIDs, categoriesList);
        SaleToCustomer sale = new SaleToCustomer(saleID++, start, end, percent, categoriesList, productIDs);
        SALE_DATA_MAPPER.insert(sale);

        Product product;
        for (Integer pID: productIDs) {
            //should we throw error if only one of them is illegal
            product = getProduct(pID);
            product.addSale(sale);
        }
        Category category;
        for (Integer cID: categoriesList) {
            category = getCategory(cID);
            if (category!=null)
                category.addSale(sale);        }
        return sale;
    }


    private void removeSaleFromProductsAndCategories(SaleToCustomer sale) {
        Product product;
        for (Integer pID: sale.getProducts()) {
            product = getProduct(pID);
            product.removeSale(sale);
        }
        Category category;
        for (Integer cID: sale.getCategories()) {
            category = getCategory(cID);
            if (category!=null)
                category.removeSale(sale);
        }
    }

    public void removeSale(int saleID) throws SQLException {
        SaleToCustomer sale = getSale(saleID);
        if (sale.isActive()) {
            sale.setEndDate(LocalDate.now());
        }
        else if (sale.isUpcoming()) {
            removeSaleFromProductsAndCategories(sale);
            sales.remove(saleID);
            SALE_DATA_MAPPER.remove(sale.getId());
        }
    }
    //Map<OrderId<ProductId , ( (missingAmount,defectiveAmount), description)>>
    public void transportArrived(int transportID, Map<Integer,Map<Integer, Pair<Pair<Integer, Integer>, String>>> reports) throws Exception {
        transportController.endTransport(transportID);
        List<Integer> orderIDs = transportController.getTransport(transportID).gerOrders();
        Order arrivedOrder;
        for (int orderID : orderIDs) {
            Map<Integer, Pair<Pair<Integer, Integer>, String>> rep = reports.get(orderID);
            if (rep==null)
                rep = new HashMap<>();
            arrivedOrder = supplierController.orderHasArrived(orderID, rep);
            int orderStoreID = arrivedOrder.getStoreID();
            for (OrderItem orderItem : arrivedOrder.getOrderItems()) {
                getProduct(orderItem.getProductId()).addItems(orderStoreID, orderItem.getQuantity(), orderItem.getMissingItems() + orderItem.getDefectiveItems(), orderItem.getDescription());
            }
        }
    }

//    public void transportArrived(int orderID, Map<Integer, Pair<Pair<Integer, Integer>, String>> reportOfOrder) throws Exception {
//        Order arrivedOrder = supplierController.orderHasArrived(orderID, reportOfOrder);
//        int orderStoreID = arrivedOrder.getStoreID();
//        for (OrderItem orderItem : arrivedOrder.getOrderItems()) {
//            getProduct(orderItem.getProductId()).addItems(orderStoreID, orderItem.getQuantity(), orderItem.getMissingItems()+orderItem.getDefectiveItems(), orderItem.getDescription());
//        }
//    }

    public Set<SaleToCustomer> getSaleHistoryByProduct(int productID) {
        return getProduct(productID).getSaleHistory();
    }

    public Set<SaleToCustomer> getSaleHistoryByCategory(int categoryID) {
        return getCategory(categoryID).getSaleHistory();
    }

    public List<DefectiveItems> getDefectiveItemsByStore(LocalDate start, LocalDate end, Collection<Integer> storeIDs) {
        if (storeIDs.isEmpty())
            storeIDs=storeIds;
        List<DefectiveItems> defective = new ArrayList<>();
        defective.addAll(getDamagedItemReportsByStore(start, end, storeIDs));
        defective.addAll(getExpiredItemReportsByStore(start, end, storeIDs));
        return defective;
    }

    public List<DefectiveItems> getDefectiveItemsByCategory(LocalDate start, LocalDate end, List<Integer> catIDs) {
        List<DefectiveItems> defective = new ArrayList<>();
        defective.addAll(getDamagedItemReportsByCategory(start, end, catIDs));
        defective.addAll(getExpiredItemReportsByCategory(start, end, catIDs));
        return defective;
    }

    public List<DefectiveItems> getDefectiveItemsByProduct(LocalDate start, LocalDate end, List<Integer> productIDs) {
        List<DefectiveItems> defective = new ArrayList<>();
        defective.addAll(getDamagedItemReportsByProduct(start, end, productIDs));
        defective.addAll(getExpiredItemReportsByProduct(start, end, productIDs));
        return defective;
    }

    public Collection<Product> getProducts() {
        try {
            return PRODUCT_DATA_MAPPER.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<Category> getCategories() {
        Collection<Category> categoryCollection = null;
        try {
            categoryCollection = CATEGORY_DATA_MAPPER.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Category category: categoryCollection) {
            categories.put(category.getID(), category);
        }
        Collection<Product> productCollection = getProducts();
        for (Product product: productCollection) {
            categories.get(product.getCategoryID()).addProduct(product);
        }
        return categories.values();
    }

    public List<Product> getProductsFromCategory(List<Integer> categoryIDs) {
        List<Product> products = new ArrayList<>();
        redundantCategories(categoryIDs);
        for (int i : categoryIDs)
            products.addAll(getCategory(i).getAllProductsInCategory());
        return products;
    }

    public void moveItems(int storeID, int productID, int amount) {
        Product product = getProduct(productID);
        product.moveItems(storeID, amount);
    }

    public double returnItems(int storeID, int productID, int amount, LocalDate dateBought) {
        //find product add amount
        Product product = getProduct(productID);
        return product.returnItems(storeID, amount, dateBought);
    }

    public int addStore() {
        int id = storeID++;
        storeIds.add(id);
        STORE_DAO.addStore(id);
        return id;
    }

    public void removeStore(int storeID) {
        if (!storeIds.contains(storeID))
            throw new IllegalArgumentException("There is no store with ID" + storeID);
        for (Product p : getProducts()) {
            removeProductFromStore(storeID, p.getId());
        }
        storeIds.remove(storeID);
        STORE_DAO.removeStore(storeID);
    }

    public Product addProductToStore(int storeID, List<Integer> shelvesInStore, List<Integer> shelvesInWarehouse, int productID, int minAmount, int targetAmount) {
        Product product = getProduct(productID);
        product.addLocation(storeID, shelvesInStore, shelvesInWarehouse, minAmount, targetAmount);
        return product;
    }

    public Product removeProductFromStore(int storeID, int productID) {
        Product product = getProduct(productID);
        product.removeLocation(storeID);
        return product;
    }
    
    public void loadData() throws NoSuchMethodException {
        throw new NoSuchMethodException();
    }

    public Product newProduct(String name, int categoryID, double weight, double price, String manufacturer) {
        Category category = getCategory(categoryID);
        int id = productID++;
        Product product = new Product(id, name, category, weight, price, manufacturer);
        category.addProduct(product);
        PRODUCT_DATA_MAPPER.insert(product);
        return product;
    }

    public Boolean deleteProduct(int id){
        getProduct(id).delete();
        try {
            supplierController.deleteProduct(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        int flag = PRODUCT_DATA_MAPPER.remove(Integer.toString(id));
        if(products.remove(id)!=null || flag!=-1)
            return true;
        else
            return false;
    }

    public Pair<DefectiveItems, String> reportDamaged(int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse) {
        Product product = getProduct(productID);
        if (product==null)
            throw new IllegalArgumentException("Product ID: " + productID + " is not exist");
        DefectiveItems DI = product.reportDamaged(storeID, amount, employeeID, description, inWarehouse);
        boolean underMin = product.isLow(storeID);
        return new Pair<>(DI, underMin ? underMinMessage(productID, storeID) : null);
    }

    public Pair<DefectiveItems, String> reportExpired(int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse) {
        Product product = getProduct(productID);
        DefectiveItems DI = product.reportExpired(storeID, amount, employeeID, description, inWarehouse);
        boolean underMin = product.isLow(storeID);
        return new Pair<>(DI, underMin ? underMinMessage(productID, storeID) : null);
    }

    public Pair<Double, String> buyItems(int storeID, int productID, int amount) {
        Product product = getProduct(productID);
        double price = product.getCurrentPrice()*amount;
        product.removeItems(storeID, amount, false);
        boolean underMin = product.isLow(storeID);
        return new Pair<>(price, underMin ? underMinMessage(productID, storeID) : null);
    }

    private String underMinMessage(int productID, int storeID) {
        return "WARNING: product with ID " + productID + " is in low stock in store " + storeID;
    }

    private void checkDates(LocalDate start, LocalDate end) {
        LocalDate today = LocalDate.now();
        if (start.isAfter(today) || end.isBefore(start))
            throw new IllegalArgumentException("Illegal Dates. Cannot be in the future and end cannot be before start");
    }
    //why is storeIDS a list?
    public List<DefectiveItems> getDamagedItemReportsByStore(LocalDate start, LocalDate end, Collection<Integer> storeID) {
        if (storeID.isEmpty())
            storeID=storeIds;
        checkDates(start, end);
        List<DefectiveItems> dirList = new ArrayList<>();
        Collection<Product> productList = getProducts();
        for (Product p: productList) {
            dirList.addAll(p.getDamagedItemReportsByStore(start, end, storeID));
        }
        return dirList;
    }
    public List<DefectiveItems> getDamagedItemReportsByCategory(LocalDate start, LocalDate end, List<Integer> categoryID) {
        getCategories();
        checkDates(start, end);
        List<DefectiveItems> dirList = new ArrayList<>();
        for (Integer c: categoryID) {
            dirList.addAll(getCategory(c).getDamagedItemReports(start, end));
        }
        return dirList;
    }

    public List<DefectiveItems> getDamagedItemReportsByProduct(LocalDate start, LocalDate end, List<Integer> productID) {
        getProducts();
        checkDates(start, end);
        List<DefectiveItems> dirList = new ArrayList<>();
        for (Integer p: productID) {
            dirList.addAll(getProduct(p).getDamagedItemReports(start, end));
        }
        return dirList;
    }

    public List<DefectiveItems> getExpiredItemReportsByStore(LocalDate start, LocalDate end, Collection<Integer> storeID) {
        if (storeID.isEmpty())
            storeID=storeIds;
        checkDates(start, end);
        List<DefectiveItems> eirList = new ArrayList<>();
        Collection<Product> productList = getProducts();
        for (Product p: productList) {
            eirList.addAll(p.getExpiredItemReportsByStore(start, end, storeID));
        }
        return eirList;
    }

    public List<DefectiveItems> getExpiredItemReportsByCategory(LocalDate start, LocalDate end, List<Integer> categoryID) {
        getCategories();
        checkDates(start, end);
        List<DefectiveItems> eirList = new ArrayList<>();
        for (Integer c: categoryID) {
            eirList.addAll(getCategory(c).getExpiredItemReports(start, end));
        }
        return eirList;
    }

    public List<DefectiveItems> getExpiredItemReportsByProduct(LocalDate start, LocalDate end, List<Integer> productID) {
        getProducts();
        checkDates(start, end);
        List<DefectiveItems> eirList = new ArrayList<>();
        for (Integer p: productID) {
            eirList.addAll(getProduct(p).getExpiredItemReports(start, end));
        }
        return eirList;
    }

    public Collection<Integer> getStoreIDs() {
        return storeIds;
    }

    public Product editProductPrice(int productID, double newPrice) {
        Product p = getProduct(productID);
        p.setPrice(newPrice);
        return p;
    }

    public Product editProductName(int productID, String newName) {
        Product p = getProduct(productID);
        p.setName(newName);
        return p;
    }

    public Product moveProductToCategory(int productID, int newCatID) {
        Product p = getProduct(productID);
        p.setCategory(getCategory(newCatID));
        return p;
    }

    public Category editCategoryName(int categoryID, String newName) {
        getCategories();
        Category c = getCategory(categoryID);
        c.setName(newName);
        return c;
    }

    public Category changeParentCategory(int categoryID, int newCatID) {
        getCategories();
        Category c = getCategory(categoryID);
        c.changeParentCategory(getCategory(newCatID));
        return c;
    }

    public Category addCategory(String name, Integer parentCategoryID) {
        int id = catID++;
        if (parentCategoryID==0) {
            CATEGORY_DATA_MAPPER.insert(new Category(id, name, new HashSet<>(), new ArrayList<>(), null));
        }
        else {
            CATEGORY_DATA_MAPPER.insert(new Category(id, name, new HashSet<>(), new ArrayList<>(), getCategory(parentCategoryID)));
        }
        return getCategory(id);
    }

    public List<StockReport> getMinStockReport() {
        List<StockReport> lowOnStock = new ArrayList<>();
        for (Product p : getProducts()) {
            for (int i : storeIds) {
                if (p.isLow(i))
                    lowOnStock.add(p.getStockReport(i));
            }
        }
        return lowOnStock;
    }

    public boolean isUnderMin(int storeID, int productID) {
        return getProduct(productID).isLow(storeID);
    }

    public int getAmountInStore(int storeID, int productID) {
        if (!storeIds.contains(storeID))
            throw new IllegalArgumentException("Store " + storeID + " is not registered in the system");
        Product p = getProduct(productID);
        return p.getInStore(storeID)+p.getInWarehouse(storeID);
    }

    public List<StockReport> getStockReport(Collection<Integer> storeIDs, List<Integer> categoryIDs) {
        redundantCategories(categoryIDs);
        List<StockReport> stock = new ArrayList<>();
        for (Integer store : storeIDs) {
            for (Integer catID : categoryIDs) {
                Category category = getCategory(catID);
                for (Product p : category.getAllProductsInCategory()) {
                    if (p.getStockReport(store) != null)
                        stock.add(p.getStockReport(store));
                }
            }
        }
        return stock;
    }

    public boolean deleteCategory(int catID) {
        getCategories();
        Category categoryToRemove = getCategory(catID);
        if (categoryToRemove==null)
            throw new IllegalArgumentException("There is no category with id " + catID);
        if (!categoryToRemove.getSubcategories().isEmpty())
            throw new IllegalArgumentException("Cannot delete a category that has subcategories");
        if (!categoryToRemove.getAllProductsInCategory().isEmpty())
            throw new IllegalArgumentException("Cannot delete a category that has products still assigned to it");
        categoryToRemove.changeParentCategory(null);
        int flag = CATEGORY_DATA_MAPPER.remove(Integer.toString(catID));
        if(categories.remove(catID)!=null || flag!=-1)
            return true;
        else
            return false;
    }

    public Product changeProductMin(int store, int product, int min) {
        Product p = getProduct(product);
        p.changeProductMin(store, min);
        return p;
    }

    public Product changeProductTarget(int store, int product, int target) {
        Product p = getProduct(product);
        p.changeProductTarget(store, target);
        return p;
    }

    //public for tests
    public Map<Integer, Integer> getAmountsForMinOrders(Product product) {
        Map<Integer, Integer> amounts = new HashMap<>();
        int amount;
        for (int storeID: storeIds) {
            amount = product.getAmountForOrder(storeID);
            if (amount>0)
                amounts.put(storeID, amount);
        }
        return amounts;
    }

    //needs to be called every morning from service by some kind of trigger.Get new and updated orders.
    public List<Order> getAvailableOrders() throws Exception {
        Map<Integer, Map<Integer,Integer>> thingsToOrder = new HashMap<>(); // <productID, <storeID, amount>>
        Map<Integer, Integer> amounts;
        Collection<Integer> productIds = StockReport.dataMapper.getProductsUnderMin();
        for (Integer productID: productIds) {
            Product product = getProduct(productID);
            amounts = getAmountsForMinOrders(product);
            if (amounts.size()>0)
                thingsToOrder.put(product.getId(), amounts);
        }
        List<Order> orders = supplierController.createAllOrders(thingsToOrder); //orders we print on screen (=order to issue from suppliers) (new orders and edited order)
        //document every order we print on screen (new orders or edit amount of existed orders)
        for (Order order: orders) {
            for (OrderItem orderItem: order.getOrderItems()) {
                if (getProduct(orderItem.getProductId()).getStockReport(order.getStoreID())==null)
                    addProductToStore(order.getStoreID(), new ArrayList<>(), new ArrayList<>(), orderItem.getProductId(), 100, 200);
                getProduct(orderItem.getProductId()).addDelivery(order.getStoreID(), orderItem.getQuantity());
            }
        }
        readyOrders = orders;
        return orders;
    }


    /**
     *
     * @param productId
     * @param storeId
     * @param amount - can be negative (if negative than we subtract from the product), just use add "+". we will take care of the rest.
     */
    public void updateOnTheWayProducts(int productId, int storeId, int amount) {
        getProduct(productId).getStockReport(storeId).channgeInDelivery(amount);
    }

    public List<Order> getReadyOrders() {
        return readyOrders;
    }

    public int getTarget(int i, int productID) {
        return getProduct(productID).getStockReport(i).getTargetAmountInStore();
    }
    public int getMin(int i, int productID) {
        return getProduct(productID).getStockReport(i).getMinAmountInStore();
    }
}