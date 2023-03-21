package Domain.Business.Objects.Document;

import Domain.Service.util.ServiceDocumentFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
public class TransportDocument extends Document{
    private int transportID;
    private String startTime;
    private int truckNumber;
    private String driverName;
    private List<Integer> destinationDocuments;
    private boolean doRedesign;
    private String redesign;//Write what do?

    public TransportDocument(int id,String startTime, int truckNumber, String driverName) {
        this.transportID = id;
        this.startTime = startTime;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        destinationDocuments = new ArrayList<>();
        this.doRedesign = false;
        this.redesign = "";
    }
    public int getTransportID(){
        return transportID;
    }

    public TransportDocument(int transportID,String startTime, int truckNumber, String driverName, boolean doRedesign, String redesign) {
        this.transportID = transportID;
        this.startTime =startTime;
        this.truckNumber = truckNumber;
        this.driverName = driverName;
        this.doRedesign = doRedesign;
        this.redesign = redesign;
        destinationDocuments = new ArrayList<>();
    }

    public String getStartTime() {
        return startTime;
    }

    public List<Integer> getDestinationDocuments() {
        return destinationDocuments;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime.toString();
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(int truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public List<Integer> getDocuments(){
        return destinationDocuments;
    }
    public void addDoc(Integer id){
        destinationDocuments.add(id);
    }

    public boolean isDoRedesign() {
        return doRedesign;
    }

    public void setDoRedesign(boolean doRedesign) {
        this.doRedesign = doRedesign;
    }

    public String getRedesign() {
        return redesign;
    }

    public void setRedesign(String redesign) {
        this.redesign = redesign;
    }

    @Override
    public Domain.Service.Objects.Document.TransportDocument accept(ServiceDocumentFactory serviceDocumentFactory) {
        return serviceDocumentFactory.createServiceDocument(this);
    }
}
