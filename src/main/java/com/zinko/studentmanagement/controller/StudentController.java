package com.zinko.studentmanagement.controller;

import com.zinko.studentmanagement.models.Course;
import com.zinko.studentmanagement.models.Student;
import com.zinko.studentmanagement.models.StudentGrade;
import com.zinko.studentmanagement.service.CourseService;
import com.zinko.studentmanagement.service.StudentService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
public class StudentController implements Initializable {
    @FXML
    private TableView<Student> studentTableView;
    @FXML
    private TableColumn<Student, Integer> noCol;
    @FXML
    private TableColumn<Student, Integer> idCol;
    @FXML
    private TableColumn<Student, String> nameCOl;
    @FXML
    private TableColumn<Student, String> ageCol;
    @FXML
    private TableColumn<Student, Void> detailCol;
    private StudentService studentService;
    private CourseService courseService;

    @FXML
    public void add() {
        StudentDialogController.showView(null, (student -> {
            this.studentService.addStudent(student);
            this.initializeData();
        }));
    }

    public void createMenu() {
        MenuItem edit = new MenuItem("_Edit");
        edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

        MenuItem delete = new MenuItem("_Delete");
        delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

        studentTableView.setContextMenu(new ContextMenu(edit, delete));
        edit.setOnAction(e -> edit());
        delete.setOnAction(d -> delete());
    }

    public void edit() {
        Student student = studentTableView.getSelectionModel().getSelectedItem();
        StudentDialogController.showView(student, (updatedStudent -> {
            this.studentService.updateStudent(student);
            this.initializeData();
        }));
    }

    public void delete() {
        Student student = studentTableView.getSelectionModel().getSelectedItem();
        studentService.delete(student);
        initializeData();
    }

    private void initializeData() {
        studentTableView.getItems().clear();
        studentTableView.getItems().addAll(studentService.findAll());
        detailCol.setCellFactory(param -> new TableCell<>() {
            final Button detailsButton = new Button("Details");

            {
                detailsButton.setOnAction(event -> {
                    Student student = getTableView().getItems().get(getIndex());
                    showDetailsModal(student);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(detailsButton);
                }
            }
        });
    }

    private void showDetailsModal(Student student) {
        Stage modalStage = new Stage();
        TableView<StudentGrade> tableView = new TableView<>();
        TableColumn<StudentGrade, String> nameColumn = new TableColumn<>("Course Name");
        TableColumn<StudentGrade, Integer> gradeColumn = new TableColumn<>("Grade");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        nameColumn.setPrefWidth(200);
        gradeColumn.setPrefWidth(200);

        List<Course> enrolledCourses = student.getEnrolledCourses();

        List<StudentGrade> studentGrades = this.getStudentGrades(this.courseService.getStudentGrades(), student, enrolledCourses);

        tableView.getColumns().addAll(nameColumn, gradeColumn);
        tableView.setItems(FXCollections.observableArrayList(studentGrades));

        Label overallGradeLabel = new Label("Overall Grade: ");
        String overallGrade = String.valueOf(courseService.calculateOverallGrade(student));
        Label overallGradeValue = new Label(overallGrade);

        HBox overallGradeBox = new HBox(5, overallGradeLabel, overallGradeValue);
        overallGradeBox.setStyle("-fx-padding: 10px;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> modalStage.close());

        VBox modalVBox = new VBox(10, overallGradeBox, tableView, closeButton);
        modalVBox.setStyle("-fx-padding: 10px;");

        Scene modalScene = new Scene(modalVBox, 400, 300);

        modalStage.initModality(Modality.APPLICATION_MODAL);
        modalStage.setTitle("Student Details");
        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }


    private List<StudentGrade> getStudentGrades(Map<String, Map<Course, Integer>> studentGrades,
                                                Student student,
                                                List<Course> enrolledCourses) {
        return enrolledCourses.stream()
                .map(course -> {
                    String key = student.getName() + "_" + course.getCourseCode();
                    Map<Course, Integer> courseGrades = studentGrades.getOrDefault(key, Collections.emptyMap());
                    return StudentGrade.builder()
                            .courseName(course.getCourseName())
                            .grade(courseGrades.getOrDefault(course, 0))
                            .build();
                }).collect(Collectors.toList());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.studentService = StudentService.getInstance();
        this.courseService = CourseService.getInstance();
        this.initializeData();
        createMenu();
    }
}
