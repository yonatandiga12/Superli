package Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document;


import java.util.List;

public class DestinationDocument extends Document {
    private int destID;
    private List<String> providedProducts;
    public DestinationDocument(Domain.Service.Objects.Document.DestinationDocument destinationDocument) {
        super(destinationDocument.getDocumentSN());
        destID = destinationDocument.getDestID();
        providedProducts = destinationDocument.getProvidedProducts();
    }



    @Override
    public void display() {
        System.out.println("Destination Document:\n" +
                "Document SN: " + getDocumentSN() + "\n" +
                "Destination site ID: " + destID);
        printProvidedProductList();
    }
    private void printProvidedProductList()
    {
        System.out.println("Provided product:");
        for(int i = 0; i < providedProducts.size(); i++)
        {
            System.out.println("\t" + (i + 1) + " - " + providedProducts.get(i));
        }
    }

    public String webDisplay() {
        String format = "Destination Document:/" +
                "Document SN: " + getDocumentSN() + "/" +
                "Destination site ID: " + destID+"/";
        for(int i = 0; i < providedProducts.size(); i++)
        {
            format = format + "\t" + (i + 1) + " - " + providedProducts.get(i)+"/";
        }
        return format+" ";
    }
}
