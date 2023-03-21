package Globals.util;

import java.time.LocalDate;
import java.util.Comparator;

public class DateComparator implements Comparator<LocalDate> {
    @Override
    public int compare(LocalDate o1, LocalDate o2) {
        if (o1.isBefore(o2))
            return -1;
        if (o1.isAfter(o2))
            return 1;
        return 0;
    }
}
