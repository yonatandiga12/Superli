package Domain.Service.Objects.Document;

import Presentation.WebPresentation.Screens.ViewModels.Transport.Factories.PresentationDocumentFactory;

public abstract class Document {
    private int documentSN;

    public Document(int documentSN) {
        this.documentSN = documentSN;
    }

    public int getDocumentSN() {
        return documentSN;
    }

    public abstract Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.Document accept(PresentationDocumentFactory presentationDocumentFactory);
}
