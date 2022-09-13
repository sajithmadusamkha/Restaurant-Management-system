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
import model.BookTable;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class TableFormController {
    public JFXTextField txtTblId;
    public JFXTextField txtTblName;
    public JFXTextField txtTblStatus;
    public TableView<BookTable> tblView;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colStatus;
    public JFXButton btnAddTable;
    public JFXButton btnUpdateTable;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("tableName"));
        colStatus.setCellValueFactory(new PropertyValueFactory("status"));

        Pattern idPattern = Pattern.compile("^(T)[0-9]{3,5}$");
        Pattern TableNamePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern status = Pattern.compile("^[A-z]{3,25}$");

        map.put(txtTblId,idPattern);
        map.put(txtTblName,TableNamePattern);
        map.put(txtTblStatus,status);

        try {
            loadAllTable();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        btnAddTable.setDisable(true);
        btnUpdateTable.setDisable(true);

    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        BookTable t = new BookTable(txtTblId.getText(),txtTblName.getText(),txtTblStatus.getText());

        try {
            if(CrudUtil.execute("INSERT INTO bookTable VALUES (?,?,?)",t.getId(),t.getTableName(),t.getStatus())){
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
            loadAllTable();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        BookTable t = new BookTable(txtTblId.getText(),txtTblName.getText(),txtTblStatus.getText());
        try {
            boolean isUpdated = CrudUtil.execute("UPDATE bookTable SET talbeName = ?, status = ? WHERE id = ?", t.getTableName(),t.getStatus(),t.getId());
            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Booking Table");
                alert.setHeaderText("Table Detail Updated!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllTable();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM bookTable WHERE id = ?",txtTblId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Table");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllTable();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    private void search() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bookTable WHERE id = ?",txtTblId.getText());
        if(resultSet.next()){
            txtTblName.setText(resultSet.getString(2));
            txtTblStatus.setText(resultSet.getString(3));
        } else {
            new Alert(Alert.AlertType.ERROR,"Empty Result!").show();
        }
    }

    private void loadAllTable() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM bookTable");
        ObservableList<BookTable> obList = FXCollections.observableArrayList();
        while (resultSet.next()){
            obList.add(new BookTable(
                    resultSet.getString("id"),
                    resultSet.getString("talbeName"),
                    resultSet.getString("status")
            ));
        }
        tblView.setItems(obList);
    }

    public void clearAllText(){
        txtTblId.clear();
        txtTblName.clear();
        txtTblStatus.clear();
        txtTblId.requestFocus();
        setBorders(txtTblId,txtTblName,txtTblStatus);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddTable,btnUpdateTable);
    }
}
