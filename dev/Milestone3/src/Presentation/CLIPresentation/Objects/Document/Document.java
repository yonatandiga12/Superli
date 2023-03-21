package Presentation.CLIPresentation.Objects.Document;

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
