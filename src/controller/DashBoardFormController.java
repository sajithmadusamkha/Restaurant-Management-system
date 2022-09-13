package controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import util.Loader;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashBoardFormController implements Loader, Initializable {

    public AnchorPane context;
    public AnchorPane dashBoard;
    public Label lblDate;
    public Label lblTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadHomeForm();
        loadDateAndTime();

    }

    private void loadDateAndTime() {
        lblDate.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime localTime = LocalTime.now();
            lblTime.setText(localTime.getHour()+":"+localTime.getMinute()+":"+localTime.getSecond());
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadHomeForm() {
        try {
            Parent  fxml = FXMLLoader.load(getClass().getResource("../view/HomeForm.fxml"));
            context.getChildren().removeAll();
            context.getChildren().setAll(fxml);
        } catch (IOException e) {
            Logger.getLogger(DashBoardFormController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void homeFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("HomeForm");
    }

    public void itemFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("ItemForm");
    }

    public void orderFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("OrderForm");
    }

    public void tableFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("TableForm");
    }

    public void deliveryOrderFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("DeliveryOrderForm");
    }

    public void customerDetailOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("CustomerForm");
    }

    public void deliveryBoyOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("DeliveryBoyForm");
    }

    public void employeesFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("EmployeesForm");
    }

    public void DineInOrderFormOnAction(ActionEvent actionEvent) throws IOException {
        loadUi("DineInOrderForm");
    }

    public void logoutOnAction(MouseEvent mouseEvent) throws IOException {
        setUi("LoginForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) dashBoard.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }

    @Override
    public void loadUi(String location) throws IOException {
        context.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        context.getChildren().add(parent);
    }
}
