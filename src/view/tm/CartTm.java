package view.tm;

import javafx.scene.control.Button;

public class CartTm {
    private String code;
    private String foodName;
    private double price;
    private int qty;
    private String date;
    private double totalCost;
    private Button btn;

    public CartTm() {
    }

    public CartTm(String code, String foodName, double price, int qty, String date, double totalCost, Button btn) {
        this.code = code;
        this.foodName = foodName;
        this.price = price;
        this.qty = qty;
        this.date = date;
        this.totalCost = totalCost;
        this.btn = btn;
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

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "CartTm{" +
                "code='" + code + '\'' +
                ", foodName='" + foodName + '\'' +
                ", price=" + price +
                ", qty=" + qty +
                ", date='" + date + '\'' +
                ", totalCost=" + totalCost +
                ", btn=" + btn +
                '}';
    }
}
