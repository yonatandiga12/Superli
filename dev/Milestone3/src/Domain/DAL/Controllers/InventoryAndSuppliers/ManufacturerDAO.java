package Domain.DAL.Controllers.InventoryAndSuppliers;

import Domain.DAL.Abstract.DAO;
import Domain.DAL.ConnectionHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ManufacturerDAO extends DAO {


    private static final ArrayList<String> manufacturers = new ArrayList<>();

    private final static int SUPPLIER_ID_COLUMN = 1;
    private final static int MANUFACTURER_COLUMN = 2;


    public ManufacturerDAO() {
        super("SupplierToManufacturer");
    }




    public void addManufacturer(int id, String manufacturer) {
        try {
            ArrayList<String> manu = getAllSupplierManufacturer(id);

            if(!manu.contains(manufacturer)){
                insert(Arrays.asList( String.valueOf(id) , manufacturer));
            }

            if(!manufacturers.contains(manufacturer)){
                manufacturers.add(manufacturer);
            }

        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }


    public void removeManufacturer(int supplierId, String name) {
        try {
            remove(Arrays.asList(1 , 2 ) ,Arrays.asList(String.valueOf(supplierId) , name) );
        } catch (SQLException throwables) {
            System.out.println(throwables.getMessage());
        }
    }

    public ArrayList<String> getAllSupplierManufacturer(int supID) {
        ArrayList<String> output = new ArrayList<>();
        try(ConnectionHandler handler = getConnectionHandler()) {
            ResultSet instanceResult = select(handler.get(), String.valueOf(supID));
            while (instanceResult.next()) {
                String manufacturer = instanceResult.getString(2);
                output.add(manufacturer);
                if(!manufacturers.contains(manufacturer)){
                    manufacturers.add(manufacturer);
                }
            }
        } catch (Exception throwables) {
            System.out.println(throwables.getMessage());
        }
        return output;
    }
}
