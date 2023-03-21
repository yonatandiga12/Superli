package Domain.Service.Objects.Shift;

import Globals.Enums.ShiftTypes;
import Globals.util.HumanInteraction;
import Presentation.CLIPresentation.Screens.ScreenShiftFactory;

public class EveningShift extends Shift{
    public EveningShift(Domain.Business.Objects.Shift.EveningShift bShift) {
        super(bShift);
    }

    @Override
    public String toString() {
        return "Evening shift: " + date.format(HumanInteraction.dateFormat);
    }

    @Override
    public Presentation.CLIPresentation.Screens.Shift accept(ScreenShiftFactory screenShiftFactory) {
        return screenShiftFactory.createScreenShift(this);
    }

    @Override
    public ShiftTypes getType() {
        return ShiftTypes.Evening;
    }
}
