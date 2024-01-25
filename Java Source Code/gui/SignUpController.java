package gui;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.Client;
import client.ClientSide;
import database.DataAccessLayer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import org.json.simple.JSONObject;

public class SignUpController implements Initializable {

    @FXML
    private TextField emailTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField conPasswordTxt;
    @FXML
    private Button signUpButton;
    @FXML
    private TextField phoneTxt;

    DataAccessLayer database;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        signUpButton.setOnAction(e -> {
            // Get username and password
            String username = usernameTxt.getText().trim();
            String password = passwordTxt.getText().trim();
            String conPass = conPasswordTxt.getText().trim();
            String email = emailTxt.getText().trim();
            String phone = phoneTxt.getText().trim();

            Client client = new Client();
            client.setEmail(email);
            client.setPassword(password);
            client.setUsername(username);
            client.setPhone(phone);

            switch(validate(client, conPass)){
                case "Good":
                    handleSignUp(client, e);
                    break;
                case "Empty Field":
                    showAlert("Enter all required Fields");
                    break;
                case "Invalid Password":
                    showAlert("Passwords Don't match");
                    break;
                case "Invalid Email":
                    showAlert("Enter a valid Email Address");
                    break;
                case "Invalid Balance":
                    showAlert("Enter a valid balance number");
                    break;
            }
        });
    }

    public static String validate(Client client, String conPassword){
        // Define the email pattern using regex
        String emailRegex = "([a-zA-Z0-9_.%+-]{3,}@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(client.getEmail());

        if(client.getUsername().isEmpty() ||
                client.getPassword().isEmpty() ||
                client.getEmail().isEmpty() ||
                client.getPhone().isEmpty())
            return "Empty Field";
        else if(!matcher.matches())
            return "Invalid Email";
        else if (!client.getPassword().equals(conPassword))
            return "Invalid Password";
        else
            return "Good";
    }

    private void handleSignUp(Client client, ActionEvent e) {
        try {
            database.addUser(client);
            try {
                switchToHome(e, client);
            } catch (Exception ex) {
                showAlert("An Error Happened");
            }
        } catch (SQLException ex) {
            if(ex.getErrorCode() == 1)
               showAlert("Username is taken\nChoose another one!");
            else
                showAlert("An Error happened!!\nTry Later");
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
}
