package Domain.Business.Objects.Shift;

import Domain.DAL.Controllers.ShiftDataMappers.ShiftDataMapper;
import Domain.Service.util.ServiceShiftFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class MorningShift extends Shift {

    public MorningShift(LocalDate workday, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount,int transport_managersCount) throws Exception {
        super(workday, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount,logistics_managerCount,transport_managersCount);
    }

    public MorningShift(LocalDate workday, String managerId, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managerCount, int logistics_managerCount,int transport_managersCount) throws Exception {
        super(workday, managerId, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount,logistics_managerCount,transport_managersCount);
    }

    public MorningShift(LocalDate workday, String managerId, int carrierCount, int cashierCount, int storekeeperCount, int sorterCount,
                        int hr_managerCount, int logistics_managerCount, int transport_managersCount,
                        Set<String> carrierIDs, Set<String> cashierIDs, Set<String> storekeeperIDs,
                        Set<String> sorterIDs, Set<String> hr_managerIDs, Set<String> logistics_managerIDs,
                        Set<String> transportManagersIDs,Set<String> canWorkInIds
    ) throws Exception {
        super(workday, managerId, carrierCount, cashierCount, storekeeperCount, sorterCount, hr_managerCount,logistics_managerCount,transport_managersCount,carrierIDs,
                cashierIDs,
                storekeeperIDs,
                sorterIDs,
                hr_managerIDs,
                logistics_managerIDs,
                transportManagersIDs,canWorkInIds );
    }

    @Override
    public Domain.Service.Objects.Shift.Shift accept(ServiceShiftFactory factory) {
        return factory.createServiceShift(this);
    }


    @Override
    public String printDayAndType() {
        return "Morning- " + getWorkday().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    @Override
    public void save(ShiftDataMapper shiftDataMapper) throws SQLException {
        shiftDataMapper.save(this);
    }

    @Override
    public void update(ShiftDataMapper shiftDataMapper) throws SQLException {
        shiftDataMapper.update(this);
    }
}
