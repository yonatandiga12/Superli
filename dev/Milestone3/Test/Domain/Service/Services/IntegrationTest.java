package Domain.Service.Services;

import Domain.DAL.Abstract.DAO;
import Domain.Service.Services.HR.EmployeeService;
import Domain.Service.Services.HR.ShiftService;
import Domain.Service.Services.Transport.DocumentService;
import Domain.Service.Services.Transport.OrderService;
import Domain.Service.Services.Transport.TransportService;
import Domain.Service.Services.Transport.TruckService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.LocalDate;
import java.util.HashMap;



public class IntegrationTest {
    static LocalDate date=LocalDate.parse("2021-09-19");
    static EmployeeService employeeService = new EmployeeService();
    static ShiftService shiftService = new ShiftService();
    static DocumentService documentService = new DocumentService();
    static OrderService orderService = new OrderService();
    static TransportService transportService = new TransportService();
    static TruckService truckService =new TruckService();
    static InventoryService inventoryService = new InventoryService();
    static SupplierService supplierService = new SupplierService();
    static HashMap<Integer,Integer> productMap = new HashMap<>();

    @BeforeAll
    public static void createTables(){
        DAO.setDBForTests(IntegrationTest.class);
    }

    @AfterAll
    public static void removeData(){
        DAO.deleteTestDB(IntegrationTest.class);
    }

}
