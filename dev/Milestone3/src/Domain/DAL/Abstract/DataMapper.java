package Domain.DAL.Abstract;

import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class DataMapper<T> extends DAO {
    public DataMapper(String tableName) {
        super(tableName);
    }

    public synchronized T get(String id) throws Exception {
        Map<String,T> map = getMap();
        T output = map.get(id);
        if (output != null)
            return output;

        try(ConnectionHandler connection = getConnectionHandler()){
            ResultSet instanceResult = select(connection.get(), id);
            if (!instanceResult.next())
                return null;


            output = buildObject(instanceResult);
            map.put(id, output);
            return output;
        }
        catch (SQLException e){
            throw new RuntimeException("FATAL ERROR WITH DB CONNECTION. STOP WORK IMMEDIATELY!");
        }
    }

    /**
     *
     * @param id the id of instance you want to update
     * @param propertyColumnNumber the columnNumber you want to update
     * @param toUpdate the value to update
     * @param <K> whatever
     * @throws RuntimeException
     * @throws SQLException
     */
    public synchronized  <K> void updateProperty (String id, int propertyColumnNumber,K toUpdate) throws RuntimeException, SQLException {
       /* T instance = get(id); // throw exception if not found
        getUpdateFunction(propertyColumnNumber).update(instance,toUpdate); */
        super.update(Arrays.asList(propertyColumnNumber),Arrays.asList(toUpdate),Arrays.asList(1),Arrays.asList(id));
    }
    // protected abstract <K>UpdateFunction<T,K> getUpdateFunction(int propertyColumnNumber);

    public <K> void addToSet(String id, String listName, K toAdd) throws SQLException{
        getLinkDTO(listName).add(id,toAdd);
    }

    public <K> void removeFromSet(String id, String listName, K toRemove) throws SQLException{
        getLinkDTO(listName).remove(id,toRemove);
    }

    public <K> void replaceSet(String id, String listName, Set<K> toReplace) throws SQLException{
        getLinkDTO(listName).replaceSet(id,toReplace);
    }

    public synchronized void save(String id, T instance) throws SQLException{
        insert(instance);
        getMap().put(id,instance);
    }

    protected void removeFromIdentityMap(String id){
        getMap().remove(id);
    }

    public int delete(String id) throws SQLException {
        Set<LinkDAO> links = getAllLinkDTOs();
        for (LinkDAO link : links)
            link.remove(id);
        removeFromIdentityMap(id);
        return remove(id);
    }

    public Set<T>getAll()throws Exception{
        Set<T> output = new HashSet<>();
        try(ConnectionHandler connection = getConnectionHandler()){
            ResultSet resultSet = super.select(connection.get());
            while (resultSet.next()) {
                if (getMap().get(resultSet.getString(1)) != null)
                    output.add(getMap().get(resultSet.getString(1)));
                else {
                    T instance = buildObject(resultSet);
                    output.add(buildObject(resultSet));
                    getMap().put(instanceToId(instance), instance);
                }
            }
        }
        return output;
    }
    protected abstract Map<String, T> getMap();
    protected abstract  LinkDAO getLinkDTO(String setName);
    protected abstract T buildObject(ResultSet instanceResult) throws Exception;
    public abstract void insert(T instance) throws SQLException;
    public abstract String instanceToId(T instance);

    /**
     *
     * @return a set of all the linkDAOs that the objects holds
     */
    protected abstract Set<LinkDAO> getAllLinkDTOs();
}