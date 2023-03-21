package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.Agreement.NotTransportingAgreement;
import Domain.DAL.Abstract.DAO;

import java.sql.SQLException;
import java.util.Arrays;

public class SelfTransportingDAO extends DAO {

    public SelfTransportingDAO() {
        super("SelfTransport");
    }

    public void updateSupplier(int id) {
        try {
            insert(Arrays.asList(id));
        } catch (SQLException throwable) {
            System.out.println(throwable.getMessage());
        }
    }

    public Agreement loadAgreement(int supplierId) {
        return new NotTransportingAgreement();
    }
}
