package Domain.Business.Objects.Site;

public abstract class Site {
    private static int incID = 0;
    private int id;
    private Address address;
    private String contactId;
    private String phoneNumber;

    public static void rest(){incID = 0;}
    public Site(Address address, String contactId, String phoneNumber) {
        id = incID++;
        this.address = address;
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
    }
    public Site(int id,Address address, String contactId, String phoneNumber) {
        this.id = id;
        incID++;
        this.address = address;
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }
    public String getContactId(){return contactId;}
    public String getPhoneNumber(){return phoneNumber;}

    public void setAddress(Address address) {
        this.address = address;
    }
}
