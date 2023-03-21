package Domain.Service.Objects.Employee;

import Globals.Enums.JobTitles;
import Presentation.CLIPresentation.Screens.ScreenEmployeeFactory;
import Presentation.WebPresentation.Screens.Models.HR.EmployeeFactory;

/**
 * Service model of the HR manager
 */
public class HR_Manager extends Employee{
    public HR_Manager(Domain.Business.Objects.Employee.HR_Manager bHR_Manager){
        super(bHR_Manager);
    }

    @Override
    public Presentation.CLIPresentation.Screens.Employee accept(ScreenEmployeeFactory screenEmployeeFactory) {
        return screenEmployeeFactory.createScreenEmployee(this);
    }

    @Override
    public JobTitles getType() {
        return JobTitles.HR_Manager;
    }

    @Override
    public Presentation.WebPresentation.Screens.Models.HR.Employee accept(EmployeeFactory employeeFactory) {
        return employeeFactory.createEmployee(this);
    }
}
