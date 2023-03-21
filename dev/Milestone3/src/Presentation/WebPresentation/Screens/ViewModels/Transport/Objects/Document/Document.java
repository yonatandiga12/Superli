package Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document;

abstract public class Document {
    private int documentSN;

    public Document(int documentSN) {
        this.documentSN = documentSN;
    }

    public int getDocumentSN() {
        return documentSN;
    }

    abstract public void display();
}
