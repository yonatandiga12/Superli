package Domain.Service.Objects.Employee;

import Globals.Enums.JobTitles;
import Presentation.CLIPresentation.Screens.ScreenEmployeeFactory;
import Presentation.WebPresentation.Screens.Models.HR.EmployeeFactory;

public class Transport_Manager extends Employee{

    /**
     * Service Employee constructor based on his business type counterpart
     *
     * @param bEmployee Business type representing the employee
     */
    public Transport_Manager(Domain.Business.Objects.Employee.Transport_Manager bEmployee) {
        super(bEmployee);
    }

    @Override
    public Presentation.CLIPresentation.Screens.Employee accept(ScreenEmployeeFactory screenEmployeeFactory) {
        return screenEmployeeFactory.createScreenEmployee(this);
    }

    @Override
    public JobTitles getType() {
        return JobTitles.Transport_Manager;
    }

    @Override
    public Presentation.WebPresentation.Screens.Models.HR.Employee accept(EmployeeFactory employeeFactory) {
        return employeeFactory.createEmployee(this);
    }
}
