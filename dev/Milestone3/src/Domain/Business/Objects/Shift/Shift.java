package Domain.Business.Objects.Shift;

import Domain.DAL.Controllers.ShiftDataMappers.ShiftDataMapper;
import Domain.Service.util.ServiceShiftFactory;
import Globals.Enums.JobTitles;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Shift {

    private static final int MIN_CARRIERS= 1;
    private static final int MIN_CASHIERS = 1;
    private static final int MIN_STOREKEEPERS= 1;
    private static final int MIN_SORTERS = 1;
    private static final int MIN_HR_MANAGERS = 0;
    private static final int MIN_LOGISTICS_MANAGERS = 0;
    private static final int MIN_TRANSPORT_MANAGERS = 0;
    // properties
    private final LocalDate workday;
    private String shiftManagerId;
    private int carrierCount;
    private int cashierCount;
    private int storekeeperCount;
    private int sorterCount;
    private int hr_managersCount;
    private int logistics_managersCount;
    private int transport_managersCount;

    private Set<String> carrierIDs;
    private Set<String> cashierIDs;
    private Set<String> storekeeperIDs;
    private Set<String> sorterIDs;
    private Set<String> hr_managerIDs;
    private Set<String> logistics_managerIDs;
    private Set<String> transport_managerIDs;

    private final Set<String> availableEmployees;

    // constructors
    public Shift(LocalDate workday, String shiftManagerId,
                 int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managersCount, int logistics_managersCount,int transport_managersCount,
                 Set<String> carrierIDs, Set<String> cashierIDs, Set<String> storekeeperIDs, Set<String> sorterIDs, Set<String> hr_managerIDs, Set<String> logistics_managerIDs,Set<String>transportManagerIDs, Set<String> availableEmployees) throws Exception {
        this.workday = workday;
        this.shiftManagerId = shiftManagerId;

        checkCountValidity(carrierCount, MIN_CARRIERS, JobTitles.Carrier);
        this.carrierCount = carrierCount;
        checkCountValidity(cashierCount, MIN_CASHIERS, JobTitles.Cashier);
        this.cashierCount = cashierCount;
        checkCountValidity(storekeeperCount, MIN_STOREKEEPERS, JobTitles.Storekeeper);
        this.storekeeperCount = storekeeperCount;
        checkCountValidity(sorterCount, MIN_SORTERS, JobTitles.Sorter);
        this.sorterCount = sorterCount;
        checkCountValidity(hr_managersCount, MIN_HR_MANAGERS, JobTitles.HR_Manager);
        this.hr_managersCount = hr_managersCount;
        checkCountValidity(logistics_managersCount, MIN_LOGISTICS_MANAGERS, JobTitles.Logistics_Manager);
        this.logistics_managersCount = logistics_managersCount;
        checkCountValidity(transport_managersCount, MIN_TRANSPORT_MANAGERS, JobTitles.Transport_Manager);
        this.transport_managersCount =transport_managersCount;

        this.carrierIDs =new HashSet<>(carrierIDs);
        this.cashierIDs = new HashSet<>(cashierIDs);
        this.storekeeperIDs = new HashSet<>(storekeeperIDs);
        this.sorterIDs = new HashSet<>(sorterIDs);
        this.hr_managerIDs = new HashSet<>(hr_managerIDs);
        this.logistics_managerIDs = new HashSet<>(logistics_managerIDs);
        this.transport_managerIDs = new HashSet<>(transportManagerIDs);
        this.availableEmployees = new HashSet<>(availableEmployees);
    }

    public Shift(LocalDate date, String managerId, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount, int transportManagersCount) throws Exception {
        this(date, managerId,
                carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount, transportManagersCount,
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    public Shift(LocalDate date, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount, int transportManagersCount) throws Exception {
        this(date, "-1",
                carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount, transportManagersCount,
                new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
    }


    public LocalDate getWorkday() {
        return workday;
    }

    public String getShiftManagerId() {
        return shiftManagerId;
    }

    public void setShiftManagerId(String shiftManagerId) throws Exception {
        if (shiftManagerId == null)
            throw new Exception("A shift has to have a shift manager");
        if (carrierIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a carrier. " +
                    "\nIf you'd like him to manager this shift please remove him from from the carrier list of this shift");
        if (cashierIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a cashier. " +
                    "\nIf you'd like him to manager this shift please remove him from from the cashier list of this shift");
        if (storekeeperIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a storekeeper. " +
                    "\nIf you'd like him to manager this shift please remove him from from the storekeeper list of this shift");
        if (sorterIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a sorter. " +
                    "\nIf you'd like him to manager this shift please remove him from from the sorter list of this shift");
        if (hr_managerIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a HR manager. " +
                    "\nIf you'd like him to manager this shift please remove him from from the HR manager list of this shift");
        if (logistics_managerIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a logistics manager. " +
                    "\nIf you'd like him to manager this shift please remove him from from the logistics manager list of this shift");
        if (transport_managerIDs.contains(shiftManagerId))
            throw new Exception("This manager is already assigned for this shift as a transport manager. " +
                    "\nIf you'd like him to manager this shift please remove him from from the transport manager list of this shift");
        this.shiftManagerId = shiftManagerId;
    }

    public int getCarrierCount() {
        return carrierCount;
    }

    public void setCarrierCount(int carrierCount) throws Exception {
        checkCountValidity(carrierCount, MIN_CARRIERS, JobTitles.Carrier);
        checkSizeValidity(carrierCount, carrierIDs.size());
        this.carrierCount = carrierCount;
    }

    public int getCashierCount() {
        return cashierCount;
    }

    public void setCashierCount(int cashierCount) throws Exception {
        checkCountValidity(cashierCount, MIN_CASHIERS, JobTitles.Cashier);
        checkSizeValidity(cashierCount, cashierIDs.size());
        this.cashierCount = cashierCount;
    }

    public int getStorekeeperCount() {
        return storekeeperCount;
    }

    public void setStorekeeperCount(int storekeeperCount) throws Exception {
        checkCountValidity(storekeeperCount, MIN_STOREKEEPERS, JobTitles.Storekeeper);
        checkSizeValidity(storekeeperCount, storekeeperIDs.size());
        this.storekeeperCount = storekeeperCount;
    }

    public int getSorterCount() {
        return sorterCount;
    }

    public void setSorterCount(int sorterCount) throws Exception {
        checkCountValidity(sorterCount, MIN_SORTERS, JobTitles.Sorter);
        checkSizeValidity(sorterCount, sorterIDs.size());
        this.sorterCount = sorterCount;
    }

    public int getHr_managersCount() {
        return hr_managersCount;
    }

    public void setHr_managersCount(int hr_managersCount) throws Exception {
        checkCountValidity(hr_managersCount, MIN_HR_MANAGERS, JobTitles.HR_Manager);
        checkSizeValidity(hr_managersCount, hr_managerIDs.size());
        this.hr_managersCount = hr_managersCount;
    }

    public int getLogistics_managersCount() {
        return logistics_managersCount;
    }

    public void setLogistics_managersCount(int logistics_managersCount) throws Exception {
        checkCountValidity(logistics_managersCount, MIN_LOGISTICS_MANAGERS, JobTitles.Logistics_Manager);
        checkSizeValidity(logistics_managersCount, logistics_managerIDs.size());
        this.logistics_managersCount = logistics_managersCount;
    }

    public int getTransport_managersCount() {
        return transport_managersCount;
    }

    public void setTransport_managersCount(int transport_managersCount) throws Exception {
        checkCountValidity(transport_managersCount, MIN_TRANSPORT_MANAGERS, JobTitles.Transport_Manager);
        checkSizeValidity(transport_managersCount, transport_managerIDs.size());
        this.transport_managersCount = transport_managersCount;
    }

    public Set<String> getCarrierIDs() {
        return new HashSet<>(carrierIDs);
    }

    public void setCarrierIDs(Set<String> carrierIDs) throws Exception {
        checkSizeValidity(carrierCount, carrierIDs.size());
        if (carrierIDs.contains(shiftManagerId))
            throw new Exception("One of these carriers (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.carrierIDs = new HashSet<>(carrierIDs);
    }

    public Set<String> getCashierIDs() {
        return cashierIDs;
    }

    public void setCashierIDs(Set<String> cashierIDs) throws Exception {
        checkSizeValidity(cashierCount, cashierIDs.size());
        if (cashierIDs.contains(shiftManagerId))
            throw new Exception("One of these cashiers (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.cashierIDs = new HashSet<>(cashierIDs);
    }

    public Set<String> getStorekeeperIDs() {
        return storekeeperIDs;
    }

    public void setStorekeeperIDs(Set<String> storekeeperIDs) throws Exception {
        checkSizeValidity(storekeeperCount, storekeeperIDs.size());
        if (storekeeperIDs.contains(shiftManagerId))
            throw new Exception("One of these storekeepers (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.storekeeperIDs = new HashSet<>(storekeeperIDs);
    }

    public Set<String> getSorterIDs() {
        return sorterIDs;
    }

    public void setSorterIDs(Set<String> sorterIDs) throws Exception {
        checkSizeValidity(sorterCount, sorterIDs.size());
        if (sorterIDs.contains(shiftManagerId))
            throw new Exception("One of these sorters (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.sorterIDs = new HashSet<>(sorterIDs);
    }

    public Set<String> getHr_managerIDs() {
        return hr_managerIDs;
    }

    public void setHr_managerIDs(Set<String> hr_managerIDs) throws Exception {
        checkSizeValidity(hr_managersCount, hr_managerIDs.size());
        if (hr_managerIDs.contains(shiftManagerId))
            throw new Exception("One of these HR managers (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.hr_managerIDs = new HashSet<>(hr_managerIDs);
    }

    public Set<String> getLogistics_managerIDs() {
        return logistics_managerIDs;
    }

    public void setLogistics_managerIDs(Set<String> logistics_managerIDs) throws Exception {
        checkSizeValidity(logistics_managersCount, logistics_managerIDs.size());
        if (logistics_managerIDs.contains(shiftManagerId))
            throw new Exception("One of these logistics managers (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.logistics_managerIDs = new HashSet<>(logistics_managerIDs);
    }

    public void setTransport_managerIDs(Set<String> transport_managerIDs) throws Exception {
        checkSizeValidity(transport_managersCount, transport_managerIDs.size());
        if (transport_managerIDs.contains(shiftManagerId))
            throw new Exception("One of these logistics managers (" + shiftManagerId + ") is assigned as manager of this shift, please assign someone else");
        this.transport_managerIDs = new HashSet<>(transport_managerIDs);
    }

    public Set<String> getTransport_managerIDs() {
        return transport_managerIDs;
    }

    public Set<String> getAssignedEmployeesIDs() {
        Set<String> assigned = new HashSet<>();
        if(!shiftManagerId.equals("-1"))
            assigned.add(shiftManagerId);
        assigned.addAll(carrierIDs);
        assigned.addAll(cashierIDs);
        assigned.addAll(sorterIDs);
        assigned.addAll(storekeeperIDs);
        assigned.addAll(hr_managerIDs);
        assigned.addAll(logistics_managerIDs);
        assigned.addAll(transport_managerIDs);
        return assigned;
    }

    public Set<String> getAvailableEmployeeIDs() {
        return availableEmployees;
    }

    public Set<String> getOnlyAvailableEmployeeIDs() {
        return availableEmployees.stream()
                .filter(id -> !id.equals(shiftManagerId))
                .filter(id -> !carrierIDs.contains(id))
                .filter(id -> !cashierIDs.contains(id))
                .filter(id -> !sorterIDs.contains(id))
                .filter(id -> !storekeeperIDs.contains(id))
                .filter(id -> !hr_managerIDs.contains(id))
                .filter(id -> !logistics_managerIDs.contains(id))
                .filter(id -> !transport_managerIDs.contains(id))
                .collect(Collectors.toSet());
    }

    public void registerAsAvailable(String id){
        availableEmployees.add(id);
    }

    public void unregisterFromAvailable(String id) throws Exception {
        if (getAssignedEmployeesIDs().contains(id))
            throw new Exception("Cannot unregister, already assigned for this shift. Please remove assignment first.");
        availableEmployees.remove(id);
    }

    public boolean isEmployeeAssigned(String id){
        return shiftManagerId.equals(id) ||
                carrierIDs.contains(id) ||
                cashierIDs.contains(id) ||
                storekeeperIDs.contains(id) ||
                sorterIDs.contains(id) ||
                hr_managerIDs.contains(id) ||
                logistics_managerIDs.contains(id);
    }

    public boolean isEmployeeAvailable(String id) {
        return availableEmployees.contains(id);
    }

    public boolean isShiftComplete(){
        return shiftManagerId != null
                && carrierIDs.size() == carrierCount
                && cashierIDs.size() == cashierCount
                && sorterIDs.size() == sorterCount
                && storekeeperIDs.size() == storekeeperCount
                && hr_managerIDs.size() == hr_managersCount
                && logistics_managerIDs.size() == logistics_managersCount
                && transport_managerIDs.size() == transport_managersCount;
    }

    public abstract Domain.Service.Objects.Shift.Shift accept(ServiceShiftFactory factory);

    private void checkCountValidity(int count, int minimum, JobTitles type) throws Exception {
        if (count < minimum)
            throw new Exception(String.format("A shift can't have less than %s %s(s)", minimum, type));
    }



    private void checkSizeValidity(int count, int size) throws Exception {
        if (count < size)
            throw new Exception("A shift can't hold more employees more than configured count");
    }

    public abstract String printDayAndType();

    public abstract void save(ShiftDataMapper shiftDataMapper) throws SQLException;

    public abstract void update(ShiftDataMapper shiftDataMapper) throws SQLException;
}
