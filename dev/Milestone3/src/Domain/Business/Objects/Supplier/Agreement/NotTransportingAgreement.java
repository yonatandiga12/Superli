package Domain.Business.Objects.Supplier.Agreement;

import java.util.ArrayList;
import java.util.List;

public class NotTransportingAgreement extends Agreement {

    public NotTransportingAgreement() {
        super();
    }

    @Override
    public boolean isTransporting() {
        return false;
    }

    @Override
    public int daysToDelivery() {
        return 2; // means not delivering
    }

    public List<Integer> getDays() {
        List<Integer> result = new ArrayList<>();
        result.add(-1);
        return result;
    }
}
