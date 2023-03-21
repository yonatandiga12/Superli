package Domain.Service.Objects.InventoryObjects;

public class Product {
    private final int id;
    private final String name;
    private final int categoryID;
    private final double originalPrice;
    private final double currentPrice;
    private final double weight;
    private final String manufacturer;
    public Product(Domain.Business.Objects.Inventory.Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.categoryID = p.getCategoryID();
        this.originalPrice = p.getOriginalPrice();
        this.currentPrice = p.getCurrentPrice();
        this.weight = p.getWeight();
        this.manufacturer = p.getManufacturer();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getWeight() {
        if (weight>0)
            return String.valueOf(weight);
        return "N/A";
    }
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryID=" + categoryID +
                ", originalPrice=" + String.format("%.2f", originalPrice) +
                ", currentPrice=" + String.format("%.2f", currentPrice) +
                ", weight=" + getWeight() +
                ", manufacturer=" + manufacturer +
                '}';
    }
}
