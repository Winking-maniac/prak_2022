package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "lesson_hub")
@IdClass(LessonId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long lesson_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long course_id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @Column(name = "time_from")
    @NonNull
    private java.time.LocalTime time_from;

    @Column(name = "time_till")
    @NonNull
    private java.time.LocalTime time_till;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "lesson")
    private List<StudentLesson> students;
}
