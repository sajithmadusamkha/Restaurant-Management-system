package model;

public class DeliveryOrder {
    String id;
    String deliveryBoyId;
    String location;

    public DeliveryOrder() {
    }

    public DeliveryOrder(String id, String deliveryBoyId, String location) {
        this.id = id;
        this.deliveryBoyId = deliveryBoyId;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(String deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DeliveryOrder{" +
                "id='" + id + '\'' +
                ", deliveryBoyId='" + deliveryBoyId + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
