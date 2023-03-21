package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Inventory.SaleToCustomer;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SalesDataMapper extends DataMapper<SaleToCustomer> {

    private final static ConcurrentMap<String, SaleToCustomer> SALE_IDENTITY_MAP = new ConcurrentHashMap<>();
    private final static SalesToProductDAO salesToProductDAO = new SalesToProductDAO();
    private final static SalesToCategoryDAO salesToCategoryDAO = new SalesToCategoryDAO();

    private final static int ID_COLUMN = 1;
    private final static int START_DATE_COLUMN = 2;
    private final static int END_DATE_COLUMN = 3;
    private final static int PERCENT_COLUMN = 4;

    public SalesDataMapper(){
        super("Sales");
    }

    public Map<Integer, SaleToCustomer> getIntegerMap() {
        Map<Integer, SaleToCustomer> output = new HashMap<>();
        for (Map.Entry<String, SaleToCustomer> entry: SALE_IDENTITY_MAP.entrySet()) {
            output.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return output;
    }

    @Override
    public String instanceToId(SaleToCustomer instance) {
        return String.valueOf(instance.getId());
    }

    @Override
    public Map getMap() {
        return SALE_IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    @Override
    protected SaleToCustomer buildObject(ResultSet resultSet) {
        try {
            return new SaleToCustomer(resultSet.getInt(ID_COLUMN),
                    (java.sql.Date.valueOf(resultSet.getString(START_DATE_COLUMN))).toLocalDate(),
                    (java.sql.Date.valueOf(resultSet.getString(END_DATE_COLUMN))).toLocalDate(),
                    resultSet.getInt(PERCENT_COLUMN),
                    getCategories(resultSet.getInt(ID_COLUMN)),
                    getProducts(resultSet.getInt(ID_COLUMN))
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Integer> getProducts(int sale) {
        return salesToProductDAO.getProductsOfSale(sale);
    }

    private List<Integer> getCategories(int sale) {
        return salesToCategoryDAO.getCategoriesOfSale(sale);
    }

    public int remove(Object id) throws SQLException {
        salesToProductDAO.removeBySale(id);
        salesToCategoryDAO.removeBySale(id);
        return super.remove(id);//super.remove(id);
    }

    @Override
    public void insert(SaleToCustomer instance){
        String startDate = "" + instance.getStartDate().getYear() + "-" + ((instance.getStartDate().getMonthValue()<10) ? ("0" + instance.getStartDate().getMonthValue()) : (instance.getStartDate().getMonthValue())) + "-" + ((instance.getStartDate().getDayOfMonth()<10) ? ("0" + instance.getStartDate().getDayOfMonth()) : (instance.getStartDate().getDayOfMonth()));
        String endDate = "" + instance.getEndDate().getYear() + "-" + ((instance.getEndDate().getMonthValue()<10) ? ("0" + instance.getEndDate().getMonthValue()) : (instance.getEndDate().getMonthValue())) + "-" + ((instance.getEndDate().getDayOfMonth()<10) ? ("0" + instance.getEndDate().getDayOfMonth()) : (instance.getEndDate().getDayOfMonth()));
        try {
            insert(Arrays.asList(instance.getId(),
                    startDate,
                    endDate,
                    instance.getPercent()));
            SALE_IDENTITY_MAP.put(Integer.toString(instance.getId()), instance);
            for (int c : instance.getCategories())
                salesToCategoryDAO.insert(Arrays.asList(instance.getId(), c));
            for (int p : instance.getProducts())
                salesToProductDAO.insert(Arrays.asList(instance.getId(), p));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }


    public Collection<SaleToCustomer> getSalesByCategory(int category) {
        List<SaleToCustomer> output = new ArrayList<>();
        List<Integer> saleIDs = salesToCategoryDAO.getSales(category);
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), ID_COLUMN, saleIDs);
            while (instanceResult.next()) {
                SaleToCustomer curr = buildObject(instanceResult);
                output.add(curr);
                SALE_IDENTITY_MAP.put(Integer.toString(curr.getId()), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<SaleToCustomer> getSalesByProduct(int product) {
        List<SaleToCustomer> output = new ArrayList<>();
        List<Integer> saleIDs = salesToProductDAO.getSales(product);
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), ID_COLUMN, saleIDs);
            while (instanceResult.next()) {
                SaleToCustomer curr = buildObject(instanceResult);
                output.add(curr);
                SALE_IDENTITY_MAP.put(Integer.toString(curr.getId()), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SALE_IDENTITY_MAP.values();
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

    public void updateEndDate(int saleID, LocalDate date) {
        String endDate = "" + date.getYear() + "-" + ((date.getMonthValue()<10) ? ("0" + date.getMonthValue()) : (date.getMonthValue())) + "-" + ((date.getDayOfMonth()<10) ? ("0" + date.getDayOfMonth()) : (date.getDayOfMonth()));
        try {
            updateProperty(Integer.toString(saleID), END_DATE_COLUMN, endDate);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteByProduct(int id) {
        Collection<SaleToCustomer> d = getSalesByProduct(id);
        try {
            for (SaleToCustomer def : d) {
                delete(String.valueOf(def.getId()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
