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
import model.DineInOrder;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class DineInOrderFormController {
    public JFXTextField txtOrderId;
    public JFXTextField txtTableId;
    public JFXTextField txtEmpId;
    public JFXTextField txtCusId;
    public TableView<DineInOrder> dineInTblView;
    public TableColumn colId;
    public TableColumn colCusId;
    public TableColumn colTblId;
    public TableColumn colEmpId;
    public JFXButton btnPlaceOrder;
    public JFXButton btnUpdateOrder;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colCusId.setCellValueFactory(new PropertyValueFactory("cusId"));
        colEmpId.setCellValueFactory(new PropertyValueFactory("empId"));
        colTblId.setCellValueFactory(new PropertyValueFactory("tableId"));

        try {
            loadAllOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern orderIdPattern = Pattern.compile("^(D)[0-9]{3,5}$");
        Pattern cusIdPattern = Pattern.compile("^(C)[0-9]{3,5}$");
        Pattern empIdPattern = Pattern.compile("^(E)[0-9]{3,5}$");
        Pattern tblIdPattern = Pattern.compile("^(T)[0-9]{3,5}$");

        map.put(txtOrderId,orderIdPattern);
        map.put(txtCusId,cusIdPattern);
        map.put(txtEmpId,empIdPattern);
        map.put(txtTableId,tblIdPattern);

        btnPlaceOrder.setDisable(true);
        btnUpdateOrder.setDisable(true);
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        DineInOrder d = new DineInOrder(
                txtOrderId.getText(),txtCusId.getText(),txtEmpId.getText(),txtTableId.getText()
        );

        try {
            if(CrudUtil.execute("INSERT INTO Orders VALUES (?,?,?,?)",d.getId(),d.getCusId(),d.getEmpId(),d.getTableId())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Place Order");
                alert.setHeaderText("Order Saved!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        try {
            loadAllOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void updateOnAction(ActionEvent actionEvent) {
        DineInOrder d = new DineInOrder(
                txtOrderId.getText(),txtCusId.getText(),txtEmpId.getText(),txtTableId.getText()
        );

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE Orders SET customerId = ?, employeeId = ?, tableId = ?  WHERE id = ?",d.getCusId(),d.getEmpId(),d.getTableId(),d.getId());
            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Dine-In-Orders");
                alert.setHeaderText("Order Detail Updated!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllOrders();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM Orders WHERE id = ?",txtOrderId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Dine-In-Order");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllOrders();
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

    private void search() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Orders WHERE id = ?", txtOrderId.getText());
        if(resultSet.next()){
            txtCusId.setText(resultSet.getString(2));
            txtEmpId.setText(resultSet.getString(3));
            txtTableId.setText(resultSet.getString(4));
        } else {
            new Alert(Alert.AlertType.ERROR,"Empty Result!").show();
        }
    }

    private void loadAllOrders() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Orders");
        ObservableList<DineInOrder> obList = FXCollections.observableArrayList();
        while (resultSet.next()){
            obList.add(new DineInOrder(
                    resultSet.getString("id"),
                    resultSet.getString("customerId"),
                    resultSet.getString("employeeId"),
                    resultSet.getString("tableId")
            ));
        }
        dineInTblView.setItems(obList);
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnPlaceOrder,btnUpdateOrder);
    }

    public void clearAllText(){
        txtOrderId.clear();
        txtCusId.clear();
        txtEmpId.clear();
        txtTableId.clear();
        txtOrderId.requestFocus();
        setBorders(txtOrderId,txtCusId,txtEmpId,txtTableId);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }
}
