package client;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Course {
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleIntegerProperty code = new SimpleIntegerProperty();
    private final SimpleIntegerProperty duration = new SimpleIntegerProperty();
    private final SimpleIntegerProperty hours = new SimpleIntegerProperty();
    private final SimpleStringProperty year = new SimpleStringProperty();
    private final SimpleStringProperty semester = new SimpleStringProperty();
    private final SimpleStringProperty letter = new SimpleStringProperty();
    private final SimpleIntegerProperty grade = new SimpleIntegerProperty();
    private final SimpleFloatProperty gpa = new SimpleFloatProperty();

    public String getYear() {
        return year.get();
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public String getSemester() {
        return semester.get();
    }

    public SimpleStringProperty semesterProperty() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public String getLetter() {
        return letter.get();
    }

    public SimpleStringProperty letterProperty() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter.set(letter);
    }

    public int getGrade() {
        return grade.get();
    }

    public SimpleIntegerProperty gradeProperty() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade.set(grade);
    }

    public float getGpa() {
        return gpa.get();
    }

    public SimpleFloatProperty gpaProperty() {
        return gpa;
    }

    public void setGpa(float gpa) {
        this.gpa.set(gpa);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCode() {
        return code.get();
    }

    public SimpleIntegerProperty codeProperty() {
        return code;
    }

    public void setCode(int code) {
        this.code.set(code);
    }

    public int getDuration() {
        return duration.get();
    }

    public SimpleIntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public int getHours() {
        return hours.get();
    }

    public SimpleIntegerProperty hoursProperty() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours.set(hours);
    }
}
