package Domain.Service.Objects.SupplierObjects;

public class ServiceContactObject {

    private String name;
    private String phone;

    public ServiceContactObject(String contactName, String phoneNumber) {
        this.name = contactName;
        this.phone = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }
}
