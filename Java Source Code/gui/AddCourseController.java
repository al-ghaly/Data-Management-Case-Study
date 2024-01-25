package gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.DataAccessLayer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddCourseController implements Initializable {

    @FXML
    private TextField name;
    @FXML
    private TextField duration;
    @FXML
    private Button add;
    DataAccessLayer database;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();

        add.setOnAction(e -> {
            if (name.getText().isEmpty() || duration.getText().isEmpty())
                showAlert("Enter all required data !", true);
            else {
                try {
                    int durationInt = Integer.parseInt(duration.getText());
                    try {
                        int results = database.addCourse(name.getText(), durationInt);
                        if (results == 2)
                            showAlert("Done", false);
                        else
                            showAlert("An Error happened HH !", true);
                    }
                    catch (SQLException ex){
                        showAlert("An error happened trying to connect to the database! !", true);
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Enter a valid Duration !", true);
                }
            }
        });
    }

    public void showAlert(String message, boolean error) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(message);
        a.setHeaderText(error?"An Error Happened!":"Done!");
        a.setTitle("Error");
        a.show();
    }
}
