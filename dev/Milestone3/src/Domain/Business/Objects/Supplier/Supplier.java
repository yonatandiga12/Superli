package Domain.Business.Objects.Supplier;

import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.Agreement.ByOrderAgreement;
import Domain.Business.Objects.Supplier.Agreement.NotTransportingAgreement;
import Domain.Business.Objects.Supplier.Agreement.RoutineAgreement;
import Domain.DAL.Controllers.InventoryAndSuppliers.AgreementController;
import Domain.DAL.Controllers.InventoryAndSuppliers.AgreementItemDAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.OrderDAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.SuppliersDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Supplier {

    private int id;
    private int bankNumber;
    private String address;
    private String name;
    private ArrayList<Contact> contacts;
    private String payingAgreement;
    private Agreement agreement;
    private ArrayList<String> manufacturers;

    private HashMap<Integer, Order> orders;

    private SuppliersDAO suppliersDAO;

    private final int ROUTINE  = 1;
    private final int BY_ORDER  = 2;
    private final int NOT_TRANSPORTING  = 3;

    private static int globalID = -1;


    public Supplier(String name, int bankNumber, String address,String payingAgreement, ArrayList<Contact> contacts, ArrayList<String> manufacturers, SuppliersDAO dao){

        this.suppliersDAO = dao;
        if(globalID == -1){
            findGlobalIDFromDataBase(dao);
        }

        this.id = globalID;
        globalID++;
        this.name = name;
        this.bankNumber = bankNumber;
        this.address = address;
        this.payingAgreement = payingAgreement;
        this.contacts = contacts;
        this.manufacturers = manufacturers;
        this.orders = new HashMap<>();
        agreement = null;
    }

    private void findGlobalIDFromDataBase(SuppliersDAO dao){
        globalID = dao.findSupplierGlobalID();
    }

    public Supplier(int id, int bankNumber, String address, String name, String payingAgreement){
        this.id = id;
        this.name = name;
        this.bankNumber = bankNumber;
        this.address = address;
        this.payingAgreement = payingAgreement;

        this.manufacturers = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.orders = new HashMap<>();
        agreement = null;
    }

    public Supplier(int id, int bankNumber, String address, String name, String payingAgreement,ArrayList<Contact> contacts, ArrayList<String> manufacturers ,SuppliersDAO dao){
        suppliersDAO = dao;
        this.id = id;
        this.name = name;
        this.bankNumber = bankNumber;
        this.address = address;
        this.payingAgreement = payingAgreement;

        this.contacts = contacts;
        this.manufacturers = manufacturers;
        this.orders = new HashMap<>();
        agreement = null;
    }

    public static void setGlobalId(int id){
        globalID = id ;
    }

    public int getId() {
        return id;
    }

    private void agreementExists() throws Exception {
        if(agreement == null){
            throw new Exception("You have no agreement with this supplier!");
        }
    }

    public String getAddress() {
        return address;
    }

    public String getPayingAgreement() {
        return payingAgreement;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public boolean removeContact(Contact contact) {
        return contacts.remove(contact);
    }


    public void addAgreement(int agreementType, String agreementDays, SuppliersDAO suppliersDAO) throws Exception {
        if(agreementType > NOT_TRANSPORTING || agreementType < ROUTINE)
            throw new Exception("Invalid agreement type!");

        createAgreement(agreementType, agreementDays, suppliersDAO);


    }




    public void updateAddress(String address) {
        this.address = address;
    }

    public void updateBankNumber(int bankNumber) {
        this.bankNumber = bankNumber;
    }


    public void updateName(String newName) {
        this.name = newName;
    }



    public void addContact(Contact contact) {
        contacts.add(contact);
    }


    public void updatePayingAgreement(String payingAgreement) {
        this.payingAgreement = payingAgreement;
        //If we add this payingAgreement to the agreement,  need to update there too
    }


    public void addManufacturer(String manufacturer) throws Exception {
        if(manufacturers.contains(manufacturer)){
            throw new Exception("This manufacturer already exists in the system!");
        }

        manufacturers.add(manufacturer);
    }



    public String getName() {
        return name;
    }

    public List<String> getAgreementItems() throws Exception {
        agreementExists();
        return agreement.getItemsInMapFormat();
    }

    public void updateBulkPriceForItem(int itemID, Map<Integer, Integer> newBulkPrices, AgreementController agreementController) throws Exception {
        agreementExists();
        agreementController.updateBulkPriceForItem(id, itemID, newBulkPrices);
        agreement.getItem(itemID).setBulkPrices(newBulkPrices);
    }

    public void updatePricePerUnitForItem(int itemID, float newPrice, AgreementItemDAO agreementItemDAO) throws Exception {
        agreementExists();
        agreementItemDAO.updatePPU(itemID, newPrice);
        agreement.getItem(itemID).setPrice(newPrice);
    }


    public void addItem(int itemId, int idBySupplier, String itemManu, float itemPrice, Map<Integer, Integer> bulkPrices, SuppliersDAO suppliersDAO) throws Exception {
        agreementExists();
        if(agreement.itemExists(itemId) || agreement.IdBySupplierExists(idBySupplier))
            throw new Exception("item with this ID already exists!");

        AgreementItem item = new AgreementItem(itemId, idBySupplier,  itemManu, itemPrice, bulkPrices);
        ArrayList<AgreementItem> _items = new ArrayList<>();
        _items.add(item);
        AgreementController agreementController = suppliersDAO.getAgreementController();
        agreementController.addAgreementItems( _items, id);
        agreement.addItem(item);
        if(!manufacturers.contains(itemManu)){
            suppliersDAO.addSupplierManufacturer(id, itemManu);
            manufacturers.add(itemManu);
        }
    }


    public void deleteItem(int itemId, SuppliersDAO suppliersDAO) throws Exception {
        agreementExists();
        String manu = agreement.getItem(itemId).getManufacturer();
        AgreementItemDAO agreementItemDAO = suppliersDAO.getAgreementItemDAO();
        agreementItemDAO.removeItem(id, itemId);
        agreement.removeItem(itemId);

        if(!agreement.isManufacturerRepresented(manu)){
            suppliersDAO.removeSupplierManufacturer(id, manu);
            manufacturers.remove(manu);
        }
    }


    public boolean isTransporting() throws Exception {
        agreementExists();
        return agreement.isTransporting();
    }



    public void updateItemId(int prodcutId, int newIdBySupplier, AgreementItemDAO agreementItemDAO) throws Exception {
        agreementExists();
        agreementItemDAO.updateItemIdBySupplier(prodcutId, newIdBySupplier);
        agreement.setItemId(prodcutId, newIdBySupplier);
    }


    /*
    public void updateItemName(int itemId, String newName, AgreementItemDAO agreementItemDAO) throws Exception {
        agreementExists();
        agreementItemDAO.updateItemName(itemId, newName);
        agreement.getItem(itemId).setName(newName);
    }

     */

    public void updateItemManufacturer(int itemId, String manufacturer, SuppliersDAO suppliersDAO) throws Exception {
        AgreementItemDAO agreementItemDAO = suppliersDAO.getAgreementItemDAO();
        agreementExists();
        String manu = agreement.getItem(itemId).getManufacturer();

        agreementItemDAO.updateManufacturer(itemId, manufacturer);
        agreement.getItem(itemId).setManufacturer(manufacturer);


        if(!agreement.isManufacturerRepresented(manu)){
            //suppliersDAO.removeSupplierManufacturer(id, manu);
            suppliersDAO.addSupplierManufacturer(id, manufacturer);
            //manufacturers.remove(manu);
            manufacturers.add(manufacturer);
        }
    }



    public void addAgreementItems(List<String> itemsString, SuppliersDAO suppliersDAO) throws Exception {
        AgreementController agreementController =  suppliersDAO.getAgreementController();
        agreementExists();
        agreement.setItemsFromString(itemsString, id,  agreementController);

        manufacturers = new ArrayList<>();

        for(AgreementItem item : agreement.getItems()){
            suppliersDAO.addSupplierManufacturer(id, item.getManufacturer());
            manufacturers.add(item.getManufacturer());
        }
    }



    public void updateAgreementType( int agreementType, String agreementDays) throws Exception {
        agreementExists();
        List<AgreementItem> items = agreement.getItems();
        createAgreementAgain(agreementType, agreementDays);
        agreement.setItems(items);
    }

    private void createAgreementAgain(int agreementType, String agreementDays) throws Exception {
        switch(agreementType){
            case ROUTINE :
                if(!RoutineAgreement.hasDays(agreementDays)){
                    throw new Exception("You provided no days!");
                }
                List<Integer> days = RoutineAgreement.daysStringToDay(agreementDays);
                agreement = new RoutineAgreement(days);
                break;
            case BY_ORDER :
                days = new ArrayList<>();   days.add(Integer.parseInt(agreementDays));
                agreement = new ByOrderAgreement(Integer.parseInt(agreementDays));
                break;
            case NOT_TRANSPORTING :
                agreement = new NotTransportingAgreement();
                break;
        }
    }


    private void createAgreement(int agreementType, String agreementDays, SuppliersDAO suppliersDAO) throws Exception {
        AgreementController agreementController = suppliersDAO.getAgreementController();

        suppliersDAO.updateAgreementType(id, agreementType);

        switch(agreementType){
            case ROUTINE :
                if(!RoutineAgreement.hasDays(agreementDays)){
                    throw new Exception("You provided no days!");
                }
                List<Integer> days = RoutineAgreement.daysStringToDay(agreementDays);
                agreementController.updateAgreementDays(id, days, ROUTINE);
                agreement = new RoutineAgreement(days);
                break;
            case BY_ORDER :
                days = new ArrayList<>();
                days.add(Integer.parseInt(agreementDays));
                agreementController.updateAgreementDays(id, days, BY_ORDER);
                agreement = new ByOrderAgreement(Integer.parseInt(agreementDays));
                break;
            case NOT_TRANSPORTING :
                agreementController.updateAgreementDays(id, null,NOT_TRANSPORTING);
                agreement = new NotTransportingAgreement();
                break;
        }
    }

    public List<Integer> getAgreementDays(){
        return agreement.getDays();
    }



    public List<Integer> getDaysOfDelivery() {
        if(agreement instanceof RoutineAgreement){
            return ((RoutineAgreement)agreement).getDaysOfDelivery();
        }
        else{
            return null;
        }
    }

    public int getDeliveryDays(){
        if(agreement instanceof ByOrderAgreement){
            return ((ByOrderAgreement)agreement).getDeliveryDays();
        }
        else{
            return -1;
        }
    }

    // < id , name , bankAccount , address , payingAgreement , Contact1Name , Contact1Phone ,  Contact2Name , Contact2Phone ... >
    public ArrayList<String> getSupplierInfo() {
        ArrayList<String> result = new ArrayList<>();

        result.add(String.valueOf(getId()));
        result.add(getName());
        result.add(String.valueOf(getBankNumber()));
        result.add(getAddress());
        result.add(getPayingAgreement());
        for(Contact contact : contacts){
            result.add(contact.getName());
            result.add(contact.getPhone());
        }
        return result;
    }

    public int daysToDelivery() throws Exception {
        agreementExists();
        return agreement.daysToDelivery();
    }

    public boolean isRoutineAgreement() {
        return agreement != null && agreement instanceof RoutineAgreement;
    }

    public boolean isByOrderAgreement(){
        return agreement != null && agreement instanceof ByOrderAgreement;
    }

    public boolean isNotTransportingAgreement() {
        return agreement != null && agreement instanceof NotTransportingAgreement;
    }

    public void setDaysOfDelivery(String days, AgreementController agreementController) throws Exception{
        agreementExists();
        if(!(agreement instanceof RoutineAgreement)){
            throw new Exception("The supplier's agreement is not Routine agreement");
        }

        ((RoutineAgreement) agreement).setDaysOfDelivery(days, id, agreementController);
    }

    public void setDaysUntilDelivery(int days, AgreementController agreementController) throws Exception{
        agreementExists();
        if(!(agreement instanceof ByOrderAgreement)){
            throw new Exception("The supplier's agreement is not Routine agreement");
        }

        ((ByOrderAgreement) agreement).setDeliveryDays(days, id, agreementController);
    }

    public void addDaysOfDelivery(String days, AgreementController agreementController) throws Exception {
        agreementExists();
        if(!(agreement instanceof RoutineAgreement)){
            throw new Exception("The supplier's agreement is not Routine agreement");
        }

        ((RoutineAgreement) agreement).addDaysOfDelivery(id, days, agreementController);
    }

    public void removeDayOfDelivery(int day) throws Exception {
        agreementExists();
        if(!(agreement instanceof RoutineAgreement)){
            throw new Exception("The supplier's agreement is not Routine agreement");
        }

        ((RoutineAgreement) agreement).removeDayOfDelivery(day);
    }

    public AgreementItem getItem(int itemId) throws Exception {
        agreementExists();
        return agreement.getItem(itemId);
    }

    public List<Contact> getAllContact(){
        return contacts;
    }


    public void removeContact(String name, SuppliersDAO suppliersDAO) throws Exception {
        for(Contact c : contacts){
            if(Objects.equals(name, c.getName())){
                suppliersDAO.removeSupplierContact(id, c.getName());
                contacts.remove(c);
                return;
            }
        }

        throw new Exception("No such contact Exists!");
    }

    public List<String> getManufacturers(){
        return new ArrayList<>(manufacturers);
    }

    public void removeManufacturer(String name, SuppliersDAO suppliersDAO) throws Exception {
        boolean found = false;

        for(String s : manufacturers){
            if(s.equals(name)){
                found = true;
                break;
            }
        }

        if(!found){
            throw new Exception("This manufacturer is not represented by the current supplier!");
        }

        if(agreement != null && agreement.isManufacturerRepresented(name)){
            throw new Exception("This manufacturer is selling items to the supplier, remove them first!");
        }

        suppliersDAO.removeSupplierManufacturer(id, name);
        manufacturers.remove(name);
    }


    public boolean hasAgreement(){
        return agreement != null;
    }



    public Order addNewOrder(int storeId, OrderDAO orderDAO, AgreementController agreementController) throws Exception {
        agreementExists();

        Order order = new Order(agreement.daysToDelivery(), id, storeId);
        orderDAO.addOrder(order);
        orders.put(order.getId(), order);

        setLastOrderId(agreementController, order.getId());

        return order;
    }


    public void addOneItemToOrder(int orderId, int itemId, int itemQuantity, OrderDAO orderDAO) throws Exception {
        agreementExists();
        if(!agreement.itemExists(itemId))
            throw new Exception(String.format("Item with ID: %d does not Exists!", itemId));
        if(!orderExists(orderId, orderDAO))
            throw new Exception(String.format("Order with ID: %d does not Exists!", orderId));

        if(itemQuantity == 0){
            throw new Exception("Can't add 0 items to the order!");
        }


        if(!orders.get(orderId).changeable()){
            throw new Exception("Can't change order: time exception.");
        }

        AgreementItem currItem = agreement.getItem(itemId);

        float ppu = currItem.getPricePerUnit();
        int discount = agreement.getItem(itemId).getDiscount(itemQuantity);
        Double finalPrice = agreement.getItem(itemId).calculateTotalPrice(itemQuantity);

        //currItem.getWeight()
        double weight = currItem.getWeight();
        orders.get(orderId).addItem(itemId, agreement.getItem(itemId).getIdBySupplier() , agreement.getItem(itemId).getName(), itemQuantity, ppu, discount, finalPrice, weight, orderDAO);

    }

    public boolean removeOrder(int orderId, OrderDAO orderDAO) throws Exception {
        if(!orderExists(orderId, orderDAO))
            throw new Exception(String.format("Order with ID: %d does not Exists!", orderId));

        if(!orders.get(orderId).changeable()){
            throw new Exception("Can't change order: time exception.");
        }

        orderDAO.removeOrder(orderId);
        orders.remove(orderId);
        return true;
    }

    public void updateOrder(int orderID, int itemID, int quantity, OrderDAO orderDAO) throws Exception {
        agreementExists();
        if(!orderExists(orderID, orderDAO)){
            throw new Exception(String.format("Order with ID: %d does not Exists!", orderID));
        }

        if(!orders.get(orderID).changeable()){
            throw new Exception("Can't change order: time exception.");
        }


        if(!agreement.itemExists(itemID)){
            throw new Exception(String.format("Item with ID: %d does not Exists!", itemID));
        }

        orders.get(orderID).updateItemQuantity(itemID, quantity, agreement.getItem(itemID).getDiscount(quantity), agreement.getOrderPrice(itemID, quantity), orderDAO);
    }

    public void removeItemFromOrder(int orderId, int itemId, OrderDAO orderDAO) throws Exception {
        if(!orderExists(orderId, orderDAO))
            throw new Exception(String.format("Order with ID: %d does not Exists!", orderId));
        if(!orders.get(orderId).changeable()){
            throw new Exception("Can't change order: time exception.");
        }
        if(!orders.get(orderId).itemExists(itemId))
            throw new Exception("Item with this ID does not exist in thins order!");
        orders.get(orderId).removeItem(itemId, orderDAO);
    }


    //result = { orderId, supplierId, creationDate, ArrivalDate, storeId, status , items...}
    public List<String> getOrder(int orderId, OrderDAO orderDAO) throws Exception {
        Order currOrder = getOrderFromList(orderId, orderDAO);
        List<String> result = new ArrayList<>();
        result.add(String.valueOf(currOrder.getId()));
        result.add(String.valueOf(id));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");
        String creationDate = formatter.format(currOrder.getCreationTime());
        String arrivalDate = formatter.format(currOrder.getArrivaltime());

        result.add(creationDate);
        result.add(arrivalDate);
        result.add(String.valueOf(currOrder.getStoreID()));
        result.add(currOrder.getStatusString());

        List<OrderItem> items = currOrder.getOrderItems();
        for(OrderItem item : items){
            result.addAll(item.getStringInfo());
        }
        return result;
    }

    public Order getOrderFromList(int orderId, OrderDAO orderDAO) throws Exception {
        if(!orderExists(orderId, orderDAO))
            throw new Exception(String.format("Order with ID: %d does not Exists!", orderId));
        if(!orders.containsKey(orderId))
            throw new Exception(String.format("Order with ID: %d does not Exists!", orderId));
        return orders.get(orderId);
    }

    public boolean orderExists(int currOrderId, OrderDAO orderDAO) throws Exception {
        if(orders.containsKey(currOrderId) )
            return true;
        if(orderDAO.containsKey(currOrderId, suppliersDAO)){
            Order order = orderDAO.getOrder(currOrderId, suppliersDAO);
            if(order.getSupplierId() == id){
                orders.put(order.getId(), order);
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }


    public Double getTotalPriceForItem(int itemId, int quantity) throws Exception {
        agreementExists();
        return agreement.getOrderPrice(itemId, quantity);
    }


    public ArrayList<Order> getFutureOrders() {
        ArrayList<Order> result = new ArrayList<>();
        result.addAll(orders.values());    //This should be the "future" orders
        return result;
    }

    public ArrayList<Order> getOrdersForTomorrow() {

        //check all orders dates and return the ones for tomorrow
        ArrayList<Order> result = new ArrayList<>();
        for(Order order : orders.values()){
            if(order.getDaysUntilOrder(LocalDate.now()) == 1){
                result.add(order);
            }
        }
        return result;
    }

    public int getLastOrderId() {
        if(isRoutineAgreement()){
            return ((RoutineAgreement)agreement).getLastOrderId();
        }
        return -1;
    }

    public void setLastOrderId(AgreementController agreementController, int orderId) throws SQLException {
        if(isRoutineAgreement()){
            agreementController.setLastOrderId(id, orderId);
            ((RoutineAgreement)agreement).setLastOrderId(orderId);
        }
    }


    public boolean itemExists(int productId) {
        return agreement.itemExists(productId);
    }

    public int getAgreementType() {
        if(isRoutineAgreement())
            return ROUTINE;
        else if(isByOrderAgreement())
            return BY_ORDER;
        else if(isNotTransportingAgreement())
            return NOT_TRANSPORTING;
        else
            return -1;
    }


    public void addAgreementFromDB(Agreement agreement) {
        this.agreement = agreement;
    }


    public Order getOrderObject(int orderID, OrderDAO orderDAO) throws Exception {
        return getOrderFromList(orderID, orderDAO);
    }


    public void addOrderNotToDB(Order order) {
        if(orders.containsKey(order.getId())){
            orders.replace(order.getId(), order);
        }
        else{
            orders.put(order.getId(), order);
        }
    }

    public AgreementItem getAgreementItem(int id) throws Exception {
        return agreement.getItem(id);
    }
}
