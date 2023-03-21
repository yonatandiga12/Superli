package Domain.Business.Objects;

import Globals.Enums.LicenseTypes;
import Globals.Enums.TruckModel;

import java.util.Set;

import static Globals.Enums.LicenseTypes.*;

public class Truck {
    private int licenseNumber;
    private TruckModel model;
    private int netWeight;
    private int maxCapacityWeight;

    public Truck(int licenseNumber, TruckModel model, int netWeight, int maxCapacityWeight) {
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxCapacityWeight = maxCapacityWeight;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(int licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public TruckModel getModel() {
        return model;
    }

    public void setModel(TruckModel model) {
        this.model = model;
    }

    public int getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(int netWeight) {
        this.netWeight = netWeight;
    }

    public int getMaxCapacityWeight() {
        return maxCapacityWeight;
    }

    public void setMaxCapacityWeight(int maxCapacityWeight) {
        this.maxCapacityWeight = maxCapacityWeight;
    }

    public boolean canDriveOn(Set<LicenseTypes> lt) {
        boolean ans = false;
        LicenseTypes l = null;
        switch (model)
        {
            case Van:
                ans = lt.contains(B)||lt.contains(C)||lt.contains(C1)||lt.contains(CE);
                break;
            case SemiTrailer:
                ans = lt.contains(C)||lt.contains(C1)||lt.contains(CE);
                break;
            case DoubleTrailer:
                ans = lt.contains(C1)||lt.contains(CE);
                break;
            case FullTrailer:
                ans = lt.contains(CE);
                break;
            default:
        }
        return ans;
    }

}
