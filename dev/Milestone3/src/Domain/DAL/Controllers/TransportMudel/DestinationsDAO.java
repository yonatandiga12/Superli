package Domain.DAL.Controllers.TransportMudel;

import Domain.Business.Objects.Site.Address;
import Domain.Business.Objects.Site.Destination;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;
import Globals.Enums.ShippingAreas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DestinationsDAO extends DAO {

    private static Map<Integer, Destination> DESTINATION_IDENTITY_MAP = new HashMap<>();
    public DestinationsDAO() {
        super("Destinations");
        try(ConnectionHandler connection = getConnectionHandler()){
            ResultSet resultSet= select(connection.get());
            while (resultSet.next()){
                Destination destination= new Destination(resultSet.getInt(1),new Address(ShippingAreas.valueOf(resultSet.getString(5)),resultSet.getString(4)),resultSet.getString(2),resultSet.getString(3));
                DESTINATION_IDENTITY_MAP.put(destination.getId(),destination);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("we could not load data from the db");
        }
    }
    public Destination get(int id){
        return DESTINATION_IDENTITY_MAP.get(id);
    }

    public void save(Destination destination){
        if(!DESTINATION_IDENTITY_MAP.containsKey(destination.getId())){
            DESTINATION_IDENTITY_MAP.put(destination.getId(),destination);
        }
        try{
            super.remove(destination.getId());
            super.insert(Arrays.asList(destination.getId(),destination.getContactId(),destination.getPhoneNumber(),destination.getAddress().getExactAddress(),destination.getAddress().getShippingAreas()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void delete(int id) throws SQLException {
        if(DESTINATION_IDENTITY_MAP.containsKey(id)){
            DESTINATION_IDENTITY_MAP.remove(id);
        }
        super.remove(id);
    }

}
