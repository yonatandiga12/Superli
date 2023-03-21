package Domain.DAL.Controllers.ShiftDataMappers;

import Domain.Business.Objects.Shift.EveningShift;
import Globals.Enums.ShiftTypes;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class EveningShiftDAO extends AbstractShiftDAO<EveningShift> {
    private static final Map<String,EveningShift> EVENING_SHIFTS_MAP = new HashMap<>();

    public EveningShiftDAO() {
        super("EveningShifts");
    }


    @Override
    protected Map<String, EveningShift> getMap() {
        return EVENING_SHIFTS_MAP;
    }


    @Override
    protected EveningShift buildObject(ResultSet instanceResult) throws Exception {
        String id = instanceResult.getString(1);
        return new EveningShift(instanceResult.getDate(2).toLocalDate(), instanceResult.getString(3),instanceResult.getInt(4),instanceResult.getInt(5),instanceResult.getInt(6),instanceResult.getInt(7),instanceResult.getInt(8),instanceResult.getInt(9),instanceResult.getInt(10),
                shiftsCarriersLink.get(id),shiftsCashiersLink.get(id),shiftsStorekeepersLink.get(id),shiftsSortersLink.get(id),shiftsHRManagers.get(id),shiftsLogisticManagersLink.get(id),shiftsTransportManagers.get(id),constraintsEmployeesLink.get(id));
    }

    @Override
    protected String getType() {
        return ShiftTypes.Evening.toString();
    }
}
