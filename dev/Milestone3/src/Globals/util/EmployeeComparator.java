package Globals.util;

import Domain.Service.Objects.Employee.Employee;

import java.util.Comparator;

public class EmployeeComparator implements Comparator<Employee> {
    private JobTitleComparator jobTitleComparator = new JobTitleComparator();
    @Override
    public int compare(Employee o1, Employee o2) {
        int byTitle = jobTitleComparator.compare(o1.getType(), o2.getType());
        if (byTitle != 0)
            return byTitle;
        int byID = Integer.parseInt(o1.id) - Integer.parseInt(o2.id);
        if (byID != 0)
            return byID;
        return o1.id.length() - o2.id.length();
    }
}
