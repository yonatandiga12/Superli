package Domain.Business.Objects.Employee;

import Domain.DAL.Controllers.EmployeeMappers.EmployeeDataMapper;
import Domain.Service.util.ServiceEmployeeFactory;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class representing Employee for business purposes
 */
public abstract class Employee {
    private static String SALARY_MOST_BE_POSITIVE_ERROR_MSG = "Salary must be positive, entered %f";
    private static String ID_VALIDATING_REGEX = "[0-9]+";

    // properties
    private final String id;
    private String name;
    private String bankDetails;
    private int salary;
    private String employmentConditions;
    private LocalDate startingDate;
    private Set<Certifications> certifications;

    /**
     * Raw data constructor
     *
     * @param id Employee's ID
     * @param name Employee's Name
     * @param bankDetails Employee's bank details
     * @param salary Employee's salary
     * @param startingDate Employee's Starting date
     * @param certifications Employees Certifications
     */
    public Employee(String id, String name, String bankDetails, int salary, LocalDate startingDate, Set<Certifications> certifications) throws Exception {
        validateLegalID(id);
        this.id = id;
        if (name.equals(""))
            throw new Exception("Employee name can't be empty");
        this.name = name;
        if (bankDetails.equals(""))
            throw new Exception("Bank Details can't be empty");
        this.bankDetails = bankDetails;
        if (salary < 0)
            throw new Exception("Can't have negative salary");
        this.salary = salary;
        this.startingDate = startingDate;
        this.certifications = certifications;
        updateEmploymentConditions();
    }



    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new NullPointerException("Name Cannot be null");
        this.name = name;
        updateEmploymentConditions();
    }

    public String getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(String bankDetails) {
        if (name == null)
            throw new NullPointerException("Bank Details Cannot be null");
        this.bankDetails = bankDetails;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int newSalary) {
        if (salary<=0)
            throw new IllegalArgumentException(String.format(SALARY_MOST_BE_POSITIVE_ERROR_MSG,salary));
        this.salary=newSalary;
        updateEmploymentConditions();
    }

    public String getEmploymentConditions() {
        return employmentConditions;
    }

    public void updateEmploymentConditions(JobTitles title){
        this.employmentConditions =
                "Name: " + name
                        + "\nID: " + id
                        + "\nJob title: " + title
                        + "\nStarting date: " + startingDate.toString()
                        + "\nSalary per shift: " + salary;
    }

    abstract protected void updateEmploymentConditions();

    public LocalDate getStartingDate() {
        return startingDate;
    }

    public Set<Certifications> getCertifications() {
        return certifications;
    }

    public void setCertifications(Set<Certifications> certifications) {
        this.certifications = new HashSet<>(certifications);
    }

    public abstract Domain.Service.Objects.Employee.Employee accept(ServiceEmployeeFactory factory);

    public static void validateLegalID(String id) throws Exception {
        if (!id.matches(ID_VALIDATING_REGEX))
            throw new Exception("ID has to be a non-empty string consisting ONLY of numbers!");
    }

    /**
     * visitor pattern with the function save in {@param employeeDataMapper}
     * @param employeeDataMapper the data mapper to call employeeDataMapper.save(this)
     */
    public abstract void save(EmployeeDataMapper employeeDataMapper) throws Exception;

    public abstract void update(EmployeeDataMapper employeeDataMapper) throws Exception;
}
