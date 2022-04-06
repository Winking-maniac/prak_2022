package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
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
    public Lesson(@NonNull Long lesson_id,
                  @NonNull Course course,
                  @NonNull Teacher teacher) {
        this.lesson_id = lesson_id;
        this.course = course;
        this.teacher = teacher;
    }
                  public Lesson(@NonNull Long lesson_id,
                  @NonNull Course course,
                  @NonNull Teacher teacher,
                  @NonNull LocalTime time_from,
                  @NonNull LocalTime time_till,
                  String description
                  ) {
        this.lesson_id = lesson_id;
        this.course = course;
        this.teacher = teacher;
        this.time_from = time_from;
        this.time_till = time_till;
        this.description = description;
    }

    @Id
    @Column
    @NonNull
    private Long lesson_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private Long course_id;

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
