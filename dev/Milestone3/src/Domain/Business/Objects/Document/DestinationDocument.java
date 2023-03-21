package Domain.Business.Objects.Document;


import Domain.Service.util.ServiceDocumentFactory;

import java.util.ArrayList;
import java.util.List;

public class DestinationDocument extends  Document{
    private int ID;
    private int destID;
    private List<String> providedProducts;

    public DestinationDocument(int id,int destID, List<String> providedProducts) {
        ID = id;
        this.destID = destID;
        this.providedProducts = providedProducts;
    }
    public DestinationDocument(int id,int destID){
        ID = id;
        this.destID = destID;
        providedProducts = new ArrayList<>();
    }
    public int getID(){
        return ID;
    }

    public int getDestID() {
        return destID;
    }

    public void setDestID(int destID) {
        this.destID = destID;
    }

    public List<String> getProvidedProducts() {
        return providedProducts;
    }

    public void addProduct(String product){
        providedProducts.add(product);
    }

    @Override
    public Domain.Service.Objects.Document.Document accept(ServiceDocumentFactory serviceDocumentFactory) {
        return serviceDocumentFactory.createServiceDocument(this);
    }
}
