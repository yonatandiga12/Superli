package Domain.Business.Objects.Inventory;

import Domain.DAL.Controllers.InventoryAndSuppliers.*;

import java.time.LocalDate;
import java.util.*;

import static Globals.Defect.Damaged;
import static Globals.Defect.Expired;

public class Product {
    private int id;
    private String name;
    private Category category;
    private Map<Integer, StockReport> stockReports; //<storeID, stockReports>
    private List<Location> locations;
    private List<DefectiveItems> damagedItemReport;
    private List<DefectiveItems> expiredItemReport;
    private double weight;
    private String manufacturer;
    private double price;
    private List<SaleToCustomer> sales;
    public static final ProductDataMapper PRODUCT_DATA_MAPPER = new ProductDataMapper();
    private static final StockReportDataMapper STOCK_REPORT_DATA_MAPPER = StockReport.dataMapper;
    private static final SalesDataMapper SALE_DATA_MAPPER = SaleToCustomer.SALES_DATA_MAPPER;
    private static final DefectiveItemsDataMapper DEFECTIVE_ITEMS_DATA_MAPPER = DefectiveItems.DEFECTIVE_ITEMS_DATA_MAPPER;
    private static final LocationDataMapper LOCATION_DATA_MAPPER = Location.LOCATION_DATA_MAPPER;
    private static int locationIDCounter = LOCATION_DATA_MAPPER.getMax()+1;
    private static int defectReportCounter = DEFECTIVE_ITEMS_DATA_MAPPER.getMax()+1;


    public int getId() { return id; }
    public String getName() { return name; }
    public int getCategoryID() {return category.getID();}
    public double getOriginalPrice() { return price; }

    public void setName(String name) {
        this.name = name;
        PRODUCT_DATA_MAPPER.updateName(id, name);
    }
    public void setPrice(double price) {
        this.price = price;
        PRODUCT_DATA_MAPPER.updatePrice(id, price);
    }
    public void setCategory(Category category) {
        if (category!=null)
            category.removeProduct(this);
        this.category = category;
        category.addProduct(this);
        PRODUCT_DATA_MAPPER.updateCategory(id, getCategoryID());
    }

    public Product(int id, String name, Category category, double weight, double price, String manufacturer) {
        this.id = id;
        this.name = name;
        this.name = name;
        this.category = category;
        this.weight = weight;
        this.price = price;
        this.manufacturer = manufacturer;
        sales = new ArrayList<>();
        damagedItemReport = new ArrayList<>();
        expiredItemReport = new ArrayList<>();
        locations = new ArrayList<>();
        stockReports = new HashMap<>();
    }

