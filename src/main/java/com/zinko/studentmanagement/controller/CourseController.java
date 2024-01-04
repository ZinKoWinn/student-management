package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.models.Course;
import com.zinko.studentmanagement.models.Student;
import com.zinko.studentmanagement.service.CourseService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class CourseController implements Initializable {
    @FXML
    private TableView<Course> courseTableView;
    @FXML
    private TableColumn<Course, Integer> noCol;
    @FXML
    private TableColumn<Course, String> courseCodeCol;
    @FXML
    private TableColumn<Course, String> courseNameCOl;
    @FXML
    private TableColumn<Student, Integer> maxCapacityCol;

    private CourseService courseService;

    @FXML
    public void add() {
        CourseDialogController.showView(null, (course -> {
            this.courseService.addCourse(course);
            this.initializeData();
        }));
    }

    public void createMenu() {
        MenuItem edit = new MenuItem("_Edit");
        edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

        MenuItem delete = new MenuItem("_Delete");
        delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

        courseTableView.setContextMenu(new ContextMenu(edit, delete));
        edit.setOnAction(e -> edit());
        delete.setOnAction(d -> delete());
    }

    public void edit() {
        Course course = courseTableView.getSelectionModel().getSelectedItem();
        CourseDialogController.showView(course, (updatedCourse -> {
            this.courseService.updateCourse(course);
            this.initializeData();
        }));
    }

    public void delete() {
        Course course = courseTableView.getSelectionModel().getSelectedItem();
        courseService.delete(course);
        initializeData();
    }

    private void initializeData() {
        courseTableView.getItems().clear();
        courseTableView.getItems().addAll(courseService.findAll());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.courseService = CourseService.getInstance();
        this.initializeData();
        createMenu();
    }
}
