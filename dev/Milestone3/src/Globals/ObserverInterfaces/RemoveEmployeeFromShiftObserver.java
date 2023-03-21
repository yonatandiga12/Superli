package Globals.ObserverInterfaces;
import Globals.Enums.ShiftTypes;

import java.time.LocalDate;
import java.util.Set;

public interface RemoveEmployeeFromShiftObserver {
    void observe(Set<String> eId, LocalDate shiftDate, ShiftTypes shiftType);
}
