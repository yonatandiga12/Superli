package Globals.util;

import Globals.Enums.ShiftTypes;

import java.util.Comparator;

public class ShiftTypeComparator implements Comparator<ShiftTypes> {
    @Override
    public int compare(ShiftTypes o1, ShiftTypes o2) {
        return o1.ordinal() - o2.ordinal();
    }
}
