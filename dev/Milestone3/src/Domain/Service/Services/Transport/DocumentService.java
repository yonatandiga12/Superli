package Domain.Service.Services.Transport;

import Domain.Business.Controllers.Transport.DocumentController;
import Domain.Service.util.Result;
import Domain.Service.util.ServiceDocumentFactory;
public class DocumentService {
    private DocumentController controller;
    private final ServiceDocumentFactory serviceDocumentFactory = new ServiceDocumentFactory();

    public DocumentService() {
        controller = new DocumentController();
    }

    public Result getDestinationDocument(int destinationDocumentSN)
    {
        try {
            return Result.makeOk(serviceDocumentFactory.createServiceDocument(controller.getDestinationDocument(destinationDocumentSN)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }

    public Result getTransportDocument(int transportDocumentSN)
    {
        try {
            return Result.makeOk(serviceDocumentFactory.createServiceDocument(controller.getTransportDocument(transportDocumentSN)));
        }
        catch (Exception e){
            return Result.makeError(e.getMessage());
        }
    }
}
