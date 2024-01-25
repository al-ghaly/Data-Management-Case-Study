package gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import database.DataAccessLayer;


public class LogInController implements Initializable {

    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    DataAccessLayer database;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        loginButton.setOnAction(e -> {
            // Get username and password
            String username = usernameTxt.getText();
            String password = passwordTxt.getText();

            // Perform login asynchronously in a separate thread
            if (username.isEmpty() || password.isEmpty()) {
                showAlert("Enter all required data!");
            } else {
                handleLogin(username, password, e);
            }
        });

        signUpButton.setOnAction(e -> {
                try {
                    switchToSignUp(e);
                } catch (Exception ex) {
                    showAlert("An Error Happened");
                }
        });

    }

    private void handleLogin(String username, String password, ActionEvent e) {
        try {
            String userPassword = database.getPassword(username);
            if (!password.equals(userPassword)) {
                showAlert("Wrong Password!!");
            } else {
                try {
                    Client client = new Client();
                    client.setUsername(username);
                    switchToHome(e, client);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showAlert("An Error Happened");
                }
            }
        } catch (SQLException ex) {
            if(ex.getErrorCode() == 17289) {
                showAlert("Username does not exist !");
            }
            else {
                showAlert("An Error happened trying to connect to the DB !");
            }
        }
    }

    public void showAlert(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(message);
        a.setHeaderText("An Error Happened!");
        a.setTitle("Error!!");
        a.show();
    }

    public void switchToHome(ActionEvent event, Client client) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/Home.fxml"));

        // Create an instance of your controller and set the data
        HomeController homeController = new HomeController();
        homeController.setData(client);

        loader.setControllerFactory(clazz -> {
            if (clazz == HomeController.class) {
                return homeController;
            } else {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Parent home = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(home);
        stage.setScene(scene);
        stage.show();

    }

    public void switchToSignUp(ActionEvent event) throws Exception {
        Parent signUp = FXMLLoader.load(getClass().getResource("../gui/SignUp.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(signUp);
        stage.setScene(scene);
        stage.show();
    }
}
