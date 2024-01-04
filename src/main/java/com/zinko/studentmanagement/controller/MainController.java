package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.StudentManagementSystemApplication;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class MainController implements Initializable {
    @FXML
    private StackPane viewHolder;
    @FXML
    private VBox VView;

    public static void showView() {
        try {
            Parent root = FXMLLoader.load(StudentManagementSystemApplication.class.getResource("/views/main.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Student Management System");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void buttonClick() {
        VView.getChildren().stream().filter(node -> node instanceof Button).map(node -> (Button) node)
                .forEach(button -> {
                    button.setOnAction(e -> {
                        String name = button.getText().replace(" ", "").toLowerCase();
                        fadeShow();
                        loadView(name);
                    });
                });
    }

    private void fadeShow() {
        FadeTransition fade = new FadeTransition();
        fade.setDuration(Duration.millis(4000));
        fade.setNode(viewHolder);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    public void loadView(String fileName) {
        try {
            Parent view = FXMLLoader.load(StudentManagementSystemApplication.class.getResource("/views/".concat(fileName.concat(".fxml"))));
            viewHolder.getChildren().clear();
            viewHolder.getChildren().add(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadView("course");
        buttonClick();
    }
}
