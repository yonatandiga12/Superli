package Domain.Business.Objects.Site;

public class Source extends Site {
    public Source(Address address, String contactId, String phoneNumber) {
        super(address, contactId, phoneNumber);
    }

    public Source(int id, Address address, String contactId, String phoneNumber) {
        super(id, address, contactId, phoneNumber);
    }
}
