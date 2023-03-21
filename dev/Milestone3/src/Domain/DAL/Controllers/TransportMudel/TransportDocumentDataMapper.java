package Domain.DAL.Controllers.TransportMudel;
import Domain.Business.Objects.Document.TransportDocument;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TransportDocumentDataMapper extends DAO {
    private final static Map<Integer, TransportDocument> TRANSPORT_DOCUMENTS_MAP = new HashMap<>();

    public TransportDocumentDataMapper() {
        super("TransportDocument");
        try(ConnectionHandler connection = getConnectionHandler()){
            TransportDocument document;
            ResultSet resultSet =select(connection.get());
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                if (!TRANSPORT_DOCUMENTS_MAP.containsKey(id)){
                    document = new TransportDocument(id,resultSet.getString(2),resultSet.getInt(3), resultSet.getString(4),resultSet.getBoolean(5),resultSet.getString(6));
                    TRANSPORT_DOCUMENTS_MAP.put(id,document);
                }
                else {
                    document = TRANSPORT_DOCUMENTS_MAP.get(id);
                }
                if(!document.getDocuments().contains(resultSet.getInt(7))){
                    document.getDocuments().add(resultSet.getInt(7));
                }
            }
        } catch (SQLException throwables) {
            throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
        }
    }

    //Methods:
    public TransportDocument get(int transportId) throws Exception {
        return TRANSPORT_DOCUMENTS_MAP.get(transportId);
    }

    public void save(TransportDocument document){
        if (!TRANSPORT_DOCUMENTS_MAP.containsKey(document.getTransportID()))
            TRANSPORT_DOCUMENTS_MAP.put(document.getTransportID(),document);
        try {
            super.remove(document.getTransportID());
            for(Integer doc :document.getDestinationDocuments() ){
                super.insert(Arrays.asList(document.getTransportID(),document.getStartTime(),document.getTruckNumber(),document.getDriverName(),document.isDoRedesign(),document.getRedesign(),doc));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
        }
    }

    public void remove(int id){
        TRANSPORT_DOCUMENTS_MAP.remove(id);
        try {
            super.remove(id);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
        }
    }
}
