package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LessonId implements Serializable
{
    private Long lesson_id;
    private CourseId course_id;
}