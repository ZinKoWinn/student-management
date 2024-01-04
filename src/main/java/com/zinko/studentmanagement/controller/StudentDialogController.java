package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.StudentManagementSystemApplication;
import com.zinko.studentmanagement.models.Student;
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
 * Controller class for the student dialog window.
 *
 * <p>
 * This class handles the interactions with the student dialog window, allowing users to add or edit student information.
 * </p>
 *
 * <p>
 * The controller provides methods for setting data, clearing input fields, saving student information,
 * and handling input validation.
 * </p>
 *
 *
 * @author Zin Ko Winn
 * @see Student
 * @since 2024-01-03
 */
public class StudentDialogController {

    @FXML
    private Label title;
    @FXML
    private TextField studentId, name, age;

    private Student student;
    private Consumer<Student> saveHandler;
    private static Stage stage;

    /**
     * Shows the student dialog window for adding or editing student information.
     *
     * @param student     The student to be edited or null for adding a new student.
     * @param saveHandler The consumer to handle saving the student information.
     */
    public static void showView(Student student, Consumer<Student> saveHandler) {
        try {
            FXMLLoader loader = new FXMLLoader(StudentManagementSystemApplication.class.getResource("/views/student-dialog.fxml"));
            Parent view = loader.load();

            stage = new Stage();
            StudentDialogController controller = loader.getController();
            controller.saveHandler = saveHandler;
            controller.setData(student);

            stage.setScene(new Scene(view));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets data for the student dialog.
     *
     * @param student The student to be edited or null for adding a new student.
     */
    public void setData(Student student) {
        this.student = student;
        if (student != null) {
            name.setText(student.getName());
            age.setText(String.valueOf(student.getAge()));
            studentId.setText(student.getStudentId());
            title.setText("EDIT");
        } else {
            title.setText("ADD");
        }
        setStageProperties();
    }

    /**
     * Clears input fields and sets prompt text.
     */
    public void clear() {
        studentId.setText("");
        name.setText("");
        age.setText("");

        setPromptText("Enter student's id", "Enter student's name", "Enter student's age");
    }

    /**
     * Saves the student information after validating input.
     */
    public void save() {
        try {
            if (student == null) {
                student = new Student();
            }

            if (isValidInput(studentId, "Student ID") && isValidInput(name, "Name") && isValidIntegerInput(age, "Age")) {
                student.setStudentId(studentId.getText());
                student.setName(name.getText());
                student.setAge(Integer.parseInt(age.getText()));
                saveHandler.accept(student);
                close();
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred while saving the student information.");
            e.printStackTrace();
        }
    }

    /**
     * Validates if the input in the TextField is not empty.
     *
     * @param inputField The TextField to be validated.
     * @param inputName  The name of the input field for error messages.
     * @return True if the input is valid, false otherwise.
     */
    private boolean isValidInput(TextField inputField, String inputName) {
        if (inputField.getText().isEmpty()) {
            showAlert("Invalid Input", inputName + " cannot be empty.");
            return false;
        }
        return true;
    }

    /**
     * Validates if the input in the TextField is a valid integer greater than 0.
     *
     * @param inputField The TextField to be validated.
     * @param inputName  The name of the input field for error messages.
     * @return True if the input is a valid integer, false otherwise.
     */
    private boolean isValidIntegerInput(TextField inputField, String inputName) {
        try {
            int ageValue = Integer.parseInt(inputField.getText());
            if (ageValue <= 0) {
                showAlert("Invalid Input", "Age must be greater than 0.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid integer for " + inputName + ".");
            return false;
        }
    }

    /**
     * Displays an alert with the given title and content.
     *
     * @param title   The title of the alert.
     * @param content The content of the alert.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Sets properties for the stage, including title and icon.
     */
    private void setStageProperties() {
        stage.setTitle(title.getText() + " Student");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/" + title.getText().toLowerCase() + ".png")));
    }

    /**
     * Closes the student dialog window.
     */
    public void close() {
        title.getScene().getWindow().hide();
    }

    /**
     * Sets prompt text for input fields.
     *
     * @param idPrompt   The prompt text for student ID.
     * @param namePrompt The prompt text for student name.
     * @param agePrompt  The prompt text for student age.
     */
    private void setPromptText(String idPrompt, String namePrompt, String agePrompt) {
        studentId.setPromptText(idPrompt);
        name.setPromptText(namePrompt);
        age.setPromptText(agePrompt);
    }
}
