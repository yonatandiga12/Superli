package Domain.DAL.Controllers.EmployeeMappers;
import Domain.Business.Objects.Employee.Carrier;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.Controllers.EmployeeLinks.CarrierLicensesDAO;
import Globals.Enums.Certifications;
import Globals.Enums.LicenseTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CarrierDAO extends AbstractEmployeeDAO<Carrier> {

    private final static Map<String, Carrier> CARRIER_IDENTITY_MAP = new HashMap<>();
    private final CarrierLicensesDAO carrierLicensesDAO;

    public CarrierDAO() {
        super("Carriers");
        carrierLicensesDAO = new CarrierLicensesDAO();
    }


    @Override
    protected Map<String, Carrier> getMap() {
        return CARRIER_IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        switch (setName){
            case "licenses":
                return carrierLicensesDAO;
            default:
                return super.getLinkDTO(setName);
        }
    }

    @Override
    protected Carrier buildObject(ResultSet instanceResult) throws Exception {
        String id = instanceResult.getString(1);
        String name = instanceResult.getString(2);
        String bankDetails =instanceResult.getString(3);
        int salary = instanceResult.getInt(4);
        LocalDate startingDate = instanceResult.getDate(6).toLocalDate();
        Set<Certifications> certifications =  getEmployeeCertificationController().get(instanceResult.getString(1));
        Set<LicenseTypes> licenses = carrierLicensesDAO.get(instanceResult.getString(1));
        return new Carrier(id , name ,bankDetails, salary, startingDate, certifications, licenses);
    }

    @Override
    public void insert(Carrier instance) throws SQLException {
        carrierLicensesDAO.replaceSet(instance.getId(),instance.getLicenses());
        super.insert(instance);
    }
}
