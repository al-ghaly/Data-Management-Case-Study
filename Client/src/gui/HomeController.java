package gui;

import java.net.URL;
import java.util.ResourceBundle;
import client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class HomeController implements Initializable {

    @FXML
    private Button studentButton;
    @FXML
    private Button coursesButton;
    @FXML
    private Button instructorsButton;
    @FXML
    private Button enrollButton;
    @FXML
    private Button gradingButton;
    @FXML
    private Button logOutButton;
    @FXML
    private Button aboutButton;
    @FXML
    private Label usernameTxt;

    Client client;
    @FXML
    private Button notButton;

    public void setData(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       usernameTxt.setText(client.getUsername());

       aboutButton.setOnAction(e -> {
          popUpAbout();
       });

        logOutButton.setOnAction(e -> {
            try {
                switchToLogIn(e);
            } catch (Exception ex) {
                showAlert("An Error Happened");
            }
        });

        studentButton.setOnAction(e -> {
            try {
                switchToStudents();
            } catch (Exception ex) {
                showAlert("An Error Happened");
            }
        });

        notButton.setOnAction(e -> {
            try {
                switchToNotifications();
            } catch (Exception ex) {
                showAlert("An Error Happened");
            }
        });

        enrollButton.setOnAction(e -> {
            try {
                switchToEnroll();
            } catch (Exception ex) {
                showAlert("An Error Happened");
            }
        });

        gradingButton.setOnAction(e -> {
            try {
                switchToGrading();
            } catch (Exception ex) {
                showAlert("An Error Happened");
            }
        });
    }

    public void popUpAbout(){
        Stage popupStage = new Stage();
        About root = new About();
        Scene aboutScene = new Scene(root);
        popupStage.setScene(aboutScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }

    public void switchToLogIn(ActionEvent event) throws Exception{
        Parent logIn = FXMLLoader.load(getClass().getResource("../gui/LogIn.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(logIn);
        stage.setScene(scene);
        stage.show();
    }

    public void showAlert(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(message);
        a.setHeaderText("An Error Happened!");
        a.setTitle("Error!!");
        a.show();
    }

    public void switchToStudents() throws Exception{
        Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource("../gui/Students.fxml"));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }

    public void switchToNotifications() throws Exception{
        Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource("../gui/Notifications.fxml"));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }

    public void switchToEnroll() throws Exception{
        Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource("../gui/Enroll.fxml"));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }
    public void switchToGrading() throws Exception{
        Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource("../gui/Grading.fxml"));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }
}
