package com.zinko.studentmanagement.service;

import com.zinko.studentmanagement.models.Course;
import com.zinko.studentmanagement.models.Student;

import java.util.*;

/**
 * Service class for managing courses and student enrollment and grading.
 *
 * <p>
 * This class provides methods for CRUD operations on courses, enrolling students in courses,
 * assigning grades to students, calculating overall grades for students, and retrieving student grades.
 * </p>
 *
 * <p>
 * The class follows the singleton pattern to ensure a single instance is used throughout the application.
 * </p>
 *
 * @author Zin Ko Winn
 * @see Course
 * @see Student
 * @since 2024-01-03
 */
public class CourseService {
    private static CourseService INSTANCE;
    private static List<Course> courses = new ArrayList<>();
    private static Map<String, Map<Course, Integer>> studentGrades = new HashMap<>();

    private CourseService() {
    }

    /**
     * Gets the singleton instance of CourseService.
     *
     * @return The singleton instance of CourseService.
     */
    public static CourseService getInstance() {
        return INSTANCE == null ? INSTANCE = new CourseService() : INSTANCE;
    }

    /**
     * Retrieves a list of all courses.
     *
     * @return The list of courses.
     */
    public List<Course> findAll() {
        return courses;
    }

    /**
     * Adds a new course to the system.
     *
     * @param course The course to be added.
     */
    public void addCourse(Course course) {
        course.setId(courses.size() + 1);
        courses.add(course);
    }

    /**
     * Updates an existing course in the system.
     *
     * @param c The updated course information.
     */
    public void updateCourse(Course c) {
        Course course = findCourse(c.getCourseCode());
        if (course != null) {
            course.setId(c.getId());
            course.setCourseCode(c.getCourseCode());
            course.setCourseName(c.getCourseName());
            course.setMaxCapacity(c.getMaxCapacity());
            course.setStudents(c.getStudents());
        } else {
            System.out.println("Course not found. Update failed.");
        }
    }

    /**
     * Enrolls a student in a course if there is available capacity.
     *
     * @param student The student to be enrolled.
     * @param course  The course in which the student will be enrolled.
     */
    public void enrollStudent(Student student, Course course) {
        if (course.getCurrentEnrollment() <= course.getMaxCapacity()) {
            course.addStudent(student);
            student.enrollInCourse(course);
        } else {
            System.out.println("Course " + course.getCourseCode() + " is full. Enrollment failed.");
        }
    }

    /**
     * Assigns a grade to a student for a specific course.
     *
     * @param student The student to whom the grade will be assigned.
     * @param course  The course for which the grade will be assigned.
     * @param grade   The grade to be assigned.
     */
    public void assignGrade(Student student, Course course, int grade) {
        student.assignGrade(course, grade);
    }

    /**
     * Calculates the overall grade for a student based on enrolled courses and grades.
     *
     * @param student The student for whom to calculate the overall grade.
     * @return The overall grade for the student.
     */
    public double calculateOverallGrade(Student student) {
        int totalPoints = student.getEnrolledCourses().stream()
                .map(course -> studentGrades.getOrDefault(student.getName() + "_" + course.getCourseCode(), Collections.emptyMap())
                        .getOrDefault(course, 0))
                .mapToInt(Integer::intValue)
                .sum();

        long totalCourses = student.getEnrolledCourses().size();

        return totalCourses == 0 ? 0.0 : (double) totalPoints / totalCourses;
    }

    /**
     * Retrieves the map containing student grades for all courses.
     *
     * @return The map containing student grades.
     */
    public Map<String, Map<Course, Integer>> getStudentGrades() {
        return studentGrades;
    }

    private Course findCourse(String courseCode) {
        for (Course course : courses) {
            if (course.getCourseCode().equals(courseCode)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Deletes a course from the system.
     *
     * @param c The course to be deleted.
     */
    public void delete(Course c) {
        Course course = findCourse(c.getCourseCode());
        if (course != null) {
            courses.remove(course);
        }
    }
}
