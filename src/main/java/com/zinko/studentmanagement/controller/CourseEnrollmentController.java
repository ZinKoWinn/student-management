package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.models.Course;
import com.zinko.studentmanagement.models.Student;
import com.zinko.studentmanagement.service.CourseService;
import com.zinko.studentmanagement.service.StudentService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class CourseEnrollmentController implements Initializable {
    @FXML
    private ComboBox<Course> courses;
    @FXML
    private ComboBox<Student> students;

    private final CourseService courseService = CourseService.getInstance();
    private final StudentService studentService = StudentService.getInstance();

    @FXML
    public void enrollment() {
        Course course = courses.getValue();
        Student student = students.getValue();

        if (isValidInput(course, "Course") && isValidInput(student, "Student")) {
            courseService.enrollStudent(student, course);
            clear();
        }
    }

    private <T> boolean isValidInput(T input, String inputName) {
        if (input == null) {
            showAlert("Invalid Input", "Please select a valid " + inputName + ".");
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void clear() {
        courses.setValue(null);
        students.setValue(null);
        courses.setPromptText("Select Course");
        students.setPromptText("Select Student");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBox(courses, courseService.findAll(), Course::getCourseName);
        initializeComboBox(students, studentService.findAll(), Student::getName);

        courses.valueProperty().addListener((observable, oldValue, newValue) ->
                students.getItems().setAll(
                        studentService.findAll().stream()
                                .filter(student -> !student.getEnrolledCourses().contains(newValue))
                                .collect(Collectors.toList())
                )
        );
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
}
