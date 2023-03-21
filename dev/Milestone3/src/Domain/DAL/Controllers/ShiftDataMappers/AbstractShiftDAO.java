package Domain.DAL.Controllers.ShiftDataMappers;

import Domain.Business.Objects.Shift.Shift;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.Controllers.ShiftEmployeesLink.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractShiftDAO<T extends Shift> extends DataMapper<T> {

    protected final ShiftsCarriersLink shiftsCarriersLink;
    protected final ShiftsCashiersLink shiftsCashiersLink;
    protected final ShiftsHRManagers shiftsHRManagers;
    protected final ShiftsLogisticManagersLink shiftsLogisticManagersLink;
    protected final ShiftsSortersLink shiftsSortersLink;
    protected final ShiftsStorekeepersLink shiftsStorekeepersLink;
    protected final ShiftsTransportManagers shiftsTransportManagers;
    protected final ConstraintsEmployeesLink constraintsEmployeesLink;

    public AbstractShiftDAO(String tableName) {
        super(tableName);
        shiftsCarriersLink = new ShiftsCarriersLink();
        shiftsCashiersLink = new ShiftsCashiersLink();
        shiftsHRManagers = new ShiftsHRManagers();
        shiftsLogisticManagersLink = new ShiftsLogisticManagersLink();
        shiftsSortersLink= new ShiftsSortersLink();
        shiftsStorekeepersLink = new ShiftsStorekeepersLink();
        shiftsTransportManagers = new ShiftsTransportManagers();
        this.constraintsEmployeesLink = new ConstraintsEmployeesLink();
    }
    @Override
    protected LinkDAO getLinkDTO(String setName) {
        switch (setName){
            case "carriers":
                return shiftsCarriersLink;
            case "cashiers":
                return shiftsCashiersLink;
            case "HRManagers":
                return shiftsHRManagers;
            case "logisticsManagers":
                return shiftsLogisticManagersLink;
            case "sorters":
                return shiftsSortersLink;
            case "storekeepers":
                return shiftsStorekeepersLink;
            case "transportManagers":
                return shiftsTransportManagers;
            case "constraints":
                return constraintsEmployeesLink;
            default:
                throw new IllegalArgumentException("illegal set name");
        }
    }

    @Override
    public void insert(T instance) throws SQLException {
        String id = instance.getWorkday().toString()+ getType();
        shiftsCarriersLink.replaceSet(id,instance.getCarrierIDs());
        shiftsCashiersLink.replaceSet(id,instance.getCashierIDs());
        shiftsHRManagers.replaceSet(id,instance.getHr_managerIDs());
        shiftsLogisticManagersLink.replaceSet(id,instance.getLogistics_managerIDs());
        shiftsSortersLink.replaceSet(id,instance.getSorterIDs());
        shiftsStorekeepersLink.replaceSet(id,instance.getStorekeeperIDs());
        shiftsTransportManagers.replaceSet(id,instance.getTransport_managerIDs());
        constraintsEmployeesLink.replaceSet(id,instance.getAvailableEmployeeIDs());
        super.remove(instance.getWorkday().toString()+getType());
        super.insert(Arrays.asList(id,instance.getWorkday(),instance.getShiftManagerId(),instance.getCarrierCount(),instance.getCashierCount(),instance.getStorekeeperCount(),instance.getSorterCount(),instance.getHr_managersCount(),instance.getLogistics_managersCount(),instance.getTransport_managersCount()));
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        Set<LinkDAO> linkDAOS = new HashSet<>();
        linkDAOS.add(shiftsCarriersLink);
        linkDAOS.add(shiftsCashiersLink);
        linkDAOS.add(shiftsHRManagers);
        linkDAOS.add(shiftsLogisticManagersLink);
        linkDAOS.add(shiftsSortersLink);
        linkDAOS.add(shiftsStorekeepersLink);
        linkDAOS.add(shiftsTransportManagers);
        linkDAOS.add(constraintsEmployeesLink);
        return linkDAOS;
    }

    protected abstract String getType();

    @Override
    public String instanceToId(T instance) {
        return instance.getWorkday().toString()+getType();
    }
}
