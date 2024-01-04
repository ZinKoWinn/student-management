package com.zinko.studentmanagement.models;

import java.util.*;

import com.zinko.studentmanagement.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a student in the student management system.
 *
 * <p>
 * This class encapsulates information about a student, including their unique identifier (id),
 * student ID, name, age, and a list of courses in which the student is enrolled.
 * </p>
 *
 * <p>
 * The class provides methods to enroll the student in a course and assign grades for enrolled courses.
 * It also overrides the equals and hashCode methods for proper comparison and hashing based on the
 * student's attributes.
 * </p>
 *
 * <p>
 * This class is annotated with Lombok annotations (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
 * to automatically generate getter, setter, builder, and constructor methods.
 * </p>
 *
 * @author Zin Ko Winn
 * @see Course
 * @since 2024-01-03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {
    private int id;
    private String studentId;
    private String name;
    private int age;
    @Builder.Default
    private List<Course> enrolledCourses = new ArrayList<>();

    /**
     * Enrolls the student in a course and increments the enrollment count for the course.
     *
     * @param course The course to enroll the student in.
     */
    public void enrollInCourse(Course course) {
        enrolledCourses.add(course);
        course.incrementEnrollment();
    }

    /**
     * Assigns a grade for the student in a specific course.
     *
     * @param course The course for which to assign the grade.
     * @param grade  The grade to be assigned.
     */
    public void assignGrade(Course course, int grade) {
        boolean isEnrolled = enrolledCourses.stream()
                .anyMatch(c -> c.getCourseCode().equals(course.getCourseCode()));

        if (isEnrolled) {
            Map<String, Map<Course, Integer>> grades = CourseService.getInstance().getStudentGrades();
            Map<Course, Integer> studentGrade = new HashMap<>();
            studentGrade.put(course, grade);
            grades.put(this.name.concat("_").concat(course.getCourseCode()), studentGrade);
        } else {
            System.out.println(String.format("Cannot assign grades for unenrolled course: %s of student: %s.",
                    course.getCourseName(), this.name));
        }
    }

    /**
     * Overrides the equals method for proper comparison of Student objects.
     *
     * @param o The object to compare with the current student.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id &&
                age == student.age &&
                Objects.equals(studentId, student.studentId) &&
                Objects.equals(name, student.name) &&
                Objects.equals(enrolledCourses, student.enrolledCourses);
    }

    /**
     * Overrides the hashCode method to generate a hash code based on the attributes of the student.
     *
     * @return The hash code for the student.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, studentId, name, age);
    }
}
