package Domain.Service.Objects.InventoryObjects;

import Domain.Business.Objects.Inventory.SaleToCustomer;

import java.time.LocalDate;
import java.util.List;

public class Sale {
    private final int id;
    private final int percent;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final List<Integer> categories;
    private final List<Integer> products;
    public Sale(SaleToCustomer s) {
        this.id = s.getId();
        this.percent = s.getPercent();
        this.startDate = s.getStartDate();
        this.endDate = s.getEndDate();
        this.categories = s.getCategories();
        this.products = s.getProducts();
    }

    public int getSaleID() { return id; }
    public int getPercent() { return percent; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public List<Integer> getCategories() { return categories; }
    public List<Integer> getProducts() { return products; }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", percent=" + percent +
                ", start date=" + startDate +
                ", end date=" + endDate +
                ", categories: " + categories.toString() +
                ", products: " + products.toString() +
                '}';
    }
}
