package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import model.DeliveryBoy;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class DeliveryBoyFormController {
    public JFXTextField txtBoyName;
    public JFXTextField txtVehicleNo;
    public JFXTextField txtBoyId;
    public TableView<DeliveryBoy> tblDeliveryBoy;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colVehicleNo;
    public JFXButton btnAddBoy;
    public JFXButton btnEditBoy;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colVehicleNo.setCellValueFactory(new PropertyValueFactory("vehicleNO"));

        try {
            loadAllBoys();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(D)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern vehicleNoPattern = Pattern.compile("^[A-Z-]{2,4}[0-9]{4}$");

        map.put(txtBoyId,idPattern);
        map.put(txtBoyName,namePattern);
        map.put(txtVehicleNo,vehicleNoPattern);

        btnAddBoy.setDisable(true);
        btnEditBoy.setDisable(true);
    }

    public void saveBoyOnAction(ActionEvent actionEvent) {
        DeliveryBoy deliveryBoy = new DeliveryBoy(
                txtBoyId.getText(),txtBoyName.getText(),txtVehicleNo.getText()
        );

        try {
            if(CrudUtil.execute("INSERT INTO DeliveryBoy VALUES (?,?,?)",deliveryBoy.getId(),deliveryBoy.getName(),deliveryBoy.getVehicleNO())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Save Deliver Boy");
                alert.setHeaderText("Deliver Boy Saved!");
                alert.show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        try {
            loadAllBoys();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void updateOnAction(ActionEvent actionEvent) {
        DeliveryBoy d = new DeliveryBoy(
                txtBoyId.getText(),txtBoyName.getText(),txtVehicleNo.getText()
        );

        try {
            boolean isUpdated = CrudUtil.execute("UPDATE DeliveryBoy SET name = ?, vehicleNo = ? WHERE id = ?",d.getName(),d.getVehicleNO(),d.getId());
            if(isUpdated){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Update Deliver Boy");
                alert.setHeaderText("Deliver Boy Updated!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllBoys();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        clearAllText();
    }

    public void deleteOnAction(ActionEvent actionEvent) {
        try {
            if(CrudUtil.execute("DELETE FROM DeliveryBoy WHERE id = ?",txtBoyId.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Deliver Boy");
                alert.setHeaderText("Deleted Successfully!");
                alert.show();
            } else {
                new Alert(Alert.AlertType.WARNING,"Try Again!").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllBoys();
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

    private void loadAllBoys() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM DeliveryBoy");
        ObservableList<DeliveryBoy> observableList = FXCollections.observableArrayList();
        while (resultSet.next()){
            observableList.add(new DeliveryBoy(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("vehicleNo")
            ));
        }
        tblDeliveryBoy.setItems(observableList);
    }

    private void search() throws ClassNotFoundException , SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM DeliveryBoy WHERE id = ?",txtBoyId.getText());
        if (resultSet.next()){
            txtBoyName.setText(resultSet.getString(2));
            txtVehicleNo.setText(resultSet.getString(3));
        } else {
            new Alert(Alert.AlertType.ERROR,"Empty Result!").show();
        }
    }

    public void clearAllText(){
        txtBoyId.clear();
        txtBoyName.clear();
        txtVehicleNo.clear();
        txtBoyId.requestFocus();
        setBorders(txtBoyId,txtBoyName,txtVehicleNo);
    }

    public void setBorders(TextField... textFields){
        for (TextField textField : textFields) {
            textField.setStyle("-fx-border-color: rgba(0, 0, 0, 0)");
        }
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddBoy,btnEditBoy);
    }
}
