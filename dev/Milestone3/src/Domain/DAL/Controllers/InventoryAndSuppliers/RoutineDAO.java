package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.Agreement.RoutineAgreement;
import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RoutineDAO extends DAO {

    public RoutineDAO() {
        super("Routine");
    }

    private final static int SUPPLIER_ID_COLUMN = 1;
    private final static int DAY_COLUMN = 2;
    private final static int LAST_ORDER_ID_COLUMN = 3;
    private static int lastOrderIfd = -1;

    public void addDaysOfDelivery(int supplierId, List<Integer> daysOfDelivery) {
        for(Integer currDay : daysOfDelivery){
            try {
                insert(Arrays.asList(supplierId, currDay, lastOrderIfd));
            } catch (SQLException throwables) {
                System.out.println(throwables.getMessage());
            }
        }
    }


    public Agreement loadAgreement(int supplierId) {
        List<Integer> days = new ArrayList<>();
        ResultSet resultSet = null;
        int lastOrderId = -1;
        try(ConnectionHandler handler = getConnectionHandler()) {
            resultSet = select(handler.get(), supplierId);
            while(resultSet.next()){
                days.add(resultSet.getInt(DAY_COLUMN));
                lastOrderId = resultSet.getInt(LAST_ORDER_ID_COLUMN);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return new RoutineAgreement(days, lastOrderId);
    }

    public void setLastOrderId(int supplierId, int orderId) throws SQLException {
        update(Arrays.asList(LAST_ORDER_ID_COLUMN), Arrays.asList(orderId), Arrays.asList(SUPPLIER_ID_COLUMN), Arrays.asList(supplierId));
        lastOrderIfd = orderId;
    }

    public void setLastOrderIdAfterChangeType(int supplierId) throws SQLException {
        if(lastOrderIfd != -1)
            update(Arrays.asList(LAST_ORDER_ID_COLUMN), Arrays.asList(lastOrderIfd), Arrays.asList(SUPPLIER_ID_COLUMN), Arrays.asList(supplierId));

    }

    public void removeDayOfDelivery(int supplierID, int day) throws SQLException {
        remove(Arrays.asList(SUPPLIER_ID_COLUMN, DAY_COLUMN), Arrays.asList(supplierID, day));
    }

    public void changeDaysOfDelivery(int supplierId, List<Integer> daysOfDelivery) throws SQLException {
        remove(Arrays.asList(SUPPLIER_ID_COLUMN), Arrays.asList(supplierId));
        addDaysOfDelivery(supplierId, daysOfDelivery);
        setLastOrderId(supplierId, lastOrderIfd);
    }
}
