package Presentation.CLIPresentation.Screens;

public class ScreenEmployeeFactory {
    private Screen caller;
    public Employee createScreenEmployee(Screen caller, Domain.Service.Objects.Employee.Employee sEmployee){
        this.caller = caller;
        return sEmployee.accept(this);
    }

    public Carrier createScreenEmployee(Domain.Service.Objects.Employee.Carrier sEmployee){
        return new Carrier(caller, sEmployee);
    }

    public Cashier createScreenEmployee(Domain.Service.Objects.Employee.Cashier sEmployee){
        return new Cashier(caller, sEmployee);
    }

    public HR_Manager createScreenEmployee(Domain.Service.Objects.Employee.HR_Manager sEmployee){
        return new HR_Manager(caller, sEmployee);
    }

    public Logistics_Manager createScreenEmployee(Domain.Service.Objects.Employee.Logistics_Manager sEmployee){
        return new Logistics_Manager(caller, sEmployee);
    }

    public Sorter createScreenEmployee(Domain.Service.Objects.Employee.Sorter sEmployee){
        return new Sorter(caller, sEmployee);
    }

    public Storekeeper createScreenEmployee(Domain.Service.Objects.Employee.Storekeeper sEmployee){
        return new Storekeeper(caller, sEmployee);
    }

    public Transport_Manager createScreenEmployee(Domain.Service.Objects.Employee.Transport_Manager sEmployee){
        return new Transport_Manager(caller, sEmployee);
    }
}
