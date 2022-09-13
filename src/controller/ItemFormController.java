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
import javafx.scene.input.MouseEvent;
import model.Item;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ItemFormController {

    public JFXTextField txtCode;
    public JFXTextField txtName;
    public JFXTextField txtPrice;
    public JFXTextField txtQty;
    public TableView<Item> foodTblView;
    public TableColumn colCode;
    public TableColumn colFoodName;
    public TableColumn colPrice;
    public TableColumn colQty;
    public JFXButton btnUpdate;
    public JFXButton btnSave;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory("code"));
        colFoodName.setCellValueFactory(new PropertyValueFactory("foodName"));
        colPrice.setCellValueFactory(new PropertyValueFactory("price"));
        colQty.setCellValueFactory(new PropertyValueFactory("qtyOnHand"));

        try {
            loadAllItem();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern codePattern = Pattern.compile("^(I)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern pricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");
        Pattern qtyPattern = Pattern.compile( "^[0-9]{1,}$");

        map.put(txtCode,codePattern);
        map.put(txtName,namePattern);
        map.put(txtPrice,pricePattern);
        map.put(txtQty,qtyPattern);

        btnSave.setDisable(true);
        btnUpdate.setDisable(true);
    }

    public void updateOnAction(ActionEvent actionEvent) {
        Item i = new Item(
                txtCode.getText(),txtName.getText(),Double.parseDouble(txtPrice.getText()),Integer.parseInt(txtQty.getText())
        );

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE FOOD SET foodName = ?, price = ?, qtyOnHand = ? WHERE code = ?",i.getFoodName(),i.getPrice(),i.getQtyOnHand(),i.getCode());
            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Food");
                alert.setHeaderText("Food Detail Updated!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllItem();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void saveOnAction(ActionEvent actionEvent) {
        Item item = new Item(
                txtCode.getText(),txtName.getText(),Double.parseDouble(txtPrice.getText()),Integer.parseInt(txtQty.getText())
        );

        try {
            if(CrudUtil.execute("INSERT INTO Food VALUES (?,?,?,?)",item.getCode(),item.getFoodName(),item.getPrice(),item.getQtyOnHand())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Food");
                alert.setHeaderText("Food Saved!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        try {
            loadAllItem();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void deleteOnAction(MouseEvent mouseEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM Food WHERE code = ?",txtCode.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Food Item");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllItem();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Food WHERE code = ?", txtCode.getText());
        if(resultSet.next()){
            txtName.setText(resultSet.getString(2));
            txtPrice.setText(String.valueOf(resultSet.getDouble(3)));
            txtQty.setText(String.valueOf(resultSet.getInt(4)));
        } else {
            new Alert(Alert.AlertType.ERROR,"Empty Result!").show();
        }
    }

    private void loadAllItem() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Food");
        ObservableList<Item> obList = FXCollections.observableArrayList();
        while (resultSet.next()){
            obList.add(new Item(
                    resultSet.getString("code"),
                    resultSet.getString("foodName"),
                    resultSet.getDouble("price"),
                    resultSet.getInt("qtyOnHand")
            ));
        }
        foodTblView.setItems(obList);
    }

    public void clearAllText(){
        txtCode.clear();
        txtName.clear();
        txtPrice.clear();
        txtQty.clear();
        txtCode.requestFocus();
        setBorders(txtCode,txtName,txtPrice,txtQty);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnSave,btnUpdate);
    }
}
