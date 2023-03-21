package Domain.DAL.Controllers.ShiftEmployeesLink;

import Domain.DAL.Abstract.LinkDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftsCarriersLink extends LinkDAO<String> {

    // properties
    public ShiftsCarriersLink() {
        super("ShiftsCarriers");
    }

    @Override
    protected String buildObject(ResultSet resultSet) throws SQLException {
        return resultSet.getString(2);
    }
}
