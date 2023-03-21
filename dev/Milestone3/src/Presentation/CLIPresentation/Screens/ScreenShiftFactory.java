package Presentation.CLIPresentation.Screens;


public class ScreenShiftFactory {
    private Screen caller;
    public Shift createScreenShift(Screen caller, Domain.Service.Objects.Shift.Shift sShift){
        this.caller = caller;
        return sShift.accept(this);
    }

    public EveningShift createScreenShift(Domain.Service.Objects.Shift.EveningShift sShift){
        return new EveningShift(caller, sShift);
    }

    public MorningShift createScreenShift(Domain.Service.Objects.Shift.MorningShift sShift){
        return new MorningShift(caller, sShift);
    }
}
