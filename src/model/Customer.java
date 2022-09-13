package model;

public class Customer {
    String id;
    String name;
    String address;
    String teleNo;

    public Customer() {
    }

    public Customer(String id, String name, String address, String teleNo) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.teleNo = teleNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTeleNo() {
        return teleNo;
    }

    public void setTeleNo(String teleNo) {
        this.teleNo = teleNo;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", teleNo='" + teleNo + '\'' +
                '}';
    }
}
