package Domain.DAL.Controllers.EmployeeLinks;

import Domain.DAL.Abstract.LinkDAO;
import Globals.Enums.LicenseTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CarrierLicensesDAO extends LinkDAO<LicenseTypes> {
    // dict of employeeID and its Licenses
    public CarrierLicensesDAO() {
        super("CarriersLicenses");
    }

    @Override
    protected LicenseTypes buildObject(ResultSet resultSet) throws SQLException {
        return LicenseTypes.valueOf(resultSet.getString(2));
    }
}
