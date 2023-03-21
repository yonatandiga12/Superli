package Domain.DAL.Controllers.ShiftDataMappers;


import Domain.Business.Objects.Shift.MorningShift;
import Globals.Enums.ShiftTypes;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class MorningShiftDAO extends AbstractShiftDAO<MorningShift> {
    private static Map<String, MorningShift> MORNING_SHIFTS_IDENTITY_MAP = new HashMap<>();

    public MorningShiftDAO() {
        super("MorningShifts");
    }

    @Override
    protected Map<String, MorningShift> getMap() {
        return MORNING_SHIFTS_IDENTITY_MAP;
    }


    @Override
    protected MorningShift buildObject(ResultSet instanceResult) throws Exception {
        String id = instanceResult.getString(1);
        return new MorningShift(instanceResult.getDate(2).toLocalDate(), instanceResult.getString(3),instanceResult.getInt(4),instanceResult.getInt(5),instanceResult.getInt(6),instanceResult.getInt(7),instanceResult.getInt(8),instanceResult.getInt(9),instanceResult.getInt(10),
                shiftsCarriersLink.get(id),shiftsCashiersLink.get(id),shiftsStorekeepersLink.get(id),shiftsSortersLink.get(id),shiftsHRManagers.get(id),shiftsLogisticManagersLink.get(id),shiftsTransportManagers.get(id),constraintsEmployeesLink.get(id));
    }


    @Override
    protected String getType() {
        return ShiftTypes.Morning.toString();
    }
}
