package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import client.Student;
import database.DataAccessLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StudentsController implements Initializable {

    @javafx.fxml.FXML
    private TableView<Student> tableStudents;
    @javafx.fxml.FXML
    private Button buttonRemoveStudent;
    @javafx.fxml.FXML
    private Button buttonAddStudent;
    @javafx.fxml.FXML
    private Button buttonEditStudent;
    @javafx.fxml.FXML
    private TextField search;
    @javafx.fxml.FXML
    private RadioButton all;
    boolean allStudents = true;

    DataAccessLayer database;
    ObservableList<Student> students  =
            FXCollections.observableArrayList();
    ObservableList<Student> filteredStudents;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        all.setSelected(true);
        database = DataAccessLayer.getInstance();
        tableStudents.setMaxWidth(900);
        loadStudents();
        TableColumn<Student, Integer> column1 = new TableColumn<>("Id");
        column1.setCellValueFactory(new PropertyValueFactory<>("Id"));
        column1.setPrefWidth(50);
        tableStudents.getColumns().add(column1);

        List<String> strColumns = Arrays.asList("Name", "Email", "Age", "City", "Street", "Status");
        strColumns.forEach(i -> {
            TableColumn<Student, String> column = new TableColumn<>(i);
            column.setCellValueFactory(new PropertyValueFactory<>(i));
            //column.setPrefWidth(375);
            tableStudents.getColumns().add(column);
        });
        TableColumn<Student, Integer> column2 = new TableColumn<>("Department");
        column2.setCellValueFactory(new PropertyValueFactory<>("Department"));
        column2.setPrefWidth(100);
        tableStudents.getColumns().add(column2);
        TableColumn<Student, Integer> column3 = new TableColumn<>("GPA");
        column3.setCellValueFactory(new PropertyValueFactory<>("Gpa"));
        tableStudents.getColumns().add(column3);
        // Disable the remove/edit button if no items are selected
        buttonRemoveStudent.disableProperty().bind(
                tableStudents.getSelectionModel().selectedItemProperty().isNull());
        buttonEditStudent.disableProperty().bind(
                tableStudents.getSelectionModel().selectedItemProperty().isNull());
        tableStudents.setItems(students);
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> filterStudents(newValue));

        all.selectedProperty().addListener((observable, oldValue, newValue) -> {
            allStudents = newValue;
            filterStudents(search.getText());
        });

        tableStudents.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                // Double-click detected
                int selectedIndex = tableStudents.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < tableStudents.getItems().size()) {
                    Student selectedStudent = tableStudents.getItems().get(selectedIndex);
                    try {
                        openStudentProfile(selectedStudent);
                    } catch (Exception ex) {
                        showAlert("An Error Happened", true);
                    }
                }
            }
        });
        buttonRemoveStudent.setOnAction(e -> {
            Student selectedStudent = tableStudents.getSelectionModel().getSelectedItem();
            int id = selectedStudent.getId();
            if (selectedStudent != null) {
                if (showConfirm("Are you sure you want to remove  "
                        + selectedStudent.getName() + " ?").equals("Ok")) {
                    if(removeStudent(id) == 1){
                        selectedStudent.setStatus("Archived");
                        tableStudents.refresh();
                        filterStudents(search.getText());
                        showAlert("Student " + selectedStudent.getName() + " removed", false);
                    }
                    else {
                        showAlert("An Error happened trying to delete the student !", true);
                    }
                }
            }
        });

        buttonEditStudent.setOnAction(e -> {
            Student selectedStudent = tableStudents.getSelectionModel().getSelectedItem();
            try {
                openAlterStudent(selectedStudent.getId(), selectedStudent.getDepartment());
            } catch (Exception ex) {
                showAlert("An Error Happened", true);
            }
        });

        buttonAddStudent.setOnAction(e -> {
            try {
                openAlterStudent(null, null);
            } catch (Exception ex) {
                showAlert("An Error Happened", true);
            }
        });
    }

    private int removeStudent(int id) {
        try {
            return database.deleteStudent(id);
        } catch (SQLException ex) {
            return -1;
        }
    }

    private void filterStudents(String newValue) {
        // Filter the friends list based on the entered text
        filteredStudents = FXCollections.observableArrayList();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(newValue.toLowerCase())) {
                if(allStudents || student.getStatus().equals("Active"))
                    filteredStudents.add(student);
            }
        }
        tableStudents.setItems(filteredStudents);
    }

    private void loadStudents() {
        try {
            ResultSet studentsData = database.getStudents();
            while (studentsData.next()) {
                Student student = new Student();
                String name = studentsData.getString(1);
                student.setName(name);
                String email = studentsData.getString(2);
                student.setEmail(email);
                int id = studentsData.getInt(3);
                student.setId(id);
                int age = studentsData.getInt(4);
                student.setAge(age);
                String city = studentsData.getString(5);
                student.setCity(city);
                String street = studentsData.getString(6);
                student.setStreet(street);
                int department = studentsData.getInt(7);
                student.setDepartment(department);
                String status = studentsData.getString(8);
                student.setStatus(status);
                float gpa = studentsData.getFloat(9);
                student.setGpa(gpa);
                int totalHours = studentsData.getInt(10);
                student.setTotalHours(totalHours);
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

    public String showConfirm(String message) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText(message);
        a.setHeaderText("Action is irreversible!");
        a.setTitle("Confirm");

        Optional<ButtonType> result = a.showAndWait();
        // Check the user's choice
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User clicked OK
            return "Ok";
        } else {
            return "Cancel";
        }
    }

    private void openStudentProfile(Student student) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/StudentProfile.fxml"));

        // Create an instance of your controller and set the data
        StudentProfileController studentProfileController = new StudentProfileController();
        studentProfileController.setData(student);

        loader.setControllerFactory(clazz -> {
            if (clazz == StudentProfileController.class) {
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

    private void openAlterStudent(Integer id, Integer dep_id) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../gui/StudentAlter.fxml"));

        // Create an instance of your controller and set the data
        StudentAlterController studentAlterController = new StudentAlterController();
        studentAlterController.setData(id, dep_id, students);

        loader.setControllerFactory(clazz -> {
            if (clazz == StudentAlterController.class) {
                return studentAlterController;
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
}
