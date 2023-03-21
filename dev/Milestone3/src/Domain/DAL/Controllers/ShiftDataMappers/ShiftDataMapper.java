package Domain.DAL.Controllers.ShiftDataMappers;

import Domain.Business.Objects.Shift.EveningShift;
import Domain.Business.Objects.Shift.MorningShift;
import Domain.Business.Objects.Shift.Shift;
import Globals.Enums.ShiftTypes;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShiftDataMapper {
    // properties
    private MorningShiftDAO morningShiftDataMapper;
    private EveningShiftDAO eveningShiftDataMapper;

    // constructor
    public ShiftDataMapper() {
        this.morningShiftDataMapper = new MorningShiftDAO();
        this.eveningShiftDataMapper = new EveningShiftDAO();
    }


    // functions
   public Shift get(LocalDate date, ShiftTypes shiftType){
       try {
            switch (shiftType){
                case Morning:
                    return morningShiftDataMapper.get(date.toString()+shiftType.toString());
                case Evening:
                    return eveningShiftDataMapper.get(date.toString()+shiftType.toString());
                default:
                    throw new IllegalArgumentException("no case for this ShiftType");
            }
        } catch (Exception e) {
           e.printStackTrace();
           throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
       }
   }



   public void save(Shift shift) throws SQLException {
       shift.save(this);
   }

   public void save(MorningShift morningShift) throws SQLException {
        morningShiftDataMapper.save(morningShift.getWorkday()+ShiftTypes.Morning.toString(),morningShift);
   }

    public void save(EveningShift eveningShift) throws SQLException {
        eveningShiftDataMapper.save(eveningShift.getWorkday()+ShiftTypes.Evening.toString(),eveningShift);
    }

    public void update(Shift shift) throws SQLException {
        shift.update(this);
    }

    public void update(MorningShift shift) throws SQLException {
        morningShiftDataMapper.insert(shift);
    }

    public void update(EveningShift shift) throws SQLException {
        eveningShiftDataMapper.insert(shift);
    }

    //TODO
    //should delete shift with this key
    public void delete(LocalDate date, ShiftTypes type) {
        try {
            switch (type){
                case Evening:
                    eveningShiftDataMapper.delete(date.toString()+type.toString());
                    break;

                case Morning:
                    morningShiftDataMapper.delete(date.toString()+type.toString());
                    break;
                default:
                    throw new RuntimeException("no such type is define");
                }
        }
        catch (SQLException throwables) {
            throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
        }
    }

    //should return all shifts (of any type) between date start and date end (inclusive)
    public Set<Shift> getBetween(LocalDate start, LocalDate end) {
        Set<Shift> shifts =getBetween(start,end,ShiftTypes.Morning);
        shifts.addAll(getBetween(start,end,ShiftTypes.Evening));
        return shifts;
    }

    //should return all shifts of type 'type' between date start and date end (inclusive)
    public Set<Shift> getBetween(LocalDate start, LocalDate end, ShiftTypes type) {
        Set<Shift> output = new HashSet<>();

        long numOfDays = ChronoUnit.DAYS.between(start, end.plusDays(1));

        java.util.List<LocalDate> listOfDates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());

        for (LocalDate date:listOfDates){
            try{
                Shift shift;
                switch (type) {
                    case Morning:
                        shift = morningShiftDataMapper.get(date.toString() + type.toString());
                        break;
                    case Evening:
                        shift = eveningShiftDataMapper.get(date.toString() + type.toString());
                        break;
                    default:
                        throw new RuntimeException("undefine case");
                }
                if (shift != null)
                    output.add(shift);
            } catch (Exception e) {
                throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
            }
        }
        return output;
    }

}

