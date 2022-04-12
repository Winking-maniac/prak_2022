package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lesson_hub")
//@IdClass(LessonId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Serializable {
    public Lesson(@NonNull Long lesson_id,
                  @NonNull Course course,
                  @NonNull Teacher teacher) {
        this.lesson_id = lesson_id;
        this.course = course;
        this.teacher = teacher;
        this.time_till = new Timestamp(2020, 1, 1,20, 30, 40, 0);
        this.time_from = new Timestamp(2020, 1, 1,10, 30, 40, 0);



    }
    public Lesson(
                @NonNull Course course,
                @NonNull Teacher teacher,
                @NonNull Timestamp time_from,
                @NonNull Timestamp time_till,
                String description) {
        this.course = course;
        this.teacher = teacher;
        this.time_from = time_from;
        this.time_till = time_till;
        this.description = description;
    }

    @Id
    @Column
    @NonNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lesson_id;


    @Column(updatable = false, insertable = false)
    @NonNull
    private Long course_id;

    @Column(updatable = false, insertable = false)
    @NonNull
    private Long teacher_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @Column(name = "time_from")
    @NonNull
    private java.sql.Timestamp time_from;

    @Column(name = "time_till")
    @NonNull
    private java.sql.Timestamp time_till;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "lesson")
    private List<StudentLesson> students;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return getLesson_id().equals(lesson.getLesson_id()) && getCourse_id().equals(lesson.getCourse_id()) && getTeacher_id().equals(lesson.getTeacher_id()) && getCourse().equals(lesson.getCourse()) && getTeacher().equals(lesson.getTeacher()) && getTime_from().equals(lesson.getTime_from()) && getTime_till().equals(lesson.getTime_till()) && Objects.equals(getDescription(), lesson.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLesson_id(), getCourse_id(), getTeacher_id(), getCourse(), getTeacher(), getTime_from(), getTime_till(), getDescription());
    }
}
