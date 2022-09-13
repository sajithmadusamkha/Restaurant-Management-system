package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import model.DeliveryOrder;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class DeliveryOrderFormController {

    public TableView tblDeliveryOrder;
    public TableColumn colDeliveryId;
    public TableColumn colDeliverBoyId;
    public TableColumn colLocation;
    public JFXTextField txtDOId;
    public JFXTextField txtDBoyId;
    public JFXTextField txtLocation;
    public JFXButton btnSaveOrder;
    public JFXButton btnEditOrder;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        colDeliveryId.setCellValueFactory(new PropertyValueFactory("id"));
        colDeliverBoyId.setCellValueFactory(new PropertyValueFactory("deliveryBoyId"));
        colLocation.setCellValueFactory(new PropertyValueFactory("location"));

        try {
            loadAllDeliveryOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(L)[0-9]{3,5}$");
        Pattern boyId = Pattern.compile("^(D)[0-9]{3,5}$");
        Pattern location = Pattern.compile("^[A-z0-9 ,/]{4,50}$");

        map.put(txtDOId,idPattern);
        map.put(txtDBoyId,boyId);
        map.put(txtLocation,location);

        btnSaveOrder.setDisable(true);
        btnEditOrder.setDisable(true);
    }

    public void saveOrderOnAction(ActionEvent actionEvent) {
        DeliveryOrder d = new DeliveryOrder(
                txtDOId.getText(),txtDBoyId.getText(),txtLocation.getText()
        );

        try {
            if(CrudUtil.execute("INSERT INTO DeliveryOrder VALUES (?,?,?)",d.getId(),d.getDeliveryBoyId(),d.getLocation())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Delivery Order");
                alert.setHeaderText("Delivery Order Saved!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        try {
            loadAllDeliveryOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void updateOrderOnAction(ActionEvent actionEvent) {
        DeliveryOrder d = new DeliveryOrder(
                txtDOId.getText(),txtDBoyId.getText(),txtLocation.getText()
        );

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE DeliveryOrder SET deliveryBoyId = ?, location = ? WHERE id = ?",d.getDeliveryBoyId(),d.getLocation(),d.getId());
            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Delivery Order");
                alert.setHeaderText("Delivery Order Detail Updated!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllDeliveryOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void deleteOrderOnAction(ActionEvent actionEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM DeliveryOrder WHERE id = ?",txtDOId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Deliver Order");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllDeliveryOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllDeliveryOrders() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM DeliveryOrder");
        ObservableList<DeliveryOrder> obList = FXCollections.observableArrayList();
        while (resultSet.next()){
            obList.add(new DeliveryOrder(
                    resultSet.getString("id"),
                    resultSet.getString("deliveryBoyId"),
                    resultSet.getString("location")
            ));
        }
        tblDeliveryOrder.setItems(obList);
    }

    private void search() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM DeliveryOrder WHERE id = ?",txtDOId.getText());
        if(resultSet.next()){
            txtDBoyId.setText(resultSet.getString(2));
            txtLocation.setText(resultSet.getString(3));
        } else {
            new Alert(Alert.AlertType.ERROR,"Empty Result!").show();
        }
    }

    public void clearAllText(){
        txtDOId.clear();
        txtDBoyId.clear();
        txtLocation.clear();
        txtDOId.requestFocus();
        setBorders(txtDOId,txtDBoyId,txtLocation);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnSaveOrder,btnEditOrder);
    }
}
