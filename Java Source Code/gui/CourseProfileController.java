package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import client.Course;
import client.Student;
import database.DataAccessLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class CourseProfileController implements Initializable {

    @FXML
    private Label usernameTxt;
    @FXML
    private Label emailTxt;
    @FXML
    private Label phoneTxt;
    @FXML
    private Label GPA;
    @FXML
    private Label balanceTxt;
    @FXML
    private Label totalHours;
    @FXML
    private TableView<Student> courses;

    Course student;
    DataAccessLayer database;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        usernameTxt.setText(student.getName());
        balanceTxt.setText("Code: " + student.getCode());
        phoneTxt.setText("Duration: " + student.getDuration());
        emailTxt.setText("Credit Hours: " + student.getHours());
        GPA.setText("Average GPA: " + student.getGpa());
        totalHours.setText("Enrolled Students: " + student.getGrade());

        TableColumn<Student, Integer> column = new TableColumn<>("Id");
        column.setCellValueFactory(new PropertyValueFactory<>("Id"));
        courses.getColumns().add(column);

        TableColumn<Student, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("Name"));
        courses.getColumns().add(column2);

        TableColumn<Student, String> column3 = new TableColumn<>("Year");
        column3.setCellValueFactory(new PropertyValueFactory<>("City"));
        courses.getColumns().add(column3);
        TableColumn<Student, String> column4 = new TableColumn<>("Semester");
        column4.setCellValueFactory(new PropertyValueFactory<>("Street"));
        courses.getColumns().add(column4);

        TableColumn<Student, Integer> column6 = new TableColumn<>("Grade");
        column6.setCellValueFactory(new PropertyValueFactory<>("Age"));
        courses.getColumns().add(column6);

        TableColumn<Student, String> column5 = new TableColumn<>("Letter");
        column5.setCellValueFactory(new PropertyValueFactory<>("Email"));
        courses.getColumns().add(column5);

        TableColumn<Student, Integer> column7 = new TableColumn<>("GPA");
        column7.setCellValueFactory(new PropertyValueFactory<>("TotalHours"));
        courses.getColumns().add(column7);

        // Fill the table
        fillTable();
        courses.setMaxWidth(500);
    }

    public void fillTable() {
        try {
            ResultSet results = database.getCourseStudents(student.getCode());
            ObservableList<Student> courseList =
                    FXCollections.observableArrayList();
            while (results.next()) {
                Student course = new Student();
                int code = results.getInt(1);
                course.setId(code);
                String name = results.getString(2);
                course.setName(name);
                String year = results.getString(3);
                course.setCity(year);
                String semester = results.getString(4);
                course.setStreet(semester);
                int grade = results.getInt(5);
                course.setAge(grade);
                String letter = results.getString(6);
                course.setEmail(letter);
                int duration = results.getInt(7);
                course.setTotalHours(duration);
                courseList.add(course);
            }
            courses.setItems(courseList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("An error happened trying to connect with the database !", true);
        }
    }

    public void setData(Course student) {
        this.student = student;
    }

    public void showAlert(String message, boolean error) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(message);
        a.setHeaderText(error?"An Error Happened!":"Done!");
        a.setTitle("Error");
        a.show();
    }
}
