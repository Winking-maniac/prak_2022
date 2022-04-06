package ru.msu.prak_2022.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseId implements Serializable {
    private Long teacher_id;
    private Long course_id;
}
