package Domain.Business.Controllers.HR;

import Domain.Business.Objects.Employee.*;
import Domain.DAL.Controllers.EmployeeMappers.EmployeeDataMapper;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;
import Globals.Enums.LicenseTypes;
import Globals.ObserverInterfaces.EditCarrierLicenseObserver;
import Globals.ObserverInterfaces.RemoveEmployeeObserver;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EmployeeController {
    private static final String EmployeeNotFoundErrorMsg = "Employee id %s could not be found";
    private final static Set<RemoveEmployeeObserver> REMOVE_EMPLOYEE_OBSERVER = new HashSet<>();
    private final static Set<EditCarrierLicenseObserver> EDIT_CARRIER_LICENSE_OBSERVERS = new HashSet<>();

    // properties
    private final EmployeeDataMapper employeeDataMapper = new EmployeeDataMapper();

    //CREATE
    public void registerEmployee(JobTitles title, String id, String name, String bankDetails, int salary, LocalDate startingDate, Set<Certifications> certifications) throws Exception {
        checkUnusedEmployeeID(id);
        Employee employee = employeeDataMapper.get(id);
        if (employee != null)
            throw new Exception(String.format("An employee with ID: %s already exists: %s", id, employee.getName()));
        switch (title) {
            case Carrier:
                employeeDataMapper.save(new Carrier(id, name, bankDetails, salary, startingDate, certifications, new HashSet<>()));
                break;
            case Cashier:
                employeeDataMapper.save(new Cashier(id, name, bankDetails, salary, startingDate, certifications));
                break;
            case HR_Manager:
                employeeDataMapper.save(new HR_Manager(id, name, bankDetails, salary, startingDate, certifications));
                break;
            case Storekeeper:
                employeeDataMapper.save(new Storekeeper(id, name, bankDetails, salary, startingDate, certifications));
                break;
            case Logistics_Manager:
                employeeDataMapper.save(new Logistics_Manager(id, name, bankDetails, salary, startingDate, certifications));
                break;
            case Sorter:
                employeeDataMapper.save(new Sorter(id, name, bankDetails, salary, startingDate, certifications));
                break;
            case Transport_Manager:
                employeeDataMapper.save(new Transport_Manager(id, name, bankDetails, salary, startingDate, certifications));
                break;
        }
    }

    //READ
    public Employee getEmployee(String employeeID) throws Exception {
        Employee employee = employeeDataMapper.get(employeeID);
        if (employee == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return employee;
    }

    public Carrier getCarrier(String employeeID) throws Exception {
        Carrier carrier = employeeDataMapper.getCarrier(employeeID);
        if (carrier == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return carrier;
    }

    public Cashier getCashier(String employeeID) throws Exception {
        Cashier cashier = employeeDataMapper.getCashier(employeeID);
        if (cashier == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return cashier;
    }

    public Sorter getSorter(String employeeID) throws Exception {
        Sorter sorter = employeeDataMapper.getSorter(employeeID);
        if (sorter == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return sorter;
    }

    public Storekeeper getStorekeeper(String employeeID) throws Exception {
        Storekeeper storekeeper = employeeDataMapper.getStorekeeper(employeeID);
        if (storekeeper == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return storekeeper;
    }

    public HR_Manager getHR_Manager(String employeeID) throws Exception {
        HR_Manager hr_manager = employeeDataMapper.getHR_Manager(employeeID);
        if (hr_manager == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return hr_manager;
    }

    public Logistics_Manager getLogistics_Manager(String employeeID) throws Exception {
        Logistics_Manager logistics_manager = employeeDataMapper.getLogistics_Manager(employeeID);
        if (logistics_manager == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return logistics_manager;
    }

    public Transport_Manager getTransport_Manager(String employeeID) throws Exception {
        Transport_Manager transport_manager = employeeDataMapper.getTransport_Manager(employeeID);
        if (transport_manager == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, employeeID));
        return transport_manager;
    }

    public Set<Employee> getEmployee(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.get(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Carrier> getCarrier(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getCarrier(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Cashier> getCashier(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getCashier(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Sorter> getSorter(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getSorter(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Storekeeper> getStorekeeper(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getStorekeeper(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<HR_Manager> getHR_Manager(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getHR_Manager(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Logistics_Manager> getLogistics_Manager(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getLogistics_Manager(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Transport_Manager> getTransport_Manager(Set<String> workersId) throws Exception {
        return workersId.stream().map((id) -> {
            try {
                return employeeDataMapper.getTransport_Manager(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    public Set<Employee> getEmployee() throws Exception {
        return employeeDataMapper.get();
    }

    public Collection<Carrier> getCarrier() throws Exception {
        return employeeDataMapper.getCarrier();
    }

    public Collection<Cashier> getCashier() throws Exception {
        return employeeDataMapper.getCashier();
    }

    public Collection<Sorter> getSorter() throws Exception {
        return employeeDataMapper.getSorter();
    }

    public Collection<Storekeeper> getStorekeeper() throws Exception {
        return employeeDataMapper.getStorekeeper();
    }

    public Collection<HR_Manager> getHR_Manager() throws Exception {
        return employeeDataMapper.getHR_Manager();
    }

    public Collection<Logistics_Manager> getLogistics_Manager() throws Exception {
        return employeeDataMapper.getLogistics_Manager();
    }

    public Collection<Transport_Manager> getTransport_Manager() throws Exception {
        return employeeDataMapper.getTransport_Manager();
    }

    //UPDATE
    public void editEmployeeName(String id, String name) throws Exception {
        Employee employee = employeeDataMapper.get(id);
        if (employee == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, id));
        employee.setName(name);
        employeeDataMapper.save(employee);
    }

    public void editEmployeeBankDetails(String id, String bankDetails) throws Exception {
        Employee employee = employeeDataMapper.get(id);
        if (employee == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, id));
        employee.setBankDetails(bankDetails);
        employeeDataMapper.save(employee);
    }

    public void editEmployeeCertifications(String id, Set<Certifications> certifications) throws Exception {
        Employee employee = employeeDataMapper.get(id);
        if (employee == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, id));
        employee.setCertifications(certifications);
        employeeDataMapper.save(employee);
    }

    public void editEmployeeSalary(String id, int newSalary) throws Exception {
        Employee employee = employeeDataMapper.get(id);
        if (employee == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, id));
        employee.setSalary(newSalary);
        employeeDataMapper.save(employee);
    }

    public void editCarrierLicenses(String id, Set<LicenseTypes> licences) throws Exception {
        Carrier carrier = employeeDataMapper.getCarrier(id);
        if (carrier == null)
            throw new Exception(String.format(EmployeeNotFoundErrorMsg, id));
        for(EditCarrierLicenseObserver observer:EDIT_CARRIER_LICENSE_OBSERVERS)
            observer.observe(id,licences);
        carrier.setLicences(licences);
        employeeDataMapper.save(carrier);
    }

    //DELETE
    public void removeEmployee(String id) throws Exception {
        for (RemoveEmployeeObserver observer : REMOVE_EMPLOYEE_OBSERVER)
            observer.observe(id);

        employeeDataMapper.delete(id);
    }

    public void deleteData() {
    }

    //MISC
    public void checkUnusedEmployeeID(String id) throws Exception {
        Employee employee = employeeDataMapper.get(id);
        if (employee != null)
            throw new Exception(String.format("An employee with ID: %s already exists: %s", id, employee.getName()));
        Employee.validateLegalID(id);
    }

    public void validateID(String id) throws Exception {
        getEmployee(id);
    }

    public void validateIDs(Set<String> ids) throws Exception {
        for (String id : ids)
            validateID(id);
    }

    public String getEmploymentConditionsOf(String id) throws Exception {
        Employee employee = employeeDataMapper.get(id);
        if (employee == null)
            throw new RuntimeException(String.format(EmployeeNotFoundErrorMsg, id));
        return employee.getEmploymentConditions();
    }

    public void registerToRemoveEmployeeEvent(RemoveEmployeeObserver removeEmployeeObserver){
        REMOVE_EMPLOYEE_OBSERVER.add(removeEmployeeObserver);
    }

    public void registerToChangeCarrierLicenseEvent(EditCarrierLicenseObserver observer){
        EDIT_CARRIER_LICENSE_OBSERVERS.add(observer);
    }
}