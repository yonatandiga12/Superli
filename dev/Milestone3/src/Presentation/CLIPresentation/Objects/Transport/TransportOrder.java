package Presentation.CLIPresentation.Objects.Transport;

import java.util.HashMap;

public class TransportOrder {
    private int orderID;
    private int srcID;
    private int dstID;
    private HashMap<Integer, Integer> productList;

    public TransportOrder(int orderID, int srcID, int dstID, HashMap<Integer, Integer> productList) {
        this.orderID = orderID;
        this.srcID = srcID;
        this.dstID = dstID;
        this.productList = productList;
    }

    public TransportOrder(int srcID, int dstID) {
        this.srcID = srcID;
        this.dstID = dstID;
        productList = new HashMap<>();
    }

    public TransportOrder(Domain.Service.Objects.TransportOrder order) {
        this.orderID = order.getOrderID();
        this.srcID = order.getSrcID();
        this.dstID = order.getDstID();
        this.productList = order.getProducts();
    }

    public void canCloseOrder() throws Exception {
        if (productList.size() <= 0)
        {
            throw new Exception("The product does not exist in the order!");
        }
    }

    public void addProduct(int productSN, int countOfProduct) throws Exception {
        if(!productList.containsKey(productSN))
        {
            productList.put(productSN, countOfProduct);
        }
        else {
            throw new Exception("The product is already in the order!\n" +
                    "You can update the product quantity.");
        }
    }

    public void removeProduct(int productSN) throws Exception {
        if(productList.containsKey(productSN))
        {
            productList.remove(productSN);
        }
        else {
            throw new Exception("The product does not exist in the order!");
        }
    }

    public void updateProduct(int productSN, int countOfProduct) throws Exception {
        if(productList.containsKey(productSN))
        {
            productList.replace(productSN, countOfProduct);
        }
        else {
            throw new Exception("The product does not exist in the order!");
        }
    }

    public int getSrcID() {
        return srcID;
    }

    public void setSrcID(int srcID) {
        this.srcID = srcID;
    }

    public int getDstID() {
        return dstID;
    }

    public void setDstID(int dstID) {
        this.dstID = dstID;
    }

    public HashMap<Integer, Integer> getProductList() {
        return productList;
    }

    public void setProductList(HashMap<Integer, Integer> productList) {
        this.productList = productList;
    }
    private void displayProducts()
    {
        for(int product: productList.keySet())
        {
            displayProduct(product, productList.get(product));
        }
    }
    private void displayProduct(int productID, int quantity)
    {
        System.out.println("Product ID: " + productID + " Quantity: " + quantity);
    }
    public void display()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Order ID: " + orderID + "\nSource: " + srcID + "\nDestination: " + dstID);
        displayProducts();
    }
}
