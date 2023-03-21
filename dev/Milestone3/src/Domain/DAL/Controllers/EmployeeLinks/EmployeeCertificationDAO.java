package Domain.DAL.Controllers.EmployeeLinks;
import Domain.DAL.Abstract.LinkDAO;
import Globals.Enums.Certifications;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeCertificationDAO extends LinkDAO<Certifications> {

    public EmployeeCertificationDAO() {
        super("EmployeesCertifications");
    }

    @Override
    protected Certifications buildObject(ResultSet resultSet) throws SQLException {
        return Certifications.valueOf(resultSet.getString(2));
    }
}
