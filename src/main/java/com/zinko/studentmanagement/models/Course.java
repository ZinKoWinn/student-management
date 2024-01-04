package com.zinko.studentmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a course in the student management system.
 *
 * <p>
 * This class encapsulates information about a course, including its unique identifier (id), course code,
 * course name, maximum capacity, current enrollment count, and a list of students enrolled in the course.
 * </p>
 *
 * <p>
 * The class provides methods to increment the enrollment count and add a student to the course.
 * It also overrides the equals and hashCode methods for proper comparison and hashing based on the
 * course's attributes.
 * </p>
 *
 * <p>
 * This class is annotated with Lombok annotations (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
 * to automatically generate getter, setter, builder, and constructor methods.
 * </p>
 *
 * @author Zin Ko Winn
 * @since 2024-01-03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private int id;
    private String courseCode;
    private String courseName;
    @Builder.Default
    private int maxCapacity = Integer.MAX_VALUE;
    private int currentEnrollment;
    @Builder.Default
    private List<Student> students = new ArrayList<>();

    /**
     * Increments the current enrollment count for the course.
     */
    public void incrementEnrollment() {
        currentEnrollment++;
    }

    /**
     * Adds a student to the list of enrolled students in the course.
     *
     * @param student The student to be added to the course.
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Overrides the equals method for proper comparison of Course objects.
     *
     * @param o The object to compare with the current course.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                maxCapacity == course.maxCapacity &&
                currentEnrollment == course.currentEnrollment &&
                Objects.equals(courseCode, course.courseCode) &&
                Objects.equals(courseName, course.courseName) &&
                Objects.equals(students, course.students);
    }

    /**
     * Overrides the hashCode method to generate a hash code based on the attributes of the course.
     *
     * @return The hash code for the course.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, courseCode, courseName, maxCapacity, currentEnrollment);
    }
}
