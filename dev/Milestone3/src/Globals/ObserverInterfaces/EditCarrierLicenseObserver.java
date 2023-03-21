package Globals.ObserverInterfaces;

import Globals.Enums.LicenseTypes;

import java.util.Set;

public interface EditCarrierLicenseObserver {
    void observe(String id, Set<LicenseTypes> newLicenses) throws Exception;
}
