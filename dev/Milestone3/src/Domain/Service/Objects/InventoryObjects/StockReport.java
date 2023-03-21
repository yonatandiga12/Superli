package Domain.Service.Objects.InventoryObjects;

public class StockReport {

    private final int storeID;
    private final int productID;
//    private final String productName;
    private final int amountInStore;
    private final int amountInWarehouse;
    private final int amountInDeliveries;
    private final int minAmountInStore;
    private final int targetAmountInStore;


    public StockReport(Domain.Business.Objects.Inventory.StockReport stockReport) {
        this.storeID = stockReport.getStoreID();
        this.productID = stockReport.getProductID();
//        this.productName = stockReport.getProductName();
        this.amountInStore = stockReport.getAmountInStore();
        this.amountInWarehouse = stockReport.getAmountInWarehouse();
        this.amountInDeliveries = stockReport.getAmountInDeliveries();
        this.minAmountInStore = stockReport.getMinAmountInStore();
        this.targetAmountInStore = stockReport.getTargetAmountInStore();
    }

    public int getStoreID() { return storeID; }
    public int getProductID() { return productID; }
    public int getAmountInStore() { return amountInStore; }
    public int getAmountInWarehouse() { return amountInWarehouse; }
    public int getAmountInDeliveries() { return amountInDeliveries; }
    public int getMinAmountInStore() { return minAmountInStore; }
    public int getTargetAmountInStore() { return targetAmountInStore; }

    @Override
    public String toString() {
        return "Store ID: " + getStoreID() + "\n| Product ID: " + getProductID() + "\n| Amount In Store: " + getAmountInStore() + "\n| Amount In Warehouse: " + getAmountInWarehouse() + "\n| Amount In Deliveries: " + getAmountInDeliveries() + "\n| Minimum Amount: " + getMinAmountInStore() + "\n| Target Amount: " + getTargetAmountInStore();
    }
}
