package Domain.Business.Objects.Supplier;

import Domain.DAL.Controllers.InventoryAndSuppliers.OrderDAO;
import Globals.Enums.OrderStatus;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class Order {



    private int id;
    private int supplierID;
    private LocalDate creationDate;
    private LocalDate arrivalTime;
    private ArrayList<OrderItem> orderItems;
    private int storeID;
    private static int globalID = 1;
    private OrderStatus status;


    private LocalDate calculateArrivalTime(LocalDate creationDate, int daysToArrival) {
        return creationDate.plusDays(daysToArrival);
    }

    private LocalDate getTodayDate() {
        return LocalDate.now();
    }

    public Order(int daysToArrival, int supplierID, int storeID){
        this.supplierID = supplierID;
        this.id = globalID;
        globalID++;

        //this.creationDate = Calendar.getInstance().getTime();
        this.creationDate = getTodayDate();
        this.orderItems = new ArrayList<>();
        this.storeID = storeID;

        //Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, daysToArrival);
        //arrivalTime = cal.getTime();

        arrivalTime = calculateArrivalTime(creationDate, daysToArrival);
        status = OrderStatus.waiting;
    }


    public Order(int daysToArrival, int supplierID, int storeID, OrderItem item){
        this.supplierID = supplierID;
        this.id = globalID;
        globalID++;
        //creationDate = new Date();
        this.creationDate = getTodayDate();
        this.orderItems = new ArrayList<>();
        this.storeID = storeID;

        //Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE, daysToArrival);
        //arrivalTime = cal.getTime();
        arrivalTime = calculateArrivalTime(creationDate, daysToArrival);

        orderItems.add(item);
        status = OrderStatus.waiting;
    }


    //For uploading from dal
    public Order(int id, int supplierId, LocalDate creationDate, LocalDate arrivalTime, int storeID, OrderStatus status){
        this.id = id;
        this.supplierID = supplierId;
        this.creationDate = creationDate;
        this.arrivalTime = arrivalTime;
        //globalID++;
        this.storeID = storeID;
        this.orderItems = new ArrayList<>();
        this.status = status;
    }


    //copy constructor, create new Id
    public Order(Order orderArriavalTimePassed) {
        this.id = globalID;
        this.supplierID = orderArriavalTimePassed.getSupplierId();
        this.creationDate = getTodayDate();
        this.orderItems = orderArriavalTimePassed.getOrderItems();
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 1);
//        arrivalTime = cal.getTime();
        arrivalTime = calculateArrivalTime(creationDate, 1);

        globalID++;
        this.storeID = orderArriavalTimePassed.getStoreID();
        this.status = OrderStatus.waiting;
    }

    public Order (Order order, OrderItem orderItem, int storeID){
        id = order.id;
        supplierID = order.supplierID;
        this.creationDate = getTodayDate();
        this.orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        arrivalTime = calculateArrivalTime(creationDate, 1);
        this.storeID = storeID;
        this.status = OrderStatus.waiting;

    }

    /*
    public Order(Order order, OrderItem orderItem) {
        this.id = globalID;
        globalID++;
        this.supplierID = order.getSupplierId();
        this.creationDate = getTodayDate();
        this.orderItems = new ArrayList<>();
        orderItems.add(orderItem);
        arrivalTime = calculateArrivalTime(creationDate, 1);
        this.storeID = order.getStoreID();
        this.status = OrderStatus.waiting;

    }
     */

    public Order(Order order, ArrayList<OrderItem> orderItems) {
        this.id = order.getId();
        this.supplierID = order.getSupplierId();
        this.creationDate = getTodayDate();
        this.orderItems = orderItems;
        arrivalTime = calculateArrivalTime(creationDate, 1);
        this.storeID = order.getStoreID();
        this.status = OrderStatus.waiting;

    }

    public static void setGlobalId(int i) {
        globalID = i;
    }


    public void addItem(int productId, int idBySupplier, String name, int quantity, float ppu, int discount, Double finalPrice, double weight, OrderDAO orderDAO) throws Exception {
        if(!changeable()){
            throw new Exception("This order can't be changed!");
        }
        OrderItem orderItem = new OrderItem(productId, idBySupplier,  name, quantity, ppu, discount, finalPrice, weight);
        orderDAO.addItem(id, orderItem);
        orderItems.add(orderItem);
    }

    public boolean itemExists(int itemId) {
        for(OrderItem orderItem : orderItems){
            if(orderItem.getProductId() == itemId)
                return true;
        }
        return false;

    }

    public void removeItem(int itemId, OrderDAO orderDAO) throws Exception {
        if(!changeable()){
            throw new Exception("This order can't be changed!");
        }

        for(OrderItem orderItem : orderItems){
            if(orderItem.getProductId() == itemId){
                orderDAO.removeOrderItem(id, orderItem.getProductId());
                orderItems.remove(orderItem);
                return;
            }
        }
    }

    public void updateItemQuantity(int itemId, int quantity, int discount, double finalPrice, OrderDAO orderDAO)throws Exception{
        if(!changeable()){
            throw new Exception("This order can't be changed!");
        }

        if(!itemExists(itemId)){
            throw new Exception("The requested item is not ordered!");
        }

        if(quantity == 0){
            throw new Exception("Unable to order 0 items, try to remove the item from the order instead.");
        }

        for(OrderItem item : orderItems){
            if(item.getProductId() == itemId){
                orderDAO.updateItemQuantity(id, item.getProductId(), quantity);
                orderDAO.updateItemDiscount(id, item.getProductId(), discount);
                orderDAO.updateItemFinalPrice(id, item.getProductId(), finalPrice);
                item.setQuantity(quantity);
                item.setDiscount(discount);
                item.setFinalPrice(finalPrice);
                return;
            }
        }
    }

    public OrderStatus getStatus() {
        return status;
    }


    public LocalDate getDate() {
        return creationDate;
    }


    public int getStoreID() { return storeID; } //WROTE BY AMIR
    public int getId() {
        return id;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public boolean changeable(){
        return arrivalTime.isAfter(LocalDate.now());
        //return arrivalTime.after(Calendar.getInstance().getTime());
    }

    public boolean passed(){
        return arrivalTime.isBefore(LocalDate.now());

//        return arrivalTime.before(Calendar.getInstance().getTime());
    }


    public int getSupplierId() {
        return supplierID;
    }

    public boolean hasEnoughItemQuantity(int productID, int amount) {
        OrderItem currOrderItem = getOrderItem(productID);
        if(currOrderItem != null)
            return currOrderItem.getQuantity() >= amount;
        return false;
    }

    private OrderItem getOrderItem(int productID) {
        for(OrderItem orderItem : orderItems) {
            if (orderItem.getProductId() == productID) {
                return orderItem;
            }
        }
        return null;
    }

    public int getDaysUntilOrder(LocalDate currDate) {
        Period period = Period.between(currDate, arrivalTime);
        if(currDate.isBefore(arrivalTime)){
            return period.getDays();
        }
        return -1;
        //long diff = arrivalTime.getTime() - currDate.getTime();
        //return (int) (diff / (1000*60*60*24));
    }

    public int getQuantityOfItem(int productId) {
        for(OrderItem orderItem : orderItems){
            if(orderItem.getProductId() == productId)
                return orderItem.getQuantity();
        }
        return 0;
    }

    public LocalDate getCreationTime() {
        return creationDate;
    }

    public LocalDate getArrivaltime() {
        return arrivalTime;
    }

    public void uploadItemsFromDB(ArrayList<OrderItem> items) {
        this.orderItems = items;
    }

    public String getStatusString() {
        if(status == OrderStatus.waiting)
            return "waiting";
        else if(status == OrderStatus.complete)
            return "complete";
        return "ordered";
    }


    public void order(){
        this.status = OrderStatus.ordered;
    }

    public void start(){
        this.status = OrderStatus.complete;
    }

    public double getOrderWeight(){
        double total = 0;
        for(OrderItem orderItem : orderItems){
            total += orderItem.getWeight()* orderItem.getQuantity();
        }
        return total;
    }

    public void setArrivalTime(LocalDate date) {
        arrivalTime = date;
    }

    public double getWeightOfItem(int itemID) throws Exception {
        for(OrderItem item : orderItems){
            if(item.getProductId() == itemID)
                return item.getWeight();
        }
        throw new Exception(String.format("No Item with Id %d in Order %d", itemID, id));
    }

    public boolean containsItem(int itemId) {
        for(OrderItem orderItem : orderItems){
            if(orderItem.getProductId() == itemId)
                return true;
        }
        return false;
    }

    public void setMissingAmountOfItem(int itemId, int missingAmount, OrderDAO orderDAO) throws Exception {
        OrderItem item = getOrderItem(itemId);
        if(item != null && item.getQuantity() < missingAmount)
            throw new Exception("Missing amount is bigger than the quantity of this Item!");
        item.setMissingItems(missingAmount);
        orderDAO.setOrderItemMissingAmount(itemId,missingAmount);
    }

    public void setDefectiveAmountOfItem(int itemId, int defectiveAmount, OrderDAO orderDAO) throws Exception {
        OrderItem item = getOrderItem(itemId);
        if(item != null && item.getQuantity() < defectiveAmount)
            throw new Exception("Defective amount is bigger than the quantity of this Item!");
        item.setDefectiveItems(defectiveAmount);
        orderDAO.setOrderItemDefectiveAmount(itemId, defectiveAmount);
    }

    public void setDescriptionOfItem(int itemId, String description, OrderDAO orderDAO) throws Exception {
        OrderItem item = getOrderItem(itemId);
        item.setDescription(description);
        orderDAO.setOrderItemDescription(itemId, description);
    }
    public List<String> getProductList(){
        List<String> products = new ArrayList<>();
        for(OrderItem o : this.orderItems){
            products.add(o.getName());
        }
        return products;
    }

}
