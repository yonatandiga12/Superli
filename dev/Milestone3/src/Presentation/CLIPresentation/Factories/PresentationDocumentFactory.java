package Presentation.CLIPresentation.Factories;

import Presentation.CLIPresentation.Objects.Document.DestinationDocument;
import Presentation.CLIPresentation.Objects.Document.Document;
import Presentation.CLIPresentation.Objects.Document.TransportDocument;

public class PresentationDocumentFactory {
    public Document createPresentationDocument(Domain.Service.Objects.Document.Document doc){
        return null;
    }

    public DestinationDocument createPresentationDocument(Domain.Service.Objects.Document.DestinationDocument destinationDoc){
        return new DestinationDocument(destinationDoc);
    }

    public TransportDocument createPresentationDocument(Domain.Service.Objects.Document.TransportDocument transportDoc){
        return new TransportDocument(transportDoc);
    }
}
