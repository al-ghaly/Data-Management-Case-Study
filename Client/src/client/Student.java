package client;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty email = new SimpleStringProperty();
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleIntegerProperty age = new SimpleIntegerProperty();
    private final SimpleStringProperty city = new SimpleStringProperty();
    private final SimpleStringProperty street = new SimpleStringProperty();
    private final SimpleIntegerProperty department = new SimpleIntegerProperty();
    private final SimpleStringProperty status = new SimpleStringProperty();

    public float getGpa() {
        return gpa.get();
    }

    public SimpleFloatProperty gpaProperty() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa.set(gpa);
    }

    private final SimpleFloatProperty gpa = new SimpleFloatProperty();

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getAge() {
        return age.get();
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public String getStreet() {
        return street.get();
    }

    public SimpleStringProperty streetProperty() {
        return street;
    }

    public void setStreet(String street) {
        this.street.set(street);
    }

    public int getDepartment() {
        return department.get();
    }

    public SimpleIntegerProperty departmentProperty() {
        return department;
    }

    public void setDepartment(int department) {
        this.department.set(department);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
