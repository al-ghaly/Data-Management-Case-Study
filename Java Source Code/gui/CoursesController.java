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

public class CoursesController implements Initializable {

    @javafx.fxml.FXML
    private TableView<Course> tableStudents;
    @javafx.fxml.FXML
    private Button buttonAddStudent;
    @javafx.fxml.FXML
    private TextField search;

    DataAccessLayer database;
    ObservableList<Course> students  =
            FXCollections.observableArrayList();
    ObservableList<Course> filteredStudents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        tableStudents.setMaxWidth(700);
        loadStudents();

        TableColumn<Course, Integer> column = new TableColumn<>("Code");
        column.setCellValueFactory(new PropertyValueFactory<>("Code"));
        tableStudents.getColumns().add(column);

        TableColumn<Course, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableStudents.getColumns().add(column2);

        List<String> intColumns = Arrays.asList("Duration", "Hours");
        intColumns.forEach(i -> {
            TableColumn<Course, Integer> column3 = new TableColumn<>(i);
            column3.setCellValueFactory(new PropertyValueFactory<>(i));
            tableStudents.getColumns().add(column3);
        });
        TableColumn<Course, Integer> column3 = new TableColumn<>("NO. Students");
        column3.setCellValueFactory(new PropertyValueFactory<>("Grade"));
        column3.setPrefWidth(150);
        tableStudents.getColumns().add(column3);
        TableColumn<Course, Integer> column4 = new TableColumn<>("Average GPA");
        column4.setCellValueFactory(new PropertyValueFactory<>("Gpa"));
        column4.setPrefWidth(150);
        tableStudents.getColumns().add(column4);

        tableStudents.setItems(students);
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> filterStudents(newValue));

        tableStudents.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Double-click detected
                int selectedIndex = tableStudents.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < tableStudents.getItems().size()) {
                    Course selectedStudent = tableStudents.getItems().get(selectedIndex);
                    try {
                        openStudentProfile(selectedStudent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        showAlert("An Error Happened", true);
                    }
                }
            }
        });
        buttonAddStudent.setOnAction(e -> {
            try {
                openAlterStudent();
            } catch (Exception ex) {
                showAlert("An Error Happened", true);
            }
        });
    }

    private void filterStudents(String newValue) {
        // Filter the friends list based on the entered text
        filteredStudents = FXCollections.observableArrayList();
        for (Course student : students) {
            if (student.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filteredStudents.add(student);
            }
        }
        tableStudents.setItems(filteredStudents);
    }

    private void loadStudents() {
        try {
            ResultSet studentsData = database.getCoursesData();
            while (studentsData.next()) {
                Course student = new Course();
                String name = studentsData.getString(2);
                student.setName(name);
                int code = studentsData.getInt(1);
                student.setCode(code);
                int duration = studentsData.getInt(3);
                student.setDuration(duration);
                int hours = studentsData.getInt(4);
                student.setHours(hours);
                float gpa = studentsData.getFloat(6);
                student.setGpa(gpa);
                int studentsNo = studentsData.getInt(5);
                student.setGrade(studentsNo);
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

    private void openStudentProfile(Course student) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/CourseProfile.fxml"));

        // Create an instance of your controller and set the data
        CourseProfileController studentProfileController = new CourseProfileController();
        studentProfileController.setData(student);

        loader.setControllerFactory(clazz -> {
            if (clazz == CourseProfileController.class) {
                return studentProfileController;
            } else {
                try {
                    return clazz.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        Stage popupStage = new Stage();
        Parent home = loader.load();
        Scene scene = new Scene(home);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }

    private void openAlterStudent() throws Exception{
        Stage popupStage = new Stage();
        Parent students = FXMLLoader.load(getClass().getResource("../gui/AddCourse.fxml"));
        Scene scene = new Scene(students);
        popupStage.setScene(scene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.show();
    }
}
