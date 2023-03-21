package Domain.Service.Services.HR;

import Domain.Business.Controllers.HR.ShiftController;
import Domain.Service.Objects.Employee.Employee;
import Domain.Service.Objects.Shift.Shift;
import Domain.Service.util.Result;
import Domain.Service.util.ServiceEmployeeFactory;
import Domain.Service.util.ServiceShiftFactory;
import Globals.Enums.ShiftTypes;
import Globals.util.ShiftComparator;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ShiftService {
    private final ShiftController controller = new ShiftController();
    private final ServiceShiftFactory shiftFactory = new ServiceShiftFactory();
    private final ServiceEmployeeFactory employeeFactory = new ServiceEmployeeFactory();

    //CREATE

    public Result<Object> createShift(LocalDate date, ShiftTypes type, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount, int transport_managerCount) {
        try {
            controller.createShift(date, type, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount, logistics_managerCount, transport_managerCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //READ

    public Result<Shift> getShift(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(shiftFactory.createServiceShift(controller.getShift(workday, type)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Shift>> getShiftsBetween(LocalDate start, LocalDate end) {
        try {
            return Result.makeOk(controller.getShiftsBetween(start, end).stream().map(shiftFactory::createServiceShift).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    //UPDATE

    public Result<Object> editShiftManagerID(LocalDate workday, ShiftTypes type, String managerID) {
        try {
            controller.editShiftManagerID(workday, type, managerID);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftCarrierCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftCarrierCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftCashierCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftCashierCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftStorekeeperCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftStorekeeperCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftSorterCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftSorterCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftHR_ManagerCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftHR_ManagerCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftLogistics_ManagerCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftLogistics_ManagerCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftTransport_ManagerCount(LocalDate workday, ShiftTypes type, int newCount) {
        try {
            controller.editShiftTransport_ManagerCount(workday, type, newCount);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftCarrierIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftCarrierIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftCashierIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftCashierIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftStorekeeperIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftStorekeeperIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftSorterIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftSorterIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftHR_ManagerIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftHR_ManagerIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftLogistics_ManagerIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftLogistics_ManagerIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> editShiftTransport_ManagerIDs(LocalDate workday, ShiftTypes type, Set<String> ids) {
        try {
            controller.editShiftTransport_ManagerIDs(workday, type, ids);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> registerAsAvailable(LocalDate workday, ShiftTypes type, String id) {
        try {
            controller.registerAsAvailable(workday, type, id);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    public Result<Object> unregisterFromAvailable(LocalDate workday, ShiftTypes type, String id) {
        try {
            controller.unregisterFromAvailable(workday, type, id);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //REMOVE

    public Result<Object> removeShift(LocalDate date, ShiftTypes type) {
        try {
            controller.removeShift(date, type);
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
        return Result.makeOk(null);
    }

    //MISC

    public Result<Set<Shift>> getEmployeeShiftsBetween(String id, LocalDate start, LocalDate end) {
        try {
            return Result.makeOk(controller.getEmployeeShiftsBetween(id, start, end).stream().map(shiftFactory::createServiceShift).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<String> getEmployeeWorkDetailsForCurrentMonth(String id) {
        try {
            return Result.makeOk(controller.getEmployeeWorkDetailsForCurrentMonth(id));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Employee> getAssignedShiftManagerFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(employeeFactory.createServiceEmployee(controller.getAssignedShiftManagerFor(workday, type)));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedCarriersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedCarriersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedCashiersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedCashiersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedSortersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedSortersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedStorekeepersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedStorekeepersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedHR_ManagersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedHR_ManagersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedLogistics_ManagersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedLogistics_ManagersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedTransport_ManagersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedTransport_ManagersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAssignedEmployeesFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAssignedEmployeesFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableCarriersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableCarriersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableCashiersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableCashiersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableSortersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableSortersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableStorekeepersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableStorekeepersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableHR_ManagersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableHR_ManagersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableLogistics_ManagersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableLogistics_ManagersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableTransport_ManagersFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableTransport_ManagersFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableEmployeesFor(LocalDate workday, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableEmployeesFor(workday, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }



    public Result<Set<Shift>> getEmployeeConstraintsBetween(String id, LocalDate start, LocalDate end) {
        try {
            return Result.makeOk(controller.getEmployeeConstraintsBetween(id, start, end).stream().map(shiftFactory::createServiceShift).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Employee>> getAvailableShiftManagersFor(LocalDate date, ShiftTypes type) {
        try {
            return Result.makeOk(controller.getAvailableShiftManagersFor(date, type).stream().map(employeeFactory::createServiceEmployee).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<Set<Shift>> getIncompleteShiftsBetween(LocalDate start, LocalDate end) {
        try {
            return Result.makeOk(controller.getIncompleteShiftsBetween(start, end).stream().map(shiftFactory::createServiceShift).collect(Collectors.toSet()));
        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }

    public Result<List<String>> getImportantHRMessages() {
        try {
            List<Shift> incompleteShifts = controller.getIncompleteShiftsBetween(LocalDate.now(), LocalDate.now()).stream().map(shiftFactory::createServiceShift).sorted(new ShiftComparator()).collect(Collectors.toList());
            List<String> messages = incompleteShifts.stream().filter(s -> s.shiftManagerId.equals("-1")).map(s -> s.toString() + " - No manager assigned!").collect(Collectors.toList());
            messages.addAll(incompleteShifts.stream().filter(s -> !s.shiftManagerId.equals("-1")).map(s -> s.toString() + " - Incomplete assignment").collect(Collectors.toList()));
            return Result.makeOk(messages);

        } catch (Exception e) {
            return Result.makeError(e.getMessage());
        }
    }
}