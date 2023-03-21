package Domain.Service.Objects.Document;

import Presentation.WebPresentation.Screens.ViewModels.Transport.Factories.PresentationDocumentFactory;

import java.util.List;

public class TransportDocument extends Document {
    private int transportID;
    private String startTime;
    private int truckNumber;
    private String driverName;
    private List<Integer> destinationDocuments;
    private boolean doRedesign;
    private String redesign;//Write what do?
    public TransportDocument(Domain.Business.Objects.Document.TransportDocument transportDoc) {
        super(transportDoc.getTransportID());
        transportID = transportDoc.getTransportID();
        startTime = transportDoc.getStartTime();
        truckNumber = transportDoc.getTruckNumber();
        driverName = transportDoc.getDriverName();
        destinationDocuments = transportDoc.getDestinationDocuments();
        doRedesign = transportDoc.isDoRedesign();
        redesign = transportDoc.getRedesign();
    }

    public int getTransportID() {
        return transportID;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public List<Integer> getDestinationDocuments() {
        return destinationDocuments;
    }

    public boolean isDoRedesign() {
        return doRedesign;
    }

    public String getRedesign() {
        return redesign;
    }

    @Override
    public Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.Document accept(PresentationDocumentFactory presentationDocumentFactory) {
        return presentationDocumentFactory.createPresentationDocument(this);
    }
}
