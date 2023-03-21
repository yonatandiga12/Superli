package Presentation;

import Domain.Business.Controllers.InventoryController;
import Domain.Service.Objects.Employee.*;
import Domain.Service.Objects.InventoryObjects.*;
import Domain.Service.Objects.Shift.Shift;
import Domain.Service.Objects.SupplierObjects.ServiceItemObject;
import Domain.Service.Objects.SupplierObjects.ServiceOrderItemObject;
import Domain.Service.Objects.SupplierObjects.ServiceOrderObject;
import Domain.Service.Objects.SupplierObjects.ServiceSupplierObject;
import Domain.Service.Services.HR.EmployeeService;
import Domain.Service.Services.HR.ShiftService;
import Domain.Service.Services.InventoryService;
import Domain.Service.Services.SupplierService;
import Domain.Service.Services.Transport.DocumentService;
import Domain.Service.Services.Transport.OrderService;
import Domain.Service.Services.Transport.TransportService;
import Domain.Service.Services.Transport.TruckService;
import Domain.Service.util.Result;
import Globals.Enums.*;
import Globals.Pair;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Factories.PresentationDocumentFactory;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.DestinationDocument;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Document.TransportDocument;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Order;
import Presentation.WebPresentation.Screens.ViewModels.Transport.Objects.Transport;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BackendController {
    private final EmployeeService employeeService = new EmployeeService();
    private final ShiftService shiftService = new ShiftService();
    private final TruckService truckService = new TruckService();
    private final DocumentService documentService = new DocumentService();
    private final TransportService transportService = new TransportService();
    private final OrderService orderService = new OrderService();
    private final PresentationDocumentFactory presentationDocumentFactory = new PresentationDocumentFactory();
    private final SupplierService supplierService = new SupplierService();
    private final InventoryService inventoryService = new InventoryService();

    ///EMPLOYEES
    //CREATE

    public BackendController(){
        supplierService.setInventoryController(InventoryController.getInventoryController());
        inventoryService.setSupplierController(supplierService.getSupplierController());
        getAvailableOrders();
    }

    public void addEmployee(JobTitles jobTitle, String id, String name, String bankDetails, Integer salary, LocalDate startingDate, Set<Certifications> certifications) throws Exception {
        Result<Object> result = employeeService.registerEmployee(jobTitle, id, name, bankDetails, salary, startingDate, certifications);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //READ

    public Employee getEmployee(String id) throws Exception {
        Result<Employee> result = employeeService.getEmployee(id);
        isError(result);
        return result.getValue();
    }

    public Carrier getCarrier(String id) throws Exception {
        Result<Carrier> result = employeeService.getCarrier(id);
        isError(result);
        return result.getValue();
    }

    public Cashier getCashier(String id) throws Exception {
        Result<Cashier> result = employeeService.getCashier(id);
        isError(result);
        return result.getValue();
    }

    public Sorter getSorter(String id) throws Exception {
        Result<Sorter> result = employeeService.getSorter(id);
        isError(result);
        return result.getValue();
    }

    public Storekeeper getStorekeeper(String id) throws Exception {
        Result<Storekeeper> result = employeeService.getStorekeeper(id);
        isError(result);
        return result.getValue();
    }

    public HR_Manager getHR_Manager(String id) throws Exception {
        Result<HR_Manager> result = employeeService.getHR_Manager(id);
        isError(result);
        return result.getValue();
    }

    public Logistics_Manager getLogistics_Manager(String id) throws Exception {
        Result<Logistics_Manager> result = employeeService.getLogistics_Manager(id);
        isError(result);
        return result.getValue();
    }

    public Transport_Manager getTransport_Manager(String id) throws Exception {
        Result<Transport_Manager> result = employeeService.getTransport_Manager(id);
        isError(result);
        return result.getValue();
    }

    public Set<Employee> getEmployees(Set<String> employeeIDs) throws Exception {
        Result<Set<Employee>> result = employeeService.getEmployees(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Carrier> getCarriers(Set<String> employeeIDs) throws Exception {
        Result<Set<Carrier>> result = employeeService.getCarriers(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Cashier> getCashiers(Set<String> employeeIDs) throws Exception {
        Result<Set<Cashier>> result = employeeService.getCashiers(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Sorter> getSorters(Set<String> employeeIDs) throws Exception {
        Result<Set<Sorter>> result = employeeService.getSorters(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Storekeeper> getStorekeepers(Set<String> employeeIDs) throws Exception {
        Result<Set<Storekeeper>> result = employeeService.getStorekeepers(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<HR_Manager> getHR_Managers(Set<String> employeeIDs) throws Exception {
        Result<Set<HR_Manager>> result = employeeService.getHR_Managers(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Logistics_Manager> getLogistics_Managers(Set<String> employeeIDs) throws Exception {
        Result<Set<Logistics_Manager>> result = employeeService.getLogistics_Managers(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Transport_Manager> getTransport_Managers(Set<String> employeeIDs) throws Exception {
        Result<Set<Transport_Manager>> result = employeeService.getTransport_Managers(employeeIDs);
        isError(result);
        return result.getValue();
    }

    public Set<Employee> getAllEmployees() throws Exception {
        Result<Set<Employee>> result = employeeService.getAllEmployees();
        isError(result);
        return result.getValue();
    }

    public Set<Cashier> getAllCashiers() throws Exception {
        Result<Set<Cashier>> result = employeeService.getAllCashiers();
        isError(result);
        return result.getValue();
    }

    public Set<Carrier> getAllCarriers() throws Exception {
        Result<Set<Carrier>> result = employeeService.getAllCarriers();
        isError(result);
        return result.getValue();
    }

    public Set<Storekeeper> getAllStorekeepers() throws Exception {
        Result<Set<Storekeeper>> result = employeeService.getAllStorekeepers();
        isError(result);
        return result.getValue();
    }

    public Set<Sorter> getAllSorters() throws Exception {
        Result<Set<Sorter>> result = employeeService.getAllSorters();
        isError(result);
        return result.getValue();
    }

    public Set<HR_Manager> getAllHR_Managers() throws Exception {
        Result<Set<HR_Manager>> result = employeeService.getAllHR_Managers();
        isError(result);
        return result.getValue();
    }

    public Set<Logistics_Manager> getAllLogistics_Managers() throws Exception {
        Result<Set<Logistics_Manager>> result = employeeService.getAllLogistics_Managers();
        isError(result);
        return result.getValue();
    }

    public Set<Transport_Manager> getAllTransport_Managers() throws Exception {
        Result<Set<Transport_Manager>> result = employeeService.getAllTransport_Managers();
        isError(result);
        return result.getValue();
    }

    //TODO: try to get rid of this

    public String getEmploymentConditionsOf(String id) throws Exception {
        Result<String> result = employeeService.getEmploymentConditionsOf(id);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    //UPDATE

    public void editEmployeeName(String eID, String newName) throws Exception {
        Result<Object> result = employeeService.editEmployeeName(eID, newName);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editEmployeeBankDetails(String eID, String newNankDetails) throws Exception {
        Result<Object> result = employeeService.editEmployeeBankDetails(eID, newNankDetails);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editEmployeeSalary(String eID, int newSalary) throws Exception {
        Result<Object> result = employeeService.editEmployeeSalary(eID, newSalary);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editEmployeeCertifications(String eId, Set<Certifications> newCertifications) throws Exception {
        Result<Object> result = employeeService.editEmployeeCertifications(eId, newCertifications);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editCarrierLicenses(String cId, Set<LicenseTypes> newLicenses) throws Exception {
        Result<Object> result = employeeService.editCarrierLicenses(cId, newLicenses);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //DELETE

    public void removeEmployee(String id) throws Exception {
        Result<Object> result = employeeService.removeEmployee(id);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //MISC

    public void checkUnusedEmployeeID(String id) throws Exception {
        Result<Object> result = employeeService.checkUnusedEmployeeID(id);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void validateID(String id) throws Exception {
        Result<Object> result = employeeService.validateID(id);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void validateIDs(Set<String> ids) throws Exception {
        Result<Object> result = employeeService.validateIDs(ids);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    ///SHIFTS
    //CREATE

    public void createShift(LocalDate date, ShiftTypes type, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount, int transport_managerCount) throws Exception {
        Result<Object> result = shiftService.createShift(date, type, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount, transport_managerCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //READ

    public Shift getShift(LocalDate date, ShiftTypes type) throws Exception {
        Result<Shift> result = shiftService.getShift(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public String getEmployeeWorkDetailsForCurrentMonth(String id) throws Exception {
        Result<String> result = shiftService.getEmployeeWorkDetailsForCurrentMonth(id);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Shift> getShiftsBetween(LocalDate start, LocalDate end) throws Exception {
        Result<Set<Shift>> result = shiftService.getShiftsBetween(start, end);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Shift> getEmployeeShiftsBetween(String eId, LocalDate start, LocalDate end) throws Exception {
        Result<Set<Shift>> result = shiftService.getEmployeeShiftsBetween(eId, start, end);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableShiftManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableShiftManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedSortersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedSortersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedStorekeepersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedStorekeepersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedCarriersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedCarriersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedCashiersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedCashiersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedHR_ManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedHR_ManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedLogistics_ManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedLogistics_ManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedTransports_ManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedTransport_ManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAssignedJobTitleFor(LocalDate date, ShiftTypes type, JobTitles title) throws Exception {
       if (title.equals(JobTitles.Carrier))
           return getAssignedCarriersFor(date, type);
        if (title.equals(JobTitles.Cashier))
            return getAssignedCashiersFor(date, type);
        if (title.equals(JobTitles.Sorter))
            return getAssignedSortersFor(date, type);
        if (title.equals(JobTitles.Storekeeper))
            return getAssignedStorekeepersFor(date, type);
        if (title.equals(JobTitles.HR_Manager))
            return getAssignedHR_ManagersFor(date, type);
        if (title.equals(JobTitles.Logistics_Manager))
            return getAssignedLogistics_ManagersFor(date, type);
        if (title.equals(JobTitles.Transport_Manager))
            return getAssignedTransports_ManagersFor(date, type);
       return null;
    }

    public Set<Employee> getAvailableSortersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableSortersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableStorekeepersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableStorekeepersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableCarriersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableCarriersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableCashiersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableCashiersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableHR_ManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableHR_ManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableLogistics_ManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedLogistics_ManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableTransports_ManagersFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAssignedTransport_ManagersFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Employee> getAvailableJobTitleFor(LocalDate date, ShiftTypes type, JobTitles title) throws Exception {
        if (title.equals(JobTitles.Carrier))
            return getAvailableCarriersFor(date, type);
        if (title.equals(JobTitles.Cashier))
            return getAvailableCashiersFor(date, type);
        if (title.equals(JobTitles.Sorter))
            return getAvailableSortersFor(date, type);
        if (title.equals(JobTitles.Storekeeper))
            return getAvailableStorekeepersFor(date, type);
        if (title.equals(JobTitles.HR_Manager))
            return getAvailableHR_ManagersFor(date, type);
        if (title.equals(JobTitles.Logistics_Manager))
            return getAvailableLogistics_ManagersFor(date, type);
        if (title.equals(JobTitles.Transport_Manager))
            return getAssignedTransports_ManagersFor(date, type);
        return null;
    }

    //UPDATE

    public void registerToConstraint(String eId, Shift shift) throws Exception {
        Result<Object> result = shiftService.registerAsAvailable(shift.date, shift.getType(), eId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void unregisterFromConstraint(String eId, Shift shift) throws Exception{
        Result<Object> result = shiftService.unregisterFromAvailable(shift.date, shift.getType(), eId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftManagerID(LocalDate date, ShiftTypes type, String shiftManagerId) throws Exception {
        validateID(shiftManagerId);
        Result<Object> result = shiftService.editShiftManagerID(date, type, shiftManagerId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftCarrierCount(LocalDate date , ShiftTypes type, int newCarrierCount) throws Exception {
        Result<Object> result = shiftService.editShiftCarrierCount(date, type, newCarrierCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftCashierCount(LocalDate date , ShiftTypes type, int newCashierCount) throws Exception {
        Result<Object> result = shiftService.editShiftCashierCount(date, type, newCashierCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftSorterCount(LocalDate date , ShiftTypes type, int newSorterCount) throws Exception {
        Result<Object> result = shiftService.editShiftSorterCount(date, type, newSorterCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftStorekeeperCount(LocalDate date , ShiftTypes type, int newStorekeeperCount) throws Exception {
        Result<Object> result = shiftService.editShiftStorekeeperCount(date, type, newStorekeeperCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftHR_ManagerCount(LocalDate date , ShiftTypes type,int  newHr_managersCount) throws Exception {
        Result<Object> result = shiftService.editShiftHR_ManagerCount(date, type, newHr_managersCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftLogistics_ManagerCount(LocalDate date , ShiftTypes type, int newLogistics_managersCount) throws Exception {
        Result<Object> result = shiftService.editShiftLogistics_ManagerCount(date, type, newLogistics_managersCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftTransport_ManagerCount(LocalDate date , ShiftTypes type, int transport_managersCount) throws Exception {
        Result<Object> result = shiftService.editShiftTransport_ManagerCount(date, type, transport_managersCount);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftJobtitleCount(LocalDate date, ShiftTypes type, int newCount, JobTitles title) throws Exception {
        if (title.equals(JobTitles.Carrier))
            editShiftCarrierCount(date, type, newCount);
        if (title.equals(JobTitles.Cashier))
            editShiftCashierCount(date, type, newCount);
        if (title.equals(JobTitles.Sorter))
            editShiftSorterCount(date, type, newCount);
        if (title.equals(JobTitles.Storekeeper))
            editShiftStorekeeperCount(date, type, newCount);
        if (title.equals(JobTitles.HR_Manager))
            editShiftHR_ManagerCount(date, type, newCount);
        if (title.equals(JobTitles.Logistics_Manager))
            editShiftLogistics_ManagerCount(date, type, newCount);
        if (title.equals(JobTitles.Transport_Manager))
            editShiftTransport_ManagerCount(date, type, newCount);
    }

    public void editShiftCarrierIDs(LocalDate date, ShiftTypes type, Set<String> newCarrierIDs) throws Exception {
        validateIDs(newCarrierIDs);
        Result<Object> result = shiftService.editShiftCarrierIDs(date, type, newCarrierIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftCashierIDs(LocalDate date, ShiftTypes type, Set<String> newCashierIDs) throws Exception {
        validateIDs(newCashierIDs);
        Result<Object> result = shiftService.editShiftCashierIDs(date, type, newCashierIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftStorekeeperIDs(LocalDate date, ShiftTypes type, Set<String> newStorekeeperIDs) throws Exception {
        validateIDs(newStorekeeperIDs);
        Result<Object> result = shiftService.editShiftStorekeeperIDs(date, type, newStorekeeperIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftSorterIDs(LocalDate date, ShiftTypes type, Set<String> newSorterIDs) throws Exception {
        validateIDs(newSorterIDs);
        Result<Object> result = shiftService.editShiftSorterIDs(date, type, newSorterIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftHR_ManagerIDs(LocalDate date, ShiftTypes type, Set<String> newHr_managerIDs) throws Exception {
        validateIDs(newHr_managerIDs);
        Result<Object> result = shiftService.editShiftHR_ManagerIDs(date, type, newHr_managerIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftLogistics_ManagerIDs(LocalDate date, ShiftTypes type, Set<String> newLogistics_managerIDs) throws Exception {
        validateIDs(newLogistics_managerIDs);
        Result<Object> result = shiftService.editShiftLogistics_ManagerIDs(date, type, newLogistics_managerIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftTransport_ManagerIDs(LocalDate date, ShiftTypes type, Set<String> newTransport_managersIDs) throws Exception {
        validateIDs(newTransport_managersIDs);
        Result<Object> result = shiftService.editShiftTransport_ManagerIDs(date, type, newTransport_managersIDs);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    public void editShiftJobTitleIDs(LocalDate date, ShiftTypes type, Set<String> newAssigned, JobTitles title) throws Exception {
        if (title.equals(JobTitles.Carrier))
            editShiftCarrierIDs(date, type, newAssigned);
        if (title.equals(JobTitles.Cashier))
            editShiftCashierIDs(date, type, newAssigned);
        if (title.equals(JobTitles.Sorter))
            editShiftSorterIDs(date, type, newAssigned);
        if (title.equals(JobTitles.Storekeeper))
            editShiftStorekeeperIDs(date, type, newAssigned);
        if (title.equals(JobTitles.HR_Manager))
            editShiftHR_ManagerIDs(date, type, newAssigned);
        if (title.equals(JobTitles.Logistics_Manager))
            editShiftLogistics_ManagerIDs(date, type, newAssigned);
        if (title.equals(JobTitles.Transport_Manager))
            editShiftTransport_ManagerIDs(date, type, newAssigned);
    }

    //DELETE

    public void removeShift(LocalDate date, ShiftTypes type) throws Exception {
        Result<Object> result = shiftService.removeShift(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //MISC
    public List<String> getImportantHRMessagesShifts() throws Exception {
        Result<List<String>> result = shiftService.getImportantHRMessages();
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }


    //private
    private static void isError(Result result) throws Exception {
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //
    private void throwIfError(Result result) throws Exception {
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
    }

    //Truck:
    public void addTruck(int licenseNumber, TruckModel truckModel, int netWeight, int maxCapacityWeight) throws Exception {
        Result result =  truckService.addTruck(licenseNumber, truckModel, netWeight, maxCapacityWeight);
        throwIfError(result);
    }

    public void removeTruck(int licenseNumber) throws Exception {
        Result result =  transportService.removeTruck(licenseNumber);
        throwIfError(result);
    }

    //Document:
    public TransportDocument getTransportDocument(int tdSN) throws Exception {
        Result<Domain.Service.Objects.Document.TransportDocument> result = documentService.getTransportDocument(tdSN);
        throwIfError(result);
        return presentationDocumentFactory.createPresentationDocument(result.getValue());
    }

    public DestinationDocument getDestinationDocument(int ddSN) throws Exception {
        Result<Domain.Service.Objects.Document.DestinationDocument> result = documentService.getDestinationDocument(ddSN);
        throwIfError(result);
        return presentationDocumentFactory.createPresentationDocument(result.getValue());
    }
    public List<String> getImportantHRMessagesTransport() throws Exception {
        return Arrays.stream(orderService.getImportantMessages()).collect(Collectors.toList());
    }
    public List<String> getImportantHRMessagesInventory() {
        return inventoryService.getReadyOrders();
    }
    public List<String> getImportantHRMessagesSuppliers(){
        return new LinkedList<>();
    }

    //Transport Order
    public void addTransportOrder(int srcID, int dstID, HashMap<Integer, Integer> productList) throws Exception {
        Result result = orderService.addOrder(srcID, dstID, productList);
        throwIfError(result);
    }

    //Transport
    private Set<Transport> toPLTransports(Set<Domain.Service.Objects.Transport> transports)
    {
        Set<Transport> transportList = new HashSet<>();
        for (Domain.Service.Objects.Transport transport: transports) {
            transportList.add(new Transport(transport));
        }
        return transportList;
    }
    public Set<Transport> getCompleteTransports() throws Exception {
        Result<Set<Domain.Service.Objects.Transport>> result = transportService.getCompletedTransport();
        throwIfError(result);
        return toPLTransports(result.getValue());
    }

    public Set<Transport> getInProgressTransports() throws Exception {
        Result<Set<Domain.Service.Objects.Transport>> result = transportService.getInProgressTransports();
        throwIfError(result);
        return toPLTransports(result.getValue());
    }
    private Set<Order> toPLTransportOrder(Set<Domain.Service.Objects.SupplierObjects.ServiceOrderObject> orders)
    {
        Set<Order> transportList = new HashSet<>();
        for (Domain.Service.Objects.SupplierObjects.ServiceOrderObject order: orders) {
            transportList.add(new Order(order));
        }
        return transportList;
    }
    public Set<Transport> getPendingTransports() throws Exception {
        Result<Set<Domain.Service.Objects.Transport>> result = transportService.getPaddingTransport();
        throwIfError(result);
        return toPLTransports(result.getValue());
    }

    public void addOrderToTransport(int transportID, int orderID) throws Exception {
        Result result = transportService.addOrderToTransport(transportID, orderID);
        throwIfError(result);
    }

    public int createNewTransport(Pair<LocalDate, ShiftTypes> localDateShiftTypesPair) throws Exception {
        Result<Integer> result = transportService.createTransport(localDateShiftTypesPair);
        throwIfError(result);
        return result.getValue();
    }

    public Set<Employee> getAvailableEmployeesFor(LocalDate date, ShiftTypes type) throws Exception {
        Result<Set<Employee>> result = shiftService.getAvailableEmployeesFor(date, type);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Shift> getEmployeeConstraintsBetween(String eId, LocalDate start, LocalDate end) throws Exception {
        Result<Set<Shift>> result = shiftService.getEmployeeConstraintsBetween(eId, start, end);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Set<Order> getPendingOrders() throws Exception {
        Result<Set<Domain.Service.Objects.SupplierObjects.ServiceOrderObject>> result = orderService.getPendingOrders();
        throwIfError(result);
        return toPLTransportOrder(result.getValue());
    }

    public void startTransport(int transportID) throws Exception {
        Result result = transportService.startTransport(transportID);
        throwIfError(result);
    }

    public void placeCarrier(int transportID, int carrierID) throws Exception {
        Result result = transportService.placeDriver(transportID, Integer.toString(carrierID));
        throwIfError(result);
    }

    public void placeTruck(int transportID, int truckLN) throws Exception {
        Result result = transportService.placeTruck(transportID, truckLN);
        throwIfError(result);
    }

    public Set<Shift> getIncompleteShiftsBetween(LocalDate start, LocalDate end) throws Exception {
        Result<Set<Shift>> result = shiftService.getIncompleteShiftsBetween(start, end);
        throwIfError(result);
        return result.getValue();
    }

    public void advanceSite(int transportSN, int siteID) throws Exception {
        Result result = transportService.advanceSite(transportSN, siteID);
        throwIfError(result);
    }

    private void getAvailableOrders() {
        Result<Collection<ServiceOrderObject>> r = inventoryService.getAvailableOrders();
        if (r.isOk()) {
            for (ServiceOrderObject soo: r.getValue()) {
                System.out.println(soo);
            }
            if (r.getValue().isEmpty())
                System.out.println("No orders to make");
        }
        else {
            System.out.println("Error receiving relevant orders");
        }
    }

    private boolean getValueFromBooleanResult(Result<Boolean> result) throws Exception {
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean updateSupplierBankNumber(int supplierID, int input) throws Exception {
        Result<Boolean> result = supplierService.updateSupplierBankNumber(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean updateSupplierAddress(int supplierID, String input) throws Exception {
        Result<Boolean> result = supplierService.updateSupplierAddress(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean updateSupplierName(int supplierID, String input) throws Exception {
        Result<Boolean> result = supplierService.updateSupplierName(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean updateSupplierPayingAgreement(int supplierID, String input) throws Exception {
        Result<Boolean> result = supplierService.updateSupplierPayingAgreement(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean addAgreement(int supplierId, int input, String stringInput) throws Exception {
        Result<Boolean> result = supplierService.addAgreement(supplierId, input, stringInput);
        return getValueFromBooleanResult(result);
    }

    public List<String> getAllContacts(int supplierId) throws Exception {
        Result<List<String>> result = supplierService.getAllContacts(supplierId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean addSupplierContact(int supplierID, String name, String phone) throws Exception {
        Result<Boolean> result = supplierService.addSupplierContact(supplierID, name, phone);
        return getValueFromBooleanResult(result);
    }

    public boolean removeContact(int supplierID, String contact) throws Exception {
        Result<Boolean> result = supplierService.removeContact(supplierID, contact);
        return getValueFromBooleanResult(result);
    }

    public boolean isSuppliersEmpty() throws Exception {
        Result<Boolean> result = supplierService.isSuppliersEmpty();
        return getValueFromBooleanResult(result);
    }

    public boolean doesSupplierExists(int id) throws Exception {
        Result<Boolean> result = supplierService.supplierExists(id);
        return getValueFromBooleanResult(result);
    }

    public ServiceSupplierObject getSupplierInfo(int supplierId) throws Exception {
        Result<ServiceSupplierObject> result = supplierService.getSupplierInfo(supplierId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public List<String> getManufacturers(int supplierId) throws Exception {
        Result<List<String>> result = supplierService.getManufacturers(supplierId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean addSupplierManufacturer(int supplierId, String manufacturer) throws Exception {
        Result<Boolean> result = supplierService.addSupplierManufacturer(supplierId, manufacturer);
        return getValueFromBooleanResult(result);
    }

    public boolean removeManufacturer(int supplierId, String manufacturer) throws Exception {
        Result<Boolean> result = supplierService.removeManufacturer(supplierId, manufacturer);
        return getValueFromBooleanResult(result);
    }

    public ServiceOrderObject getOrder(int supplierId, int input) throws Exception {
        Result<ServiceOrderObject> result = supplierService.getOrder(supplierId, input);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Integer order(int supplierId, int storeId) throws Exception {
        Result<Integer> result = supplierService.order(supplierId, storeId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean addItemToOrder(int supplierID, int orderId, int itemID, int quantity) throws Exception {
        Result<Boolean> result = supplierService.addItemToOrder(supplierID, orderId, itemID, quantity);
        return getValueFromBooleanResult(result);
    }

    public Integer addSupplier(String name, int bankNumber, String address, String payingAgreement, ArrayList<Pair<String, String>> contacts, ArrayList<String> manufacturers) throws Exception {
        Result<Integer> result = supplierService.addSupplier(name, bankNumber, address, payingAgreement, contacts, manufacturers);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean removeSupplier(int id) throws Exception {
        Result<Boolean> result = supplierService.removeSupplier(id);
        return getValueFromBooleanResult(result);
    }

    public boolean hasAgreement(int supplierId) throws Exception {
        Result<Boolean> result = supplierService.hasAgreement(supplierId);
        return getValueFromBooleanResult(result);
    }

    public boolean isRoutineAgreement(int supplierId) throws Exception {
        Result<Boolean> result = supplierService.isRoutineAgreement(supplierId);
        return getValueFromBooleanResult(result);
    }

    public boolean isByOrderAgreement(int supplierId) throws Exception {
        Result<Boolean> result = supplierService.isByOrderAgreement(supplierId);
        return getValueFromBooleanResult(result);
    }

    public List<ServiceItemObject> itemsFromOneSupplier(int supplierID) throws Exception {
        Result<List<ServiceItemObject>> result = supplierService.itemsFromOneSupplier(supplierID);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean addItemToAgreement(int supplierID, int id, int idBySupplier, String manufacturer, float pricePerUnit, Map<Integer, Integer> bulkMap) throws Exception {
        Result<Boolean> result = supplierService.addItemToAgreement(supplierID, id, idBySupplier, manufacturer, pricePerUnit, bulkMap);
        return getValueFromBooleanResult(result);
    }

    public boolean deleteItemFromAgreement(int supplierID, int input) throws Exception {
        Result<Boolean> result = supplierService.deleteItemFromAgreement(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean changeAgreementType(int supplierID, int input, String days) throws Exception {
        Result<Boolean> result = supplierService.changeAgreementType(supplierID, input, days);
        return getValueFromBooleanResult(result);
    }

    public boolean setDaysOfDelivery(int supplierID, String input) throws Exception {
        Result<Boolean> result = supplierService.setDaysOfDelivery(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean addDaysOfDelivery(int supplierID, String input) throws Exception {
        Result<Boolean> result = supplierService.addDaysOfDelivery(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean removeDayOfDelivery(int supplierID, int input) throws Exception {
        Result<Boolean> result = supplierService.removeDayOfDelivery(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean changeDaysUntilDelivery(int supplierID, int input) throws Exception {
        Result<Boolean> result = supplierService.changeDaysUntilDelivery(supplierID, input);
        return getValueFromBooleanResult(result);
    }

    public ServiceItemObject getItem(int supplierID, int input) throws Exception {
        Result<ServiceItemObject> result = supplierService.getItem(supplierID, input);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean updateItemId(int supplierID, int itemID, int input) throws Exception {
        Result<Boolean> result = supplierService.updateItemId(supplierID, itemID, input);
        return getValueFromBooleanResult(result);

    }

    public boolean updateItemManufacturer(int supplierID, int itemID, String input) throws Exception {
        Result<Boolean> result = supplierService.updateItemManufacturer(supplierID, itemID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean updatePricePerUnitForItem(int supplierID, int itemID, float input) throws Exception {
        Result<Boolean> result = supplierService.updatePricePerUnitForItem(supplierID, itemID, input);
        return getValueFromBooleanResult(result);
    }

    public boolean addBulkPriceForItem(int supplierID, int itemID, int quantity, int discount) throws Exception {
        Result<Boolean> result = supplierService.addBulkPriceForItem(supplierID, itemID, quantity, discount);
        return getValueFromBooleanResult(result);

    }

    public boolean removeBulkPriceForItem(int supplierID, int itemID, int quantity) throws Exception {
        Result<Boolean> result = supplierService.removeBulkPriceForItem(supplierID, itemID, quantity);
        return getValueFromBooleanResult(result);
    }

    public boolean editBulkPriceForItem(int supplierID, int itemID, int quantity, int discount) throws Exception {
        Result<Boolean> result = supplierService.editBulkPriceForItem(supplierID, itemID, quantity, discount);
        return getValueFromBooleanResult(result);
    }

    public Double calculatePriceForItemOrder(int supplierID, int itemID, int input) throws Exception {
        Result<Double> result = supplierService.calculatePriceForItemOrder(supplierID, itemID, input);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Integer daysToDelivery(int supplierID) throws Exception {
        Result<Integer> result = supplierService.daysToDelivery(supplierID);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public List<Integer> getDaysOfDelivery(int supplierID) throws Exception {
        Result<List<Integer>> result = supplierService.getDaysOfDelivery(supplierID);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public Integer getDeliveryDays(int supplierID) throws Exception {
        Result<Integer> result = supplierService.getDeliveryDays(supplierID);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }



//    private final ShiftService shiftService = new ShiftService();
//    private final ConstraintService constraintService = new ConstraintService();

    //    public Set<Employee> getAllEmployees() throws Exception {
//        Result<Set<Employee>> result = inventoryService.getAllEmployees();
//        if (result.isError())
//            throw new Exception("Error occurred: " + result.getError());
//        return result.getValue();
//    }
//
//    public Set<Employee> getAllCashiers() throws Exception {
//        return getAllEmployees().stream().filter((x) -> x.getType() == JobTitles.Cashier).collect(Collectors.toSet());
//    }


    public Result<Collection<Integer>> getStoreIDs(){
        return inventoryService.getStoreIDs();
    }

    public Result<List<Sale>> getRemovableSales(){
        return inventoryService.getRemovableSales();
    }

    public Result<Product> newProduct(String name, int categoryID, double weight, double price, String manufacturer){
        return inventoryService.newProduct(name, categoryID, weight, price, manufacturer);
    }

    public Result<Boolean> deleteProduct(int id){
        return inventoryService.deleteProduct(id);
    }

    public Result<Sale> addSale(List<Integer> categories, List<Integer> products, int percent, LocalDate start, LocalDate end){
        return inventoryService.addSale(categories, products, percent, start, end);
    }

    public Result<Object> removeSale(int saleID){
        return inventoryService.removeSale(saleID);
    }

    public Result<Product> addProductToStore(int storeID, List<Integer> shelvesInStore, List<Integer> shelvesInWarehouse, int productID, int minAmount, int targetAmount){
        return inventoryService.addProductToStore(storeID, shelvesInStore, shelvesInWarehouse, productID, minAmount, targetAmount);
    }

    public Result<Product> removeProductFromStore(int storeID, int productID){
        return inventoryService.removeProductFromStore(storeID, productID);
    }

    public Result<Set<Sale>> getSaleHistoryByProduct(int productId){
        return inventoryService.getSaleHistoryByProduct(productId);
    }

    public Result<Set<Sale>> getSaleHistoryByCategory(int categoryID){
        return inventoryService.getSaleHistoryByCategory(categoryID);
    }

    public Result<List<Product>> getProducts(){
        return inventoryService.getProducts();
    }

    public Result<List<Product>> getProductsFromCategory(List<Integer> categoryIDs){
        return inventoryService.getProductsFromCategory(categoryIDs);
    }

    public Result<List<Category>> getCategories(){
        return inventoryService.getCategories();
    }

    public Result<Category> getCategory(int categoryID){
        return inventoryService.getCategory(categoryID);
    }

    public Result<Pair<Double, String>> buyItems(int storeID, int productID, int amount){
        return inventoryService.buyItems(storeID, productID, amount);
    }

    public Result<List<StockReport>> getMinStockReport(){
        return inventoryService.getMinStockReport();
    }

    public Result<List<StockReport>> storeStockReport(Collection<Integer> storeIDs, List<Integer> categories){
        return inventoryService.storeStockReport(storeIDs, categories);
    }

    public Result<Boolean> isUnderMin(int store, int product){
        return inventoryService.isUnderMin(store, product);
    }

    public Result<Pair<DefectiveItemReport, String>> reportDamaged(int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse){
        return inventoryService.reportDamaged(storeID, productID, amount, employeeID, description, inWarehouse);
    }

    public Result<Pair<DefectiveItemReport, String>> reportExpired(int storeID, int productID, int amount, int employeeID, String description, boolean inWarehouse){
        return inventoryService.reportExpired(storeID, productID, amount, employeeID, description, inWarehouse);
    }

    public Result<List<DefectiveItemReport>> getDefectiveItemsByStore(LocalDate start, LocalDate end, List<Integer> storeIDs){
        return inventoryService.getDefectiveItemsByStore(start, end, storeIDs);
    }

    public Result<List<DefectiveItemReport>> getDefectiveItemsByCategory(LocalDate start, LocalDate end, List<Integer> categoryIDs){
        return inventoryService.getDefectiveItemsByCategory(start, end, categoryIDs);
    }

    public Result<List<DefectiveItemReport>> getDefectiveItemsByProduct(LocalDate start, LocalDate end, List<Integer> productIDs){
        return inventoryService.getDefectiveItemsByProduct(start, end, productIDs);
    }

    public Result<List<DefectiveItemReport>> getDamagedItemsByStore(LocalDate start, LocalDate end, List<Integer> storeIDs){
        return inventoryService.getDamagedItemsReportByStore(start, end, storeIDs);
    }

    public Result<List<DefectiveItemReport>> getDamagedItemsByCategory(LocalDate start, LocalDate end, List<Integer> categoryIDs){
        return inventoryService.getDamagedItemsReportByCategory(start, end, categoryIDs);
    }

    public Result<List<DefectiveItemReport>> getDamagedItemsByProduct(LocalDate start, LocalDate end, List<Integer> productIDs){
        return inventoryService.getDamagedItemsReportByProduct(start, end, productIDs);
    }

    public Result<List<DefectiveItemReport>> getExpiredItemsByStore(LocalDate start, LocalDate end, List<Integer> storeIDs){
        return inventoryService.getExpiredItemReportsByStore(start, end, storeIDs);
    }

    public Result<List<DefectiveItemReport>> getExpiredItemsByCategory(LocalDate start, LocalDate end, List<Integer> categoryIDs){
        return inventoryService.getExpiredItemReportsByCategory(start, end, categoryIDs);
    }

    public Result<List<DefectiveItemReport>> getExpiredItemsByProduct(LocalDate start, LocalDate end, List<Integer> productIDs){
        return inventoryService.getExpiredItemReportsByProduct(start, end, productIDs);
    }

    public Result<Object> moveItems(int storeID, int productID, int amount){
        return inventoryService.moveItems(storeID, productID, amount);
    }

    public Result<Double> returnItems(int storeID, int productID, int amount, LocalDate dateBought){
        return inventoryService.returnItems(storeID, productID, amount, dateBought);
    }

    public Result<Integer> addStore(){
        return inventoryService.addStore();
    }

    public Result<Object> removeStore(int storeID){
        return inventoryService.removeStore(storeID);
    }

    public Result<Product> editProductPrice(int productID, double newPrice){
        return inventoryService.editProductPrice(productID, newPrice);
    }

    public Result<Product> editProductName(int productID, String newName){
        return inventoryService.editProductName(productID, newName);
    }

    public Result<Product> moveProductToCategory(int productID, int newCatID){
        return inventoryService.moveProductToCategory(productID, newCatID);
    }

    public Result<Category> editCategoryName(int categoryID, String newName){
        return inventoryService.editCategoryName(categoryID, newName);
    }

    public Result<Category> changeCategoryParent(int categoryID, int parentID){
        return inventoryService.changeCategoryParent(categoryID, parentID);
    }

    public Result<Category> addNewCategory(String name, int parentCategoryID){
        return inventoryService.addNewCategory(name, parentCategoryID);
    }

    public Result<Boolean> deleteCategory(int catID) {
        return inventoryService.deleteCategory(catID);
    }

    public Result<Product> changeProductMin(int store, int product, int min) {
        return inventoryService.changeProductMin(store, product, min);
    }

    public Result<Product> changeProductTarget(int store, int product, int max) {
        return inventoryService.changeProductTarget(store, product, max);
    }

    public Result<Boolean> orderExists(int supID, int orderID){
        return supplierService.orderExists(supID, orderID);
    }

    public Result<Boolean> removeItemFromOrder(int supID, int orderID, int itemID){
        return supplierService.removeItemFromOrder(supID, orderID, itemID);
    }

    public Result<Boolean> updateItemQuantityInOrder(int supID, int orderID, int itemID, int quantity){
        return supplierService.updateItemQuantityInOrder(supID, orderID, itemID, quantity);
    }

    public Result<Product> getProduct(int product) {
        return inventoryService.getProduct(product);
    }

    public Result<Object> transportArrived(int transportID, Map<Integer, Map<Integer, Pair<Pair<Integer, Integer>, String>>> reportOfOrder) {
        return inventoryService.transportArrived(transportID, reportOfOrder);
    }


    public ArrayList<Integer> getSuppliersID() throws Exception {
        Result<ArrayList<Integer>> result = supplierService.getSuppliersIds();
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public ArrayList<ServiceOrderObject> getAllOrdersForSupplier(int supplierId) throws Exception {
        Result<ArrayList<ServiceOrderObject>> result = supplierService.getAllOrdersForSupplier(supplierId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public ArrayList<ServiceOrderItemObject> getAllOrdersItemsInDiscounts(int supplierId) throws Exception {
        Result<ArrayList<ServiceOrderItemObject>> result = supplierService.getAllOrdersItemsInDiscounts(supplierId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public List<Integer> geOrdersID(int supplierId) throws Exception {
        Result<List<Integer>> result = supplierService.getOrdersIds(supplierId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean removeOrder(int orderId) throws Exception {
        Result<Boolean> result = supplierService.removeOrder(orderId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public ServiceOrderObject getOrder(int orderId) throws Exception {
        Result<ServiceOrderObject> result = supplierService.getOrder(orderId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }


    public int getSupplierWIthOrderID(int orderId) throws Exception {
        Result<Integer> result = supplierService.getSupplierWIthOrderID(orderId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public int getMatchingProductIdForIdBySupplier(int idBySupplier) throws Exception {
        Result<Integer> result = supplierService.getMatchingProductIdForIdBySupplier(idBySupplier);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }

    public boolean orderItemExistsInOrder(int supplierId, int orderId, int itemId) throws Exception {
        Result<Boolean> result = supplierService.orderItemExistsInOrder(supplierId, orderId, itemId);
        if (result.isError())
            throw new Exception("Error occurred: " + result.getError());
        return result.getValue();
    }
    public int getTarget(int i, int productID) {
        return inventoryService.getTarget(i, productID);
    }
    public int getMin(int i, int productID) {
        return inventoryService.getMin(i, productID);
    }

}