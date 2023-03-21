package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Inventory.Category;
import Domain.Business.Objects.Inventory.Product;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ProductDataMapper extends DataMapper<Product> {


    private final static ConcurrentMap<String, Product> PRODUCT_IDENTITY_MAP = new ConcurrentHashMap<>();
    private final static CategoryDataMapper CATEGORY_DATA_MAPPER = Category.CATEGORY_DATA_MAPPER;

    private final static int ID_COLUMN = 1;
    private final static int CATEGORY_COLUMN = 2;
    private final static int NAME_COLUMN = 3;
    private final static int WEIGHT_COLUMN = 4;
    private final static int MANUFACTURER_COLUMN = 5;
    private final static int PRICE_COLUMN = 6;

    public ProductDataMapper(){
        super("Product");
    }

    public Map<Integer, Product> getIntegerMap() {
        Map<Integer, Product> output = new HashMap<>();
        for (Map.Entry<String, Product> entry: PRODUCT_IDENTITY_MAP.entrySet()) {
            output.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return output;
    }

    @Override
    public String instanceToId(Product instance) {
        return String.valueOf(instance.getId());
    }

    @Override
    public Map getMap() {
        return PRODUCT_IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    @Override
    protected Product buildObject(ResultSet resultSet) {
        try {
            return new Product(resultSet.getInt(ID_COLUMN),
                    resultSet.getString(NAME_COLUMN),
                    CATEGORY_DATA_MAPPER.get(Integer.toString(resultSet.getInt(CATEGORY_COLUMN))),
                    resultSet.getDouble(WEIGHT_COLUMN),
                    resultSet.getDouble(PRICE_COLUMN),
                    resultSet.getString(MANUFACTURER_COLUMN)
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int remove(Object id) {
        try{
            PRODUCT_IDENTITY_MAP.remove(id);
            return super.remove(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void insert(Product instance){
        try {
            insert(Arrays.asList(instance.getId(),
                    instance.getCategoryID(),
                    instance.getName(),
                    instance.getWeight(),
                    instance.getManufacturer(),
                    instance.getOriginalPrice()));
            PRODUCT_IDENTITY_MAP.put(Integer.toString(instance.getId()), instance);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }

    public void updateCategory(int productID, int category) {
        try {
            updateProperty(Integer.toString(productID), CATEGORY_COLUMN, category);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePrice(int productID, double price) {
        try {
            updateProperty(Integer.toString(productID), PRICE_COLUMN, price);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateName(int productID, String name) {
        try {
            updateProperty(Integer.toString(productID), NAME_COLUMN, name);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }



    public List<Product> getProductsFromCategory(int id) {
        try(ConnectionHandler handler = getConnectionHandler()) {
            List<Integer> productIDs = new ArrayList<>();
            ResultSet resultSet = executeQuery(handler.get(), String.format("Select %s from %s where %s=" + id,
                    getColumnName(ID_COLUMN),
                    tableName,
                    getColumnName(CATEGORY_COLUMN)));
            while (resultSet.next()) {
                productIDs.add(resultSet.getInt(1));
            }
            List<Product> products = new ArrayList<>();
            for (Integer productID: productIDs) {
                products.add(get(Integer.toString(productID)));
            }
            return products;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getIDCount() {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = getMax(handler.get(), ID_COLUMN);
            while (instanceResult.next()) {
                return instanceResult.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void removeTestProducts() {
        try(ConnectionHandler handler = getConnectionHandler()){
            ResultSet resultSet = executeQuery(handler.get(),String.format("Select * FROM %s WHERE %s LIKE \"%s\"",tableName, getColumnName(NAME_COLUMN), "Test%"));
            List<Integer> products = new ArrayList<>();
            while (resultSet.next()) {
                products.add(resultSet.getInt(ID_COLUMN));
            }
            LocationDataMapper locationDataMapper = new LocationDataMapper();
            for (Integer product : products)
                locationDataMapper.removeByProduct(product);
            StockReportDataMapper stockReportDataMapper = new StockReportDataMapper();
            for (Integer product : products)
                stockReportDataMapper.removeProduct(product);
            executeNonQuery(String.format("DELETE FROM %s WHERE %s LIKE \"%s\"",tableName, getColumnName(NAME_COLUMN), "Test%"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
