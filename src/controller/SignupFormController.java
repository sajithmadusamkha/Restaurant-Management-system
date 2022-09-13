package controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Login;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.Loader;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SignupFormController implements Loader {

    public AnchorPane signupContext;
    public TextField txtUserName;
    public PasswordField txtPassword;
    public TextField txtUserId;
    public JFXButton btnSignup;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        try {
            setUserId();
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        btnSignup.setDisable(true);
    }

    public void signupOnAction(ActionEvent actionEvent) throws IOException {
        Image img = new Image("./assets/check.png");
        Login l = new Login(txtUserId.getText(),txtUserName.getText(),txtPassword.getText());
        try {
            if(CrudUtil.execute("INSERT INTO Login VALUES (?,?,?)",l.getId(),l.getName(),l.getPassword())){
                Notifications notifications = Notifications.create().title("SignUp Successful!").text("Welcome To Hops & Chops");
                notifications.darkStyle();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.TOP_RIGHT);
                notifications.graphic(new ImageView(img));
                notifications.show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Try Again").show();
            }
            loadUi("LoginForm");
        }catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void backOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        loadUi("LoginForm");
    }

    @Override
    public void loadUi(String location) throws IOException {
        signupContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        signupContext.getChildren().add(parent);
    }

    public void txtFields_Key_released(KeyEvent keyEvent) {
        validate();

        if(keyEvent.getCode() == KeyCode.ENTER){
            Object response = validate();
            if(response instanceof TextField){
                TextField textField = (TextField) response;
                textField.requestFocus();
            } else if(response instanceof Boolean) {

            }
        }
    }

    public Object validate(){
        Pattern idPattern = Pattern.compile("^(U)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern passwordPattern = Pattern.compile("^[A-z0-9 ,/*#$%^&!]{4,50}$");

        if(!idPattern.matcher(txtUserId.getText()).matches()){
            addError(txtUserId);
            return txtUserId;
        } else{
            removeError(txtUserId);
            if(!namePattern.matcher(txtUserName.getText()).matches()){
                addError(txtUserName);
                return txtUserName;
            } else {
                removeError(txtUserName);
                if(!passwordPattern.matcher(txtPassword.getText()).matches()){
                    addError(txtPassword);
                    return txtPassword;
                } else{
                    removeError(txtPassword);
                }
            }
        }
        return true;
    }

    private void addError(TextField txtField) {
        if (txtField.getText().length() > 0) {
            txtField.setStyle("-fx-border-color: red");
        }
        btnSignup.setDisable(true);
    }

    private void removeError(TextField txtField) {
        txtField.setStyle("-fx-border-color: green");
        btnSignup.setDisable(false);
    }

    private void setUserId() throws SQLException, ClassNotFoundException {
        ResultSet resultSet = CrudUtil.execute("SELECT MAX(id) FROM Login");
        resultSet.next();
        resultSet.getString("MAX(id)");
        if(resultSet.getString("MAX(id)") == null){
            txtUserId.setText("U001");
        } else {
            Long id = Long.parseLong(resultSet.getString("MAX(id)").substring(2,resultSet.getString("MAX(id)").length()));
            id++;
            txtUserId.setText("U"+String.format("%03d",id));
        }
    }
}

