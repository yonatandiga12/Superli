package Domain.Business.Objects.Employee;

import Domain.DAL.Controllers.EmployeeMappers.EmployeeDataMapper;
import Domain.Service.util.ServiceEmployeeFactory;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;
import Globals.Enums.LicenseTypes;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Business model of the Carrier
 */
public class Carrier extends Employee {
    private Set<LicenseTypes> licenses;
    public Carrier(String id, String name, String bankDetails, int salary, LocalDate startingDate, Set<Certifications> certifications, Set<LicenseTypes> licenses) throws Exception {
        super(id, name, bankDetails, salary, startingDate,certifications);
        this.licenses = licenses;
    }


    protected void updateEmploymentConditions() {
        super.updateEmploymentConditions(JobTitles.Carrier);
    }

    @Override
    public Domain.Service.Objects.Employee.Employee accept(ServiceEmployeeFactory factory) {
        return factory.createServiceEmployee(this);
    }

    @Override
    public void save(EmployeeDataMapper employeeDataMapper) throws Exception {
        employeeDataMapper.save(this);
    }

    public Set<LicenseTypes> getLicenses() {
        return licenses;
    }

    public void setLicences(Set<LicenseTypes> licences) {
        this.licenses = new HashSet<>(licences);
    }


    @Override
    public void update(EmployeeDataMapper employeeDataMapper) throws Exception {
        employeeDataMapper.update(this);
    }

}
