package Domain.Business.Objects.Site;

import Globals.Enums.ShippingAreas;

public class Address {
    private ShippingAreas shippingAreas;
    private String exactAddress;

    public Address(ShippingAreas shippingAreas, String exactAddress) {
        this.shippingAreas = shippingAreas;
        this.exactAddress = exactAddress;
    }

    public ShippingAreas getShippingAreas() {
        return shippingAreas;
    }

    public void setShippingAreas(ShippingAreas shippingAreas) {
        this.shippingAreas = shippingAreas;
    }

    public String getExactAddress() {
        return exactAddress;
    }

    public void setExactAddress(String exactAddress) {
        this.exactAddress = exactAddress;
    }
}
