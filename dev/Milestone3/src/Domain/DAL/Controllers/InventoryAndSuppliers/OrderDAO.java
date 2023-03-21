package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Supplier.Order;
import Domain.Business.Objects.Supplier.OrderItem;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;
import Globals.Enums.OrderStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class OrderDAO extends DataMapper<Order> {

    private final static Map<String, Order> ORDER_IDENTITY_MAP = new HashMap<>();

    private final OrderItemDAO orderItemDAO;


    private final static int ORDER_ID_COLUMN = 1;
    private final static int SUPPLIER_ID_COLUMN = 2;
    private final static int STORE_ID_COLUMN = 3;
    private final static int CREATION_TIME_COLUMN = 4;
    private final static int ARRIVAL_TIME_COLUMN = 5;
    private final static int STATUS_COLOUMN = 6;


    public OrderDAO()  {
        super("Orders");
        orderItemDAO = new OrderItemDAO();
        /*try {
            getAllOrders();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public Order getOrder(int orderId, SuppliersDAO suppliersDAO) throws Exception {
        if(ORDER_IDENTITY_MAP.containsKey(String.valueOf(orderId)))
            return ORDER_IDENTITY_MAP.get(String.valueOf(orderId));
        Order order = get(String.valueOf(orderId));

        if(order == null){
            throw new Exception("Error!\nNo such order.");
        }

        order.uploadItemsFromDB(uploadAllItemsFromOrder(order.getId(), suppliersDAO.getAgreementItemDAO()));
        return order;
    }


    @Override
    protected Map<String, Order> getMap() {
        return ORDER_IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    @Override
    protected Order buildObject(ResultSet instanceResult) throws Exception {

        return new Order(instanceResult.getInt(ORDER_ID_COLUMN),
                instanceResult.getInt(SUPPLIER_ID_COLUMN),
                LocalDate.parse(instanceResult.getString(CREATION_TIME_COLUMN)),
                LocalDate.parse(instanceResult.getString(ARRIVAL_TIME_COLUMN)),
                instanceResult.getInt(STORE_ID_COLUMN),
                getStatus(instanceResult));
    }

    private OrderStatus getStatus(ResultSet instanceResult) throws SQLException {
        String status = instanceResult.getString(STATUS_COLOUMN);
        //status : waiting, ordered, complete
        if(status.equals("waiting"))
            return OrderStatus.waiting;
        else if(status.equals("complete"))
            return  OrderStatus.complete;
        else
            return OrderStatus.ordered;
    }

    @Override
    public void insert(Order order) throws SQLException {
        insert(Arrays.asList(String.valueOf(order.getId()), String.valueOf(order.getSupplierId()),
                String.valueOf(order.getStoreID()), String.valueOf(order.getCreationTime()), String.valueOf(order.getArrivaltime()), String.valueOf(order.getStatus())));

        ArrayList<OrderItem> items = order.getOrderItems();
        if(items != null && items.size() > 0){
            for(OrderItem orderItem : items){
                orderItemDAO.addItem(order.getId(), orderItem);
            }
        }
    }

    @Override
    public String instanceToId(Order instance) {
        return String.valueOf(instance.getId());
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }

    public void addOrder(Order order) throws SQLException {
        insert(order);
        ORDER_IDENTITY_MAP.put(String.valueOf(order.getId()), order);

    }

    public void addItem(int orderId, OrderItem orderItem) throws SQLException {

        orderItemDAO.addItem(orderId, orderItem);
    }

    public void removeOrder(int orderId) throws SQLException {
        remove(orderId);
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(orderId);
        orderItemDAO.removeOrders(ids);
        ORDER_IDENTITY_MAP.remove(String.valueOf(orderId));
    }

    public void removeOrderItem(int orderId, int productId) throws SQLException {
        orderItemDAO.removeItem(orderId, productId);
    }

    public void updateItemQuantity(int orderId, int productId, int quantity) throws SQLException {
        orderItemDAO.updateItemQuantity(orderId, productId, quantity);
    }

    public void updateItemDiscount(int orderId, int productId, int discount) throws SQLException {
    orderItemDAO.updateItemDiscount(orderId, productId, discount);
    }

    public void updateItemFinalPrice(int orderId, int productId, double finalPrice) throws SQLException {
        orderItemDAO.updateItemFinalPrice( orderId, productId, finalPrice);
    }

    public boolean containsKey(int id, SuppliersDAO suppliersDAO) throws Exception {
        if(ORDER_IDENTITY_MAP.containsKey(String.valueOf(id)))
            return true;
        Order order = getOrder(id, suppliersDAO);

        return order != null;
    }

    public void updateOrder(Order newOrder) throws SQLException {
        removeOrder(newOrder.getId());
        addOrder(newOrder);
        ORDER_IDENTITY_MAP.replace(String.valueOf(newOrder.getId()), newOrder);

    }

    public void updateStatus(Order order) throws SQLException {
        update(Arrays.asList(STATUS_COLOUMN),Arrays.asList(order.getStatus()),Arrays.asList(ORDER_ID_COLUMN),Arrays.asList(order.getId()));
    }

    public ArrayList<Order> getLastOrdersFromALlSuppliers(ArrayList<Integer> orderIds) {
        ArrayList<Order> result = new ArrayList<>();
        for(Integer orderId : orderIds){
            try(ConnectionHandler handler = getConnectionHandler()) {
                ResultSet instanceResult = select(handler.get(), orderId);
                while (instanceResult.next()) {
                    Order order = buildObject(instanceResult);
                    result.add(order);
                }
            } catch (Exception throwables) {
                System.out.println(throwables.getMessage());
            }
        }

        return result;
    }

    public int getGlobalId() {
        ArrayList<Integer> ids = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get());
            while (instanceResult.next()) {
                ids.add(instanceResult.getInt(1));
            }
        } catch (Exception throwables) {
            System.out.println(throwables.getMessage());
        }
        Collections.sort(ids, Collections.reverseOrder());
        if(ids.isEmpty())
            return 0;
        return ids.get(0);
    }

    public void removeSupplierOrders(int supplierId) throws SQLException {
        List<Integer> ordersIds = getSupplierOrdersIds(supplierId);
        orderItemDAO.removeOrders(ordersIds);
        remove(Arrays.asList(SUPPLIER_ID_COLUMN), Arrays.asList(supplierId));
    }

    public ArrayList<Integer> getSupplierOrdersIds(int supplierId) {
        ArrayList<Integer> ids = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(ORDER_ID_COLUMN),  Arrays.asList(SUPPLIER_ID_COLUMN), Arrays.asList(supplierId));
            while (instanceResult.next()) {
                ids.add(instanceResult.getInt(1));
            }
        } catch (Exception throwables) {
            System.out.println(throwables.getMessage());
        }
        return ids;
    }

    public ArrayList<OrderItem> uploadAllItemsFromOrder(int orderId, AgreementItemDAO agreementItemDAO) {
        return orderItemDAO.uploadAllItemsFromOrder(orderId, agreementItemDAO);
    }

    //for tests
    public void removeByStore(int store) {
        try (ConnectionHandler handler = getConnectionHandler()){
            ResultSet resultSet = select(handler.get(), Arrays.asList(STORE_ID_COLUMN), Arrays.asList(store));
            List<Integer> ordersIds = new ArrayList<>();
            while (resultSet.next()) {
                ordersIds.add(resultSet.getInt(ORDER_ID_COLUMN));
            }
            orderItemDAO.removeOrders(ordersIds);
            remove(Arrays.asList(STORE_ID_COLUMN),Arrays.asList(store));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<OrderItem> getItemsInDiscountInSUpplier(int supplierId,AgreementItemDAO agreementItemDAO) {
        List<OrderItem> items = new ArrayList<>();
        List<Integer> ids = getSupplierOrdersIds(supplierId);
        for(Integer orderId : ids){
            for(OrderItem orderItem : uploadAllItemsFromOrder(orderId, agreementItemDAO)){
                if(orderItem.getDiscount() != 0)
                    items.add(orderItem);
            }
        }
        return items;
    }

    public void setOrderArrivalTime(int orderId ,LocalDate date) throws SQLException {
        updateProperty(String.valueOf(orderId), ARRIVAL_TIME_COLUMN, String.valueOf(date));
    }


    public void setOrderItemMissingAmount(int itemId, int missingAmount) throws SQLException {
        orderItemDAO.updateMissingAmount(itemId, missingAmount);
    }

    public void setOrderItemDefectiveAmount(int itemId, int defectiveAmount) throws SQLException {
        orderItemDAO.updateDefectiveAmount(itemId, defectiveAmount);
    }

    public void setOrderItemDescription(int itemId, String desc) throws SQLException {
        orderItemDAO.updateDescription(itemId, desc);
    }
    public List<Order> getAllOrders() throws Exception {
        List<Order> orders = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()){
            ResultSet resultSet= select(handler.get());
            while (resultSet.next()){
                int id = resultSet.getInt(ORDER_ID_COLUMN);
                orders.add(getOrder(id,new SuppliersDAO()));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("we could not load data from the db");
        }
        return orders;
    }
}

