package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.Business.Objects.Supplier.Agreement.Agreement;
import Domain.Business.Objects.Supplier.Contact;
import Domain.Business.Objects.Supplier.Order;
import Domain.Business.Objects.Supplier.Supplier;
import Domain.DAL.Abstract.DataMapper;
import Domain.DAL.Abstract.LinkDAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SuppliersDAO extends DataMapper<Supplier> {


    private final static Map<String, Supplier> SUPPLIER_IDENTITY_MAP = new HashMap<>();

    private final ContactDAO contactDAO;
    private final ManufacturerDAO manufacturerDAO;
    private final AgreementController agreementController;



    private final static int ID_COLUMN = 1;
    private final static int BANK_COLUMN = 2;
    private final static int ADDRESS_COLUMN = 3;
    private final static int NAME_COLUMN = 4;
    private final static int PAYMENT_COLUMN = 5;
    private final static int AGREEMENTTYPE_COLUMN = 6;

    public SuppliersDAO(){
        super("Supplier");
        contactDAO = new ContactDAO();
        manufacturerDAO = new ManufacturerDAO();
        agreementController = new AgreementController();
    }


    @Override
    protected Map<String, Supplier> getMap() {
        return SUPPLIER_IDENTITY_MAP;
    }

    @Override
    protected LinkDAO getLinkDTO(String setName) {
        return null;
    }

    @Override
    protected Supplier buildObject(ResultSet instanceResult) throws Exception {
        return new Supplier(instanceResult.getInt(ID_COLUMN),instanceResult.getInt(BANK_COLUMN),instanceResult.getString(ADDRESS_COLUMN),instanceResult.getString(NAME_COLUMN)
                ,instanceResult.getString(PAYMENT_COLUMN));
    }

    @Override
    public void insert(Supplier supplier) throws SQLException {

        insert(Arrays.asList(String.valueOf(supplier.getId()), String.valueOf(supplier.getBankNumber()),
                String.valueOf(supplier.getAddress()), String.valueOf(supplier.getName()), String.valueOf(supplier.getPayingAgreement()), String.valueOf(supplier.getAgreementType())));

        List<Contact> contacts = supplier.getAllContact();
        if(contacts != null && contacts.size()> 0){
            for(Contact contact : contacts){
                contactDAO.addContact(supplier.getId(), contact);
            }
        }

        List<String> manufacturers = supplier.getManufacturers();
        if(manufacturers != null && manufacturers.size() > 0){
            for(String manufacturer : manufacturers){
                manufacturerDAO.addManufacturer(supplier.getId(), manufacturer);
            }
        }

    }

    @Override
    public String instanceToId(Supplier instance) {
        return String.valueOf(instance.getId());
    }

    @Override
    protected Set<LinkDAO> getAllLinkDTOs() {
        return new HashSet<>();
    }


    public Supplier getSupplier(int id){

        try {
            return get(String.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public ArrayList<Supplier> getAllSuppliers(){
        ArrayList<Supplier> suppliers = new ArrayList<>();
        for(Supplier supplier : SUPPLIER_IDENTITY_MAP.values()){
            suppliers.add(supplier);
        }
        return suppliers;
    }


    public void removeSupplier(int id){
        try {

            manufacturerDAO.remove(id);
            contactDAO.remove(id);
            agreementController.removeSupplier(id);

            remove(id);
            SUPPLIER_IDENTITY_MAP.remove("" + id);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public boolean supplierExist(int id){
        makeSureSupplierInMap(id);
        //If we upload all the suppliers from the beginning we should have all of them in the map
        if(SUPPLIER_IDENTITY_MAP.containsKey(String.valueOf(id)))
            return true;
        return false;
    }

    public boolean isEmpty() {
        return (getAllSuppliers().size() == 0);

    }

    public void addSupplierContact(int id, Contact contact) {
        contactDAO.addContact(id, contact);
    }

    public void removeSupplierContact(int supID, String name) {
        contactDAO.removeContact(supID, name);
    }

    public ArrayList<Contact> getAllSupplierContacts(int supID) {
        return contactDAO.getAllSupplierContact(supID);
    }

    public void addSupplierManufacturer(int id, String name){
        manufacturerDAO.addManufacturer(id, name);
    }

    public void removeSupplierManufacturer(int id, String name){
        manufacturerDAO.removeManufacturer(id, name);
    }

    public ArrayList<String> getAllSupplierManufacturers(int id){
        return manufacturerDAO.getAllSupplierManufacturer(id);
    }


    public void updateSupplierAddress(int id, String address) {

        try {

            updateProperty(String.valueOf(id), ADDRESS_COLUMN , address);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public void updateSupplierBankNumber(int id, int bankNumber) {
        try {
            updateProperty(String.valueOf(id), BANK_COLUMN, bankNumber);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public void updateSupplierName(int id, String newName) {
        try {
            updateProperty(String.valueOf(id), NAME_COLUMN, newName);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public void updateSupplierPayingAgreement(int id, String payingAgreement) {
        try {
            updateProperty(String.valueOf(id), PAYMENT_COLUMN, payingAgreement);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }



    public void updateAgreementType(int id, int agreementType) {
        try {
            updateProperty(String.valueOf(id), AGREEMENTTYPE_COLUMN, agreementType);
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }


    public boolean isTransporting(int supplierId) {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet result = select(handler.get(),Arrays.asList(6), Arrays.asList(1), Arrays.asList(supplierId) );
            if(result.next()){
                int resultInt = result.getInt(1);
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean hasAgreement(int supID) {
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet result = select(handler.get(),Arrays.asList(6), Arrays.asList(1), Arrays.asList(supID) );
            if(result.next()){
                int resultInt = result.getInt(1);
                return resultInt != -1;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }


    public AgreementController getAgreementController() {
        return agreementController;
    }

    public AgreementItemDAO getAgreementItemDAO() {
        return agreementController.getAgreementItemDAO();
    }


    public int loadAllSuppliersInfo() throws SQLException {
        int largestId = loadAllSuppliers();
        loadAllAgreementsAndAgreementItems();
        return largestId;
    }

    private void loadAllAgreementsAndAgreementItems()  {
        try(ConnectionHandler handler = getConnectionHandler()) {
            for(String id : SUPPLIER_IDENTITY_MAP.keySet()){
                ResultSet resultSet = select(handler.get(), Arrays.asList(AGREEMENTTYPE_COLUMN), Arrays.asList(ID_COLUMN), Arrays.asList(Integer.parseInt(id)));
                if(resultSet.next()){
                    int agreementType = resultSet.getInt(1);
                    Agreement currAgreement = agreementController.loadAgreementAndItems(Integer.parseInt(id), agreementType);
                    SUPPLIER_IDENTITY_MAP.get(id).addAgreementFromDB(currAgreement);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private int loadAllSuppliers() {
        ArrayList<Integer> supplierIds = new ArrayList<>();  // get the largest one and set global id to be biggest+1
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get());
            while (instanceResult.next()) {
                //Load without contacts and manufacturers
                //Supplier currSupplier = new Supplier(instanceResult.getInt(1), instanceResult.getInt(2),
                //        instanceResult.getString(3), instanceResult.getString(4), instanceResult.getString(5));
                //SUPPLIER_IDENTITY_MAP.put(String.valueOf(currSupplier.getId()), currSupplier);

                //Load everything!
                int id = instanceResult.getInt(1);
                supplierIds.add(id);
                Supplier currSupplier = new Supplier(id, instanceResult.getInt(2),
                        instanceResult.getString(3), instanceResult.getString(4), instanceResult.getString(5)
                        , contactDAO.getAllSupplierContact(id), manufacturerDAO.getAllSupplierManufacturer(id), this);
                SUPPLIER_IDENTITY_MAP.put(String.valueOf(currSupplier.getId()), currSupplier);
            }
        } catch (Exception throwables) {
            System.out.println(throwables.getMessage());
        }

        Collections.sort(supplierIds, Collections.reverseOrder());
        if(supplierIds.isEmpty())
            return 0;
        return supplierIds.get(0);
    }

    /**
     * @return the globalID for the Supplier objects
     */
    public int findSupplierGlobalID(){
        int finalID = 0;  // get the largest one and set global id to be biggest+1
        int tempID;
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get());
            while (instanceResult.next()) {
                tempID = instanceResult.getInt(1);

                if(tempID > finalID){
                    finalID = tempID;
                }

            }
        }
        catch (Exception throwables) {
            System.out.println(throwables.getMessage());
        }

        return finalID + 1;
    }

    private void makeSureSupplierInMap(int id){
        String _id = ""+id;

        if (!SUPPLIER_IDENTITY_MAP.containsKey(_id)){
            try(ConnectionHandler handler = getConnectionHandler()) {
                ResultSet instanceResult = select(handler.get(), id);
                Supplier currSupplier = new Supplier(id, instanceResult.getInt(2),
                        instanceResult.getString(3), instanceResult.getString(4), instanceResult.getString(5)
                        , contactDAO.getAllSupplierContact(id), manufacturerDAO.getAllSupplierManufacturer(id), this);
                SUPPLIER_IDENTITY_MAP.put(String.valueOf(currSupplier.getId()), currSupplier);
            }
            catch (Exception throwables) {
                // in this case the table does not contain the supplier, nothing to do
            }
        }
    }

    public ArrayList<Integer> getAllSuppliersIds() {
        ArrayList<Integer> ids = new ArrayList<>();
        for(String id : SUPPLIER_IDENTITY_MAP.keySet()){
            ids.add(Integer.parseInt(id));
        }
        return ids;
    }

    public void removeTestSuppliers() {
        try (ConnectionHandler handler = getConnectionHandler()){
            ResultSet resultSet = executeQuery(handler.get(),String.format("Select * FROM %s WHERE %s LIKE \"%s\"",tableName, getColumnName(NAME_COLUMN), "Test%"));
            List<Integer> suppliers = new ArrayList<>();
            while (resultSet.next()) {
                suppliers.add(resultSet.getInt(ID_COLUMN));
            }
            for (Integer supplier : suppliers) {
                contactDAO.remove(supplier);
                manufacturerDAO.remove(supplier);
                agreementController.removeSupplier(supplier);
            }
            executeNonQuery(String.format("DELETE FROM %s WHERE %s LIKE \"%s\"",tableName, getColumnName(NAME_COLUMN), "Test%"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(Order order1) {

    }
}
