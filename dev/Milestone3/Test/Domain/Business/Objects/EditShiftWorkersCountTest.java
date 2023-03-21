package Domain.Business.Objects;

import Domain.Business.Controllers.HR.ShiftController;
import Domain.Business.Objects.Shift.MorningShift;
import Domain.Business.Objects.Shift.Shift;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class EditShiftWorkersCountTest {
    static LocalDate date=LocalDate.parse("2021-06-19");


    @org.junit.Test
    public void EditCarriersCount() {
        try {
            Shift shift = new MorningShift(date,"101010",2,2,2,2,2,2,2);
            int prev = shift.getCarrierCount();
            shift.setCarrierCount(prev+3);
            assertEquals(prev+3, shift.getCarrierCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void EditCashiersCount() {
        ShiftController shiftController = new ShiftController();
        try {
            Shift shift = new MorningShift(date,"101010",2,2,2,2,2,2,2);
            int prev = shift.getCarrierCount();
            shift.setCashierCount(prev+3);
            assertEquals(prev+3, shift.getCashierCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}