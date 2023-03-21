package Globals.util;

import Domain.Service.Objects.Shift.Shift;

import java.util.Comparator;

public class ShiftComparator implements Comparator<Shift> {
    private final DateComparator dateComparator = new DateComparator();
    private final ShiftTypeComparator shiftTypeComparator = new ShiftTypeComparator();

    @Override
    public int compare(Shift o1, Shift o2) {
        int byDate = dateComparator.compare(o1.date, o2.date);
        if (byDate == 0)
            return shiftTypeComparator.compare(o1.getType(), o2.getType());
        return byDate;
    }
}
