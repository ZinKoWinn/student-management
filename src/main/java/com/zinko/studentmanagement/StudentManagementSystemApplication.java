package com.zinko.studentmanagement;

import com.zinko.studentmanagement.controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class StudentManagementSystemApplication extends Application {
    @Override
    public void start(Stage stage) {
        MainController.showView();
    }

    public static void main(String[] args) {
        launch();
    }
}
