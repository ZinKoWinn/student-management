package com.zinko.studentmanagement.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the grade of a student in a specific course.
 *
 * <p>
 * This class encapsulates information about a student's grade, including the course name and the assigned grade.
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
public class StudentGrade {
    /**
     * The name of the course for which the grade is assigned.
     */
    private String courseName;

    /**
     * The grade assigned to the student in the course.
     */
    private int grade;
}
