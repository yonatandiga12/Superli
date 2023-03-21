package Domain.Service.Services.HR;

import Domain.Business.Controllers.HR.EmployeeController;
import Domain.Service.Objects.Employee.*;
import Domain.Service.util.Result;
import Domain.Service.util.ServiceEmployeeFactory;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;
import Globals.Enums.LicenseTypes;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service controller for employee operations
 *
 * All EmployeeService's methods return Results detailing the success. encapsulating values/error messages
 */
public class EmployeeService {
    private final EmployeeController controller = new EmployeeController();
    private final ServiceEmployeeFactory factory = new ServiceEmployeeFactory();

    /**
     * Calls for employee data deletion
     *
     * @return Result detailing success of operation
     */
    public Result<Object> deleteData() {
        try {
            controller.deleteData();
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //CREATE

    /**
     * Registers new employee
     *
     * @param title                Type of the employee
     * @param id                   Employee's ID
     * @param name                 Employee's name
     * @param bankDetails          Employee's bank details
     * @param salary               Employee's salary
     * @param startingDate         Employee's starting date
     * @param certifications       All of the employee's certifications
     * @return Result detailing process' success
     */
    public Result<Object> registerEmployee(JobTitles title, String id, String name, String bankDetails, int salary, LocalDate startingDate, Set<Certifications> certifications) {
        try {
            controller.registerEmployee(title, id, name, bankDetails, salary, startingDate, certifications);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //READ

    /**
     * Getter for employee
     *
     * @param id ID of the employee we want to get
     * @return Result holding requested employee of error message if failed
     */
    public Result<Employee> getEmployee(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getEmployee(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Carrier> getCarrier(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getCarrier(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Cashier> getCashier(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getCashier(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Sorter> getSorter(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getSorter(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Storekeeper> getStorekeeper(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getStorekeeper(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<HR_Manager> getHR_Manager(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getHR_Manager(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Logistics_Manager> getLogistics_Manager(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getLogistics_Manager(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Transport_Manager> getTransport_Manager(String id) {
        try {
            return Result.makeOk(factory.createServiceEmployee(controller.getTransport_Manager(id)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getEmployees(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getEmployee(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Carrier>> getCarriers(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getCarrier(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Cashier>> getCashiers(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getCashier(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Sorter>> getSorters(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getSorter(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Storekeeper>> getStorekeepers(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getStorekeeper(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<HR_Manager>> getHR_Managers(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getHR_Manager(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Logistics_Manager>> getLogistics_Managers(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getLogistics_Manager(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Transport_Manager>> getTransport_Managers(Set<String> employeeIDs) {
        try {
            return Result.makeOk(controller.getTransport_Manager(employeeIDs).stream().map(factory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAllEmployees() {
        try {
            return Result.makeOk(controller.getEmployee().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Carrier>> getAllCarriers() {
        try {
            return Result.makeOk(controller.getCarrier().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Cashier>> getAllCashiers() {
        try {
            return Result.makeOk(controller.getCashier().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Sorter>> getAllSorters() {
        try {
            return Result.makeOk(controller.getSorter().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Storekeeper>> getAllStorekeepers() {
        try {
            return Result.makeOk(controller.getStorekeeper().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<HR_Manager>> getAllHR_Managers() {
        try {
            return Result.makeOk(controller.getHR_Manager().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Logistics_Manager>> getAllLogistics_Managers() {
        try {
            return Result.makeOk(controller.getLogistics_Manager().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Transport_Manager>> getAllTransport_Managers() {
        try {
            return Result.makeOk(controller.getTransport_Manager().stream().map((factory::createServiceEmployee)).collect(Collectors.toSet()));
        } catch (Exception e) {
            e.printStackTrace();
            return Result.makeError(e.getMessage());
        }
    }

    //UPDATE

    /**
     * Edit's employee's name
     *
     * @param id   Employee's ID
     * @param name Employee's new name
     * @return Result detailing process' success
     */
    public Result<Object> editEmployeeName(String id, String name) {
        try {
            controller.editEmployeeName(id, name);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editEmployeeBankDetails(String id, String bankDetails) {
        try {
            controller.editEmployeeBankDetails(id, bankDetails);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editEmployeeCertifications(String id, Set<Certifications> certifications) {
        try {
            controller.editEmployeeCertifications(id, certifications);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editEmployeeSalary(String id, int newSalary) {
        try {
            controller.editEmployeeSalary(id, newSalary);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editCarrierLicenses(String id, Set<LicenseTypes> Licences) {
        try {
            controller.editCarrierLicenses(id, Licences);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //DELETE

    public Result<Object> removeEmployee(String id) {
        try {
            controller.removeEmployee(id);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> validateID(String id) {
        try {
            controller.validateID(id);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> validateIDs(Set<String> ids) {
        try {
            controller.validateIDs(ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //TODO: try to get rid of this

    public Result<Object> checkUnusedEmployeeID(String id) {
        try {
            controller.checkUnusedEmployeeID(id);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<String> getEmploymentConditionsOf(String id) {
        try {
            return Result.makeOk(controller.getEmploymentConditionsOf(id));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }
}