package Domain.Service.util;

import Domain.Service.Objects.Shift.EveningShift;
import Domain.Service.Objects.Shift.MorningShift;
import Domain.Service.Objects.Shift.Shift;

public class ServiceShiftFactory {
    public Shift createServiceShift(Domain.Business.Objects.Shift.Shift bShift){
        return bShift.accept(this);
    }

    public MorningShift createServiceShift(Domain.Business.Objects.Shift.MorningShift bShift){
        return new MorningShift(bShift);
    }

    public EveningShift createServiceShift(Domain.Business.Objects.Shift.EveningShift bShift){
        return new EveningShift(bShift);
    }

}
