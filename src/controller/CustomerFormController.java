package controller;

import DBConnection.DBConnection;
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
import javafx.scene.input.MouseEvent;
import model.Customer;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomerFormController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtTeleNo;
    public TableView<Customer> customerTblView;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colTeleNo;
    public JFXButton btnAddCustomer;
    public JFXButton btnUpdateCustomer;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();


    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colTeleNo.setCellValueFactory(new PropertyValueFactory("teleNo"));

        try {
            loadAllCustomer();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(C)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,50}$");
        Pattern teleNoPattern = Pattern.compile("^0(1|7|6|8)[0-9]{8}$");

        map.put(txtId,idPattern);
        map.put(txtName,namePattern);
        map.put(txtAddress,addressPattern);
        map.put(txtTeleNo,teleNoPattern);

        btnAddCustomer.setDisable(true);
        btnUpdateCustomer.setDisable(true);
    }

    public void addCustomerOnAction(ActionEvent actionEvent) {
        Customer c = new Customer(
                txtId.getText(),txtName.getText(),txtAddress.getText(),txtTeleNo.getText()
        );

        try {
            if(CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?)",c.getId(),c.getName(),c.getAddress(),c.getTeleNo())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Customer");
                alert.setHeaderText("Customer Saved!");
                alert.show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        try {
            loadAllCustomer();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void updateOnAction(ActionEvent actionEvent) {
        Customer c = new Customer(
                txtId.getText(),txtName.getText(),txtAddress.getText(),txtTeleNo.getText()
        );
        try {
            boolean isUpdated = CrudUtil.execute("UPDATE Customer SET name = ?, adrress = ?, teleNo = ? WHERE id = ?",c.getName(),c.getAddress(),c.getTeleNo(),c.getId());
            if (isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Customer");
                alert.setHeaderText("Customer Detail Updated!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllCustomer();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void deleteOnAction(MouseEvent mouseEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM Customer WHERE id = ?",txtId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Customer");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllCustomer();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();

    }

    private void loadAllCustomer() throws ClassNotFoundException, SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer");

        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while(resultSet.next()){
            obList.add(new Customer(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("adrress"),
                    resultSet.getString("teleNo")
            ));
        }

        customerTblView.setItems(obList);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void search() throws ClassNotFoundException , SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer WHERE id = ?",txtId.getText());
        if (resultSet.next()){
            txtName.setText(resultSet.getString(2));
            txtAddress.setText(resultSet.getString(3));
            txtTeleNo.setText(resultSet.getString(4));
        } else {
            new Alert(Alert.AlertType.ERROR,"Empty Result!").show();
        }
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddCustomer,btnUpdateCustomer);
    }

    public void clearAllText(){
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtTeleNo.clear();
        txtId.requestFocus();
        setBorders(txtId,txtName,txtAddress,txtTeleNo);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }

}
