package Domain.Business.Objects;

import Globals.Enums.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransportOrder {
    private static int incID = 0;
    private int ID;
    private int srcID;
    private int dstID;
    private HashMap<String, Integer> productList;
    private OrderStatus status;

    public TransportOrder(int src, int dst,HashMap<Integer, Integer> productList) {
        ID = incID++;
        this.srcID = src;
        this.dstID = dst;
        this.productList = convert(productList);
        status = OrderStatus.waiting;
    }

    public TransportOrder(int id,int src, int dst,OrderStatus status) {
        ID = id;
        incID++;
        this.srcID = src;
        this.dstID = dst;
        this.productList = new HashMap<>();
        this.status = status;
    }




    public int getID() {
        return ID;
    }

    public int getSrc() {
        return srcID;
    }

    public void setSrc(int src) {
        this.srcID = src;
    }

    public int getDst() {
        return dstID;
    }

    public void setDst(int dst) {
        this.dstID = dst;
    }

    public HashMap<String, Integer> getProductList() {
        return productList;
    }
    public HashMap<Integer, Integer> getProductHM() {
        HashMap<Integer, Integer> productHM = new HashMap<>();
        for(String sn: productList.keySet())
        {
            productHM.put(Integer.parseInt(sn), productList.get(sn));
        }
        return productHM;
    }
    public List<String> getProducts(){
        List<String> pro = new ArrayList<>();
        for (String s:productList.keySet()) {
            pro.add(s);
        }
        return pro;
    }

    public void setProductList(HashMap<String, Integer> productList) {
        this.productList = productList;
    }

    public OrderStatus getStatus(){return status;}
    public void order(){
        status = OrderStatus.ordered;

    }

    public HashMap<String,Integer> convert(HashMap<Integer,Integer> toConvert){
        HashMap<String,Integer> converted = new HashMap<>();
        for (Integer id:toConvert.keySet()) {
            converted.put(id.toString(),toConvert.get(id));
        }
        return converted;
    }

}
