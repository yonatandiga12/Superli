package Domain.DAL.Controllers.TransportMudel;

import Domain.DAL.Abstract.LinkDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class TransportDestinationsDAO extends LinkDAO<Integer> {
    public TransportDestinationsDAO() {
        super("TransportDestination");
    }

    @Override
    protected Integer buildObject(ResultSet resultSet) throws SQLException {
        return resultSet.getInt(2);
    }

    public Set<Integer> get(int id) throws SQLException {
        return super.get(Integer.toString(id));
    }
}
