package Domain.Service.Objects.Employee;

import Globals.Enums.JobTitles;
import Presentation.CLIPresentation.Screens.ScreenEmployeeFactory;
import Presentation.WebPresentation.Screens.Models.HR.EmployeeFactory;

/**
 * Service model of the Storekeeper
 */
public class Storekeeper extends Employee{
    public Storekeeper(Domain.Business.Objects.Employee.Storekeeper bStorekeeper){
        super(bStorekeeper);
    }

    @Override
    public Presentation.CLIPresentation.Screens.Employee accept(ScreenEmployeeFactory screenEmployeeFactory) {
        return screenEmployeeFactory.createScreenEmployee(this);
    }

    @Override
    public JobTitles getType() {
        return JobTitles.Storekeeper;
    }

    @Override
    public Presentation.WebPresentation.Screens.Models.HR.Employee accept(EmployeeFactory employeeFactory) {
        return employeeFactory.createEmployee(this);
    }
}
