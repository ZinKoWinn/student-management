package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.StudentManagementSystemApplication;
import com.zinko.studentmanagement.models.Course;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.function.Consumer;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class CourseDialogController {
    @FXML
    private Label title;
    @FXML
    private TextField courseCode, courseName, maximumCapacity;

    private Course course;
    private Consumer<Course> saveHandler;
    private static Stage stage;

    public static void showView(Course course, Consumer<Course> saveHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(StudentManagementSystemApplication.class.getResource("/views/course-dialog.fxml"));
            Parent view = loader.load();

            stage = new Stage();
            CourseDialogController controller = loader.getController();
            controller.saveHandler = saveHandler;
            controller.setData(course);

            stage.setScene(new Scene(view));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(Course course) {
        this.course = course;
        String mode = (course == null) ? "ADD" : "EDIT";
        stage.setTitle("Course " + mode);
        title.setText(mode);

        Image icon = new Image(getClass().getResourceAsStream("/images/" + mode.toLowerCase() + ".png"));
        stage.getIcons().add(icon);

        if (course != null) {
            courseCode.setText(course.getCourseCode());
            courseName.setText(course.getCourseName());
            maximumCapacity.setText(String.valueOf(course.getMaxCapacity()));
        }
    }

    @FXML
    public void clear() {
        courseCode.setText("");
        courseName.setText("");
        maximumCapacity.setText("");

        courseCode.setPromptText("Enter course's code");
        courseName.setPromptText("Enter course's name");
        maximumCapacity.setPromptText("Enter course's maximum capacity");
    }

    public void save() {
        try {
            if (course == null) {
                course = new Course();
            }

            if (isValidInput(courseCode, "Course Code") && isValidInput(courseName, "Course Name") && isValidIntegerInput(maximumCapacity, "Maximum Capacity")) {
                course.setCourseCode(courseCode.getText());
                course.setCourseName(courseName.getText());
                course.setMaxCapacity(Integer.parseInt(maximumCapacity.getText()));
                saveHandler.accept(course);
                close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidInput(TextField inputField, String inputName) {
        if (inputField.getText().isEmpty()) {
            showAlert("Invalid Input", inputName + " cannot be empty.");
            return false;
        }
        return true;
    }

    private boolean isValidIntegerInput(TextField inputField, String inputName) {
        try {
            Integer.parseInt(inputField.getText());
            return true;
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid integer for " + inputName + ".");
            return false;
        }
    }

    public void close() {
        title.getScene().getWindow().hide();
        clear();
    }
}
