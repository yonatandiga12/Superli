package Domain.Service.Objects.Shift;

import Globals.Enums.JobTitles;
import Globals.Enums.ShiftTypes;
import Presentation.CLIPresentation.Screens.ScreenShiftFactory;

import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service model for Shift
 */
public abstract class Shift {
    public final LocalDate date;
    public final String shiftManagerId;

    public final int carrierCount;
    public final int cashierCount;
    public final int storekeeperCount;
    public final int sorterCount;
    public final int hr_managersCount;
    public final int logistics_managersCount;
    public final int transport_managersCount;

    public final Map<JobTitles, Integer> titleToCount;
    public final Map<JobTitles, Set<String>> titleToIDs;

    public final Set<String> carrierIDs;
    public final Set<String> cashierIDs;
    public final Set<String> storekeeperIDs;
    public final Set<String> sorterIDs;
    public final Set<String> hr_managerIDs;
    public final Set<String> logistics_managerIDs;
    public final Set<String> transport_managersIDs;

    private Shift(LocalDate date, String shiftManagerId,
                  int carrierCount, int cashierCount, int storekeeperCount, int sorterCount, int hr_managersCount, int logistics_managersCount, int transport_managersCount,
                  Set<String> carrierIDs, Set<String> cashierIDs, Set<String> storekeeperIDs, Set<String> sorterIDs, Set<String> hr_managerIDs, Set<String> logistics_managerIDs, Set<String> transport_managersIDs){
        this.date = date;
        this.shiftManagerId = shiftManagerId;

        this.carrierCount = carrierCount;
        this.cashierCount = cashierCount;
        this.storekeeperCount = storekeeperCount;
        this.sorterCount = sorterCount;
        this.hr_managersCount = hr_managersCount;
        this.logistics_managersCount = logistics_managersCount;
        this.transport_managersCount = transport_managersCount;

        this.carrierIDs = Collections.unmodifiableSet(carrierIDs);
        this.cashierIDs = Collections.unmodifiableSet(cashierIDs);
        this.storekeeperIDs = Collections.unmodifiableSet(storekeeperIDs);
        this.sorterIDs = Collections.unmodifiableSet(sorterIDs);
        this.hr_managerIDs = Collections.unmodifiableSet(hr_managerIDs);
        this.logistics_managerIDs = Collections.unmodifiableSet(logistics_managerIDs);
        this.transport_managersIDs = Collections.unmodifiableSet(transport_managersIDs);

        titleToCount = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(JobTitles.Sorter, sorterCount),
                new AbstractMap.SimpleEntry<>(JobTitles.Storekeeper, storekeeperCount),
                new AbstractMap.SimpleEntry<>(JobTitles.Carrier, carrierCount),
                new AbstractMap.SimpleEntry<>(JobTitles.Cashier, cashierCount),
                new AbstractMap.SimpleEntry<>(JobTitles.HR_Manager, hr_managersCount),
                new AbstractMap.SimpleEntry<>(JobTitles.Logistics_Manager, logistics_managersCount),
                new AbstractMap.SimpleEntry<>(JobTitles.Transport_Manager, transport_managersCount)
        ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

        titleToIDs = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>(JobTitles.Sorter, sorterIDs),
                new AbstractMap.SimpleEntry<>(JobTitles.Storekeeper, storekeeperIDs),
                new AbstractMap.SimpleEntry<>(JobTitles.Carrier, carrierIDs),
                new AbstractMap.SimpleEntry<>(JobTitles.Cashier, cashierIDs),
                new AbstractMap.SimpleEntry<>(JobTitles.HR_Manager, hr_managerIDs),
                new AbstractMap.SimpleEntry<>(JobTitles.Logistics_Manager, logistics_managerIDs),
                new AbstractMap.SimpleEntry<>(JobTitles.Transport_Manager, transport_managersIDs)
                ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));
    }

    public Shift(Domain.Business.Objects.Shift.Shift bShift){
        this(bShift.getWorkday(), bShift.getShiftManagerId(),
                bShift.getCarrierCount(), bShift.getCashierCount(), bShift.getStorekeeperCount(), bShift.getSorterCount(), bShift.getHr_managersCount(), bShift.getLogistics_managersCount(), bShift.getTransport_managersCount(),
                bShift.getCarrierIDs(), bShift.getCashierIDs(), bShift.getStorekeeperIDs(), bShift.getSorterIDs(), bShift.getHr_managerIDs(), bShift.getLogistics_managerIDs(), bShift.getTransport_managerIDs());
    }

    public abstract Presentation.CLIPresentation.Screens.Shift accept(ScreenShiftFactory screenShiftFactory);

    public abstract ShiftTypes getType();
}
