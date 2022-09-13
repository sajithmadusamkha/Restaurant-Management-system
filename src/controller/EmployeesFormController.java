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
import model.Employee;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class EmployeesFormController {
    public JFXTextField txtEmpId;
    public JFXTextField txtEmpName;
    public JFXTextField txtEmpAddress;
    public JFXTextField txtEmpSalary;
    public TableView<Employee> empTblView;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public JFXButton btnUpdateEmployee;
    public JFXButton btnAddEmployee;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory("salary"));

        try {
            loadAllEmployee();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(E)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,50}$");
        Pattern salaryPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");

        map.put(txtEmpId,idPattern);
        map.put(txtEmpName,namePattern);
        map.put(txtEmpAddress,addressPattern);
        map.put(txtEmpSalary,salaryPattern);

        btnAddEmployee.setDisable(true);
        btnUpdateEmployee.setDisable(true);
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Employee emp = new Employee(
                txtEmpId.getText(),txtEmpName.getText(),txtEmpAddress.getText(),Double.parseDouble(txtEmpSalary.getText())
        );

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE Employee SET name = ?, adrress = ?, salary = ? WHERE id = ?",emp.getName(),emp.getAddress(),emp.getSalary(),emp.getId());
            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Employee");
                alert.setHeaderText("Employee Detail Updated!");
                alert.show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllEmployee();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        Employee emp = new Employee(
                txtEmpId.getText(),txtEmpName.getText(),txtEmpAddress.getText(),Double.parseDouble(txtEmpSalary.getText())
        );

        try {
            if(CrudUtil.execute("INSERT INTO Employee VALUES (?,?,?,?)",emp.getId(),emp.getName(),emp.getAddress(),emp.getSalary())){
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
            loadAllEmployee();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM Employee WHERE id = ?",txtEmpId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Employee");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllEmployee();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        search();
    }

    private void search() {
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM Employee WHERE id =?", txtEmpId.getText());
            if(resultSet.next()){
                txtEmpName.setText(resultSet.getString(2));
                txtEmpAddress.setText(resultSet.getString(3));
                txtEmpSalary.setText(String.valueOf(resultSet.getDouble(4)));
            }
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadAllEmployee() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Employee");
        ObservableList<Employee> obList = FXCollections.observableArrayList();
        while(resultSet.next()){
            obList.add(new Employee(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("adrress"),
                    resultSet.getDouble("salary")
            ));
        }
        empTblView.setItems(obList);
    }

    public void clearAllText(){
        txtEmpId.clear();
        txtEmpName.clear();
        txtEmpAddress.clear();
        txtEmpSalary.clear();
        txtEmpId.requestFocus();
        setBorders(txtEmpId,txtEmpName,txtEmpAddress,txtEmpSalary);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddEmployee,btnUpdateEmployee);
    }
}
