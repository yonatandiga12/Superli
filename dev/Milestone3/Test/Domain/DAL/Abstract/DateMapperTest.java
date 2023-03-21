package Domain.DAL.Abstract;

import Domain.Business.Objects.Shift.MorningShift;
import Domain.DAL.Controllers.ShiftDataMappers.MorningShiftDAO;
import Domain.DAL.Controllers.ShiftEmployeesLink.ShiftsStorekeepersLink;
import Globals.Enums.ShiftTypes;
import org.junit.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class DateMapperTest {

    static MorningShiftDAO dataMapper = new MorningShiftDAO();
    static LocalDate birthday = LocalDate.parse("1998-07-25");
    static ShiftTypes morning = ShiftTypes.Morning;
    static ShiftTypes evening = ShiftTypes.Evening;
    static String morningId = birthday.toString()+morning.toString();
    static String eveningId = birthday.toString()+evening.toString();
    static String id  = birthday.toString() + morningId;

    @BeforeClass
    public static void beforeAll() throws Exception {
        dataMapper.delete(id);
    }

    @AfterClass
    public static void afterAll() throws Exception {
        dataMapper.delete(id);
    }

    @Before
    public void before() throws Exception {
        dataMapper.insert(Arrays.asList(id,birthday,-1,10,10,10,10,10,10,10));
    }

    @After
    public void after() throws Exception {
        dataMapper.delete(id);
    }


    @Test
    public void get() throws Exception {
        try {
            assertNotNull(dataMapper.get(id));
            dataMapper.delete(eveningId);
            assertNull(dataMapper.get(eveningId));
        } catch (SQLException throwables) {
            fail();
        }
    }

    @Test
    public void updateProperty() throws Exception {
        try {
            dataMapper.updateProperty(id,3,"189");
            assertEquals(dataMapper.get(id).getShiftManagerId(),"189");
        } catch (SQLException throwables) {
            fail();
        }
    }

    @Test
    public void addToSet() {
        try {
            dataMapper.removeFromSet(id,"storekeepers","198");
            dataMapper.addToSet(id,"storekeepers","198");
            assertTrue(dataMapper.get(id).getStorekeeperIDs().contains("198"));
        } catch (SQLException throwables) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void removeFromSet() {
        try {
            if (!dataMapper.get(id).getStorekeeperIDs().contains("198"))
                dataMapper.addToSet(id,"storekeepers","198");
            dataMapper.removeFromSet(id,"storekeepers","198");
            assertFalse(dataMapper.get(id).getStorekeeperIDs().contains("198"));
        } catch (SQLException throwables) {
            fail();
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void replaceSet() {
        try {
            ShiftsStorekeepersLink shiftsStorekeepersLink = new ShiftsStorekeepersLink();
            Set<String> newSet = new HashSet<>();
            newSet.add("205");
            dataMapper.removeFromSet(id,"storekeepers","198");
            dataMapper.removeFromSet(id,"storekeepers","200");
            dataMapper.addToSet(id,"storekeepers","198");
            dataMapper.addToSet(id,"storekeepers","200");
            assertTrue(dataMapper.get(id).getStorekeeperIDs().size()>1);
            dataMapper.replaceSet(id,"storekeepers",newSet);
            assertEquals(shiftsStorekeepersLink.get(id).size(),1);
            assertTrue(shiftsStorekeepersLink.get(id).contains("205"));
        } catch (SQLException throwables) {
            fail();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void save() throws Exception {
        try {
            String newID = birthday.plusDays(1).toString()+morning.toString();;
            dataMapper.delete(newID);
            MorningShift shift = new MorningShift(birthday.plusDays(1),"1235",12,12,12,12,12,12,12);
            dataMapper.save(newID,shift);
            assertNotNull(dataMapper.get(newID));
        } catch (SQLException throwables) {
            fail();
        }
    }
}