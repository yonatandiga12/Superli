package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BulkPricesDAO extends DAO {


    public BulkPricesDAO() {
        super("BulkPrices");
    }



    public void addBulkPrices(int supID, int productId, Map<Integer, Integer> bulkPrices) throws SQLException {

        for(Map.Entry<Integer, Integer> enrty : bulkPrices.entrySet()){
            insert(Arrays.asList(String.valueOf(productId), String.valueOf(supID), String.valueOf(enrty.getKey()) , String.valueOf(enrty.getValue())));
        }

    }

    public void updateBulkPrice(int supID,int itemID, Map<Integer, Integer> newBulkPrices) throws SQLException {
        remove(itemID);
        addBulkPrices(supID, itemID, newBulkPrices);
    }


    public Map<Integer, Integer> getAllBulkPrices(int supID, int itemId) {
        Map<Integer, Integer> output = new HashMap<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(),itemId);
            while (instanceResult.next()) {
                if(supID == instanceResult.getInt(2)){
                    output.put(instanceResult.getInt(3), instanceResult.getInt(4));
                }
            }
        } catch (Exception throwables) {
            System.out.println(throwables.getMessage());
        }

        return output;
    }


    public void removeSupplierBulk(int supplierId, int productId) throws SQLException {
        remove(Arrays.asList(1, 2), Arrays.asList(productId, supplierId));

    }
}
