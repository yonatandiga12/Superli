package Domain.DAL.Controllers.ShiftEmployeesLink;

import Domain.DAL.Abstract.LinkDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftsLogisticManagersLink extends LinkDAO<String> {
    public ShiftsLogisticManagersLink() {
        super("ShiftsLogisticManagers");
    }

    @Override
    protected String buildObject(ResultSet resultSet) throws SQLException {
        return resultSet.getString(2);
    }
}