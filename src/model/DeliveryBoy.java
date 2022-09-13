package model;

public class DeliveryBoy {
    String id;
    String name;
    String vehicleNO;

    public DeliveryBoy() {
    }

    public DeliveryBoy(String id, String name, String vehicleNO) {
        this.id = id;
        this.name = name;
        this.vehicleNO = vehicleNO;
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

    public String getVehicleNO() {
        return vehicleNO;
    }

    public void setVehicleNO(String vehicleNO) {
        this.vehicleNO = vehicleNO;
    }

    @Override
    public String toString() {
        return "DeliveryBoy{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", vehicleNO='" + vehicleNO + '\'' +
                '}';
    }
}
