package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class StudentLessonId implements Serializable
{
    private Long student_id;
    private Long course_id;
    private Long lesson_id;
}