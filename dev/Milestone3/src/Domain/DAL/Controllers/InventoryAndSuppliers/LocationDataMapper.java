package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Inventory.Location;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocationDataMapper extends DataMapper<Location> {
    private final static ConcurrentMap<String, Location> IDENTITY_MAP = new ConcurrentHashMap<>();

    private final static LocationToShelfDAO locationToShelfDAO = new LocationToShelfDAO();

    private final static int ID_COLUMN = 1;
    private final static int PRODUCT_COLUMN = 2;
    private final static int STORE_COLUMN = 3;
    private final static int IN_WAREHOUSE_COLUMN = 4;

    public LocationDataMapper(){
        super("Location");
    }

    @Override
    public String instanceToId(Location instance) {
        return String.valueOf(instance.getLocationID());
    }

    @Override
    public Map getMap() {
        return IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    @Override
    protected Location buildObject(ResultSet resultSet) {
        try {
            return new Location(resultSet.getInt(ID_COLUMN),
                    resultSet.getInt(STORE_COLUMN),
                    resultSet.getInt(IN_WAREHOUSE_COLUMN)==1,
                    getShelves(resultSet.getInt(ID_COLUMN))
            );
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Integer> getShelves(int location) {
        return locationToShelfDAO.getShelves(location);
    }


    public int remove(Object id) {
        try {
            locationToShelfDAO.remove(id);
            return super.remove(id);
        }
        catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void insert(Location instance, int productID){
        try {
            int id = instance.getLocationID();
            insert(Arrays.asList(id,
                    productID,
                    instance.getStoreID(),
                    instance.getInWarehouse() ? 1 : 0));
            IDENTITY_MAP.put(Integer.toString(instance.getLocationID()), instance);
            for (Integer shelf : instance.getShelves()) {
                locationToShelfDAO.insert(Arrays.asList(id, shelf));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Location instance) {
        throw new RuntimeException("Will not be implemented for Location. Use insert(Location l, int product) instead");
//        try {
//            insert(Arrays.asList(instance.getLocationID(),
//                    instance.getStartDate(),
//                    instance.getEndDate(),
//                    instance.getPercent()));
//            IDENTITY_MAP.put(Integer.toString(instance.getId()), instance);
//            for (int c : instance.getCategories())
//                salesToProductDAO.insert(Arrays.asList(instance.getId(), c));
//            for (int p : instance.getProducts())
//                salesToProductDAO.insert(Arrays.asList(instance.getId(), p));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }

    public Collection<Location> getLocationByProduct(int product) {
        List<Location> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), PRODUCT_COLUMN, Collections.singletonList(product));
            while (instanceResult.next()) {
                Location curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(curr.getLocationID()), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public Collection<Location> getLocationByStore(int store) {
        List<Location> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), STORE_COLUMN, Collections.singletonList(store));
            while (instanceResult.next()) {
                Location curr = buildObject(instanceResult);
                output.add(curr);
                IDENTITY_MAP.put(Integer.toString(curr.getLocationID()), curr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public void removeByStore(int storeID) {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet locationsToRemove = select(handler.get(), Arrays.asList(STORE_COLUMN), Arrays.asList(storeID));
            List<Integer> locationsIDs = new ArrayList<>();
            while (locationsToRemove.next()) {
                locationsIDs.add(locationsToRemove.getInt(ID_COLUMN));
            }
            for (Integer i : locationsIDs) {
                locationToShelfDAO.remove(i);
            }
            remove(Arrays.asList(STORE_COLUMN), Arrays.asList(storeID));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getMax() {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet max = getMax(handler.get(), ID_COLUMN);
            return max.getInt(1);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void removeByProduct(Integer product) {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet locationsToRemove = select(handler.get(), Arrays.asList(PRODUCT_COLUMN), Arrays.asList(product));
            List<Integer> locationsIDs = new ArrayList<>();
            while (locationsToRemove.next()) {
                locationsIDs.add(locationsToRemove.getInt(ID_COLUMN));
            }
            for (Integer i : locationsIDs) {
                locationToShelfDAO.remove(i);
            }
            remove(Arrays.asList(PRODUCT_COLUMN), Arrays.asList(product));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
