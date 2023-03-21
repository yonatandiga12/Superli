package Presentation.CLIPresentation.Objects.Transport;

public class Transport {
    private int transportID;
    private String startTime;
    private String endTime;
    private String driverID;
    private  int truckNumber;
    private  int truckWeight;

    public Transport(Domain.Service.Objects.Transport transport) {
        this.transportID = transport.getTransportID();
        this.startTime = transport.getStartTime();
        this.endTime = transport.getEndTime();
        this.driverID = transport.getDriverID();
        this.truckNumber = transport.getTruckNumber();
        this.truckWeight = transport.getTruckWeight();
    }

    public Transport(int transportID, String startTime, String endTime, String driverID, int truckNumber, int truckWeight) {
        this.transportID = transportID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.driverID = driverID;
        this.truckNumber = truckNumber;
        this.truckWeight = truckWeight;
    }

    public void display()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Transport ID: " + transportID);
        printDates();
        printCarrier();
        printTruckDetails();
    }
    private String printCarrier()
    {
        if(driverID.equals(""))
        {
            return "Carrier ID: The carrier has not been assigned yet!";
        }
        else
        {
            return "Carrier ID: " + driverID;
        }

    }

    private String printTruckDetails()
    {
        if(truckNumber == -1)
        {
            return "Truck License Number:  The truck has not been installed yet!";
        }
        else
        {
            return "Truck License Number: " + truckNumber +
                    "/Truck Weight: " + truckWeight;
        }

    }

    private String printDates()
    {
        String dateInfo = "";
        if(!startTime.equals(""))
        {
            dateInfo = "Start Time: " + startTime +"/";
        }
        else {
            dateInfo = "Start Time: The transport has not left yet!/";
    }
        if(!endTime.equals(""))
        {
            dateInfo = dateInfo +"End Time: " + endTime;
        }
        else {
            dateInfo = dateInfo + "End Time: The Transport has not been completed yet!";
        }
        return dateInfo;
    }
    public String displayWeb(){
        String format = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~/";
        format = format + "Transport ID: " + transportID + "/";
        format = format + printDates() + "/";
        format = format + printCarrier() + "/";
        format = format + printTruckDetails()+"/s";
        return format;
    }

}
