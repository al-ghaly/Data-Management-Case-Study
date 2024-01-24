package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ResourceBundle;

import client.Course;
import client.Student;
import database.DataAccessLayer;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


public class GradingController implements Initializable {

    @FXML
    private TextField studentSearch;
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private Button addMarketBtn;
    @FXML
    private TextField CoursesSearch;
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private Label nameTxt;
    @FXML
    private Label semesterTxt;
    @FXML
    private Label gpaTxt;
    @FXML
    private Label yearTxt;
    @FXML
    private Label hoursTxt;
    @FXML
    private Label enHoursTxt;
    @FXML
    private TextField gradeTxt;

    String semester;
    ObservableList<Student> students  =
            FXCollections.observableArrayList();
    ObservableList<Course> enCourses  =
            FXCollections.observableArrayList();
    DataAccessLayer database;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        loadStudents();
        TableColumn<Student, String> column = new TableColumn<>("Name");
        column.setCellValueFactory(new PropertyValueFactory<>("Name"));
        studentsTable.getColumns().add(column);

        TableColumn<Student, String> column2 = new TableColumn<>("Department");
        column2.setCellValueFactory(new PropertyValueFactory<>("City"));
        column2.setPrefWidth(140);
        studentsTable.getColumns().add(column2);
        TableColumn<Student, Integer> column3 = new TableColumn<>("GPA");
        column3.setCellValueFactory(new PropertyValueFactory<>("Gpa"));
        studentsTable.getColumns().add(column3);

        TableColumn<Course, Integer> column5 = new TableColumn<>("Code");
        column5.setCellValueFactory(new PropertyValueFactory<>("Code"));
        coursesTable.getColumns().add(column5);

        TableColumn<Course, String> column4 = new TableColumn<>("Name");
        column4.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column4.setPrefWidth(180);
        coursesTable.getColumns().add(column4);

        TableColumn<Course, Integer> column6 = new TableColumn<>("Hours");
        column6.setCellValueFactory(new PropertyValueFactory<>("Hours"));
        coursesTable.getColumns().add(column6);

        addMarketBtn.disableProperty().bind(
                Bindings.or(
                        Bindings.or(
                                coursesTable.getSelectionModel().selectedItemProperty().isNull(),
                                studentsTable.getSelectionModel().selectedItemProperty().isNull()
                        ),
                        gradeTxt.textProperty().isEmpty()
                )
        );

        studentSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> filterStudents(newValue));
        YearMonth currentYearMonth = YearMonth.now();
        int year = currentYearMonth.getYear();
        int month = currentYearMonth.getMonthValue();
        if (month < 5)
            semester = "First";
        else if(month < 9)
            semester = "Second";
        else
            semester = "Summer";
        studentsTable.setOnMouseClicked(event -> {
            int selectedIndex = studentsTable.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < studentsTable.getItems().size()) {
                Student selectedStudent = studentsTable.getItems().get(selectedIndex);
                nameTxt.setText("Name:" + selectedStudent.getName());
                gpaTxt.setText("GPA: " + selectedStudent.getGpa());
                hoursTxt.setText("Total Hours: " + selectedStudent.getTotalHours());
                enHoursTxt.setText("Enrolled Hours: " + selectedStudent.getAge());
                yearTxt.setText("Year: " + year);
                semesterTxt.setText("Semester: " + semester);
                loadEnCourses(selectedStudent.getId());
            }
        });

        CoursesSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> filterEnCourses(newValue));
        addMarketBtn.setOnAction(e -> {
            Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
            Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
            grade(selectedStudent, selectedCourse);
        });
    }

    private void loadEnCourses(int id) {
        try {
            ResultSet coursesData = database.getEnrolledCourses(id);
            enCourses  =
                    FXCollections.observableArrayList();
            while (coursesData.next()) {
                Course courseData = new Course();
                String name = coursesData.getString(2);
                courseData.setName(name);
                int code = coursesData.getInt(1);
                courseData.setCode(code);
                int hours = coursesData.getInt(3);
                courseData.setHours(hours);
                enCourses.add(courseData);
            }
            coursesTable.setItems(enCourses);
        } catch (SQLException ex) {
            showAlert("An Error happened trying to connect to the database !", true);
        }
    }

    private void filterStudents(String newValue) {
        // Filter the friends list based on the entered text
        ObservableList<Student> filteredStudents = FXCollections.observableArrayList();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filteredStudents.add(student);
            }
        }
        studentsTable.setItems(filteredStudents);
    }

    private void filterEnCourses(String newValue) {
        // Filter the friends list based on the entered text
        ObservableList<Course> filteredEnCourses = FXCollections.observableArrayList();
        for (Course course : enCourses) {
            if (course.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filteredEnCourses.add(course);
            }
        }
        coursesTable.setItems(filteredEnCourses);
    }

    public void showAlert(String message, boolean error) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(message);
        a.setHeaderText(error?"An Error Happened!":"Done!");
        a.setTitle("Error");
        a.show();
    }

    private void loadStudents() {
        try {
            ResultSet studentsData = database.getCurrentStudents();
            while (studentsData.next()) {
                Student student = new Student();
                String name = studentsData.getString(1);
                student.setName(name);
                String department = studentsData.getString(2);
                student.setCity(department);
                float gpa = studentsData.getFloat(3);
                student.setGpa(gpa);
                int id = studentsData.getInt(4);
                student.setId(id);
                int hours = studentsData.getInt(5);
                student.setTotalHours(hours);
                int enrolledHors = studentsData.getInt(6);
                student.setAge(enrolledHors);
                students.add(student);
            }
            studentsTable.setItems(students);
        } catch (SQLException ex) {
            showAlert("An Error happened trying to connect to the database !", true);
        }
    }

    private void grade(Student student, Course course){
        try {
            int grade = Integer.parseInt(gradeTxt.getText());
            if (grade > 100 || grade < 0)
                showAlert("Enter a valid Grade !", true);
            else{
                try {
                    int results = database.gradeStudent(student.getId(), course.getCode(), grade);
                    if (results == 2) {
                        showAlert("Completed", false);
                        student.setAge(student.getAge() - course.getHours());
                        enHoursTxt.setText("Enrolled Hours: " + student.getAge());
                        if (grade > 60)
                            student.setTotalHours(student.getTotalHours() + course.getHours());
                        hoursTxt.setText("Total Hours: " + student.getTotalHours());
                        enCourses.remove(course);
                        coursesTable.refresh();
                        if(student.getTotalHours() == 180){
                            int result = database.graduate(student.getId());
                            if (result == 1)
                                showAlert("Student: " + student.getName() + " Has completed Graduation requirements", false);
                        }
                    }
                    else
                        showAlert("An Error happened HH !", true);
                }
                catch (SQLException ex){
                    ex.printStackTrace();
                    showAlert("An error happened trying to connect to the database! !", true);
                }
            }
        } catch (NumberFormatException e) {
            showAlert("Enter a valid Grade !", true);
        }
    }
}
