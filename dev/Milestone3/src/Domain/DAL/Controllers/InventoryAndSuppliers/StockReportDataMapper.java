package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Inventory.StockReport;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;
import Globals.Pair;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class StockReportDataMapper extends DataMapper<StockReport> {

    private final static int STORE_COLUMN = 1;
    private final static int PRODUCT_COLUMN = 2;
    private final static int AMOUNT_IN_STORE_COLUMN = 3;
    private final static int AMOUNT_IN_WAREHOUSE_COLUMN = 4;
    private final static int MIN_COLUMN = 5;
    private final static int TARGET_COLUMN = 6;
    private final static int IN_DELIVERY_COLUMN = 7;

    private final static ConcurrentMap<Pair<Integer, Integer>, StockReport> IDENTITY_MAP = new ConcurrentHashMap<>();

    public StockReportDataMapper() {
        super("StockReport");
    }

    @Override
    public String instanceToId(StockReport instance) {
        return null;
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return null;
    }

    @Override
    protected Map<String, StockReport> getMap() {
        return null;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    protected StockReport buildObject(ResultSet resultSet){
        try {
            return new StockReport(resultSet.getInt(STORE_COLUMN),
                    resultSet.getInt(PRODUCT_COLUMN),
                    resultSet.getInt(AMOUNT_IN_STORE_COLUMN),
                    resultSet.getInt(AMOUNT_IN_WAREHOUSE_COLUMN),
                    resultSet.getInt(MIN_COLUMN),
                    resultSet.getInt(TARGET_COLUMN),
                    resultSet.getInt(IN_DELIVERY_COLUMN)
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(StockReport instance) {
        try {
            insert(Arrays.asList(instance.getStoreID(),
                    instance.getProductID(),
                    instance.getAmountInStore(),
                    instance.getAmountInWarehouse(),
                    instance.getMinAmountInStore(),
                    instance.getTargetAmountInStore(),
                    instance.getAmountInDeliveries()));
            IDENTITY_MAP.put(new Pair<>(instance.getStoreID(), instance.getProductID()), instance);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(int storeId, int productID) {
        Pair<Integer, Integer> key = new Pair(storeId, productID);
        try {
            remove(Arrays.asList(STORE_COLUMN, PRODUCT_COLUMN), Arrays.asList(storeId, productID));
            IDENTITY_MAP.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(int storeId) {
        try {
            remove(Arrays.asList(STORE_COLUMN), Arrays.asList(storeId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StockReport get(int storeId, int productID) {
        Pair<Integer, Integer> key = new Pair(storeId, productID);
        StockReport output;

        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(),Arrays.asList(STORE_COLUMN, PRODUCT_COLUMN), Arrays.asList(storeId, productID));
            if (!instanceResult.next())
                return null;
            output = buildObject(instanceResult);
            IDENTITY_MAP.put(key,output);
            return output;
        } catch (Exception e) {
            return null;
        }
    }

    public Set<StockReport> getAll() {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get());
            while (instanceResult.next()) {
                IDENTITY_MAP.put(new Pair<>(instanceResult.getInt(STORE_COLUMN), instanceResult.getInt(PRODUCT_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashSet<>(IDENTITY_MAP.values());
    }

    public Collection<StockReport> getByProduct(int productID) {
        List<StockReport> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(PRODUCT_COLUMN), Arrays.asList(productID));
            while (instanceResult.next()) {
                StockReport curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(new Pair<>(curr.getStoreID(), curr.getProductID()), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<StockReport> getByStore(int storeID) {
        List<StockReport> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(STORE_COLUMN), Arrays.asList(storeID));
            while (instanceResult.next()) {
                StockReport curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(new Pair<>(curr.getStoreID(), curr.getProductID()), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void updateMin(int productID, int storeID, int min) {
        try {
            update(Arrays.asList(MIN_COLUMN),
                    Arrays.asList(min),
                    Arrays.asList(PRODUCT_COLUMN, STORE_COLUMN),
                    Arrays.asList(productID, storeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTarget(int productID, int storeID, int target) {
        try {
            update(Arrays.asList(TARGET_COLUMN),
                    Arrays.asList(target),
                    Arrays.asList(PRODUCT_COLUMN, STORE_COLUMN),
                    Arrays.asList(productID, storeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInStore(int productID, int storeID, int amount) {
        try {
            update(Arrays.asList(AMOUNT_IN_STORE_COLUMN),
                    Arrays.asList(amount),
                    Arrays.asList(PRODUCT_COLUMN, STORE_COLUMN),
                    Arrays.asList(productID, storeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInWarehouse(int productID, int storeID, int amount) {
        try {
            update(Arrays.asList(AMOUNT_IN_WAREHOUSE_COLUMN),
                    Arrays.asList(amount),
                    Arrays.asList(PRODUCT_COLUMN, STORE_COLUMN),
                    Arrays.asList(productID, storeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateInDelivery(int productID, int storeID, int amount) {
        try {
            update(Arrays.asList(IN_DELIVERY_COLUMN),
                    Arrays.asList(amount),
                    Arrays.asList(PRODUCT_COLUMN, STORE_COLUMN),
                    Arrays.asList(productID, storeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<Integer> getProductsUnderMin() {
        try (ConnectionHandler handler = getConnectionHandler()){
            Set<Integer> products = new HashSet<>();
            ResultSet resultSet = executeQuery(handler.get(), String.format("Select %s from %s where %s+%s+%s<%s",
                    getColumnName(PRODUCT_COLUMN),
                    tableName,
                    getColumnName(IN_DELIVERY_COLUMN),
                    getColumnName(AMOUNT_IN_STORE_COLUMN),
                    getColumnName(AMOUNT_IN_WAREHOUSE_COLUMN),
                    getColumnName(MIN_COLUMN)));
            while (resultSet.next()) {
                products.add(resultSet.getInt(1));
            }
            return products;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public StockReport getProductStockReport(int productID, int storeID) {
        return get(storeID, productID);
    }

    public void removeProduct(Integer product) {
        try {
            remove(Arrays.asList(PRODUCT_COLUMN), Arrays.asList(product));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
