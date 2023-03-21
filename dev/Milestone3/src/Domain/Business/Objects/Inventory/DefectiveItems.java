package Domain.Business.Objects.Inventory;

import Domain.DAL.Controllers.InventoryAndSuppliers.DefectiveItemsDataMapper;
import Globals.Defect;

import java.time.LocalDate;

public class DefectiveItems {
    private int id;
    private LocalDate date;
    private int storeID;
    private int productID;
    private int amount;
    private int employeeID;
    private boolean inWarehouse;
    private String description;
    private Defect defect;
    public static final DefectiveItemsDataMapper DEFECTIVE_ITEMS_DATA_MAPPER = new DefectiveItemsDataMapper();

    public DefectiveItems(int id, Defect defect, LocalDate date, int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse) {
        this.id = id;
        this.date = date;
        this.storeID = storeID;
        this.productID = productID;
        this.amount = amount;
        this.employeeID = employeeID;
        this.inWarehouse = inWarehouse;
        this.description = description;
        this.defect = defect;
    }

    public int getId() {
        return id;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getStoreID() {
        return storeID;
    }
    public int getProductID() {
        return productID;
    }
    public int getAmount() {
        return amount;
    }
    public int getEmployeeID() {
        return employeeID;
    }
    public boolean getInWarehouse() { return inWarehouse; }
    public String getDescription() {
        return description;
    }
    public Defect getDefect() {
        return defect;
    }

    public boolean inDates(LocalDate startDate, LocalDate endDate) {
        return (!(startDate.isAfter(date) || endDate.isBefore(date)));
    }
}