    public double getWeight() {
        return weight;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public Integer getInStore(int store) {
        if (getStockReport(store)==null)
            throw new IllegalArgumentException("Product " + id + " is not sold in store " + store);
        return getStockReport(store).getAmountInStore();
    }

    public Integer getTotalInStore(int store) {
        if (getStockReport(store)==null)
            throw new IllegalArgumentException("Product " + id + " is not sold in store " + store);
        return getStockReport(store).getTotalAmount();
    }

    public Integer getInWarehouse(int store) {
        if (stockReports.get(store)==null)
            throw new IllegalArgumentException("Product " + id + " is not sold in store " + store);
        return getStockReport(store).getAmountInWarehouse();
    }

    public double getCurrentPrice() {
        SaleToCustomer BestCurrentSale = getCurrentSale();
        if (BestCurrentSale==null)
            return price;
        return price*(100-BestCurrentSale.getPercent())/100;
    }

    private SaleToCustomer getCurrentSale() {
        SaleToCustomer currentSale = null;
        for (SaleToCustomer sale: sales)
            if ((sale.isActive() && currentSale==null) || (sale.isActive() && currentSale!=null && currentSale.getPercent()<sale.getPercent()))
                currentSale = sale;
        return category.findCurrentBestSale(currentSale);
    }

    public void addSale(SaleToCustomer sale) {
        sales.add(sale);
    }

    public void removeItems(int storeID, int amount, boolean inWarehouse) { //bought
        StockReport stockReport = STOCK_REPORT_DATA_MAPPER.get(storeID, id);
        if (stockReport != null)
            stockReports.put(storeID, stockReport);
        if (!stockReports.containsKey(storeID))
            throw new IllegalArgumentException("Product: " + name + ", hasn't been added to store: " + storeID);
        getStockReport(storeID).removeItemsFromStore(amount, inWarehouse);
    }

    public void addItems(int storeID, int amount, int missingAndDefectiveItems, String description) {
        if (!stockReports.containsKey(storeID))
            throw new IllegalArgumentException("Product: " + name + ", hasn't been added to the store");
        if (amount < missingAndDefectiveItems)
            throw new IllegalArgumentException("You have entered more missing and defective items of product: " + id + ": " + name + " then arrived in the order");
        getStockReport(storeID).addItems(amount, missingAndDefectiveItems, description);
    }

    public void moveItems(int storeID, int amount) { //from warehouse to store
        StockReport stockReport = STOCK_REPORT_DATA_MAPPER.get(storeID, id);
        if (stockReport==null)
            throw new IllegalArgumentException("Product: " + name + ", hasn't been added to the store");
        stockReport.moveItems(amount);
    }

    public double returnItems(int storeId, int amount, LocalDate dateBought) { //from customer to store
        if (!stockReports.containsKey(storeId))
            throw new IllegalArgumentException("Product: " + name + ", hasn't been added to the store");
        getStockReport(storeId).returnItems(amount);
        return amount*getPriceOnDate(dateBought);
    }

    private double getPriceOnDate(LocalDate dateBought) {
        if (dateBought.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("An item isn't able to be bought after present time");
        List<SaleToCustomer> salesActive = new ArrayList<>();
        for (SaleToCustomer s : sales) {
            if (s.wasActive(dateBought))
                salesActive.add(s);
        }
        salesActive.addAll(category.getSalesOnDate(dateBought));
        SaleToCustomer bestSale = null;
        for (SaleToCustomer sale: salesActive)
            if (bestSale==null || bestSale.getPercent()<sale.getPercent()) {
                bestSale = sale;
            }
        if (bestSale==null)
            return getOriginalPrice();
        return getOriginalPrice()*(100-bestSale.getPercent())/100; //what if price in general changed? do we need a log of the prices?
    }

    public DefectiveItems reportDamaged(int storeID, int amount, int employeeID, String description, boolean inWarehouse) {
        removeItems(storeID, amount, inWarehouse);
        DefectiveItems dir = new DefectiveItems(defectReportCounter++, Damaged, LocalDate.now(), storeID, id, amount, employeeID, description, inWarehouse);
        damagedItemReport.add(dir);
        DEFECTIVE_ITEMS_DATA_MAPPER.insert(dir);
        return dir;
    }

    public DefectiveItems reportExpired(int storeID, int amount, int employeeID, String description, boolean inWarehouse) {
        removeItems(storeID, amount, inWarehouse);
        DefectiveItems eir = new DefectiveItems(defectReportCounter++, Expired, LocalDate.now(), storeID, id, amount, employeeID, description, inWarehouse);
        expiredItemReport.add(eir);
        DEFECTIVE_ITEMS_DATA_MAPPER.insert(eir);
        return eir;
    }

    public List<DefectiveItems> getDamagedItemReportsByStore(LocalDate start, LocalDate end, Collection<Integer> storeIDs) {
        List<DefectiveItems> dirList = new ArrayList<>();
        for (int store : storeIDs) {
            for (DefectiveItems dir : DEFECTIVE_ITEMS_DATA_MAPPER.getDamagedByStore(store, id)) {
                if (dir != null) {
                    damagedItemReport.add(dir);
                    if (dir.inDates(start, end))
                        dirList.add(dir);
                }
            }
        }
        return dirList;
    }

    public Collection<DefectiveItems> getDamagedItemReports(LocalDate start, LocalDate end) {
        List<DefectiveItems> dirList = new ArrayList<>();
        for (DefectiveItems dir: DEFECTIVE_ITEMS_DATA_MAPPER.getByDefect(Damaged, id)) {
            if (dir != null) {
                damagedItemReport.add(dir);
                if (dir.inDates(start, end))
                    dirList.add(dir);
            }
        }
        return dirList;
    }

    public List<DefectiveItems> getExpiredItemReportsByStore(LocalDate start, LocalDate end, Collection<Integer> storeIDs) {
        List<DefectiveItems> eirList = new ArrayList<>();
        for (int store : storeIDs) {
            for (DefectiveItems eir : DEFECTIVE_ITEMS_DATA_MAPPER.getExpiredByStore(store, id)) {
                if (eir != null) {
                    expiredItemReport.add(eir);
                    if (eir.inDates(start, end))
                        eirList.add(eir);
                }
            }
        }
        return eirList;
    }

    public Collection<DefectiveItems> getExpiredItemReports(LocalDate start, LocalDate end) {
        List<DefectiveItems> eirList = new ArrayList<>();
        for (DefectiveItems eir: DEFECTIVE_ITEMS_DATA_MAPPER.getByDefect(Expired, id)) {
            if (eir != null) {
                expiredItemReport.add(eir);
                if (eir.inDates(start, end))
                    eirList.add(eir);
            }
        }
        return eirList;
    }

    public void addLocation(int storeID, List<Integer> shelvesInStore, List<Integer> shelvesInWarehouse, int minAmount, int targetAmount) {
        Location storeLocation = new Location(locationIDCounter++, storeID, false, shelvesInStore);
        Location warehouseLocation = new Location(locationIDCounter++, storeID, true, shelvesInWarehouse);
        if (getStockReport(storeID)!=null)
            throw new IllegalArgumentException("Product " + name + " is already sold at store " + storeID);
        stockReports.put(storeID, new StockReport(storeID, id, 0, 0, minAmount, targetAmount, 0));
        STOCK_REPORT_DATA_MAPPER.insert(stockReports.get(storeID));
        locations.add(storeLocation);
        LOCATION_DATA_MAPPER.insert(storeLocation, id);
        locations.add(warehouseLocation);
        LOCATION_DATA_MAPPER.insert(warehouseLocation, id);
    }

    public void removeLocation(int storeID) {
        if (getStockReport(storeID)==null)
            return;
        removeStockReport(storeID);
        for (int i=0; i<locations.size(); i++) {
            if (locations.get(i).getStoreID()==storeID) {
                locations.remove(i);
                i--;
            }
        }
        LOCATION_DATA_MAPPER.removeByStore(storeID);
    }

    private void removeStockReport(int storeID) {
        STOCK_REPORT_DATA_MAPPER.remove(storeID, getStockReport(storeID).getProductID());
        stockReports.remove(storeID);
    }

    public Set<SaleToCustomer> getSaleHistory() {
        Set<SaleToCustomer> result = category.getSaleHistory();
        for (SaleToCustomer sale : SALE_DATA_MAPPER.getSalesByProduct(id)) {
            if (sale.isPassed() || sale.isActive())
                result.add(sale);
        }
        return result;
    }

    public boolean isLow(int storeID) {
        StockReport stockReport = getStockReport(storeID);
        if (stockReport != null)
            return getStockReport(storeID).isLow();
        return false;
    }

    public void removeSale(SaleToCustomer sale) {
        sales.remove(sale);
    }

    public boolean belongsToCategory(Category category) {
        return (this.category==category || this.category.belongsToCategory(category));
    }

    public void changeProductMin(int store, int min) {
        StockReport stockReport = STOCK_REPORT_DATA_MAPPER.getProductStockReport(id, store);
        if (stockReport!=null)
            stockReports.put(store, stockReport);
        if (getStockReport(store)==null)
            throw new IllegalArgumentException("Product " + id + " is not being sold in store " + store + " and has no min amount");
        stockReports.get(store).changeMin(min);
    }

    public void changeProductTarget(int store, int target) {
        StockReport stockReport = STOCK_REPORT_DATA_MAPPER.getProductStockReport(id, store);
        if (stockReport!=null)
            stockReports.put(store, stockReport);
        if (getStockReport(store)==null)
            throw new IllegalArgumentException("Product " + id + " is not being sold in store " + store + " and has no target amount");
        stockReports.get(store).changeTarget(target);
    }

    public StockReport getStockReport(int store) {
        StockReport stockReport = STOCK_REPORT_DATA_MAPPER.get(store, id);//stockReports.get(store);
        if (stockReport==null) {
            stockReport = STOCK_REPORT_DATA_MAPPER.get(store, id);
        }
        return stockReport;
    }

    public int getAmountForOrder(int storeID) {
        StockReport stockReport = getStockReport(storeID);
        if (stockReport!=null && stockReport.isLow())
            return stockReport.getAmountForOrder();
        return -1;
    }

    public void addDelivery(int storeID, int amount) {
        getStockReport(storeID).addDelivery(amount);
    }

    public void delete() {
        removeLocation(1);
        DEFECTIVE_ITEMS_DATA_MAPPER.deleteByProduct(id);
        SALE_DATA_MAPPER.deleteByProduct(id);
    }
}
