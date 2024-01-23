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

public class StudentProfileController implements Initializable {

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
    @javafx.fxml.FXML
    private TableView<Course> courses;
    int total_hours = 0;

    Student student;
    DataAccessLayer database;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        usernameTxt.setText(student.getName());
        balanceTxt.setText(student.getCity());
        phoneTxt.setText(student.getAge() + " Years");
        emailTxt.setText(student.getEmail());
        GPA.setText("GPA: " + student.getGpa());
        TableColumn<Course, Integer> column = new TableColumn<>("Code");
        column.setCellValueFactory(new PropertyValueFactory<>("Code"));
        courses.getColumns().add(column);

        TableColumn<Course, String> column2 = new TableColumn<>("Name");
        column2.setCellValueFactory(new PropertyValueFactory<>("Name"));
        courses.getColumns().add(column2);

        List<String> intColumns = Arrays.asList("Duration", "Hours");
        intColumns.forEach(i -> {
            TableColumn<Course, Integer> column3 = new TableColumn<>(i);
            column3.setCellValueFactory(new PropertyValueFactory<>(i));
            courses.getColumns().add(column3);
        });
        List<String> strCols = Arrays.asList("Year", "Semester");
        strCols.forEach(i -> {
            TableColumn<Course, Integer> column4 = new TableColumn<>(i);
            column4.setCellValueFactory(new PropertyValueFactory<>(i));
            courses.getColumns().add(column4);
        });

        List<String> intColumns2 = Arrays.asList("Grade", "Gpa");
        intColumns2.forEach(i -> {
            TableColumn<Course, Integer> column3 = new TableColumn<>(i);
            column3.setCellValueFactory(new PropertyValueFactory<>(i));
            courses.getColumns().add(column3);
        });

        TableColumn<Course, String> column5 = new TableColumn<>("Letter");
        column5.setCellValueFactory(new PropertyValueFactory<>("Letter"));
        courses.getColumns().add(column5);

        // Fill the table
        fillTable();
        courses.setMaxWidth(750);
        totalHours.setText("Total Hours: " + total_hours);
    }

    public void fillTable() {
        try {
            ResultSet results = database.getCourseList(student.getId());
            ObservableList<Course> courseList =
                    FXCollections.observableArrayList();
            while (results.next()) {
                Course course = new Course();
                int code = results.getInt(1);
                course.setCode(code);
                String name = results.getString(2);
                course.setName(name);
                int duration = results.getInt(3);
                course.setDuration(duration);
                int hours = results.getInt(4);
                total_hours += hours;
                course.setHours(hours);
                String year = results.getString(5);
                course.setYear(year);
                String semester = results.getString(6);
                course.setSemester(semester);
                int grade = results.getInt(7);
                course.setGrade(grade);
                float gpa = results.getFloat(8);
                course.setGpa(gpa);
                String letter = results.getString(9);
                course.setLetter(letter);
                courseList.add(course);
            }
            courses.setItems(courseList);
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("An error happened trying to connect with the database !", true);
        }
    }

    public void setData(Student student) {
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
