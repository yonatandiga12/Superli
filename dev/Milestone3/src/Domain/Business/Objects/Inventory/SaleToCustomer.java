package Domain.Business.Objects.Inventory;

import Domain.DAL.Controllers.InventoryAndSuppliers.SalesDataMapper;

import java.time.LocalDate;
import java.util.List;

public class SaleToCustomer {

    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int percent;
    private List<Integer> categoryIDs;
    private List<Integer> productIDs;
    public static final SalesDataMapper SALES_DATA_MAPPER = new SalesDataMapper();

    public SaleToCustomer(int id, LocalDate startDate, LocalDate endDate, int percent, List<Integer> categoriesList, List<Integer> products) {
        if (percent>=100 || percent<=0)
            throw new IllegalArgumentException("Percent sale must be between 1 and 99. Received: " + percent);
        if (endDate.isBefore(startDate))
            throw new IllegalArgumentException("SaleToCustomer: Constructor: end date is before the start date");
        this.id = id;
        this.percent = percent;
        this.categoryIDs = categoriesList;
        this.productIDs = products;
        this.startDate = startDate;
        //add 1 day to the endDate in order to include the endDate's day in the sale.
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }
    public List<Integer> getCategories() {
        return categoryIDs;
    }
    public List<Integer> getProducts() {
        return productIDs;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public LocalDate getEndDate() {
        return endDate;
    }
    public int getPercent() {
        return percent;
    }

    public boolean isUpcoming() {
        return startDate.isAfter(LocalDate.now());
    }
    public boolean isPassed() {
        return endDate.isBefore(LocalDate.now());
    }
    public boolean isActive() {
        return !(isUpcoming() || isPassed());
    }
    public boolean wasActive(LocalDate dateBought) { return !(startDate.isAfter(dateBought) || endDate.isBefore(dateBought)); }

    public void setEndDate(LocalDate newEndDate) {
        endDate = newEndDate;
        SALES_DATA_MAPPER.updateEndDate(id, newEndDate);
    }
}
