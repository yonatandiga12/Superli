package Presentation.WebPresentation.Screens.ViewModels.Transport.Factories;

import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.DestinationDocument;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.Document;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.TransportDocument;

public class PresentationDocumentFactory {
    public Document createPresentationDocument(Domain.Service.Objects.Document.Document doc){
        return doc.accept(this);
    }

    public DestinationDocument createPresentationDocument(Domain.Service.Objects.Document.DestinationDocument destinationDoc){
        return new DestinationDocument(destinationDoc);
    }

    public TransportDocument createPresentationDocument(Domain.Service.Objects.Document.TransportDocument transportDoc){
        return new TransportDocument(transportDoc);
    }
}
