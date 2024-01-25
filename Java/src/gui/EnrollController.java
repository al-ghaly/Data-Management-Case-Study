package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import java.time.YearMonth;

public class EnrollController implements Initializable {

    DataAccessLayer database;
    @FXML
    private TextField studentSearch;
    @FXML
    private TableView<Student> studentsTable;
    @FXML
    private Button addMarketBtn;
    @FXML
    private TextField enrolledSearch;
    @FXML
    private TableView<Course> EnrolledTable;
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
    String semester;
    ObservableList<Student> students  =
            FXCollections.observableArrayList();
    ObservableList<Course> courses  =
            FXCollections.observableArrayList();
    ObservableList<Course> enCourses  =
            FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = DataAccessLayer.getInstance();
        loadStudents();
        loadCourses();
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
        EnrolledTable.getColumns().add(column5);

        TableColumn<Course, String> column4 = new TableColumn<>("Name");
        column4.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column4.setPrefWidth(180);
        EnrolledTable.getColumns().add(column4);

        TableColumn<Course, Integer> column6 = new TableColumn<>("Hours");
        column6.setCellValueFactory(new PropertyValueFactory<>("Hours"));
        EnrolledTable.getColumns().add(column6);

        TableColumn<Course, Integer> column7 = new TableColumn<>("Code");
        column7.setCellValueFactory(new PropertyValueFactory<>("Code"));
        coursesTable.getColumns().add(column7);

        TableColumn<Course, String> column8 = new TableColumn<>("Name");
        column8.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column8.setPrefWidth(180);
        coursesTable.getColumns().add(column8);

        TableColumn<Course, Integer> column9 = new TableColumn<>("Hours");
        column9.setCellValueFactory(new PropertyValueFactory<>("Hours"));
        coursesTable.getColumns().add(column9);

        addMarketBtn.disableProperty().bind(
                Bindings.or(
                        coursesTable.getSelectionModel().selectedItemProperty().isNull(),
                        studentsTable.getSelectionModel().selectedItemProperty().isNull()
                ));

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

        enrolledSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> filterEnCourses(newValue));

        CoursesSearch.textProperty().addListener(
                (observable, oldValue, newValue) -> filterCourses(newValue));
        addMarketBtn.setOnAction(e -> {
            Student selectedStudent = studentsTable.getSelectionModel().getSelectedItem();
            Course selectedCourse = coursesTable.getSelectionModel().getSelectedItem();
            enrollStudent(selectedStudent, selectedCourse);
        });
    }

    private void enrollStudent(Student selectedStudent, Course selectedCourse) {
        int hoursLimit;
        if (selectedStudent.getGpa() > 3)
            hoursLimit = 22;
        else if (selectedStudent.getGpa() > 2)
            hoursLimit = 18;
        else
            hoursLimit = 14;
        if(selectedStudent.getAge() + selectedCourse.getHours() > hoursLimit)
            showAlert("Hours Limit exceeded, " + selectedStudent.getName() + " Can only add up to " + hoursLimit + " Hours", true);
        else if (selectedStudent.getTotalHours() + selectedCourse.getHours() > 180)
            showAlert("Can't enroll to more than 180 hours !", true);
        else
            try {
                int results = database.enroll(selectedStudent.getId(), selectedCourse.getCode());
                if (results == 2) {
                    showAlert("Done", false);
                    enCourses.add(selectedCourse);
                    coursesTable.refresh();
                    selectedStudent.setAge(selectedStudent.getAge() + selectedCourse.getHours());
                    enHoursTxt.setText("Enrolled Hours: " + selectedStudent.getAge());
                }
                else
                    showAlert("An Error Happened!", true);
            }
            catch (SQLException ex){
                if(ex.getErrorCode() == 20003)
                    showAlert(selectedStudent.getName() + " Has not completed the prerequisites for " + selectedCourse.getName() + " !", true);
                else if (ex.getErrorCode() == 20002)
                    showAlert(selectedStudent.getName() + " Is already enrolled into " + selectedCourse.getName() + " !", true);
                else if (ex.getErrorCode() == 20001)
                    showAlert(selectedStudent.getName() + " Has already attempted " + selectedCourse.getName() + " twice !", true);
                else
                    showAlert("An Error happened !", true);
            }
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
            EnrolledTable.setItems(enCourses);
        } catch (SQLException ex) {
            showAlert("An Error happened trying to connect to the database !", true);
        }
    }

    private void loadCourses() {
        try {
            ResultSet coursesData = database.getCourses();
            courses  =
                    FXCollections.observableArrayList();
            while (coursesData.next()) {
                Course courseData = new Course();
                String name = coursesData.getString(2);
                courseData.setName(name);
                int code = coursesData.getInt(1);
                courseData.setCode(code);
                int hours = coursesData.getInt(3);
                courseData.setHours(hours);
                courses.add(courseData);
            }
            coursesTable.setItems(courses);
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
        EnrolledTable.setItems(filteredEnCourses);
    }

    private void filterCourses(String newValue) {
        // Filter the friends list based on the entered text
        ObservableList<Course> filteredCourses = FXCollections.observableArrayList();
        for (Course course : courses) {
            if (course.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filteredCourses.add(course);
            }
        }
        coursesTable.setItems(filteredCourses);
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
}
