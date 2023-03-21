package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.Agreement.ByOrderAgreement;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ByOrderDAO extends DAO {


    public ByOrderDAO() {
        super("ByOrder");
    }

    public void addTime(int id, List<Integer> agreementDays) {

        try {
            insert(Arrays.asList(id,  agreementDays.get(0)));
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }

    }

    public Agreement loadAgreement(int supplierId) {
        ResultSet resultSet = null;
        int result = 2;
        try (ConnectionHandler handler = getConnectionHandler()){
            resultSet = select(handler.get(),supplierId);
            while(resultSet.next()){
                result = resultSet.getInt(2);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new ByOrderAgreement(result);
    }
}
