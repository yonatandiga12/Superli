package Domain.Business.Controllers.Transport;


import Domain.Business.Objects.Document.DestinationDocument;
import Domain.Business.Objects.Document.TransportDocument;
import Domain.DAL.Controllers.TransportMudel.DestinationDocumentDAO;
import Domain.DAL.Controllers.TransportMudel.TransportDocumentDataMapper;

//TODO not finished methods (ADD and GET) for each document
public class DocumentController {
    //TODO move to DAL objects
    private TransportDocumentDataMapper transportDocumentsDataMapper;
    private DestinationDocumentDAO destinationDocumentsDAO;

    public DocumentController() {
        transportDocumentsDataMapper = new TransportDocumentDataMapper();
        destinationDocumentsDAO = new DestinationDocumentDAO();
    }



    public void uploadDestinationDocument(DestinationDocument document) throws Exception {
        destinationDocumentsDAO.save(document);
    }
    //TODO check if the function is needed
    public void updateDestinationDocument(DestinationDocument document) throws Exception {
       destinationDocumentsDAO.save(document);
    }

    public DestinationDocument getDestinationDocument(int destinationDocumentSN) throws Exception {
        DestinationDocument document = destinationDocumentsDAO.get(destinationDocumentSN);
        if (document == null)
            throw new Exception("The document you requested does not exist!");

        return document;
    }

    public void uploadTransportDocument(TransportDocument document) throws Exception {
       transportDocumentsDataMapper.save(document);
    }
    //TODO check if the function is needed
    public void updateTransportDocument(TransportDocument document) throws Exception {
       transportDocumentsDataMapper.save(document);
    }

    public TransportDocument getTransportDocument(int transportDocumentSN) throws Exception {
        TransportDocument document = transportDocumentsDataMapper.get(transportDocumentSN);
        if(document != null)
        {
            return document;
        }
        throw new Exception("The document you requested does not exist!");
    }
}
