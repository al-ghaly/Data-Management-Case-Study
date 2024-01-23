package gui;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import client.Student;
import database.DataAccessLayer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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

    DataAccessLayer database;
    ArrayList<Student> students = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        fillTable(tableStudents);

        buttonEditStudent.disableProperty().bind(
                tableStudents.getSelectionModel().selectedItemProperty().isNull());
        fillTable(tableStudents);
        search.textProperty().addListener(
                (observable, oldValue, newValue) -> filterStudents(newValue));

//        buttonAddFriend.setOnAction(e -> {
//            ClientTable selectedUser = tableAddFriend.getSelectionModel().getSelectedItem();
//            String friendName = selectedUser.getUsername();
//            if (selectedUser != null) {
//                if (showConfirm("Are you sure you want to add  "
//                        + friendName + " as a friend?").equals("Ok")) {
//                    addFriend(username, friendName);
//                }
//            }
//        });
    }

//    private void addFriend(String username, String friendName) {
//        try {
//            JSONObject logInData = new JSONObject();
//            logInData.put("Type", "add friend");
//            logInData.put("username", username);
//            logInData.put("friend name", friendName);
//
//            // Send the JSON string to the server
//            ClientSide.ps.println(logInData);
//            ClientSide.ps.flush();
//
//            // Read the server response
//            String response = ClientSide.dis.readLine();
//
//            if (response.equals("success")) {
//                showAlert("Request sent ^_^!", false);
//            }
//            else{
//                showAlert("An error happened connecting to server!", true);
//            }
//
//        } catch (Exception ex) {
//            // Provide feedback to the user about the error
//            showAlert("An error happened connecting to server!", true);
//        }
//    }

    private void filterStudents(String newValue) {
        // Filter the friends list based on the entered text
        ObservableList<Student> filteredStudents= FXCollections.observableArrayList();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(newValue.toLowerCase())) {
                filteredStudents.add(student);
            }
        }
        tableStudents.setItems(filteredStudents);
    }

    private void fillTable(TableView<Student> tableStudents) {
        ObservableList<Student> stds =
                FXCollections.observableArrayList(students);
        tableStudents.setItems(stds);
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
}
