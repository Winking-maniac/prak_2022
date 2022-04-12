package ru.msu.prak_2022.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentCourseId implements Serializable {
    private Long student_id;
    private Long course_id;
}
