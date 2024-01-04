package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.models.Course;
import com.zinko.studentmanagement.models.Student;
import com.zinko.studentmanagement.service.CourseService;
import com.zinko.studentmanagement.service.StudentService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class AssignGradeController implements Initializable {

    @FXML
    private ComboBox<Course> courses;
    @FXML
    private ComboBox<Student> students;
    @FXML
    private TextField grade;

    private final CourseService courseService = CourseService.getInstance();
    private final StudentService studentService = StudentService.getInstance();

    @FXML
    public void assignGrade() {
        Course course = courses.getValue();
        Student student = students.getValue();

        if (isValidInput(student, "Student") && isValidInput(course, "Course") && isValidGradeInput()) {
            int gradeValue = Integer.parseInt(grade.getText());
            courseService.assignGrade(student, course, gradeValue);
            clear();
        }
    }

    private boolean isValidInput(Object input, String inputName) {
        if (input == null) {
            showAlert("Invalid Input", "Please select a valid " + inputName + ".");
            return false;
        }
        return true;
    }

    private boolean isValidGradeInput() {
        try {
            int gradeValue = Integer.parseInt(grade.getText());
            if (gradeValue < 0 || gradeValue > 100) {
                showAlert("Invalid Grade", "Please enter a valid grade between 0 and 100.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Invalid Grade", "Please enter a valid integer for the grade.");
            return false;
        }
    }

    private void clear() {
        courses.setValue(null);
        students.setValue(null);
        grade.clear();
        setPromptText("Select Course", "Select Student", "Enter student's grade");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBox(courses, courseService.findAll(), Course::getCourseName);
        initializeComboBox(students, studentService.findAll(), Student::getName);

        students.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courses.getItems().setAll(newValue.getEnrolledCourses());
            }
        });
    }

    private <T> void initializeComboBox(ComboBox<T> comboBox, List<T> data, javafx.util.Callback<T, String> extractor) {
        comboBox.getItems().setAll(data);
        configureComboBox(comboBox, extractor);
    }

    private <T> void configureComboBox(ComboBox<T> comboBox, javafx.util.Callback<T, String> extractor) {
        comboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : extractor.call(item));
            }
        });

        comboBox.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T t) {
                return t == null ? null : extractor.call(t);
            }

            @Override
            public T fromString(String s) {
                return null;
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setPromptText(String coursePrompt, String studentPrompt, String gradePrompt) {
        courses.setPromptText(coursePrompt);
        students.setPromptText(studentPrompt);
        grade.setPromptText(gradePrompt);
    }
}
