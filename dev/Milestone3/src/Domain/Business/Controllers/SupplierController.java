package Domain.Business.Controllers;

import Domain.Business.Controllers.Transport.TransportController;
import Domain.Business.Objects.Supplier.*;
import Domain.DAL.Controllers.InventoryAndSuppliers.OrderDAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.SuppliersDAO;
import Globals.Enums.OrderStatus;
import Globals.Pair;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SupplierController {

    public SuppliersDAO suppliersDAO;
    private OrderDAO orderDAO;
    private InventoryController inventoryController;
    private TransportController transportController;


    public SupplierController(){
        this.inventoryController = null;
        suppliersDAO = new SuppliersDAO();
        orderDAO = new OrderDAO();
        // TODO: 10/06/2022 SHould get the transportController in the constructor
        transportController = new TransportController();

    }

    public void setInventoryController(InventoryController invCont){
        inventoryController = invCont;
    }


    public void loadSuppliersData(){
        try {
            int largestId = suppliersDAO.loadAllSuppliersInfo();
            Supplier.setGlobalId(largestId + 1);
            int largestOrderId = orderDAO.getGlobalId();
            Order.setGlobalId(largestOrderId + 1);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public int addSupplier(String name, int bankNumber, String address, String payingAgreement, ArrayList<Pair<String,String>> contactPairs, ArrayList<String> manufacturers) throws Exception {
        ArrayList<Contact> contacts = createContacts(contactPairs);
        Supplier supplier = new Supplier(name, bankNumber, address, payingAgreement, contacts, manufacturers, suppliersDAO);

        suppliersDAO.save(String.valueOf(supplier.getId()), supplier);
        return supplier.getId();
    }

    private ArrayList<Contact> createContacts(ArrayList<Pair<String, String>> contactPairs) throws Exception {
        ArrayList<Contact> contacts = new ArrayList<>();
        for(Pair<String,String> curr : contactPairs){
            if(!validPhoneNumber(curr.getRight()))
                throw new Exception("Invalid phone number!");
            contacts.add(new Contact( curr.getLeft(), curr.getRight()));
        }
        return contacts;
    }


    public void removeSupplier(int id) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");
        orderDAO.removeSupplierOrders(id);
        suppliersDAO.removeSupplier(id);
    }

    public void updateSupplierAddress(int id, String address) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.updateSupplierAddress(id, address);
        suppliersDAO.getSupplier(id).updateAddress(address);


    }

    public void updateSupplierBankNumber(int id, int bankNumber) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.updateSupplierBankNumber(id, bankNumber);
        suppliersDAO.getSupplier(id).updateBankNumber(bankNumber);
    }

    public void updateSupplierName(int id, String newName) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.updateSupplierName(id, newName);
        suppliersDAO.getSupplier(id).updateName(newName);

    }

    public void addSupplierContact(int id, String contactName, String contactPhone) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");
        if(!validPhoneNumber(contactPhone))
            throw new Exception("Phone number is Illegal");
        Contact contact = new Contact(contactName, contactPhone);

        suppliersDAO.addSupplierContact(id, contact);
        suppliersDAO.getSupplier(id).addContact(contact);
    }

    public void updateSupplierPayingAgreement(int id, String payingAgreement) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.updateSupplierPayingAgreement( id, payingAgreement);
        suppliersDAO.getSupplier(id).updatePayingAgreement(payingAgreement);
    }

    public void addSupplierManufacturer(int id, String manufacturer) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.addSupplierManufacturer(id, manufacturer);
        suppliersDAO.getSupplier(id).addManufacturer(manufacturer);
    }

    public void addAgreement(int supplierId, int agreementType, String agreementDays) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.getSupplier(supplierId).addAgreement(agreementType, agreementDays, suppliersDAO);
    }


    public void updateAgreementType(int supplierId,  int agreementType, String agreementDays) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.getSupplier(supplierId).updateAgreementType(agreementType, agreementDays);
        suppliersDAO.updateAgreementType(supplierId, agreementType);
        List<Integer> days = suppliersDAO.getSupplier(supplierId).getAgreementDays();
        suppliersDAO.getAgreementController().removeSupplierForChangingAgreement(supplierId);
        suppliersDAO.getAgreementController().updateAgreementDays(supplierId, days, agreementType);
    }


    public void addItemsToAgreement(int supplierId, List<String> itemsString) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.getSupplier(supplierId).addAgreementItems(itemsString, suppliersDAO);

    }


    //SHOULD BE PRIVATE, public for testing
    public boolean supplierExist(int id){
        return suppliersDAO.supplierExist(id);
    }


    //SHOULD BE PRIVATE, public for testing
    public boolean validPhoneNumber(String phoneNumber){
        for(int i = 0; i < phoneNumber.length(); i++){
            if(Character.isLetter(phoneNumber.charAt(i)))
                return false;
        }
        if(phoneNumber.length() < 8 || phoneNumber.length() > 13)
            return false;
        return true;
    }


    public List<String> itemsFromOneSupplier(int id) throws Exception {
        if(!supplierExist(id))
            throw new Exception("There is no supplier with this ID!");
        return suppliersDAO.getSupplier(id).getAgreementItems();
    }

    public void updateBulkPriceForItem(int supplierId, int itemID, Map<Integer, Integer> newBulkPrices) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        suppliersDAO.getSupplier(supplierId).updateBulkPriceForItem(itemID, newBulkPrices, suppliersDAO.getAgreementController());
    }

    public void updatePricePerUnitForItem(int supplierId, int itemID, float newPrice) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        suppliersDAO.getSupplier(supplierId).updatePricePerUnitForItem(itemID, newPrice, suppliersDAO.getAgreementItemDAO());
    }

    public void updateItemId(int supplierId, int olditemId, int newItemId) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        suppliersDAO.getSupplier(supplierId).updateItemId(olditemId, newItemId, suppliersDAO.getAgreementItemDAO());
    }

    /*
    public void updateItemName(int supplierId, int itemId, String newName) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        suppliersDAO.getSupplier(supplierId).updateItemName(itemId, newName, suppliersDAO.getAgreementItemDAO());
    }
     */



    public void updateItemManufacturer(int supplierId, int itemId, String manufacturer) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        suppliersDAO.getSupplier(supplierId).updateItemManufacturer(itemId, manufacturer, suppliersDAO);
    }



    public void addItemToAgreement(int supplierId, int itemId, int idBySupplier, String itemManu, float itemPrice, Map<Integer, Integer> bulkPrices) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");

        suppliersDAO.getSupplier(supplierId).addItem(itemId, idBySupplier, itemManu, itemPrice, bulkPrices, suppliersDAO);
    }

    public void deleteItemFromAgreement(int supplierId, int itemId) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        suppliersDAO.getSupplier(supplierId).deleteItem(itemId, suppliersDAO);
    }

    public boolean isTransporting(int supplierId) throws Exception {
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");

        return suppliersDAO.getSupplier(supplierId).isTransporting();
    }



    public List<Integer> getDaysOfDelivery(int supplierId) throws Exception{
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        return suppliersDAO.getSupplier(supplierId).getDaysOfDelivery();
    }

    public int getDeliveryDays(int supplierId) throws Exception{
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        return suppliersDAO.getSupplier(supplierId).getDeliveryDays();
    }

    public int daysToDelivery(int supplierId) throws Exception {
        if(!supplierExist(supplierId)){
            throw new Exception("There is no supplier with this ID!");
        }
        return suppliersDAO.getSupplier(supplierId).daysToDelivery();

    }

    public ArrayList<String> getSupplierInfo(int supplierId) throws Exception{
        if(!supplierExist(supplierId))
            throw new Exception("There is no supplier with this ID!");
        return suppliersDAO.getSupplier(supplierId).getSupplierInfo();
    }

    public boolean isRoutineAgreement(int supplierId) throws Exception {
        if(!supplierExist(supplierId)){
            throw new Exception("There is no supplier with this ID!");
        }
        return suppliersDAO.getSupplier(supplierId).isRoutineAgreement();
    }

    public boolean isByOrderAgreement(int supplierId) throws Exception {
        if(!supplierExist(supplierId)){
            throw new Exception("There is no supplier with this ID!");
        }
        return suppliersDAO.getSupplier(supplierId).isByOrderAgreement();
    }

    public boolean isNotTransportingAgreement(int supplierId) throws Exception {
        if(!supplierExist(supplierId)){
            throw new Exception("There is no supplier with this ID!");
        }
        return suppliersDAO.getSupplier(supplierId).isNotTransportingAgreement();
    }

    public void setDaysOfDelivery(int supplierID, String days) throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }

        suppliersDAO.getSupplier(supplierID).setDaysOfDelivery(days, suppliersDAO.getAgreementController());
    }

    public void addDaysOfDelivery(int supplierID, String days) throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }
        else{
            suppliersDAO.getSupplier(supplierID).addDaysOfDelivery(days, suppliersDAO.getAgreementController());
        }

    }

    public void removeDayOfDelivery(int supplierID, int day) throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }
        else{
            suppliersDAO.getSupplier(supplierID).removeDayOfDelivery(day);
            suppliersDAO.getAgreementController().removeDayOfDelivery(supplierID,day);
        }
    }


    public String getItem(int supplierID, int itemId) throws Exception {
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }

        return suppliersDAO.getSupplier(supplierID).getItem(itemId).getInfoInStringFormat();
    }

    public void editBulkPrice(int supplierID, int itemId, int quantity, int discount)throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }

        suppliersDAO.getAgreementItemDAO().updateBulkPrice(supplierID, itemId, quantity, discount);
        suppliersDAO.getSupplier(supplierID).getItem(itemId).editBulkPrice(quantity, discount);
    }

    public void addBulkPrice(int supplierID, int itemId, int quantity, int discount)throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }
        suppliersDAO.getAgreementItemDAO().addBulkPrice(supplierID, itemId, quantity, discount);
        suppliersDAO.getSupplier(supplierID).getItem(itemId).addBulkPrice(quantity, discount);

    }

    public void removeBulkPrice(int supplierID, int itemId, int quantity)throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }
        suppliersDAO.getAgreementItemDAO().removeBulkPrice(supplierID, itemId, quantity);
        suppliersDAO.getSupplier(supplierID).getItem(itemId).removeBulkPrice(quantity);
    }

    public double calculatePriceForItemOrder(int supplierID, int itemId, int quantity) throws Exception {
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }

        return suppliersDAO.getSupplier(supplierID).getItem(itemId).calculateTotalPrice(quantity);
    }

    public void changeDaysUntilDelivery(int supplierID, int days)throws Exception{
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }

        if(suppliersDAO.getSupplier(supplierID).isByOrderAgreement()){
            suppliersDAO.getSupplier(supplierID).setDaysUntilDelivery(days, suppliersDAO.getAgreementController());
        }
        else{
            throw new Exception("This supplier does not have a BY-ORDER-TRANSPORT agreement.");
        }
    }

    public List<String> getAllContact(int supID)throws Exception{
        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }

        List<String> contacts = new LinkedList<>();


        List<Contact> list;
        list = suppliersDAO.getSupplier(supID).getAllContact();
        if(list == null || list.size() == 0){
            list = suppliersDAO.getAllSupplierContacts(supID);
        }
        for(Contact c : list){
            contacts.add(c.toString());
        }
        return contacts;
    }

    public void removeContact(int supID, String name) throws Exception {
        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }

        suppliersDAO.getSupplier(supID).removeContact(name, suppliersDAO);

    }

    public List<String> getManufacturers(int supID) throws Exception {
        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }

        List<String> manufacturers = suppliersDAO.getSupplier(supID).getManufacturers();
        if(manufacturers == null || manufacturers.size() == 0){
            manufacturers = suppliersDAO.getAllSupplierManufacturers(supID);
        }
        return manufacturers;
    }

    public void removeManufacturer(int supID, String name) throws Exception {
        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }

        suppliersDAO.getSupplier(supID).removeManufacturer(name, suppliersDAO);
    }

    public boolean isSuppliersEmpty(){
        return suppliersDAO.isEmpty();
    }

    public boolean hasAgreement(int supID) throws Exception {
        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }

        return suppliersDAO.hasAgreement(supID);
    }



    /*
    public void addItemsToOrder(int supId, int orderId, List<String> itemsString) throws Exception {
        if(!supplierExist(supId)){
            throw new Exception("The supplier does not exists!");
        }
        for(int i = 0; i < itemsString.size(); i+=2 ){
            if(itemsString.size() <= i+2)
               throw new Exception("Some information is missing!");
            suppliersDAO.getSupplier(supId).addOneItemToOrder(orderId , Integer.parseInt(itemsString.get(i)),Integer.parseInt(itemsString.get(i+1)), orderDAO);
        }
        //suppliers.get(supId).addItemsToOrder(orderId, itemsString);
    }
     */


    public int addNewOrder(int supId, int storeId) throws Exception {
        if(!supplierExist(supId)){
            throw new Exception("The supplier does not exists!");
        }
        if(!inventoryController.getStoreIDs().contains(storeId)){
            throw new Exception("The store does not exists!");
        }
        Order order = suppliersDAO.getSupplier(supId).addNewOrder(storeId, orderDAO, suppliersDAO.getAgreementController());
        return order.getId();
    }

    // TODO: SR73
    public void addItemToOrder(int supId, int orderId, int itemId, int itemQuantity) throws Exception {
        double weight = 0;

        if(!supplierExist(supId)){
            throw new Exception("The supplier does not exists!");
        }

        Order order = suppliersDAO.getSupplier(supId).getOrderFromList(orderId, orderDAO);

        if(order.getStatus() == OrderStatus.waiting){
            // order is a new Order, or it has no transport, can be changed either way
            suppliersDAO.getSupplier(supId).addOneItemToOrder(orderId, itemId, itemQuantity, orderDAO);
            addOrderToTransport(order);
            // POSSIBLE BUG: if order has no transport but was sent earlier, what happens?
            // POSSIBLE FIX: add a new field to the order that indicates it was sent to transport, if so don't send it again.
            // NOTE: according to TRANSPORT TEAM this process is fine.
        }
        else{
            // order has a transport
            weight = suppliersDAO.getSupplier(supId).getAgreementItem(itemId).getWeight();

            if(checkWeightLegal(supId, orderId, itemId, itemQuantity, weight)){
                suppliersDAO.getSupplier(supId).addOneItemToOrder(orderId, itemId, itemQuantity, orderDAO);
                transportController.changeWeight(orderId, (int)(itemQuantity * weight));
            }
            else{
                throw new Exception("Can't update this order due to weight limit.\nYou can try to order manually.");
            }
        }

        callInventoryToUpdateOnTheWay(itemId, order.getStoreID(), itemQuantity);
    }

    // TODO: SR73
    public boolean removeOrder(int orderId) throws Exception {
        int supId = getSupplierWithOrder(orderId);

        if(supId == -1){
            throw new Exception("No Supplier with this order!");
        }

        Order order = orderDAO.getOrder(orderId, suppliersDAO);
        List<OrderItem> items = order.getOrderItems();

        if(!order.changeable() || !transportController.canDeleteOrder(order)){
            throw new Exception("Error!\nCan't delete an order if it's on-the-way or already arrived.");
        }

        for(OrderItem item : items){
            callInventoryToUpdateOnTheWay(item.getProductId(), order.getStoreID(), -1*item.getQuantity());
        }

        return suppliersDAO.getSupplier(supId).removeOrder(orderId, orderDAO);
    }

    // TODO: SR73
    public void removeItemFromOrder(int supId, int orderId, int itemId) throws Exception {
        if(!supplierExist(supId)){
            throw new Exception("The supplier does not exists!");
        }

        Order order = suppliersDAO.getSupplier(supId).getOrderFromList(orderId, orderDAO);

        if(!order.itemExists(itemId)){
            throw new Exception("Error!\nThe order does not contains this item.");
        }

        if(!order.changeable()){
            throw new Exception("Error!\nCannot change this order!");
        }

        int totalWeight = (int)(order.getWeightOfItem(itemId) *
                order.getQuantityOfItem(itemId))*(-1);

        if(!checkWeightLegal(supId, orderId, itemId, order.getQuantityOfItem(itemId)*(-1), order.getWeightOfItem(itemId))){
            throw new Exception("Error!\nCannot change this order!");
        }

        transportController.changeWeight(orderId, totalWeight);

        callInventoryToUpdateOnTheWay(itemId, order.getStoreID(), order.getQuantityOfItem(itemId)*(-1));

        suppliersDAO.getSupplier(supId).removeItemFromOrder(orderId, itemId, orderDAO);
    }

    // TODO: SR73
    public void updateItemQuantityInOrder(int supID, int orderID, int itemID, int quantity) throws Exception {
        double additionalWeight;
        int quantityDifference;
        Supplier supplier;
        int discount;
        double finalPrice;

        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }

        supplier = suppliersDAO.getSupplier(supID);
        Order order = supplier.getOrderFromList(orderID, orderDAO);

        if(!order.changeable()){
            throw new Exception("Can't change this order!");
        }
        if(!order.containsItem(itemID)){
            throw new Exception("Item isn't in this order!");
        }

        quantityDifference = quantity - order.getQuantityOfItem(itemID);

        additionalWeight = order.getWeightOfItem(itemID) * quantityDifference;
        discount = supplier.getItem(itemID).getDiscount(quantity);
        finalPrice = supplier.getItem(itemID).calculateTotalPrice(quantity);

        if(checkWeightLegal(supID, orderID, itemID, quantityDifference, order.getWeightOfItem(itemID))){
            order.updateItemQuantity(itemID, quantity, discount, finalPrice, orderDAO);
            transportController.changeWeight(orderID, (int)(additionalWeight));
            callInventoryToUpdateOnTheWay(itemID, order.getStoreID(), quantityDifference);
        }
        else{
            throw new Exception("Can't update this order due to weight limit.\nYou can try to order manually.");
        }

    }


    public List<String> getOrder(int supId, int orderId) throws Exception {
        if(!supplierExist(supId)){
            throw new Exception("The supplier does not exists!");
        }
        return suppliersDAO.getSupplier(supId).getOrder(orderId, orderDAO);
    }

    public boolean doesSupplierExists(int id) {
        return suppliersDAO.supplierExist(id);
    }

    public boolean orderExists(int supID, int orderID) throws Exception {
        if(!supplierExist(supID)){
            throw new Exception("The supplier does not exists!");
        }
        return suppliersDAO.getSupplier(supID).orderExists(orderID, orderDAO);
    }

    //Map<ProductId , ( (missingAmount,defectiveAmount), description)>
    public Order orderHasArrived(int orderID, Map<Integer, Pair<Pair<Integer, Integer>, String>> reportOfOrder) throws Exception {
        int supplierId = orderDAO.getOrder(orderID, suppliersDAO).getSupplierId();
        //I know I return here an Order Object and then search for it again...
        //I do this so the order is saved in orders list in Supplier.
        Order order = getOrderObject(supplierId, orderID);
        order.uploadItemsFromDB(uploadOrderItems(order.getId()));
        updateOrderItemsInfo(order, reportOfOrder);
        return order;
    }

    private void updateOrderItemsInfo(Order order, Map<Integer, Pair<Pair<Integer, Integer>, String>> reportOfOrder) throws Exception {
        for(Map.Entry<Integer, Pair<Pair<Integer, Integer>, String>> item : reportOfOrder.entrySet() ){
            int itemId = item.getKey();
            if(!order.containsItem(itemId))
                throw new Exception("This product does not exist is Order " + order.getId());
            int missingAmount = item.getValue().getLeft().getLeft();
            int defectiveAmount = item.getValue().getLeft().getRight();
            String description = item.getValue().getRight();
            order.setMissingAmountOfItem(itemId, missingAmount, orderDAO);
            order.setDefectiveAmountOfItem(itemId, defectiveAmount, orderDAO);
            order.setDescriptionOfItem(itemId, description, orderDAO);
        }
    }

    //public for testing
    public Order getOrderObject(int supplierID, int orderID) throws Exception {
        if(!supplierExist(supplierID)){
            throw new Exception("The supplier does not exists!");
        }
        return suppliersDAO.getSupplier(supplierID).getOrderObject(orderID, orderDAO);
    }

    // TODO: SR73
    //returns all orders that cannot be changed anymore (routine) + everything needed because of MinAmounts
    public List<Order> createAllOrders(Map<Integer, Map<Integer, Integer>> orderItemMinAmounts) throws Exception { //map<productID, Map<store, amount>>

        Map<String, ArrayList<Order>> ordersForTomorrow = getOrdersForTomorrow();

        checkForProductInTomorrowOrders(orderItemMinAmounts, ordersForTomorrow);

        //now we have the list of all the orders for low stocks
        createOrderAccordingToCheapestSupplier(orderItemMinAmounts, ordersForTomorrow);

        ArrayList<Order> result = ordersForTomorrow.get("not deletable");
        result.addAll(ordersForTomorrow.get("deletable"));

        insertToSuppliersBusiness(result);

        for(Order order : result){
            if(order.getStatus() == OrderStatus.waiting){
                addOrderToTransport(order);
            }
        }

        return result;
    }

    private void insertToSuppliersBusiness(ArrayList<Order> result) {
        for(Order order : result){
            suppliersDAO.getSupplier(order.getSupplierId()).addOrderNotToDB(order);
        }
    }

    private void createOrderAccordingToCheapestSupplier(Map<Integer, Map<Integer, Integer>> updatedQuantity, Map<String, ArrayList<Order>> orders) throws Exception {

        for(Integer productId : updatedQuantity.keySet()){
            Map<Integer, Integer> storeAndQuantity = updatedQuantity.get(productId);
            for(Map.Entry<Integer, Integer> entry : storeAndQuantity.entrySet()){
                int supplierId = getTheCheapestSupplier(productId, entry.getValue());
                if(supplierId == -1)
                    throw new Exception("No supplier supplies product " + productId);
                if( !checkIfOrderFromThisSupplierAlreadyExists(supplierId, orders, productId, entry.getKey(), entry.getValue()) )
                    createNewOrderForThisProduct(supplierId, productId, entry.getKey(), entry.getValue(), orders);
            }
        }
    }

    private boolean checkIfOrderFromThisSupplierAlreadyExists(int supplierId, Map<String, ArrayList<Order>> orders, int productId, int storeId, int quantity) throws Exception {
        ArrayList<Order> deletableOrders = orders.get("deletable");
        ArrayList<Order> notDeletableOrders = orders.get("not deletable");
        for(Order order : deletableOrders){
            if(order.getSupplierId() == supplierId){
                OrderItem orderItem = createNewOrderItem(supplierId, productId, quantity);
                Order newOrder = new Order(order, orderItem, storeId);
                deletableOrders.remove(order);
                notDeletableOrders.add(newOrder);
                deleteOrderFromDAO(order.getId());
                insertToOrderDAO(newOrder);
                suppliersDAO.getSupplier(newOrder.getSupplierId()).setLastOrderId(suppliersDAO.getAgreementController(), newOrder.getId());

                return true;
            }
        }

        boolean cantains = false;
        for(Order order : notDeletableOrders){
            if(order.getStoreID() == storeId && order.getSupplierId() == supplierId){
                OrderItem orderItem = createNewOrderItem(supplierId, productId, quantity);
                ArrayList<OrderItem> items = order.getOrderItems();

                for(OrderItem item : items){
                    if (item.getProductId() == orderItem.getProductId()){

                        if(order.getStatus() != OrderStatus.waiting && checkWeightLegal(supplierId, order.getId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getWeight())){
                            item.setQuantity(item.getQuantity() + orderItem.getQuantity());
                            cantains = true;
                            transportController.changeWeight(order.getId(), (int)(orderItem.getQuantity() * orderItem.getWeight()));
                            break;
                        }
                        else{
                            if(order.getStatus() == OrderStatus.waiting){
                                item.setQuantity(item.getQuantity() + orderItem.getQuantity());
                                cantains = true;
                                break;
                            }
                            else{
                                return false;
                            }
                        }
                    }
                }

                if(!cantains){
                    if(order.getStatus() != OrderStatus.waiting && checkWeightLegal(supplierId, order.getId(), orderItem.getProductId(), orderItem.getQuantity(), orderItem.getWeight())){
                        items.add(orderItem);
                        transportController.changeWeight(order.getId(), (int)(orderItem.getQuantity() * orderItem.getWeight()));
                    }
                    else{
                        if(order.getStatus() == OrderStatus.waiting){
                            items.add(orderItem);
                        }
                        else{
                            return false;
                        }
                    }
                }

                Order newOrder = new Order(order, items);

                notDeletableOrders.remove(order);
                notDeletableOrders.add(newOrder);
                updateOrderDAO(newOrder);
                return true;
            }
        }

        return false;
    }


    private void createNewOrderForThisProduct(int supplierId, int productId, int storeId, int quantity, Map<String, ArrayList<Order>> orders) throws Exception {
        Supplier supplier = suppliersDAO.getSupplier(supplierId);
        OrderItem orderItem  = createNewOrderItem(supplierId, productId, quantity);
        try {
            Order newOrder =  new Order(supplier.daysToDelivery() , supplierId, storeId, orderItem);

            insertToOrderDAO(newOrder);
            suppliersDAO.getSupplier(newOrder.getSupplierId()).setLastOrderId(suppliersDAO.getAgreementController(), newOrder.getId());
            orders.get("not deletable").add(newOrder);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //public for testing
    public OrderItem createNewOrderItem(int supplierId, int productId, int quantity) throws Exception {
        Supplier supplier = suppliersDAO.getSupplier(supplierId);
        AgreementItem currItem = null;
        try {
            currItem = supplier.getItem(productId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return new OrderItem( productId, currItem.getIdBySupplier() , currItem.getName() , quantity, currItem.getPricePerUnit(), currItem.getDiscount(quantity), currItem.calculateTotalPrice(quantity), currItem.getWeight());
    }

    //public for testing
    public int getTheCheapestSupplier(int productId, int quantity) {
        int supplierId = -1;
        double finalPrice = Double.MAX_VALUE;
        try {
            for(Supplier supplier : suppliersDAO.getAllSuppliers()){
                if( supplier.itemExists(productId)){
                    double currFinalPrice = supplier.getTotalPriceForItem(productId, quantity);
                    if( currFinalPrice < finalPrice){
                        finalPrice = currFinalPrice;
                        supplierId = supplier.getId();
                    }
                }
            }
        } catch (Exception e) {               //will never reach here
            System.out.println(e.getMessage());
        }
        return supplierId;
    }


    private void checkForProductInTomorrowOrders(Map<Integer, Map<Integer, Integer>> orderItemMinAmounts, Map<String, ArrayList<Order>> ordersForTomorrow) {

        for(Integer productId : orderItemMinAmounts.keySet()){
            Map<Integer, Integer> storeAndQuantity = orderItemMinAmounts.get(productId);
            for(Map.Entry<Integer, Integer> entry : storeAndQuantity.entrySet()){
                int quantityFound = searchForOrderToUpdateShortage( productId, entry.getKey() , entry.getValue(), ordersForTomorrow);
                if(quantityFound > 0)
                    storeAndQuantity.put(entry.getKey(), quantityFound);
                else
                    storeAndQuantity.remove(entry.getKey());
            }
        }
    }

    private int searchForOrderToUpdateShortage(int productId, int storeId, int quantity, Map<String, ArrayList<Order>> orders) {
        ArrayList<Order> deletableOrders = orders.get("deletable");
        ArrayList<Order> notDeletableOrders = orders.get("not deletable");

        ArrayList<Order> toRemove = new ArrayList<>();


        int quantityInOrder = quantity;
        for(Order order : deletableOrders){
            if(order.getStoreID() == storeId && order.itemExists(productId)){
                quantityInOrder -= order.getQuantityOfItem(productId);
                notDeletableOrders.add(order);
                toRemove.add(order);
            }
        }

        deletableOrders.removeAll(toRemove);

        return quantityInOrder;
    }


    private Map<String, ArrayList<Order>> getOrdersForTomorrow() throws SQLException {
        Map<String, ArrayList<Order>> result = new HashMap<>();


        ArrayList<Integer> supplierIds = getAllRoutineSuppliersDeliveringTomorrow();
        ArrayList<Order> lastOrderForRoutineSupplier = uploadLastOrderForRoutineSupplier(supplierIds);

        ArrayList<Order> ordersArrivalTimePassed = filterOrdersArrivalTimePassed(lastOrderForRoutineSupplier);        //This we create new order with all the old information

        ArrayList<Order> ordersArrivalTomorrow = filterOrdersArrivalTomorrow(lastOrderForRoutineSupplier);            // This orders cannot be deleted, only add items!!
        result.put("not deletable", ordersArrivalTomorrow);

        ArrayList<Order> newOrdersFromArrivalTimePassed = createNewOrdersFromArrivalTimePassed(ordersArrivalTimePassed);
        result.put("deletable", newOrdersFromArrivalTimePassed);



        return result;
    }


    //public for testing
    public ArrayList<Integer> getAllRoutineSuppliersDeliveringTomorrow() {
        ArrayList<Integer> result = new ArrayList<>();
        for(Supplier supplier : suppliersDAO.getAllSuppliers()){
            try {
                if(supplier.isRoutineAgreement() && supplier.daysToDelivery() == 1)
                    result.add(supplier.getId());
            } catch (Exception e) { //exception will never be caught because && before
                System.out.println(e.getMessage());
            }
        }
        return result;
    }

    //public for testing
    public ArrayList<Order> uploadLastOrderForRoutineSupplier(ArrayList<Integer> supplierIds) {
        ArrayList<Integer> orderIds = new ArrayList<>();
        Supplier currSupplier;
        for(Integer supplierId : supplierIds){
            currSupplier = suppliersDAO.getSupplier(supplierId);

            int lastOrderId = currSupplier.getLastOrderId();
            if(lastOrderId != -1)
                orderIds.add(lastOrderId);
        }
        ArrayList<Order> orders = orderDAO.getLastOrdersFromALlSuppliers(orderIds);
        for(Order order : orders){
            order.uploadItemsFromDB(uploadOrderItems(order.getId()));
        }
        return orders;
    }

    //public for testing
    public ArrayList<Order> filterOrdersArrivalTomorrow(ArrayList<Order> orders) {
        ArrayList<Order> result = new ArrayList<>();
        for(Order order : orders){
            if(order.getDaysUntilOrder(LocalDate.now()) == 1)
                result.add(order);
        }
        return result;
    }

    //public for testing
    public ArrayList<Order> filterOrdersArrivalTimePassed(ArrayList<Order> orders) {
        ArrayList<Order> result = new ArrayList<>();
        for(Order order : orders){
            if(order.passed())
                result.add(order);
        }
        return result;
    }



    private ArrayList<Order> createNewOrdersFromArrivalTimePassed(ArrayList<Order> ordersArrivalTimePassed) throws SQLException {
        ArrayList<Order> result = new ArrayList<>();
        for(Order order : ordersArrivalTimePassed){
            Order newOrder = new Order(order);
            suppliersDAO.getSupplier(newOrder.getSupplierId()).setLastOrderId(suppliersDAO.getAgreementController(), newOrder.getId());
            orderDAO.addOrder(newOrder);
            result.add(newOrder);
        }
        return result;
    }



    public void insertToOrderDAO(Order order) throws SQLException {
        orderDAO.addOrder(order);
    }

    private void deleteOrderFromDAO(int id) throws SQLException {
        orderDAO.removeOrder(id);
    }


    private void updateOrderDAO(Order newOrder) throws SQLException {
        orderDAO.updateOrder(newOrder);
    }

    public ArrayList<OrderItem> uploadOrderItems(int orderId){
        return orderDAO.uploadAllItemsFromOrder(orderId, suppliersDAO.getAgreementItemDAO());
    }


    public void addItemToOrderDAO(int orderId, int id, String name, int quantity, float pricePerUnit, int discount, double calculateTotalPrice, double weight) throws SQLException {
        orderDAO.addItem(orderId, new OrderItem(id, id, name, quantity, pricePerUnit, discount, calculateTotalPrice,  weight));
    }


    public ArrayList<Integer> getSuppliersIds() {
        return suppliersDAO.getAllSuppliersIds();
    }


    public List<Integer> getAllOrderIdsForSupplier(int supplierId) {
        return orderDAO.getSupplierOrdersIds(supplierId);
    }

    public List<String> getAllOrdersItemsInDiscount(int supplierId) {
        List<String> result = new ArrayList<>();
        List<OrderItem> items = orderDAO.getItemsInDiscountInSUpplier(supplierId, suppliersDAO.getAgreementItemDAO());
        for(OrderItem item : items){
            result.addAll(item.getStringInfo());
        }
        return result;
    }


    // TODO: SR73
    public void addOrderToTransport(Order order) throws Exception {
        List<LocalDate> availableDays = getPossibleDates(order.getSupplierId());

        LocalDate date = transportController.SchedulingOrderToTransport(order, availableDays);
        if(date == null){
            date = LocalDate.of(2100, 1, 1);
        }
        order.setArrivalTime(date);

        orderDAO.setOrderArrivalTime(order.getId(), date);
    }

    // TODO: SR73
    //public for testing!!!
    public List<LocalDate> getPossibleDates(int supplierId) {
        List<LocalDate> dates = new ArrayList<>();
        Supplier supplier = suppliersDAO.getSupplier(supplierId);

        List<Integer> availableDays = suppliersDAO.getSupplier(supplierId).getAgreementDays();
        if(availableDays.contains(-1)){ // case of selfTransport
            for(int i = 0; i < 8; i++) {
                availableDays.add(i);
            }
        }

        if(supplier.isByOrderAgreement()){
            LocalDate today = LocalDate.now();
            LocalDate closest = today.plusDays(availableDays.get(0));
            dates.add(closest);
        }


        if(supplier.isRoutineAgreement()||supplier.isNotTransportingAgreement()){
            LocalDate today = LocalDate.now();
            int dayInt = getDayInt(today);
            for(Integer currDay : availableDays){
                int addDays = (7 - dayInt + currDay) % 7;  //number of days to add
                LocalDate addDate = today.plusDays(addDays);
                if(addDate.isEqual(today))
                    dates.add(today.plusDays(7));
                else
                    dates.add(addDate);

                //if today is 6 and the date is : 10/06
                //if 2 is here we need to add him : ( 7+2-6 % 7= 3) = 13.6
                //if 4 is here we need to add him : ( 7+4-6 %7= 5) = 15.6
                // if 7 is here we need to add him : ( 7+7-6 %7= 7)

                //if today is 1 date : 12.6
                //if 7 is here than we add : (7+7-1 % 7 = 6
            }
        }

        return dates;
    }

    private int getDayInt(LocalDate today) {
        int day = -1;
        switch(today.getDayOfWeek()){
            case SUNDAY:
                day = 1; break;
            case MONDAY:
                day =  2; break;
            case TUESDAY:
                day =  3; break;
            case WEDNESDAY:
                day =  4; break;
            case THURSDAY:
                day =  5; break;
            case FRIDAY:
                day =  6; break;
            case SATURDAY:
                day =  7; break;
        }
        return  day;
    }

    // TODO:  call this functions from updateItemQuantityInOrder, return to it the total new weight if ok, if not return 0 or -1...
    //      Where should we call this function from the automatic Orders?.
    public boolean checkWeightLegal(int supplierId, int orderID, int orderItemId, int differenceQuantity, double weightOfItem) throws Exception {

        double newItemWeight = weightOfItem * differenceQuantity;  //just the added weight

        return transportController.canChangeOrder(orderID, (int) newItemWeight);

    }





    public void insertFirstDataToDB() throws Exception {

        insertSupplier1();
        insertSupplier2();

    }


    private void insertSupplier1() throws Exception {

        int storeId = 1;
        ArrayList<Pair<String, String >> contacts1 = new ArrayList<>();
        contacts1.add(new Pair<>("Yael", "0508647894"));             contacts1.add(new Pair<>("Avi", "086475421"));
        ArrayList<String> manufacturers1 = new ArrayList<>();  manufacturers1.add("Tnuva") ;       manufacturers1.add("Osem") ; manufacturers1.add("Elit");  manufacturers1.add("Struass");   manufacturers1.add("Yoplait");
        int supplierId1 = addSupplier("Avi", 123456, "Hertzel", "check", contacts1,manufacturers1);

        addAgreement(supplierId1, 1, "1 2 3 4 5 6 7");

        ArrayList<String> items = new ArrayList<>();
        items.add("1 , 1,  Osem , 3.5 , 100 , 20 ");
        items.add("2 , 2, Tnuva, 4.0 , 100 , 20 , 200 , 50 , 500 , 70");
        items.add("3 , 3,  Tara, 4.0 , 100 , 20 , 200 , 40 , 500 , 50");
        items.add("4 , 4, Yoplait, 5.9 ,  10 , 20 , 20 , 80 ");
        items.add("5 , 5, Elit, 7.0 ,  10 , 20 , 20 , 50 , 30 , 80 ");


        addItemsToAgreement(supplierId1, items);

        Order order1 = new Order(1, supplierId1, LocalDate.of(2022, 5, 25),  LocalDate.of(2022, 6, 1), storeId , OrderStatus.complete);
        int order1Id = order1.getId();
        insertToOrderDAO(order1);
        suppliersDAO.getAgreementController().setLastOrderId(supplierId1, order1Id);

        //Bamba  80 * 0.2 = 16kg
        int id = 1;
        AgreementItem curr = suppliersDAO.getSupplier(supplierId1).getItem(id);
        int quantity = 80;
        addItemToOrderDAO(order1Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        // Milk 100 * 1 = 100kg
        id = 2;
        curr = suppliersDAO.getSupplier(supplierId1).getItem(id);
        quantity = 100;
        addItemToOrderDAO(order1Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        //Order weight = 116 kg
    }


    private void insertSupplier2() throws Exception {

        int storeId = 1;

        ArrayList<Pair<String, String >> contacts2 = new ArrayList<>();
        contacts2.add(new Pair<>("Beni", "0508647894"));             contacts2.add(new Pair<>("Kvodi", "086475421"));
        ArrayList<String> manufacturers2 = new ArrayList<>();        manufacturers2.add("Tnuva") ; manufacturers2.add("Elit");  manufacturers2.add("Struass");
        int supplierId2 = addSupplier("Beni", 111111, "Yosef Tkoa", "check", contacts2, manufacturers2);

        addAgreement(supplierId2, 2, "3");

        ArrayList<String> items = new ArrayList<>();
        items.add("1 , 1, Osem , 3.5 , 100 , 30 ");
        items.add("2 , 2, Tnuva, 4.0, 100 , 30 , 200 , 60 , 500 , 80");
        items.add("3 , 3, Tara, 4.0 ,  100 , 30 , 200 , 50 , 500 , 60");
        items.add("4 , 4, Yoplait, 5.9 , 10 , 30 , 20 , 90 ");
        items.add("5 , 5, Elit, 7.0 ,  10 , 30 , 20 , 60 , 30 , 90 ");

        addItemsToAgreement(supplierId2, items);


        Order order2 = new Order(2, supplierId2, LocalDate.of(2022, 5, 29),  LocalDate.of(2022, 6, 1), storeId, OrderStatus.complete);
        int order2Id = order2.getId();
        insertToOrderDAO(order2);

        //Yoplait 20 * 0.15 = 3kg
        int id = 4;
        AgreementItem curr = suppliersDAO.getSupplier(supplierId2).getItem(id);
        int quantity = 20;
        addItemToOrderDAO(order2Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        //Halva 20 * 0.3 = 6kg
        id = 5;
        curr = suppliersDAO.getSupplier(supplierId2).getItem(id);
        quantity = 20;
        addItemToOrderDAO(order2Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        //Order 2 weight = 9 kg

    }


    public int getSupplierWithOrder(int orderId) throws Exception {
        for(Integer supplierId : getSuppliersIds()){
            if(orderExists(supplierId, orderId))
                return supplierId;
        }
        return -1;
    }


    public List<String> getOrder(int orderId) throws Exception {
        int supId = getSupplierWithOrder(orderId);
        if(supId == -1){
            throw new Exception("No Supplier has this order!");
        }
        return suppliersDAO.getSupplier(supId).getOrder(orderId, orderDAO);
    }

    public int getMatchingProductIdForIdBySupplier(int idBySupplier) throws Exception {
        return suppliersDAO.getAgreementItemDAO().getMatchingProductIdForIdBySupplier(idBySupplier);
    }

    public Boolean orderItemExistsInOrder(int supplierId, int orderId, int itemId) throws Exception {
        Order order  = suppliersDAO.getSupplier(supplierId).getOrderObject(orderId, orderDAO);
        return order.containsItem(itemId);
    }


    public void callInventoryToUpdateOnTheWay(int productId, int storeId,int amount){
        inventoryController.updateOnTheWayProducts(productId, storeId, amount);
    }


    public void deleteProduct(int id) throws Exception {
        for(Supplier supplier : suppliersDAO.getAllSuppliers()) {
            if (supplier.itemExists(id)) {
                supplier.deleteItem(id, suppliersDAO);
            }
        }
    }
}
