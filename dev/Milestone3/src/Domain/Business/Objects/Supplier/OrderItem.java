package Domain.Business.Objects.Supplier;

import java.util.ArrayList;
import java.util.List;

public class OrderItem {

    private int productId;
    private int idBySupplier;
    private String name;
    private int quantity;
    private float ppu;
    private int discount;
    private Double finalPrice;
    private int missingItems = 0;
    private int defectiveItems = 0;
    private String description = "";
    private double weight;


    public OrderItem(int id,int idBySupplier, String name, int quantity, float ppu, int discount, Double finalPrice, double weight) {
        this.productId = id;
        this.idBySupplier = idBySupplier;
        this.name = name;
        this.quantity = quantity;
        this.ppu = ppu;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public int getDefectiveItems() {
        return defectiveItems;
    }

    public String getDescription() {
        return description;
    }

    public int getMissingItems() {
        return missingItems;
    }

    public void setDefectiveItems(int defectiveItems) {
        this.defectiveItems = defectiveItems;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMissingItems(int missingItems) {
        this.missingItems = missingItems;
    }

    public String getName() {
        return name;
    }

    public int getProductId() {
        return productId;
    }

    public int getIdBySupplier() {
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

    public List<String> getStringInfo() {
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(productId));
        result.add(name);
        result.add(String.valueOf(quantity));
        result.add(String.valueOf(ppu));
        result.add(String.valueOf(discount));
        result.add(String.valueOf(finalPrice));
        result.add(String.valueOf(missingItems));
        result.add(String.valueOf(defectiveItems));
        result.add(description);
        result.add(String.valueOf(weight));
        result.add(String.valueOf(idBySupplier));
        return result;
    }

    public void setQuantity(int q){
        quantity = q;
    }

    public void setPricePerUnit(float p){
        ppu = p;
    }

    public void setDiscount(int d){
        discount = d;
    }

    public void setFinalPrice(double fp){
        finalPrice = fp;
    }

}
