package Domain.Business.Objects.Document;

import Domain.Service.util.ServiceDocumentFactory;
//TODO need to remove this ID
public abstract class Document {

    public Document() {
    }


    public abstract Domain.Service.Objects.Document.Document accept(ServiceDocumentFactory serviceDocumentFactory);
}
