package Domain.DAL.Controllers.EmployeeMappers;
import Domain.Business.Objects.Employee.Employee;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.Controllers.EmployeeLinks.EmployeeCertificationDAO;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractEmployeeDAO<T extends Employee> extends DataMapper<T> {

    // fields
    protected final EmployeeCertificationDAO employeeCertificationController;

    public AbstractEmployeeDAO(String tableName) {
        super(tableName);
        employeeCertificationController = new EmployeeCertificationDAO();
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        switch (setName){
            case "certifications":
                return  employeeCertificationController;
            default:
                throw new IllegalArgumentException("no such set");
        }
    }

    @Override
    public void insert(T instance) throws SQLException {
        employeeCertificationController.replaceSet(instance.getId(),instance.getCertifications());
        super.remove(instance.getId());
        super.insert(Arrays.asList(instance.getId(),instance.getName(),instance.getBankDetails(),instance.getSalary(),instance.getEmploymentConditions(),instance.getStartingDate()));
    }

    @Override
    protected  Set<LinkDAO> getAllLinkDTOs(){
        Set<LinkDAO> output = new HashSet<>();
        output.add(employeeCertificationController);
        return output;
    }
    public EmployeeCertificationDAO getEmployeeCertificationController(){
        return employeeCertificationController;
    }

    @Override
    public String instanceToId(T instance) {
        return instance.getId();
    }
}
