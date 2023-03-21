package Domain.Service.Objects.SupplierObjects;

public class ServiceOrderItemObject {

    private int productId;
    private int idBySupplier;
    private String name;
    private int quantity;
    private float ppu;
    private int discount;
    private Double finalPrice;
    private int missing;
    private int defective;
    private String description;
    private double weight;


    public ServiceOrderItemObject(int productId, int idBySupplier,  String name, int quantity, float ppu, int discount, Double finalPrice, int missing, int defective, String description, double weight) {
        this.productId = productId;
        this.idBySupplier = idBySupplier;
        this.name = name;
        this.quantity = quantity;
        this.ppu = ppu;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.missing = missing;
        this.defective = defective;
        this.description = description;
        this.weight = weight;
    }


    public String getName() {
        return name;
    }

    public int getId() {
        return productId;
    }

    public int supplierProductID() {
        return idBySupplier;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public float getPricePerUnit() {
        return ppu;
    }

    public int getDiscount() {
        return discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    public double getWeight() {
        return weight;
    }

    public int getDefective() {
        return defective;
    }

    public int getMissing() {
        return missing;
    }

    public String toString(){
        return "Item Id: " + productId + ", Name:" + name + ", Id by Supplier:" + idBySupplier + ", Quantity: " + quantity + ", Price Per Unit: " + ppu + ", Discount: " + discount
                + ", Final Price: " + finalPrice + "\nWeight: " + weight  + ", Missing items: " + missing + ", Defective items: " + defective + ", Description: " + description + "\n";
    }

    public String toStringDiscount(float originalPrice) {
        return "ID at Inventory: " + productId + ", Name:" + name + ", Id by Supplier:" + idBySupplier +  "\nQuantity: " + quantity + ", Price Per Unit: " + ppu + "\nDiscount: " + discount
                + "\nFinal Price: " + finalPrice + ", Original Price: " + originalPrice;
    }
}
