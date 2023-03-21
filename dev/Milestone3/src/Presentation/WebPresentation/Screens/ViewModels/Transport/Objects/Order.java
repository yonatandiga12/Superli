package Presentation.WebPresentation.Screens.ViewModels.Transport.Objects;

import Domain.Service.Objects.SupplierObjects.ServiceOrderItemObject;
import Globals.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int supplierId;
    private LocalDate creationDate;
    private LocalDate arrivalDate;
    private int storeId;
    private String status;
    private List<Pair<String,Integer>> orderItems;

    public Order(Domain.Service.Objects.SupplierObjects.ServiceOrderObject  order){
        this.orderId = order.getId();
        this.supplierId = order.getSupplierId();
        this.creationDate = order.getCreationDate();
        this.arrivalDate = order.getArrivalDate();
        this.storeId = order.getStoreId();
        this.status = order.getStatus();
        this.orderItems = new ArrayList<>();
        for(ServiceOrderItemObject item: order.getOrderItems()){
            orderItems.add(new Pair<>(item.getName(),item.getQuantity()));
        }
    }
    public String displayWeb(){
        String format = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/";
        format = format + "Order number: " + orderId + "/";
        format = format + "supplier Id - " + supplierId + "/";
        format = format + "to store Id - " + storeId + "/";
        format = format + "created in - " +  creationDate.toString() + "/";
        format = format + "in arrival expected date of  - " +  arrivalDate.toString() + "/";
        format = format + "current status - " + status + "/";
        if(orderItems.size() == 0){
            format = format + "not including items/ ";
            return format;
        }
        format = format + "Including items:/";
        for(Pair<String,Integer> item:orderItems){
            format = format + item.getLeft()+" in quantity- " + item.getRight()+"/";
        }
        return format + " ";
    }
}
