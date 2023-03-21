package Domain.Business.Objects.Supplier;

import Domain.DAL.Controllers.InventoryAndSuppliers.ProductDataMapper;
import Globals.NotNull;

import java.util.Map;

public class AgreementItem {

    //fields
    private int productId;
    private int idBySupplier;
    private String name;
    private String manufacturer;
    private float pricePerUnit;
    private double weight;
    private Map<Integer, Integer> bulkPrices; // <quantity, percent>

    ProductDataMapper productDataMapper;

    /*
          getBulkMap(instanceResult.getInt(SUPPLIER_ID_COLUMN),
          instanceResult.getInt(PRODUCT_ID_COLUMN)));
     */

    public AgreementItem(int _productId, int _idBySupplier, String _manu, float _price, Map<Integer, Integer> _bulkPrices) throws Exception {
        productDataMapper = new ProductDataMapper();
        productId = _productId;
        idBySupplier = _idBySupplier;
        manufacturer = _manu;
        pricePerUnit = _price;
        bulkPrices = _bulkPrices;
        name = getName();
        weight = getWeight();
    }

    public int getProductId(){
        return productId;
    }

    public int getIdBySupplier(){ return idBySupplier;}



    public String getManufacturer() {
        return manufacturer;
    }

    public float getPricePerUnit(){
        return pricePerUnit;
    }

    public Map<Integer, Integer> getBulkPrices(){
        return bulkPrices;
    }

    public void setProductId(int _id){
        productId = _id;
    }

    public void setIdBySupplier(int _id){
        idBySupplier = _id;
    }


    /*
    public void setName(String newName){
        name = newName;
    }
     */

    // In case the manufacturer changed its name for some reason
    public void setManufacturer(String newManufacturer){
        manufacturer = newManufacturer;
    }

    public void setPrice(float newPrice){
        pricePerUnit = newPrice;
    }


    public void setBulkPrices(Map<Integer, Integer> _bulk) throws Exception {
        NotNull.Check(_bulk);

        bulkPrices = _bulk;
    }

    public void addBulkPrice(int quantity, int discount) throws Exception {
        if(bulkPrices.containsKey(quantity)){
            throw new Exception("This quantity already exists!");
        }

        bulkPrices.put(quantity, discount);
    }

    public void removeBulkPrice(int quantity) throws Exception {
        if(!bulkPrices.containsKey(quantity)){
            throw new Exception("This quantity does not exits in the agreement!");
        }

        bulkPrices.remove(quantity);
    }

    public void editBulkPrice(int quantity, int discount) throws Exception {
        removeBulkPrice(quantity);
        addBulkPrice(quantity, discount);
    }

    public double calculateTotalPrice(int quantity){
        int closerQuantity = 0;
        double finalPrice;

        for(int q : bulkPrices.keySet()){
            if(q <= quantity && q > closerQuantity){
                closerQuantity = q;
            }
        }

        finalPrice = quantity*pricePerUnit;

        if(closerQuantity != 0){
            finalPrice *= (1-((double)bulkPrices.get(closerQuantity)/100));
        }

        return finalPrice;
    }

    //Format : " productId ,idBySupplier,  name , manufacturer , pricePerUnit , weight , quantity ,  percent , quantity , percent ..."
    //old format " id , name , manufacturer , pricePerUnit , quantity1 , percent1 , quantity2 , percent2 ...  "
    public String getInfoInStringFormat() throws Exception {
        String result = "";
        result += String.valueOf(productId) + ",";
        result += String.valueOf(idBySupplier) + ",";
        result += getName() + ",";
        result += manufacturer + ",";
        result += String.valueOf(pricePerUnit) + ",";
        result += String.valueOf(getWeight());
        for( Map.Entry<Integer, Integer> curr : bulkPrices.entrySet()){
            result += ",";
            result += String.valueOf(curr.getKey()) + ",";
            result += String.valueOf(curr.getValue());
        }
        return result;
    }


    //Format : " productId ,idBySupplier,  name , manufacturer , pricePerUnit , quantity , weight,  percent , quantity , percent ..."
    //Old Format : " id , name , manufacturer , pricePerUnit , quantity , percent , quantity , percent ..."
    public String toString(){
        try {
            if (bulkPrices == null || bulkPrices.isEmpty()) {
                return "" + productId + ", " + getName() + ", " + manufacturer + ", " + pricePerUnit + ", " + getWeight() + " [NO BULK PRICES]";
            } else {
                return "" + productId + ", " + getName() + ", " + manufacturer + ", " + pricePerUnit + ", " + getWeight() + "," + printBulkMap();
            }
        }catch (Exception e){

        }
        return "";
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

    public int getDiscount(int quantity) {
        int closerQuantity = 0;

        for(int q : bulkPrices.keySet()){
            if(q <= quantity && q > closerQuantity){
                closerQuantity = q;
            }
        }

        if(closerQuantity > 0){
            return bulkPrices.get(closerQuantity);
        }

        return 0;
    }


    public String getName() throws Exception {
        //return "name1InComment";
        return  productDataMapper.get(String.valueOf(productId)).getName();
    }



    public double getWeight() throws Exception {
        //return 1;
        return productDataMapper.get(String.valueOf(productId)).getWeight();

    }


}
