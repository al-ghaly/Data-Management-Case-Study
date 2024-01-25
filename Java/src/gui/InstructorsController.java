package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import client.Course;
import client.Student;
import database.DataAccessLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InstructorsController implements Initializable {

    @javafx.fxml.FXML
    private TableView<Student> tableStudents;
    @javafx.fxml.FXML
    private TextField search;

    DataAccessLayer database;
    ObservableList<Student> students  =
            FXCollections.observableArrayList();
    ObservableList<Student> filteredStudents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        tableStudents.setMaxWidth(700);
        loadStudents();

        TableColumn<Student, Integer> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        column1.setPrefWidth(50);
        tableStudents.getColumns().add(column1);

        List<String> strColumns = Arrays.asList("Name", "Email");
        strColumns.forEach(i -> {
            TableColumn<Student, String> column = new TableColumn<>(i);
            column.setCellValueFactory(new PropertyValueFactory<>(i));
            tableStudents.getColumns().add(column);
        });
        TableColumn<Student, Integer> column3 = new TableColumn<>("Phone");
        column3.setCellValueFactory(new PropertyValueFactory<>("City"));
        tableStudents.getColumns().add(column3);
        TableColumn<Student, String> column2 = new TableColumn<>("Department");
        column2.setCellValueFactory(new PropertyValueFactory<>("Street"));
        tableStudents.getColumns().add(column2);

        tableStudents.setItems(students);
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> filterStudents(newValue));
    }

    private void filterStudents(String newValue) {
        // Filter the friends list based on the entered text
        filteredStudents = FXCollections.observableArrayList();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filteredStudents.add(student);
            }
        }
        tableStudents.setItems(filteredStudents);
    }

    private void loadStudents() {
        try {
            ResultSet studentsData = database.getInstructors();
            while (studentsData.next()) {
                Student student = new Student();
                String name = studentsData.getString(2);
                student.setName(name);
                int code = studentsData.getInt(1);
                student.setId(code);
                String email = studentsData.getString(3);
                student.setEmail(email);
                String phone = studentsData.getString(4);
                student.setCity(phone);
                String dept = studentsData.getString(5);
                student.setStreet(dept);
                students.add(student);
            }
        } catch (SQLException ex) {
            showAlert("An Error happened trying to connect to the database !", true);
        }
    }

    public void showAlert(String message, boolean error) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(message);
        a.setHeaderText(error?"An Error Happened!":"Done!");
        a.setTitle("Error!!");
        a.show();
    }
}
