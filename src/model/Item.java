package model;

public class Item {
    String code;
    String foodName;
    double price;
    int qtyOnHand;

    public Item() {
    }

    public Item(String code, String foodName, double price, int qtyOnHand) {
        this.code = code;
        this.foodName = foodName;
        this.price = price;
        this.qtyOnHand = qtyOnHand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "Item{" +
                "code='" + code + '\'' +
                ", foodName='" + foodName + '\'' +
                ", price=" + price +
                ", qtyOnHand=" + qtyOnHand +
                '}';
    }
}
