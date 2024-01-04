package com.zinko.studentmanagement.service;

import com.zinko.studentmanagement.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing students.
 *
 * <p>
 * This class provides methods for CRUD operations on students, including retrieving all students,
 * adding a new student, getting the total number of students, finding a student by ID,
 * updating student information, and deleting a student from the system.
 * </p>
 *
 * <p>
 * The class follows the singleton pattern to ensure a single instance is used throughout the application.
 * </p>
 *
 * @author Zin Ko Winn
 * @see Student
 * @since 2024-01-03
 */
public class StudentService {
    private static StudentService INSTANCE;
    private static ArrayList<Student> students = new ArrayList<>();
    private static int totalStudents = 0;

    private StudentService() {
    }

    /**
     * Gets the singleton instance of StudentService.
     *
     * @return The singleton instance of StudentService.
     */
    public static StudentService getInstance() {
        return INSTANCE == null ? INSTANCE = new StudentService() : INSTANCE;
    }

    /**
     * Retrieves a list of all students.
     *
     * @return The list of students.
     */
    public List<Student> findAll() {
        return students;
    }

    /**
     * Adds a new student to the system.
     *
     * @param student The student to be added.
     */
    public void addStudent(Student student) {
        student.setId(students.size() + 1);
        students.add(student);
        totalStudents++;
    }

    /**
     * Retrieves the total number of students in the system.
     *
     * @return The total number of students.
     */
    public int getTotalStudents() {
        return totalStudents;
    }

    /**
     * Finds a student by their student ID.
     *
     * @param studentId The ID of the student to be found.
     * @return The found student or null if not found.
     */
    public Student findStudentById(String studentId) {
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                return student;
            }
        }
        return null;
    }

    /**
     * Updates the information of an existing student in the system.
     *
     * @param s The updated student information.
     */
    public void updateStudent(Student s) {
        Student student = this.findStudentById(s.getStudentId());
        if (student != null) {
            student.setId(s.getId());
            student.setName(s.getName());
            student.setAge(s.getAge());
            student.setStudentId(s.getStudentId());
        } else {
            System.out.println("Student ID not found. Update failed.");
        }
    }

    /**
     * Deletes a student from the system.
     *
     * @param s The student to be deleted.
     */
    public void delete(Student s) {
        Student student = this.findStudentById(s.getStudentId());
        if (student != null) {
            students.remove(student);
        }
    }
}
