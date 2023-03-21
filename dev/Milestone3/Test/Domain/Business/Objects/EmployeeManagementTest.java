package Domain.Business.Objects;

import Domain.Business.Objects.Employee.Employee;
import Domain.Business.Objects.Employee.Storekeeper;
import Globals.Enums.Certifications;
import Globals.Enums.JobTitles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeManagementTest {

    LocalDate date = LocalDate.parse("2020-07-15");
    Set<Certifications> certifications= new HashSet<>();
    @org.junit.Test
    public void EditName() {
        Employee employee = null;
        try {
            employee = new Storekeeper("9","psy","asd",2,date,certifications);
            String preName =employee.getName();
            employee.setName("Roi");
            assertNotEquals(preName,employee.getName());
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @org.junit.Test
    public void EditEmployeeEmploymentConditions() {
        Employee employee = null;
        try {
            employee = new Storekeeper("9","psy","asd",2,date,certifications);
            String preName =employee.getEmploymentConditions();
            employee.setName("Roi");
            employee.updateEmploymentConditions(JobTitles.Logistics_Manager);
            assertNotEquals(preName,employee.getEmploymentConditions());
        } catch (Exception e) {
            assertTrue(true);
        }
    }
}