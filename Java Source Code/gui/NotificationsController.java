package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DataAccessLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class NotificationsController implements Initializable {
    @javafx.fxml.FXML
    private Label header;
    @javafx.fxml.FXML
    private ListView<String> notificationsLst;
    ObservableList<String> notes = FXCollections.observableArrayList();
    DataAccessLayer database;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        header.setText( "Notifications");
        getNotifications();
        // Make the list view of nots
        notificationsLst.setItems(notes);
    }

    private void getNotifications() {
        try {
            ResultSet results = database.getNotifications();
            while (results.next()) {
                String value = results.getString(1);
                notes.add(value);
            }
        } catch (SQLException ex) {
            showAlert("An error happened trying to connect to the database !");
        }
    }

    public void showAlert(String message) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(message);
        a.setHeaderText("An Error Happened!");
        a.setTitle("Error!!");
        a.show();
    }
}
