package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.Loader;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController implements Loader {

    public AnchorPane loginContext;
    public Label loginMessageLbl;
    public TextField txtUserName;
    public PasswordField txtPwd;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        if(txtUserName.getText().isEmpty() == false && txtPwd.getText().isEmpty() == false){
            try {
                validateLogin();
            }catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            loginMessageLbl.setText("Invalid User name or password!");
        }
    }

    private void validateLogin() throws SQLException, ClassNotFoundException, IOException {
           ResultSet resultSet = CrudUtil.execute("SELECT count(1) FROM Login WHERE userName = ? AND password = ?",txtUserName.getText(),txtPwd.getText());

        while (resultSet.next()){
            if(resultSet.getInt(1) == 1){
                Image img = new Image("./assets/check.png");
                setUi("DashBoardForm");
                Notifications notifications = Notifications.create().title("Login Successful!").text("Welcome to Management System");
                notifications.darkStyle();
                notifications.hideAfter(Duration.seconds(5));
                notifications.position(Pos.TOP_RIGHT);
                notifications.graphic(new ImageView(img));
                notifications.show();
            } else {
                loginMessageLbl.setText("Invalid User name or password!");
            }
        }
    }

    public void signupOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        loadUi("SignupForm");
    }

    private void setUi(String location) throws IOException {
        Stage stage = (Stage) loginContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
    }

    @Override
    public void loadUi(String location) throws IOException {
        loginContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"));
        loginContext.getChildren().add(parent);
    }
}
