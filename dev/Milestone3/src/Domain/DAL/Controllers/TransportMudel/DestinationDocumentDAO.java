package Domain.DAL.Controllers.TransportMudel;

import Domain.Business.Objects.Document.DestinationDocument;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DestinationDocumentDAO extends DAO {
    private static Map<Integer, DestinationDocument> DESTINATION_DOCUMENT_IDENTITY_MAP = new HashMap<>();
    public DestinationDocumentDAO() {
        super("DestinationsDocuments");
        try(ConnectionHandler connection = getConnectionHandler()) {
            ResultSet resultSet= select(connection.get());
            while (resultSet.next()){
                DestinationDocument document;
                if(!DESTINATION_DOCUMENT_IDENTITY_MAP.containsKey(resultSet.getInt(1))){
                     document = new DestinationDocument(resultSet.getInt(1),resultSet.getInt(2));
                    DESTINATION_DOCUMENT_IDENTITY_MAP.put(document.getID(),document);
                }
                else {
                    document = DESTINATION_DOCUMENT_IDENTITY_MAP.get(resultSet.getInt(1));
                }
                if(!document.getProvidedProducts().contains(resultSet.getString(3))){
                    document.addProduct(resultSet.getString(3));
                }

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("we could not load data from the db");
        }
    }
    public void save(DestinationDocument document){
        if(!DESTINATION_DOCUMENT_IDENTITY_MAP.containsKey(document.getID())){
            DESTINATION_DOCUMENT_IDENTITY_MAP.put(document.getID(),document);
        }
        try{
            super.remove(document.getID());
            for(String product:document.getProvidedProducts()){
                super.insert(Arrays.asList(document.getID(),document.getDestID(),product));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public DestinationDocument get(int id){
        return DESTINATION_DOCUMENT_IDENTITY_MAP.get(id);
    }
}
