package Domain.Business.Controllers.Transport;

import Domain.Business.Objects.Supplier.Order;
import Domain.DAL.Controllers.InventoryAndSuppliers.AgreementItemDAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.OrderDAO;
import Domain.DAL.Controllers.InventoryAndSuppliers.SuppliersDAO;
import Globals.Enums.OrderStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderController {
    private final OrderDAO transportOrderDataMapper = new OrderDAO();

    public OrderController() {
    }
    /*public void addTransportOrder(int daysToArrival, int supplierID, int storeID) throws Exception {

        TransportOrder order = new TransportOrder(srcID,dstID,productList);
        transportOrderDataMapper.save(order);
    }*/

    public Order getTransportOrder(String orderID) throws Exception {
        Order order = transportOrderDataMapper.getOrder(Integer.parseInt(orderID),new SuppliersDAO());
        transportOrderDataMapper.uploadAllItemsFromOrder(order.getId(), new AgreementItemDAO());
        if (order==null){
            throw new Exception("the order not found");
        }
        return order;
    }

    /*public int getExtraWeight(TransportOrder order) {
        int weight = 0;
        for (String productID : order.getProductList().keySet()) {
            weight = weight + (int) (products.get(productID).getWeight() * order.getProductList().get(productID));
        }
        return weight;
    }*/

    public Collection<Order> getPendingOrder() throws Exception {
        Collection<Order> padding = new ArrayList<>();
        Collection<Order> orders = transportOrderDataMapper.getAllOrders();
        for (Order order:orders){
            if(order.getStatus()== OrderStatus.waiting){
                padding.add(order);
            }
        }
        return padding;
    }

    public void updateOrder(Order order) throws SQLException {
        transportOrderDataMapper.updateStatus(order);
    }

    public String[] alertsToHR() throws Exception {

        List<Order> allOrders = transportOrderDataMapper.getAllOrders();
        List<Order> alertOrders = new ArrayList<>();
        for(Order o :allOrders){
            if(o.getStatus() == OrderStatus.waiting){
                alertOrders.add(o);
            }
        }
        String[] message = new String[alertOrders.size()];
        int place = 0 ;
        for (Order order:alertOrders) {
            message[place] = "Order "+order.getId()+" cannot be in transport in a week from now";
            place++;
        }
        return message;
    }
    public void setDate(LocalDate d,int id) throws SQLException {
        transportOrderDataMapper.setOrderArrivalTime(id,d);
    }
}
