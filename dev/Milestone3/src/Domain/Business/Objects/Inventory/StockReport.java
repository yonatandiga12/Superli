package Domain.Business.Objects.Inventory;

import Domain.DAL.Controllers.InventoryAndSuppliers.StockReportDataMapper;

public class StockReport {

    private int storeID;
    private int productID;
    private int amountInStore;
    private int amountInWarehouse;
    private int amountInDeliveries;
    private int minAmountInStore;
    private int targetAmountInStore;
    public final static StockReportDataMapper dataMapper = new StockReportDataMapper();

    public StockReport(int storeID, int productID, int amountInStore, int amountInWarehouse, int minAmountInStore, int targetAmountInStore, int amountInDelivery) {
        this.storeID = storeID;
        this.productID = productID;
        this.amountInStore = amountInStore;
        this.amountInWarehouse = amountInWarehouse;
        this.amountInDeliveries = amountInDelivery;
        this.minAmountInStore = minAmountInStore;
        this.targetAmountInStore = targetAmountInStore;
    }

    public int getStoreID() {
        return storeID;
    }
    public int getProductID() {
        return productID;
    }
    public int getAmountInStore() { return amountInStore; }
    public int getAmountInWarehouse() { return amountInWarehouse; }
    public int getMinAmountInStore() {
        return minAmountInStore;
    }
    public int getTargetAmountInStore() { return targetAmountInStore; }
    public int getAmountInDeliveries() { return amountInDeliveries; }

    public void removeItemsFromStore(int amount, boolean inWarehouse) {
        if ((!inWarehouse && amountInStore-amount<0) || (inWarehouse && amountInWarehouse-amount<0))
            throw new IllegalArgumentException("Can not buy or remove more items than in stock - please check amount");
        if (inWarehouse) {
            amountInWarehouse -= amount;
            dataMapper.updateInWarehouse(productID, storeID, amountInWarehouse);
        }
        else {
            amountInStore -= amount;
            dataMapper.updateInStore(productID, storeID, amountInStore);
        }
    }

    public void moveItems(int amount) {
        if (amountInWarehouse-amount<0)
            throw new IllegalArgumentException("Can not move more items than in the warehouse");
        amountInWarehouse-=amount;
        amountInStore+=amount;
        dataMapper.updateInWarehouse(productID, storeID, amountInWarehouse);
        dataMapper.updateInStore(productID, storeID, amountInStore);
    }

    public void addItems(int amount, int missingAndDefectiveItems, String description) {
        amountInWarehouse+=amount-missingAndDefectiveItems;
        dataMapper.updateInWarehouse(productID, storeID, amountInWarehouse);
        amountInDeliveries-=amount;
        dataMapper.updateInDelivery(productID, storeID, amountInDeliveries);
    }

    public void addDelivery(int amount) {
        amountInDeliveries+=amount;
        dataMapper.updateInDelivery(productID, storeID, amountInDeliveries);
    }

    public void returnItems(int amount) {
        amountInStore+=amount;
        dataMapper.updateInStore(productID, storeID, amountInStore);
    }

    public void changeMin(int min) {
        if (min<1)
            throw new IllegalArgumentException("New min value must be positive");
        if (min > targetAmountInStore)
            throw new IllegalArgumentException("New min cannot be greater than target. target is currently " + targetAmountInStore);
        minAmountInStore = min;
        dataMapper.updateMin(productID, storeID, minAmountInStore);
    }

    public void changeTarget(int target) {
        if (target<1)
            throw new IllegalArgumentException("New target value must be positive");
        if (target < minAmountInStore)
            throw new IllegalArgumentException("New target cannot be less than min. Min is currently " + minAmountInStore);
        targetAmountInStore = target;
        dataMapper.updateTarget(productID, storeID, targetAmountInStore);
    }

    public boolean isLow() { return minAmountInStore > getTotalAmount(); }
    public int getAmountForOrder() {
        return targetAmountInStore - getTotalAmount();
    }

    private int getTotalAmountInDeliveries() {
        return amountInDeliveries;
    }
    public int getTotalAmount() { return getTotalAmountInDeliveries()+amountInStore+amountInWarehouse;}

    //for tests only!
    public void setInDelivery(int i) {
        amountInDeliveries=i;
    }

    public void channgeInDelivery(int amount) {
        amountInDeliveries+=amount;
        dataMapper.updateInDelivery(productID, storeID, amountInDeliveries);
    }
}
