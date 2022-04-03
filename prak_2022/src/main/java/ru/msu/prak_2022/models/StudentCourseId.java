package ru.msu.prak_2022.models;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseId implements Serializable {
    private long student_id;
    private long course_id;
}
