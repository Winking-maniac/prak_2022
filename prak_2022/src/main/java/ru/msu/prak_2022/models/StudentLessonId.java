package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentLessonId implements Serializable
{
    private long student_id;
    private long course_id;
    private long lesson_id;
}