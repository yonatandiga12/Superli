package Presentation.WebPresentation.Screens.Models.HR;

public class EmployeeFactory {
    public Employee createEmployee(Domain.Service.Objects.Employee.Employee sEmployee){
        return sEmployee.accept(this);
    }

    public Carrier createEmployee(Domain.Service.Objects.Employee.Carrier sEmployee){
        return new Carrier(sEmployee);
    }

    public Cashier createEmployee(Domain.Service.Objects.Employee.Cashier sEmployee){
        return new Cashier(sEmployee);
    }

    public HR_Manager createEmployee(Domain.Service.Objects.Employee.HR_Manager sEmployee){
        return new HR_Manager(sEmployee);
    }

    public Logistics_Manager createEmployee(Domain.Service.Objects.Employee.Logistics_Manager sEmployee){
        return new Logistics_Manager(sEmployee);
    }

    public Sorter createEmployee(Domain.Service.Objects.Employee.Sorter sEmployee){
        return new Sorter(sEmployee);
    }

    public Storekeeper createEmployee(Domain.Service.Objects.Employee.Storekeeper sEmployee){
        return new Storekeeper(sEmployee);
    }

    public Transport_Manager createEmployee(Domain.Service.Objects.Employee.Transport_Manager sEmployee){
        return new Transport_Manager(sEmployee);
    }
}
