package Domain.DAL.Controllers.EmployeeLinks;

import Domain.DAL.Abstract.LinkDAO;
import Globals.Enums.JobTitles;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeTypeLink extends LinkDAO<JobTitles> {
    public EmployeeTypeLink() {
        super("EmployeesType");
    }

    @Override
    protected JobTitles buildObject(ResultSet resultSet) throws SQLException {
        return JobTitles.valueOf(resultSet.getString(2));
    }
}
