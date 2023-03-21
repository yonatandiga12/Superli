package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.AgreementItem;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class AgreementController {


    private final RoutineDAO routineDAO;
    private final ByOrderDAO byOrderDAO;
    private final SelfTransportingDAO selfTransportDAO;
    private final AgreementItemDAO agreementItemDAO;


    private final int ROUTINE  = 1;
    private final int BY_ORDER  = 2;
    private final int NOT_TRANSPORTING  = 3;


    public AgreementController(){
        routineDAO = new RoutineDAO();
        byOrderDAO = new ByOrderDAO();
        selfTransportDAO = new SelfTransportingDAO();
        agreementItemDAO = new AgreementItemDAO();

    }




    public void updateAgreementDays(int supplierid, List<Integer> days, int agreementType) throws SQLException {
        switch(agreementType){
            case ROUTINE :
                routineDAO.addDaysOfDelivery(supplierid, days);
                routineDAO.setLastOrderIdAfterChangeType(supplierid);
                break;
            case BY_ORDER :
                byOrderDAO.addTime(supplierid, days);
                break;
            case NOT_TRANSPORTING :
                selfTransportDAO.updateSupplier(supplierid);
                break;
        }
    }


    public void removeSupplier(int id) throws SQLException {
        routineDAO.remove(id);
        byOrderDAO.remove(id);
        selfTransportDAO.remove(id);
        agreementItemDAO.removeSupplier(id);
    }


    public void addAgreementItems(List<AgreementItem> items, int supplierId) throws Exception {
        agreementItemDAO.addItemstoAgreement(supplierId, items);
    }

    public void updateBulkPriceForItem(int supId, int itemID, Map<Integer, Integer> newBulkPrices) throws SQLException {
        agreementItemDAO.updateBulkPrice(supId, itemID, newBulkPrices);
    }

    public AgreementItemDAO getAgreementItemDAO() {
        return  agreementItemDAO;
    }

    public void setDaysOfDelivery(int supplierId, List<Integer> list) throws SQLException {
        routineDAO.remove(supplierId);
        routineDAO.addDaysOfDelivery(supplierId, list);
    }

    public void setDeliveryDays(int supplierId, List<Integer> list) throws SQLException {
        byOrderDAO.remove(supplierId);
        byOrderDAO.addTime(supplierId, list);
    }

    public Agreement loadAgreementAndItems(int supplierId, int agreementType) throws SQLException {
        Agreement agreement;
        /*
        ROUTINE  = 1;
    private final int BY_ORDER  = 2;
    private final int NOT_TRANSPORTING
         */
        switch(agreementType){
            case ROUTINE :
                agreement = routineDAO.loadAgreement(supplierId);
                break;
            case BY_ORDER :
                agreement = byOrderDAO.loadAgreement(supplierId);
                break;
            case NOT_TRANSPORTING :
                agreement = selfTransportDAO.loadAgreement(supplierId);
                break;
            default:
                //selfTransportDAO.updateSupplier(supplierId);
                agreement = selfTransportDAO.loadAgreement(supplierId);;
        }

        Map<Integer, AgreementItem> items = agreementItemDAO.getAllAgreementItemFromSupplier(supplierId);
        agreement.addAgreementItems(items);
        return agreement;
    }

    public void setLastOrderId(int supplierId, int orderId) throws SQLException {
        routineDAO.setLastOrderId(supplierId, orderId);

    }

    public void removeDayOfDelivery(int supplierID, int day) throws SQLException {
        routineDAO.removeDayOfDelivery(supplierID, day);
    }

    public void changeDaysOfDelivery(int supplierId, List<Integer> daysOfDelivery, AgreementController agreementController) throws SQLException {
        routineDAO.changeDaysOfDelivery(supplierId, daysOfDelivery);
    }

    public void removeSupplierForChangingAgreement(int id) throws SQLException {
        routineDAO.remove(id);
        byOrderDAO.remove(id);
        selfTransportDAO.remove(id);
    }
}
