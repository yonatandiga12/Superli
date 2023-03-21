package Domain.Service.Objects;

import java.util.HashMap;

public class TransportOrder {
    private int orderID;
    private int srcID;
    private int dstID;
    private HashMap<Integer, Integer> products;

    public TransportOrder(int orderID, int srcID, int dstID, HashMap<Integer, Integer> products) {
        this.orderID = orderID;
        this.srcID = srcID;
        this.dstID = dstID;
        this.products = products;
    }

    public TransportOrder(Domain.Business.Objects.TransportOrder to) {
        this.orderID = to.getID();
        this.srcID = to.getSrc();
        this.dstID = to.getDst();
        this.products = to.getProductHM();
    }

    public int getOrderID() {
        return orderID;
    }

    public int getSrcID() {
        return srcID;
    }

    public int getDstID() {
        return dstID;
    }

    public HashMap<Integer, Integer> getProducts() {
        return products;
    }
}
