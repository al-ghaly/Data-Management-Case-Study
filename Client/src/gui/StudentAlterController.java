package gui;

import client.Department;
import client.Student;
import database.DataAccessLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentAlterController
{
    @javafx.fxml.FXML
    private TextField searchBar;
    @javafx.fxml.FXML
    private TableView<Department> departments;
    @javafx.fxml.FXML
    private TextField nameTxt;
    @javafx.fxml.FXML
    private TextField emailTxt;
    @javafx.fxml.FXML
    private TextField cityTxt;
    @javafx.fxml.FXML
    private TextField streetTxt;
    @javafx.fxml.FXML
    private Label headerTxt;
    @javafx.fxml.FXML
    private Button saveButton;
    Integer dep_id;

    ObservableList<Student> students  =
            FXCollections.observableArrayList();

    ObservableList<Department> depts = FXCollections.observableArrayList();
    Integer id;
    DataAccessLayer database;

    public void initialize() {
        if (id == null)
            headerTxt.setText("Enter the data for the new student.");
        else
            headerTxt.setText("Enter the new data for student : " + id);
        database = DataAccessLayer.getInstance();
        TableColumn<Department, Integer> column = new TableColumn<>("Code");
        column.setCellValueFactory(new PropertyValueFactory<>("Code"));
        departments.getColumns().add(column);
        List<String> strColumns = Arrays.asList("Name",
                "Manager");
        strColumns.forEach(i -> {
            TableColumn<Department, String> column1 = new TableColumn<>(i);
            column1.setCellValueFactory(new PropertyValueFactory<>(i));
            departments.getColumns().add(column1);
        });
            TableColumn<Department, Integer> column2 = new TableColumn<>("Age");
            column2.setCellValueFactory(new PropertyValueFactory<>("Age"));
            departments.getColumns().add(column2);

        fillTable();
        searchBar.textProperty().addListener(
                (observable, oldValue, newValue) -> filterDepartments(newValue));

        // Get The add from market Button to Work
        saveButton.setOnAction(e -> {
            Department selectedDept = departments.getSelectionModel().getSelectedItem();
            if (showConfirm("Are you sure you want to proceed ! ").equals("Ok")) {
                if (id == null)
                    insertStudent(selectedDept);
                else 
                    updateStudent(selectedDept);
            }
        });
    }

    private void updateStudent(Department selectedDept) {
        Student student = new Student();
        student.setId(id);
        student.setName(nameTxt.getText());
        if (selectedDept == null)
            student.setDepartment(dep_id);
        else
            student.setDepartment(selectedDept.getCode());
        student.setStreet(streetTxt.getText());
        student.setCity(cityTxt.getText());
        student.setEmail(emailTxt.getText());
        try {
            database.editStudent(student);
            showAlert("Done", false);
        }catch (SQLException ex) {
            ex.printStackTrace();
            showAlert("An error happened trying to connect with the database !", true);
        }
    }

    private void insertStudent(Department selectedDept) {
        if(nameTxt.getText().equals("")){
            showAlert("Name Field can't be empty!", true);
        }
        else if (selectedDept == null){
            showAlert("Department name can't be empty!", true);
        }
        else {
            Student student = new Student();
            student.setStatus("Active");
            student.setName(nameTxt.getText());
            student.setDepartment(selectedDept.getCode());
            student.setStreet(streetTxt.getText());
            student.setCity(cityTxt.getText());
            student.setEmail(emailTxt.getText());
            try {
                int results = database.addStudent(student);
                if (results == 2) {
                    showAlert("Done", false);
                    students.add(student);
                }
                else
                    showAlert("An Error happened", true);
            }catch (SQLException ex) {
                showAlert("An error happened trying to connect with the database !", true);
            }
        }
    }

    public void fillTable() {
        try {
            ResultSet results = database.getDepartments();
            while (results.next()) {
                Department dept = new Department();
                String name = results.getString(1);
                dept.setName(name);
                int age = results.getInt(2);
                dept.setAge(age);
                int code = results.getInt(3);
                dept.setCode(code);
                String manager = results.getString(4);
                dept.setManager(manager);
                depts.add(dept);
            }
            departments.setItems(depts);
        } catch (SQLException ex) {
            showAlert("An error happened trying to connect with the database !", true);
        }
    }

    private void filterDepartments(String searchText) {
        // Filter the friends list based on the entered text
        ObservableList<Department> filteredDepts= FXCollections.observableArrayList();
        for (Department dept : depts) {
            if (dept.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredDepts.add(dept);
            }
        }
        departments.setItems(filteredDepts);
    }

    public void setData(Integer id, Integer dep_id,  ObservableList<Student> students){
        this.id = id;
        this.dep_id = dep_id;
        this.students = students;
    }

    public void showAlert(String message, boolean error) {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText(message);
            a.setHeaderText(error?"An Error Happened!":"Done");
            a.setTitle("Error");
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
}
