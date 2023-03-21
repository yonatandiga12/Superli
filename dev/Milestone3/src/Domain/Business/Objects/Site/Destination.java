package Domain.Business.Objects.Site;

public class Destination extends Source {
    public Destination(Address address, String contactId, String phoneNumber) {
        super(address, contactId, phoneNumber);
    }

    public Destination(int id, Address address, String contactId, String phoneNumber) {
        super(id, address, contactId, phoneNumber);
    }
}
