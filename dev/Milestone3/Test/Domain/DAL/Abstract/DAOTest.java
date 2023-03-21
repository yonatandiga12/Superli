package Domain.DAL.Abstract;

import Domain.DAL.ConnectionHandler;
import Domain.DAL.Controllers.ShiftDataMappers.MorningShiftDAO;
import Globals.Enums.ShiftTypes;
import org.junit.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DAOTest {

    static MorningShiftDAO morningShiftDataMapper = new MorningShiftDAO();
    static LocalDate localDate = LocalDate.parse("1998-07-25");
    static ShiftTypes morning =ShiftTypes.Morning;
    static String id = localDate.toString()+morning.toString();
    @BeforeClass
    public static void beforeAll() throws Exception {
        morningShiftDataMapper.delete(id);
    }

    @AfterClass
    public static void afterAll() throws Exception {
        morningShiftDataMapper.delete(id);
    }

    @Before
    public void before() throws Exception {
        morningShiftDataMapper.insert(Arrays.asList(id,localDate,-1,10,10,10,10,10,10,10));
    }

    @After
    public void after() throws Exception {
        morningShiftDataMapper.delete(id);
    }


    @Test
    public void testSelect1() {
        try (ConnectionHandler connection = morningShiftDataMapper.getConnectionHandler()){
            insert();
            ResultSet resultSet = morningShiftDataMapper.select(connection.get(), Arrays.asList(1),Arrays.asList(id));
            while (resultSet.next()) {
                assertEquals(localDate, resultSet.getDate(2).toLocalDate());
            }
        } catch (SQLException throwables) {
            fail();
        }
    }

    @Test
    public void testSelect2() {
        try (ConnectionHandler connection = morningShiftDataMapper.getConnectionHandler()){
            ResultSet resultSet = morningShiftDataMapper.select(connection.get(),Arrays.asList(2,3),Arrays.asList(1),Arrays.asList(id));
            while (resultSet.next()){
                assertEquals(localDate,resultSet.getDate(1).toLocalDate());
                assertEquals(-1,resultSet.getInt(2));
                assertThrows(SQLException.class,()->resultSet.getString(3));
            }
        } catch (SQLException throwables) {
            fail();
        }
    }

    @Test
    public void insert() {
        try {
            morningShiftDataMapper.remove(id);
            assertEquals(1, morningShiftDataMapper.insert(Arrays.asList(id,localDate,-1,10,10,10,10,10,10,10)));
        } catch (SQLException throwables) {
            fail();
        }
    }

    @Test
    public void testRemove() throws SQLException {
        try{
            morningShiftDataMapper.insert(Arrays.asList(id,localDate,-1,10,10,10,10,10,10,10));
        } catch (SQLException throwables) {

        }finally {
            assertEquals(1, morningShiftDataMapper.remove(id));
        }
    }

    @Test
    public void update() {
        try (ConnectionHandler connection = morningShiftDataMapper.getConnectionHandler()){
            assertEquals(1, morningShiftDataMapper.update(Arrays.asList(4),Arrays.asList(9),Arrays.asList(1),Arrays.asList(id)));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            fail();
        }
    }
}