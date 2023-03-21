package Domain.Business.Controllers.HR;

import Domain.Business.Objects.Employee.*;
import Domain.Business.Objects.Shift.EveningShift;
import Domain.Business.Objects.Shift.MorningShift;
import Domain.Business.Objects.Shift.Shift;
import Domain.DAL.Controllers.ShiftDataMappers.ShiftDataMapper;
import Globals.Enums.Certifications;
import Globals.Enums.ShiftTypes;
import Globals.ObserverInterfaces.EditCarrierLicenseObserver;
import Globals.ObserverInterfaces.RemoveEmployeeFromShiftObserver;
import Globals.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ShiftController {
    private static final String SHIFT_ALREADY_EXIST = "There already exists a %s for date %s";
    private static final String SHIFT_DOES_NOT_EXIST = "There is no shift in date: %s type: %s";
    private static final String UNREGISTER_ASSIGNED_EMPLOYEE_ERROR = "Employee id: %s is assign to a shift in date %s";
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final Set<RemoveEmployeeFromShiftObserver> REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES = new HashSet<>();
    // properties
    private final ShiftDataMapper shiftDataMapper;
    private final EmployeeController employeeController;


    // constructor
    public ShiftController(){
        shiftDataMapper = new ShiftDataMapper();
        employeeController = new EmployeeController();
        employeeController.registerToRemoveEmployeeEvent((this::verifyEmployeeIsNotAssignForShifts));
    }

    //CREATE

    public void createShift(LocalDate date, ShiftTypes type, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount, int transport_managerCount) throws Exception {
        if (shiftDataMapper.get(date, type) != null)
            throw new Exception(String.format(SHIFT_ALREADY_EXIST, type, date.format(DATE_FORMAT)));
        switch (type) {
            case Evening:
                shiftDataMapper.save(new EveningShift(date, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount, transport_managerCount));
                break;
            case Morning:
                shiftDataMapper.save(new MorningShift(date, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount, transport_managerCount));
        }
    }

    //READ

    public Shift getShift(LocalDate workday, ShiftTypes type) throws Exception {
        Shift shift = shiftDataMapper.get(workday, type);
        if (shift == null)
            throw new Exception(String.format(SHIFT_DOES_NOT_EXIST, type, workday.format(DATE_FORMAT)));
        return shift;
    }

    public Set<Shift> getShiftsBetween(LocalDate start, LocalDate end) {
        return shiftDataMapper.getBetween(start, end);
    }

    //UPDATE

    public void editShiftManagerID(LocalDate workday, ShiftTypes type, String managerID) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setShiftManagerId(managerID);
        shiftDataMapper.save(shift);
    }

    public void editShiftCarrierCount(LocalDate workday, ShiftTypes type, int carrierCount) throws Exception
    {
        Shift shift = getShift(workday, type);
        shift.setCarrierCount(carrierCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftCashierCount(LocalDate workday, ShiftTypes type, int cashierCount) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setCashierCount(cashierCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftStorekeeperCount(LocalDate workday, ShiftTypes type, int storekeeperCount) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setStorekeeperCount(storekeeperCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftSorterCount(LocalDate workday, ShiftTypes type, int sorterCount) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setSorterCount(sorterCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftHR_ManagerCount(LocalDate workday, ShiftTypes type, int hr_managerCount) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setHr_managersCount(hr_managerCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftLogistics_ManagerCount(LocalDate workday, ShiftTypes type, int logistics_managerCount) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setLogistics_managersCount(logistics_managerCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftTransport_ManagerCount(LocalDate workday, ShiftTypes type, int transport_managerCount) throws Exception {
        Shift shift = getShift(workday, type);
        shift.setLogistics_managersCount(transport_managerCount);
        shiftDataMapper.save(shift);
    }

    public void editShiftCarrierIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getCarrierIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setCarrierIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void editShiftCashierIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getCashierIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setCashierIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void editShiftStorekeeperIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getStorekeeperIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setStorekeeperIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void editShiftSorterIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getSorterIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setSorterIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void editShiftHR_ManagerIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getHr_managerIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setHr_managerIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void editShiftLogistics_ManagerIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getLogistics_managerIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setLogistics_managerIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void editShiftTransport_ManagerIDs(LocalDate workday, ShiftTypes type, Set<String> ids) throws Exception {
        Shift shift = getShift(workday, type);
        Set<String> removedEmployees = getUnregisterEmployeeFromEdit(shift.getTransport_managerIDs(),ids);
        if (!removedEmployees.isEmpty())
            for(RemoveEmployeeFromShiftObserver observer: REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES)
                observer.observe(removedEmployees,workday,type);
        shift.setTransport_managerIDs(ids);
        shiftDataMapper.save(shift);
    }

    public void registerAsAvailable(LocalDate workday, ShiftTypes type, String id) throws Exception {
        Shift shift = getShift(workday, type);
        shift.registerAsAvailable(id);
        shiftDataMapper.save(shift);
    }

    public void unregisterFromAvailable(LocalDate workday, ShiftTypes type, String id) throws Exception {
        Shift shift = getShift(workday, type);
        shift.unregisterFromAvailable(id);
        shiftDataMapper.save(shift);
    }

    //DELETE

    public void removeShift(LocalDate date, ShiftTypes type) {
        shiftDataMapper.delete(date, type);
    }


    //MISC

    public Set<Shift> getEmployeeShiftsBetween(String id, LocalDate start, LocalDate end) {
        return getShiftsBetween(start, end).stream()
                .filter((shift) -> shift.isEmployeeAssigned(id)).collect(Collectors.toSet());
    }

    public String getEmployeeWorkDetailsForCurrentMonth(String id) {
        Pair<LocalDate, LocalDate> monthEdges = getMonthDatesEdges();
        return "Shifts during " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMM")) + ": " +
                Arrays.stream(ShiftTypes.values())
                        .map((type) -> type + " - " + shiftDataMapper.getBetween(monthEdges.getLeft(), monthEdges.getLeft(), type).stream().filter(s -> s.isEmployeeAssigned(id)).count())
                        .collect(Collectors.joining(", "));
    }

    public Employee getAssignedShiftManagerFor(LocalDate workday, ShiftTypes types) throws Exception{
        return employeeController.getEmployee(getShift(workday, types).getShiftManagerId());
    }

    public Set<Carrier> getAssignedCarriersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getCarrier(getShift(workday, type).getCarrierIDs());
    }

    public Set<Cashier> getAssignedCashiersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getCashier(getShift(workday, type).getCashierIDs());
    }

    public Set<Sorter> getAssignedSortersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getSorter(getShift(workday, type).getSorterIDs());
    }

    public Set<Storekeeper> getAssignedStorekeepersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getStorekeeper(getShift(workday, type).getStorekeeperIDs());
    }

    public Set<HR_Manager> getAssignedHR_ManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getHR_Manager(getShift(workday, type).getHr_managerIDs());
    }

    public Set<Logistics_Manager> getAssignedLogistics_ManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getLogistics_Manager(getShift(workday, type).getLogistics_managerIDs());
    }

    public Set<Transport_Manager> getAssignedTransport_ManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getTransport_Manager(getShift(workday, type).getTransport_managerIDs());
    }

    public Set<Employee> getAssignedEmployeesFor(LocalDate workday, ShiftTypes type) throws Exception {
        Set<Employee> employees = new HashSet<>();
        employees.add(getAssignedShiftManagerFor(workday, type));
        employees.addAll(getAssignedCarriersFor(workday, type));
        employees.addAll(getAssignedCashiersFor(workday, type));
        employees.addAll(getAssignedSortersFor(workday, type));
        employees.addAll(getAssignedStorekeepersFor(workday, type));
        employees.addAll(getAssignedHR_ManagersFor(workday, type));
        employees.addAll(getAssignedLogistics_ManagersFor(workday, type));
        employees.addAll(getAssignedTransport_ManagersFor(workday, type));
        return employees;
    }

    public Set<Carrier> getAvailableCarriersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getCarrier(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<Cashier> getAvailableCashiersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getCashier(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<Sorter> getAvailableSortersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getSorter(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<Storekeeper> getAvailableStorekeepersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getStorekeeper(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<HR_Manager> getAvailableHR_ManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getHR_Manager(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<Logistics_Manager> getAvailableLogistics_ManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getLogistics_Manager(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<Transport_Manager> getAvailableTransport_ManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return employeeController.getTransport_Manager(getShift(workday, type).getOnlyAvailableEmployeeIDs());
    }

    public Set<Employee> getAvailableEmployeesFor(LocalDate workday, ShiftTypes type) throws Exception {
        Set<Employee> employees = new HashSet<>();
        employees.addAll(getAvailableCarriersFor(workday, type));
        employees.addAll(getAvailableCashiersFor(workday, type));
        employees.addAll(getAvailableSortersFor(workday, type));
        employees.addAll(getAvailableStorekeepersFor(workday, type));
        employees.addAll(getAvailableHR_ManagersFor(workday, type));
        employees.addAll(getAvailableLogistics_ManagersFor(workday, type));
        employees.addAll(getAvailableTransport_ManagersFor(workday, type));
        return employees;
    }

    public Set<Employee> getAvailableShiftManagersFor(LocalDate workday, ShiftTypes type) throws Exception {
        return getAvailableEmployeesFor(workday, type).stream()
                .filter(e -> e.getCertifications().contains(Certifications.ShiftManagement))
                .collect(Collectors.toSet());
    }

    public Set<Shift> getEmployeeConstraintsBetween(String id, LocalDate start, LocalDate end) {
        return getShiftsBetween(start, end).stream().filter(s -> s.isEmployeeAvailable(id)).collect(Collectors.toSet());

    }

    public Set<Shift> getIncompleteShiftsBetween(LocalDate start, LocalDate end) {
        return getShiftsBetween(start, end).stream().filter(shift -> !shift.isShiftComplete()).collect(Collectors.toSet());
    }

    private Pair<LocalDate, LocalDate> getMonthDatesEdges() {
        LocalDate initial = LocalDate.now();
        LocalDate start = initial.withDayOfMonth(1);
        LocalDate end = initial.withDayOfMonth(initial.getMonth().length(initial.isLeapYear()));
        return new Pair<>(start, end);
    }

    public void registerToRemoveEmployeeFromShiftEvent(RemoveEmployeeFromShiftObserver observer){
        REMOVE_EMPLOYEE_FROM_SHIFT_OBSERVER_FOR_EMPLOYEES.add(observer);
    }

    public void verifyEmployeeIsNotAssignForShifts(String id) throws Exception {
        List<Shift> employeeShifts = new ArrayList<>(getEmployeeShiftsBetween(id, LocalDate.now(), LocalDate.now().plusMonths(2)));
        if (!employeeShifts.isEmpty())
            throw new RuntimeException(String.format(UNREGISTER_ASSIGNED_EMPLOYEE_ERROR,id,employeeShifts.get(0).toString()));

        for(Shift shift : getEmployeeConstraintsBetween(id,LocalDate.now(), LocalDate.now().plusMonths(2)))
            shift.unregisterFromAvailable(id);
    }

    public Set<String> getUnregisterEmployeeFromEdit(Set<String> before, Set<String> after){
        return before.stream().filter(id -> !after.contains(id)).collect(Collectors.toSet());
    }
}