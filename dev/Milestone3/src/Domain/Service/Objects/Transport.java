package Domain.Service.Objects;


public class Transport {
    private int transportID;
    private String startTime;
    private String endTime;
    private String driverID;
    private  int truckNumber;
    private  int truckWeight;
    //Todo:
    //private List<Source> sources;
    //private List<Destination> destinations;
    //private HashMap<ShippingAreas, Integer> shippingAreas;
    public Transport(Domain.Business.Objects.Transport transport) {
        transportID = transport.getSN();
        startTime = transport.getStartTime();
        endTime = transport.getEndTime();
        driverID = transport.getDriverID();
        truckNumber = transport.getTruckNumber();
        truckWeight = transport.getTruckWeight();
    }

    public int getTransportID() {
        return transportID;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDriverID() {
        return driverID;
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public int getTruckWeight() {
        return truckWeight;
    }
}
