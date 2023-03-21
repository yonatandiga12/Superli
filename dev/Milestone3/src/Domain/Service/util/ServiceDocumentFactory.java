package Domain.Service.util;


import Domain.Service.Objects.Document.DestinationDocument;
import Domain.Service.Objects.Document.Document;
import Domain.Service.Objects.Document.TransportDocument;

public class ServiceDocumentFactory {
    public Document createServiceDocument(Domain.Business.Objects.Document.Document doc){
        return doc.accept(this);
    }

    public DestinationDocument createServiceDocument(Domain.Business.Objects.Document.DestinationDocument destinationDoc){
        return new DestinationDocument(destinationDoc);
    }

    public TransportDocument createServiceDocument(Domain.Business.Objects.Document.TransportDocument transportDoc){
        return new TransportDocument(transportDoc);
    }

}
