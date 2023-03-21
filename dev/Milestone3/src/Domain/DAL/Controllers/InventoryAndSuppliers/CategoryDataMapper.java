package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Inventory.Category;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CategoryDataMapper extends DataMapper<Category> {

    private final static ConcurrentMap<String, Category> CATEGORY_IDENTITY_MAP = new ConcurrentHashMap<>();

    private final static int ID_COLUMN = 1;
    private final static int NAME_COLUMN = 2;
    private final static int PARENT_COLUMN = 3;

    public CategoryDataMapper(){
        super("Category");
    }

    public Map<Integer, Category> getIntegerMap() {
        Map<Integer, Category> output = new HashMap<>();
        for (Map.Entry<String, Category> entry: CATEGORY_IDENTITY_MAP.entrySet()) {
            output.put(Integer.parseInt(entry.getKey()), entry.getValue());
        }
        return output;
    }

    @Override
    public Map getMap() {
        return CATEGORY_IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    @Override
    protected Category buildObject(ResultSet instanceResult) throws Exception {
        return new Category(instanceResult.getInt(ID_COLUMN),
                instanceResult.getString(NAME_COLUMN),
                new HashSet<>(), //might cause bugs later
                new ArrayList<>(), //might cause bugs later
                get(Integer.toString(instanceResult.getInt(PARENT_COLUMN))));
    }

    public int remove(Object id) {
        try {
            CATEGORY_IDENTITY_MAP.remove(id);
            return super.remove(id);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void insert(Category instance) {
        try {
            insert(Arrays.asList(instance.getID(),
                    instance.getName(),
                    instance.getParentCategory()==null ? null : instance.getParentCategory().getID()));
            CATEGORY_IDENTITY_MAP.put(Integer.toString(instance.getID()), instance);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String instanceToId(Category instance) {
        return String.valueOf(instance.getID());
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }

    public void updateParentCategory(int category, Integer newParent) {
        try {
            updateProperty(Integer.toString(category), PARENT_COLUMN,newParent);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateName(int category, String newName) {
        try {
            updateProperty(Integer.toString(category), NAME_COLUMN,newName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    public void removeTestCategories() {
        try {
            executeNonQuery(String.format("DELETE FROM %s WHERE %s LIKE \"%s\"",tableName, getColumnName(NAME_COLUMN), "Test%"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
