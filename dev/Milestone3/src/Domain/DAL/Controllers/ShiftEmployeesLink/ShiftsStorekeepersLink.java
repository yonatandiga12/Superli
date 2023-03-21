package Domain.DAL.Controllers.ShiftEmployeesLink;

import Domain.DAL.Abstract.LinkDAO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ShiftsStorekeepersLink extends LinkDAO<String> {
    public ShiftsStorekeepersLink() {
        super("ShiftsStorekeepers");
    }

    @Override
    protected String buildObject(ResultSet resultSet) throws SQLException {
        return resultSet.getString(2);
    }
}
