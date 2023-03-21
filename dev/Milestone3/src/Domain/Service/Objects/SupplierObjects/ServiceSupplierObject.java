package Domain.Service.Objects.SupplierObjects;

import java.util.ArrayList;

public class ServiceSupplierObject {

    private int id;
    private int bankNumber;
    private String address;
    private String name;
    private String payingAgreement;
    private ArrayList<ServiceContactObject> contacts;




    public ServiceSupplierObject(int id, String name, int bankNumber, String address ,String payingAgreement, ArrayList<ServiceContactObject> contacts){
        this.id = id;
        this.name = name;
        this.bankNumber = bankNumber;
        this.address = address;
        this.payingAgreement = payingAgreement;
        this.contacts = new ArrayList<>();
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getBankNumber() {
        return bankNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPayingAgreement() {
        return payingAgreement;
    }

    public ArrayList<ServiceContactObject> getContacts() {
        return contacts;
    }

    public String toString(){
        return "ID: " + id + "\nName: " + name + "\nAddress: " + address + "\nBank number: " + bankNumber + "\nPaying agreement: " + payingAgreement;
    }

    public String toString(String down){
        return "ID: " + id + down +  "Name: " + name + down + "Address: " + address + down + "Bank number: " + bankNumber + down +  "Paying agreement: " + payingAgreement;
    }



}
