package Domain.Service.Services.Transport;

import Domain.Business.Controllers.Transport.OrderController;
import Domain.Business.Objects.Supplier.Order;
import Domain.Service.util.Result;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderService {
    private OrderController order;
    public OrderService(){
        order = new OrderController();
    }
    public Result addOrder(int src,int dst,HashMap<Integer,Integer> product) {
        /*try {
            order.addTransportOrder(src,dst,product);
            return Result.makeOk(null);
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }*/
        return null;
    }
    //TODO convert to Order object
    private Set<Domain.Service.Objects.SupplierObjects.ServiceOrderObject> toTOService(List<Domain.Business.Objects.Supplier.Order> orders)
    {
        Set<Domain.Service.Objects.SupplierObjects.ServiceOrderObject> orderSet = new HashSet<>();
        /*for (Domain.Business.Objects.Supplier.Order order: orders) {
            orderSet.add(new ServiceOrderObject(order.getId(), order.getSupplierId() , order.getCreationTime(),
                    order.getArrivaltime(), order.getStoreID(), order.getStatusString(), order.getOrderItems()));
        }*/
        return orderSet;
    }
    public Result getPendingOrders() {
        try {
            return Result.makeOk(toTOService((List<Order>) (order.getPendingOrder())));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }
    public String[] getImportantMessages() throws Exception {
        return order.alertsToHR();
    }

}
