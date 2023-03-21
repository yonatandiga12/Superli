package Domain.Business.Objects.Supplier;

public class Contact {

    private String name;
    private String phone;

    public Contact(String contactName, String phoneNumber) {
        this.name = contactName.trim();
        this.phone = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String toString(){
        return "Name: " + name + ", " + "Phone-Number: " + phone;
    }
}
