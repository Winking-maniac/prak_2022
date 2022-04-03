package ru.msu.prak_2022.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourseId implements Serializable {
    private long teacher_id;
    private long course_id;
}
