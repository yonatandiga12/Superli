package Domain.Business.Controllers;

import Domain.Business.Controllers.HR.EmployeeController;
import Domain.Business.Controllers.Transport.SiteController;
import Domain.Business.Controllers.Transport.TransportController;
import Domain.Business.Controllers.Transport.TruckController;
import Domain.Business.Objects.Employee.*;
import Domain.Business.Objects.Inventory.*;
import Domain.Business.Objects.Shift.EveningShift;
import Domain.Business.Objects.Shift.MorningShift;
import Domain.Business.Objects.Shift.Shift;
import Domain.Business.Objects.Supplier.AgreementItem;
import Domain.Business.Objects.Supplier.Order;
import Domain.DAL.Controllers.EmployeeMappers.EmployeeDataMapper;
import Domain.DAL.Controllers.InventoryAndSuppliers.*;
import Globals.Defect;
import Globals.Enums.*;
import Globals.Pair;
import junit.framework.TestCase;
import org.junit.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static Domain.Business.Controllers.ShiftManagementTest.shiftDataMapper;

public class EmployeeControllerTest extends TestCase {

    EmployeeController employeeController = new EmployeeController();
    @Test
    public void testEditEmployeeName() {
        try {
            SiteController s = new SiteController();
            //loadHR();
            //addInventoryTestData();
            //insertFirstDataToDB(); // the db in the desktop is updated until (not including) this line. aka employees and Inventory
            //transportData();
            employeeController.editEmployeeName("160","updated");
            assertEquals(employeeController.getEmployee("160").getName(),"updated");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    public void loadHR() throws Exception {
        EmployeeDataMapper employeeDataMapper = new EmployeeDataMapper();
        LocalDate firstOfJun = LocalDate.parse("2022-06-01");
        LocalDate endOfJun = LocalDate.parse("2022-06-30");
        Set<Certifications> notEmptyCertification = new HashSet<>();
        notEmptyCertification.add(Certifications.ShiftManagement);
        Set<Certifications> emptyCertification = new HashSet<>();
        Set<LicenseTypes> licenseTypes = new HashSet<>();
        licenseTypes.add(LicenseTypes.B);
        licenseTypes.add(LicenseTypes.CE);

        long numOfDays = ChronoUnit.DAYS.between(firstOfJun, endOfJun.plusDays(1));
        java.util.List<LocalDate> listOfDates = Stream.iterate(firstOfJun, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());

        for (LocalDate date : listOfDates){
            try {
                shiftDataMapper.delete(date, ShiftTypes.Morning);
                shiftDataMapper.delete(date,ShiftTypes.Evening);
                shiftDataMapper.save(new MorningShift(date,10,10,10,10,10,10,10));
                shiftDataMapper.save(new EveningShift(date,10,10,10,10,0,0,0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Set<String> carriers = new HashSet<>();
        Set<String> cashiers = new HashSet<>();
        Set<String> hrManagers = new HashSet<>();
        Set<String> logisticManagers = new HashSet<>();
        Set<String> sorters = new HashSet<>();
        Set<String> storekeepers = new HashSet<>();
        Set<String> transportManagers = new HashSet<>();
        LocalDate date = LocalDate.now();
        LocalDate movingDate = firstOfJun;
        for (int i =0; i<10; i++){
            employeeDataMapper.delete(Integer.toString(i+100));
            employeeDataMapper.delete(Integer.toString(i+100+10));
            employeeDataMapper.delete(Integer.toString(i+100+20));
            employeeDataMapper.delete(Integer.toString(i+100+30));
            employeeDataMapper.delete(Integer.toString(i+100+40));
            employeeDataMapper.delete(Integer.toString(i+100+50));
            employeeDataMapper.delete(Integer.toString(i+100+60));
            employeeDataMapper.save(new Carrier(Integer.toString(i+100),"carrier " + (i+1),"bank",i+10,date,emptyCertification,licenseTypes));
            carriers.add(Integer.toString(i+100));
            employeeDataMapper.save(new Cashier(Integer.toString(i+100+10),"cashier " + (i+1),"bank",i+10,date,notEmptyCertification));
            cashiers.add(Integer.toString(i+100+10));
            employeeDataMapper.save(new HR_Manager(Integer.toString(i+100+20),"hr manager " + (i+1),"bank",i+10,date,emptyCertification));
            hrManagers.add(Integer.toString(i+100+20));
            employeeDataMapper.save(new Logistics_Manager(Integer.toString(i+100+30),"logistic manager " + (i+1),"bank",i+10,date,emptyCertification));
            logisticManagers.add(Integer.toString(i+100+30));
            employeeDataMapper.save(new Sorter(Integer.toString(i+100+40),"sorter " + (i+1),"bank",i+10,date,notEmptyCertification));
            sorters.add(Integer.toString(i+100+40));
            employeeDataMapper.save(new Storekeeper(Integer.toString(i+100+50),"storekeeper " + (i+1),"bank",i+10,date,notEmptyCertification));
            storekeepers.add(Integer.toString(i+100+50));
            employeeDataMapper.save(new Transport_Manager(Integer.toString(i+100+60),"transport manager " + (i+1),"bank",i+10,date,notEmptyCertification));
            transportManagers.add(Integer.toString(i+100+60));
        }
        for (int i =0; i<10; i++, movingDate = movingDate.plusDays(1)) {
            Shift morningShift =shiftDataMapper.get(movingDate,ShiftTypes.Morning);
            Shift eveningShift =shiftDataMapper.get(movingDate,ShiftTypes.Evening);
            morningShift.registerAsAvailable(Integer.toString(i+100));
            eveningShift.registerAsAvailable(Integer.toString(i+100));
            morningShift.registerAsAvailable(Integer.toString(i+110));
            eveningShift.registerAsAvailable(Integer.toString(i+110));
            morningShift.setCarrierIDs(carriers);
            eveningShift.setCarrierIDs(carriers);
            morningShift.setShiftManagerId(Integer.toString(i+110));
            eveningShift.setShiftManagerId(Integer.toString(i+110));

            morningShift.registerAsAvailable(Integer.toString(i+120));
            morningShift.setHr_managerIDs(hrManagers);
            morningShift.registerAsAvailable(Integer.toString(i+130));
            morningShift.setLogistics_managerIDs(logisticManagers);

            eveningShift.registerAsAvailable(Integer.toString(i+140));
            eveningShift.setSorterIDs(sorters);
            eveningShift.registerAsAvailable(Integer.toString(i+150));
            eveningShift.setStorekeeperIDs(storekeepers);

            morningShift.registerAsAvailable(Integer.toString(i+160));
            morningShift.setTransport_managerIDs(transportManagers);

            shiftDataMapper.save(morningShift);
            shiftDataMapper.save(eveningShift);
        }
        loadShiftForJuly();
        loadStoreKeepersWithDrivers();
    }

    private void loadStoreKeepersWithDrivers() throws Exception {
        LocalDate firstOfJul = LocalDate.parse("2022-07-01");
        LocalDate firstOfJun =LocalDate.parse("2022-06-01");


        LocalDate movingDateJul = firstOfJul;
        LocalDate movingDateJun = firstOfJun;
        Set<String> storekeepers = new HashSet<>();
        for (int i = 150; i < 160; i++) {
            storekeepers.add(Integer.toString(i));
        }

        for (int i =150; i<160; i++, movingDateJul = movingDateJul.plusDays(1),movingDateJun = movingDateJun.plusDays(1)) {
            Shift morningShiftJul =shiftDataMapper.get(movingDateJul,ShiftTypes.Morning);
            Shift morningShiftJun =shiftDataMapper.get(movingDateJun,ShiftTypes.Morning);
            Shift eveningShiftJul =shiftDataMapper.get(movingDateJul,ShiftTypes.Evening);
            Shift eveningShiftJun =shiftDataMapper.get(movingDateJun,ShiftTypes.Evening);
            morningShiftJul.registerAsAvailable(Integer.toString(i));
            morningShiftJun.registerAsAvailable(Integer.toString(i));
            eveningShiftJul.registerAsAvailable(Integer.toString(i));
            eveningShiftJun.registerAsAvailable(Integer.toString(i));

            morningShiftJul.setStorekeeperIDs(storekeepers);
            morningShiftJun.setStorekeeperIDs(storekeepers);
            eveningShiftJul.setStorekeeperIDs(storekeepers);
            eveningShiftJun.setStorekeeperIDs(storekeepers);
        }
        movingDateJul = firstOfJul;
        movingDateJun = firstOfJun;

        for (int i =150; i<160; i++, movingDateJul = movingDateJul.plusDays(1),movingDateJun = movingDateJun.plusDays(1)) {
            Shift morningShiftJul =shiftDataMapper.get(movingDateJul,ShiftTypes.Morning);
            Shift morningShiftJun =shiftDataMapper.get(movingDateJun,ShiftTypes.Morning);
            Shift eveningShiftJul =shiftDataMapper.get(movingDateJul,ShiftTypes.Evening);
            Shift eveningShiftJun =shiftDataMapper.get(movingDateJun,ShiftTypes.Evening);
            shiftDataMapper.save(morningShiftJul);
            shiftDataMapper.save(morningShiftJun);
            shiftDataMapper.save(eveningShiftJul);
            shiftDataMapper.save(eveningShiftJun);
        }

    }

    public void loadShiftForJuly() throws Exception {
        EmployeeDataMapper employeeDataMapper = new EmployeeDataMapper();
        LocalDate firstOfJul = LocalDate.parse("2022-07-01");
        LocalDate endOfJul = LocalDate.parse("2022-07-31");
        Set<Certifications> notEmptyCertification = new HashSet<>();
        notEmptyCertification.add(Certifications.ShiftManagement);
        Set<Certifications> emptyCertification = new HashSet<>();
        Set<LicenseTypes> licenseTypes = new HashSet<>();
        licenseTypes.add(LicenseTypes.B);
        licenseTypes.add(LicenseTypes.CE);

        long numOfDays = ChronoUnit.DAYS.between(firstOfJul, endOfJul.plusDays(1));
        java.util.List<LocalDate> listOfDates = Stream.iterate(firstOfJul, date -> date.plusDays(1))
                .limit(numOfDays)
                .collect(Collectors.toList());

        for (LocalDate date : listOfDates){
            try {
                shiftDataMapper.delete(date, ShiftTypes.Morning);
                shiftDataMapper.delete(date,ShiftTypes.Evening);
                shiftDataMapper.save(new MorningShift(date,10,10,10,10,10,10,10));
                shiftDataMapper.save(new EveningShift(date,10,10,10,10,0,0,0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        LocalDate movingDate = firstOfJul;
        Set<String> carriers = new HashSet<>();
        for (int i = 100; i < 110; i++) {
            carriers.add(Integer.toString(i));
        }

        for (int i =0; i<10; i++, movingDate = movingDate.plusDays(1)) {
            Shift morningShift =shiftDataMapper.get(movingDate,ShiftTypes.Morning);
            Shift eveningShift =shiftDataMapper.get(movingDate,ShiftTypes.Evening);
            morningShift.registerAsAvailable(Integer.toString(i+100));
            eveningShift.registerAsAvailable(Integer.toString(i+100));
            morningShift.registerAsAvailable(Integer.toString(i+110));
            eveningShift.registerAsAvailable(Integer.toString(i+110));
            morningShift.setCarrierIDs(carriers);
            eveningShift.setCarrierIDs(carriers);
            morningShift.setShiftManagerId(Integer.toString(i+110));
            eveningShift.setShiftManagerId(Integer.toString(i+110));

            shiftDataMapper.save(morningShift);
            shiftDataMapper.save(eveningShift);
        }

    }
    private void addInventoryTestData() {
        addStoresTestDate();
        addCategoryAndProductTestDate();
        addSalesAndSalesToCategoryAndSalesToProductsTestDate();
        addLocationAndLocationToShelfTestDate();
        addStockReportTestDate();
        addDefectiveItemsTestDate();
    }
    private void addStoresTestDate() {
        StoreDAO storeDAO = new StoreDAO();

        storeDAO.addStore(1);
        storeDAO.addStore(2);
        storeDAO.addStore(3);
        storeDAO.addStore(4);
        storeDAO.addStore(5);
    }
    private void addCategoryAndProductTestDate() {
        CategoryDataMapper categoryDataMapper = new CategoryDataMapper();
        ProductDataMapper productDataMapper = new ProductDataMapper();

        Category c1 = new Category(1, "Snacks", new HashSet<>(), new ArrayList<>(), null);
        Category c2 = new Category(2, "Dairy", new HashSet<>(), new ArrayList<>(), null);
        Category c3 = new Category(3, "Milk", new HashSet<>(), new ArrayList<>(), c2);
        Category c4 = new Category(4, "Milk 1L", new HashSet<>(), new ArrayList<>(), c3);
        Category c5 = new Category(5, "Milk 2L", new HashSet<>(), new ArrayList<>(), c3);
        Category c6 = new Category(6, "Sweets", new HashSet<>(), new ArrayList<>(), c1);
        Category c7 = new Category(7, "Salty snacks", new HashSet<>(), new ArrayList<>(), c1);
        Category c8 = new Category(8, "Delicacies", new HashSet<>(), new ArrayList<>(), c2);
        Category c9 = new Category(9, "Yoplait", new HashSet<>(), new ArrayList<>(), c8);
        Category c10 = new Category(10, "Milky", new HashSet<>(), new ArrayList<>(), c8);

        categoryDataMapper.insert(c1);
        categoryDataMapper.insert(c2);
        categoryDataMapper.insert(c3);
        categoryDataMapper.insert(c4);
        categoryDataMapper.insert(c5);
        categoryDataMapper.insert(c6);
        categoryDataMapper.insert(c7);
        categoryDataMapper.insert(c8);
        categoryDataMapper.insert(c9);
        categoryDataMapper.insert(c10);

        productDataMapper.insert(new Product(1, "Bamba small", c7, 0.2, 3.5, "Osem"));
        productDataMapper.insert(new Product(2, "Milk 1L Tnuva", c4, 1, 4, "Tnuva"));
        productDataMapper.insert(new Product(3, "Milk 1L Tara", c4, 1, 4, "Tara"));
        productDataMapper.insert(new Product(4, "Yoplait Pineapple", c9, 0.15, 5.9, "Yoplait"));
        productDataMapper.insert(new Product(5, "Halva", c6, 0.3, 7, "Elit"));
    }
    private void addSalesAndSalesToCategoryAndSalesToProductsTestDate() {
        SalesDataMapper salesDataMapper = new SalesDataMapper();

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate beforeTwoDays = LocalDate.now().minusDays(2);
        LocalDate afterTwoDays = LocalDate.now().plusDays(2);

        salesDataMapper.insert(new SaleToCustomer(1, yesterday, tomorrow, 20, new ArrayList<>(Arrays.asList(1)), new ArrayList<>()));
        salesDataMapper.insert(new SaleToCustomer(2, beforeTwoDays, yesterday, 10, new ArrayList<>(Arrays.asList(2)), new ArrayList<>(Arrays.asList(1))));
        salesDataMapper.insert(new SaleToCustomer(3, tomorrow, afterTwoDays, 30, new ArrayList<>(), new ArrayList<>(Arrays.asList(4))));
    }
    private void addLocationAndLocationToShelfTestDate() {
        LocationDataMapper locationDataMapper = new LocationDataMapper();

        locationDataMapper.insert(new Location(1, 1, false, new ArrayList<>(Arrays.asList(13,14))), 1);
        locationDataMapper.insert(new Location(2, 1, true, new ArrayList<>(Arrays.asList(14))), 1);
        locationDataMapper.insert(new Location(3, 1, false, new ArrayList<>(Arrays.asList(85))), 2);
        locationDataMapper.insert(new Location(4, 1, true, new ArrayList<>(Arrays.asList(10))), 2);
        locationDataMapper.insert(new Location(5, 3, false, new ArrayList<>(Arrays.asList(90))), 3);
        locationDataMapper.insert(new Location(6, 3, true, new ArrayList<>(Arrays.asList(14))), 3);

    }
    private void addStockReportTestDate() {
        StockReportDataMapper stockReportDataMapper = new StockReportDataMapper();

        stockReportDataMapper.insert(new StockReport(1, 2, 55, 41, 80, 300, 0));
        stockReportDataMapper.insert(new StockReport(3, 3, 20, 30, 65, 250, 0));
        stockReportDataMapper.insert(new StockReport(1, 1, 10, 15, 40, 150, 110));
    }
    private void addDefectiveItemsTestDate() {
        DefectiveItemsDataMapper defectiveItemsDataMapper = new DefectiveItemsDataMapper();
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate beforeTwoDays = LocalDate.now().minusDays(2);

        defectiveItemsDataMapper.insert(new DefectiveItems(1, Defect.Damaged, beforeTwoDays, 1, 1, 5, 160, "5 small Bambas were found open", false));
        defectiveItemsDataMapper.insert(new DefectiveItems(2, Defect.Expired, yesterday, 1, 2, 2, 110, "2 Milk 1L tnuva were expired", true));
        defectiveItemsDataMapper.insert(new DefectiveItems(3, Defect.Damaged, today, 1, 2, 13, 144, "", true));
        defectiveItemsDataMapper.insert(new DefectiveItems(4, Defect.Expired, today, 1, 2, 15, 144, "15 Milk 1L tnuva were expired", false));
    }



    public void insertFirstDataToDB() throws Exception {

        insertSupplier1();
        insertSupplier2();

    }


    private void insertSupplier1() throws Exception {
        SupplierController supplierController = new SupplierController();
        int storeId = 1;
        ArrayList<Pair<String, String >> contacts1 = new ArrayList<>();
        contacts1.add(new Pair<>("Yael", "0508647894"));             contacts1.add(new Pair<>("Avi", "086475421"));
        ArrayList<String> manufacturers1 = new ArrayList<>();  manufacturers1.add("Tnuva") ;       manufacturers1.add("Osem") ; manufacturers1.add("Elit");  manufacturers1.add("Struass");   manufacturers1.add("Yoplait");
        int supplierId1 = supplierController.addSupplier("Avi", 123456, "Hertzel", "check", contacts1,manufacturers1);

        supplierController.addAgreement(supplierId1, 1, "1 2 3 4 5 6 7");

        ArrayList<String> items = new ArrayList<>();
        items.add("1 , 1,  Osem , 3.5 , 100 , 20 ");
        items.add("2 , 2, Tnuva, 4.0 , 100 , 20 , 200 , 50 , 500 , 70");
        items.add("3 , 3,  Tara, 4.0 , 100 , 20 , 200 , 40 , 500 , 50");
        items.add("4 , 4, Yoplait, 5.9 ,  10 , 20 , 20 , 80 ");
        items.add("5 , 5, Elit, 7.0 ,  10 , 20 , 20 , 50 , 30 , 80 ");


        supplierController.addItemsToAgreement(supplierId1, items);

        Order order1 = new Order(1, supplierId1, LocalDate.of(2022, 5, 25),  LocalDate.of(2022, 6, 1), storeId , OrderStatus.waiting);
        int order1Id = order1.getId();
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.insert(order1);
        //supplierController.suppliersDAO.add(order1);
        //supplierController.insertToOrderDAO(order1);
        supplierController.suppliersDAO.getAgreementController().setLastOrderId(supplierId1, order1Id);

        //Bamba  80 * 0.2 = 16kg
        int id = 1;
        AgreementItem curr = supplierController.suppliersDAO.getSupplier(supplierId1).getItem(id);
        int quantity = 80;
        supplierController.addItemToOrderDAO(order1Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        // Milk 100 * 1 = 100kg
        id = 2;
        curr = supplierController.suppliersDAO.getSupplier(supplierId1).getItem(id);
        quantity = 100;
        supplierController.addItemToOrderDAO(order1Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        //Order weight = 116 kg
    }


    private void insertSupplier2() throws Exception {
        SupplierController supplierController = new SupplierController();
        int storeId = 1;

        ArrayList<Pair<String, String >> contacts2 = new ArrayList<>();
        contacts2.add(new Pair<>("Beni", "0508647894"));             contacts2.add(new Pair<>("Kvodi", "086475421"));
        ArrayList<String> manufacturers2 = new ArrayList<>();        manufacturers2.add("Tnuva") ; manufacturers2.add("Elit");  manufacturers2.add("Struass");
        int supplierId2 = supplierController.addSupplier("Beni", 111111, "Yosef Tkoa", "check", contacts2, manufacturers2);

        supplierController.addAgreement(supplierId2, 2, "3");

        ArrayList<String> items = new ArrayList<>();
        items.add("1 , 1, Osem , 3.5 , 100 , 30 ");
        items.add("2 , 2, Tnuva, 4.0, 100 , 30 , 200 , 60 , 500 , 80");
        items.add("3 , 3, Tara, 4.0 ,  100 , 30 , 200 , 50 , 500 , 60");
        items.add("4 , 4, Yoplait, 5.9 , 10 , 30 , 20 , 90 ");
        items.add("5 , 5, Elit, 7.0 ,  10 , 30 , 20 , 60 , 30 , 90 ");

        supplierController.addItemsToAgreement(supplierId2, items);


        Order order2 = new Order(2, supplierId2, LocalDate.of(2022, 5, 29),  LocalDate.of(2022, 6, 1), storeId, OrderStatus.waiting);
        int order2Id = order2.getId();
        new OrderDAO().insert(order2);
        //supplierController.insertToOrderDAO(order2);

        //Yoplait 20 * 0.15 = 3kg
        int id = 4;
        AgreementItem curr = supplierController.suppliersDAO.getSupplier(supplierId2).getItem(id);
        int quantity = 20;
        supplierController.addItemToOrderDAO(order2Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        //Halva 20 * 0.3 = 6kg
        id = 5;
        curr = supplierController.suppliersDAO.getSupplier(supplierId2).getItem(id);
        quantity = 20;
        supplierController.addItemToOrderDAO(order2Id, id, curr.getName(), quantity, curr.getPricePerUnit(), curr.getDiscount(quantity), curr.calculateTotalPrice(quantity) , curr.getWeight());

        //Order 2 weight = 9 kg

    }

    private void transportData(){
        try{

            TransportController tCon = new TransportController();
            TruckController truckController = new TruckController();
            truckController.addTruck(123, TruckModel.FullTrailer,1000,1500);
            truckController.addTruck(111, TruckModel.FullTrailer,500,1500);
            truckController.addTruck(000, TruckModel.SemiTrailer,500,900);
            truckController.addTruck(987, TruckModel.DoubleTrailer,700,1200);
            tCon.createTransport(new Pair<>(LocalDate.of(2022, 6, 1),ShiftTypes.Morning));
            tCon.createTransport(new Pair<>(LocalDate.of(2022, 6, 2),ShiftTypes.Morning));
            tCon.createTransport(new Pair<>(LocalDate.of(2022, 6, 3),ShiftTypes.Morning));
            tCon.addOrderToTransport(0,1);
            tCon.addOrderToTransport(0,2);
            tCon.placeTruck(0,123);
            tCon.placeDriver(0,"101");
            tCon.startTransport(0);
            tCon.endTransport(0);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

}