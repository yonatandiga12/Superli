package Domain.Service.Objects.SupplierObjects;

import java.util.HashMap;
import java.util.Map;

public class ServiceItemObject {

    //fields
    private int productId;
    private int idBySupplier;
    private String name;
    private String manufacturer;
    private float pricePerUnit;
    private double weight;
    private Map<Integer, Integer> bulkPrices; // <quantity, percent>


    public ServiceItemObject(int _productId, int _idBySupplier, String _name, String _manu, float _price,double _weight, Map<Integer, Integer> _bulkPrices){
        productId = _productId;
        idBySupplier = _idBySupplier;
        name = _name;
        manufacturer = _manu;
        pricePerUnit = _price;
        weight = _weight;
        bulkPrices = _bulkPrices;
    }

    //Format : " productId ,idBySupplier,  name , manufacturer , pricePerUnit , weight, quantity,  percent , quantity , percent ..."
    public ServiceItemObject(String s){
        String[] fields = s.replaceAll("\\s+","").split(",");

        productId = Integer.parseInt(fields[0]);
        idBySupplier = Integer.parseInt(fields[1]);
        name = fields[2];
        manufacturer = fields[3];
        pricePerUnit = Float.parseFloat(fields[4]);
        weight = Double.parseDouble(fields[5]);
        bulkPrices = new HashMap<>();

        for(int i=6; i<fields.length; i++){
            bulkPrices.put(Integer.parseInt(fields[i]), Integer.parseInt(fields[i+1]));
            i++;
        }
    }

    public int getId(){
        return idBySupplier;
    }

    public String getName(){
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public float getPricePerUnit(){
        return pricePerUnit;
    }

    public Map<Integer, Integer> getBulkPrices(){
        return bulkPrices;
    }

    // New Format : " productId ,idBySupplier,  name , manufacturer , pricePerUnit , quantity , weight,  percent , quantity , percent ..."
    public String toString(){
        if(bulkPrices.isEmpty()){
            return "\n" + "Product ID: " + productId + " , ID by Supplier: " + idBySupplier  +" ,Name: " + name + " ,Manufacturer: " + manufacturer + " ,PricePerUnit: " + pricePerUnit + " ,Weight:" + weight + ", [NO BULK PRICES]";
        }
        else{
            return "\n" + "Product ID: " + productId + " , ID by Supplier: " + idBySupplier  +" ,Name: " + name + " ,Manufacturer: " + manufacturer + " ,PricePerUnit: " + pricePerUnit + " ,Weight:" + weight+ ", " + printBulkMap();
        }
    }

    private String printBulkMap(){
        String toReturn = "";

        for(Integer key : bulkPrices.keySet()){
            toReturn += ("quantity: " + key + ", " + " discount in percent: " + bulkPrices.get(key) + ", ");
        }

        if(toReturn.length() == 0)
            return toReturn;
        return toReturn.substring(0, toReturn.length()-2);
    }

    public void setId(int newID){
        idBySupplier = newID;
    }
}
