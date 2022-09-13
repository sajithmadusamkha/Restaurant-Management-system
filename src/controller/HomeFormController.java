package controller;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HomeFormController {

    public Label lblDiniInOrders;
    public Label lblDeliveryOrders;
    public Label lblDeliveryBoys;
    public BarChart<String,Double> barChart;

    public void initialize(){
        setDineInOrders();
        setDeliveryOrders();
        setDeliveryBoys();
        setBarGraph();
    }

    private void setDineInOrders(){
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM Orders");
            resultSet.next();
            String sql = resultSet.getString("COUNT(id)");
            lblDiniInOrders.setText(sql);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDeliveryOrders(){
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM DeliveryOrder");
            resultSet.next();
            String count = resultSet.getString("COUNT(id)");
            lblDeliveryOrders.setText(count);

        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDeliveryBoys(){
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT COUNT(id) FROM DeliveryBoy");
            resultSet.next();
            String count = resultSet.getString("COUNT(id)");
            lblDeliveryBoys.setText(count);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setBarGraph(){
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT name, salary FROM Employee");
            XYChart.Series<String,Double> series = new XYChart.Series<>();
            while (resultSet.next()){
                series.getData().add(new XYChart.Data<>(resultSet.getString("name"), resultSet.getDouble("salary")));
            }
            barChart.getData().add(series);
            series.setName("Employee Table");
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
