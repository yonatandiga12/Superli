package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Inventory.DefectiveItems;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;
import Globals.Defect;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefectiveItemsDataMapper extends DataMapper<DefectiveItems> {
    private final static int ID_COLUMN = 1;
    private final static int STORE_COLUMN = 2;
    private final static int PRODUCT_COLUMN = 3;
    private final static int DATE_COLUMN = 4;
    private final static int AMOUNT_COLUMN = 5;
    private final static int EMPLOYEE_ID_COLUMN = 6;
    private final static int DESCRIPTION_COLUMN = 7;
    private final static int DEFECT_COLUMN = 8;
    private final static int IN_WAREHOUSE_COLUMN = 9;

    private final static ConcurrentMap<String, DefectiveItems> IDENTITY_MAP = new ConcurrentHashMap<>();

    public DefectiveItemsDataMapper() {
        super("DefectiveItems");
    }


    @Override
    public String instanceToId(DefectiveItems instance) {
        return String.valueOf(instance.getId());
    }

    @Override
    protected Map<String, DefectiveItems> getMap() {
        return IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    protected DefectiveItems buildObject(ResultSet resultSet){
        try {
            Defect defect;
            String d = resultSet.getString(DEFECT_COLUMN);
            switch (d) {
                case ("Expired"):
                    defect = Defect.Expired;
                    break;
                case ("Damaged"):
                    defect = Defect.Damaged;
                    break;
                default:
                    throw new Exception("Illegal defect");
            }
            return new DefectiveItems(resultSet.getInt(ID_COLUMN),
                    defect,
                    (java.sql.Date.valueOf(resultSet.getString(DATE_COLUMN))).toLocalDate(),
                    //resultSet.getDate(DATE_COLUMN).toLocalDate(),
                    resultSet.getInt(STORE_COLUMN),
                    resultSet.getInt(PRODUCT_COLUMN),
                    resultSet.getInt(AMOUNT_COLUMN),
                    resultSet.getInt(EMPLOYEE_ID_COLUMN),
                    resultSet.getString(DESCRIPTION_COLUMN),
                    resultSet.getInt(IN_WAREHOUSE_COLUMN)==1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void insert(DefectiveItems instance) {
        //Formatter fmt = new Formatter()
        //String date = instance.getDate().format(fmt);
        String date = "" + instance.getDate().getYear() + "-" + ((instance.getDate().getMonthValue()<10) ? ("0" + instance.getDate().getMonthValue()) : (instance.getDate().getMonthValue())) + "-" + ((instance.getDate().getDayOfMonth()<10) ? ("0" + instance.getDate().getDayOfMonth()) : (instance.getDate().getDayOfMonth()));
        try {
            insert(Arrays.asList(instance.getId(),
                    instance.getStoreID(),
                    instance.getProductID(),
                    date,
                    instance.getAmount(),
                    instance.getEmployeeID(),
                    instance.getDescription(),
                    instance.getDefect(),
                    (instance.getInWarehouse()) ? (1) : (0)));
            IDENTITY_MAP.put(Integer.toString(instance.getId()), instance);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }


    public Collection<DefectiveItems> getByProduct(int productID) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(PRODUCT_COLUMN), Arrays.asList(productID));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<DefectiveItems> getByStore(int storeID) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(STORE_COLUMN), Arrays.asList(storeID));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<DefectiveItems> getByEmployee(int employeeID) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(EMPLOYEE_ID_COLUMN), Arrays.asList(employeeID));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<DefectiveItems> getByDate(Date date) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(DATE_COLUMN), Arrays.asList(date));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<DefectiveItems> getByDefect(Defect defect, int id) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(DEFECT_COLUMN, PRODUCT_COLUMN), Arrays.asList((defect==Defect.Damaged) ? ("Damaged") : ("Expired"), id));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<DefectiveItems> getDamagedByStore(int store, int product) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(DEFECT_COLUMN, STORE_COLUMN, PRODUCT_COLUMN), Arrays.asList("Damaged", store, product));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<DefectiveItems> getExpiredByStore(int store, int product) {
        List<DefectiveItems> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), Arrays.asList(DEFECT_COLUMN, STORE_COLUMN, PRODUCT_COLUMN), Arrays.asList("Expired", store, product));
            while (instanceResult.next()) {
                DefectiveItems curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(instanceResult.getInt(ID_COLUMN)), buildObject(instanceResult));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }
    public Integer getMax() {
        try (ConnectionHandler handler = getConnectionHandler()) {
            ResultSet max = getMax(handler.get(), ID_COLUMN);
            return max.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteByProduct(int id) {
        Collection<DefectiveItems> d = getByProduct(id);
        try {
            for (DefectiveItems def : d) {
                delete(String.valueOf(def.getId()));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
