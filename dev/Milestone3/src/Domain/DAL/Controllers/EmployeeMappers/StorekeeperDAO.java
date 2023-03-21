package Domain.DAL.Controllers.EmployeeMappers;

import Domain.Business.Objects.Employee.Storekeeper;
import Domain.DAL.Controllers.EmployeeLinks.EmployeeCertificationDAO;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class StorekeeperDAO extends AbstractEmployeeDAO<Storekeeper> {
    private static Map<String, Storekeeper> STOREKEEPER_IDENTITY_MAP = new HashMap<>();
    EmployeeCertificationDAO employeeCertificationController ;


    public StorekeeperDAO() {
        super("Storekeepers");
        employeeCertificationController= new EmployeeCertificationDAO();
    }




    @Override
    protected Map<String, Storekeeper> getMap() {
        return STOREKEEPER_IDENTITY_MAP;
    }


    @Override
    protected Storekeeper buildObject(ResultSet instanceResult) throws Exception {
        return new Storekeeper(instanceResult.getString(1),instanceResult.getString(2),instanceResult.getString(3),instanceResult.getInt(4),instanceResult.getDate(6).toLocalDate(),employeeCertificationController.get(instanceResult.getString(1)));
    }

}
