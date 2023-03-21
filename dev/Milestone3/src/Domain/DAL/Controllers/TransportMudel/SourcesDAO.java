package Domain.DAL.Controllers.TransportMudel;

import Domain.Business.Objects.Site.Address;
import Domain.Business.Objects.Site.Source;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;
import Globals.Enums.ShippingAreas;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SourcesDAO extends DAO {
    private static Map<Integer, Source> SOURCE_IDENTITY_MAP = new HashMap<>();

    public SourcesDAO() {
        super("Sources");
        try(ConnectionHandler connection = getConnectionHandler()){
            ResultSet resultSet= select(connection.get());
            while (resultSet.next()){
                Source source = new Source(resultSet.getInt(1),new Address(ShippingAreas.valueOf(resultSet.getString(3)),resultSet.getString(2)),resultSet.getString(4),resultSet.getString(5));
                SOURCE_IDENTITY_MAP.put(source.getId(),source);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new RuntimeException("we could not load data from the db");
        }
    }
    public Source get(int id){
        return SOURCE_IDENTITY_MAP.get(id);
    }

    public void save(Source s){
        if(!SOURCE_IDENTITY_MAP.containsKey(s.getId())){
            SOURCE_IDENTITY_MAP.put(s.getId(),s);
        }
        try{
            super.remove(s.getId());
            super.insert(Arrays.asList(s.getId(),s.getAddress().getExactAddress(),s.getAddress().getShippingAreas(),s.getContactId(),s.getPhoneNumber()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void delete(int id) throws SQLException {
        if(SOURCE_IDENTITY_MAP.containsKey(id)){
            SOURCE_IDENTITY_MAP.remove(id);
        }
        super.remove(id);
    }
}
