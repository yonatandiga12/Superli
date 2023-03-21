package Domain.DAL.Controllers.ShiftEmployeesLink;

import Domain.DAL.Abstract.LinkDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftsTransportManagers extends LinkDAO<String> {
    public ShiftsTransportManagers() {
        super("ShiftsTransportManagers");
    }

    @Override
    protected String buildObject(ResultSet resultSet) throws SQLException {
        return resultSet.getString(2);
    }
}
