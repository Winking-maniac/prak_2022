package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "student_lesson_link")
@IdClass(StudentLessonId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentLesson {
    @Id
    @Column
    @NonNull
    private long lesson_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long course_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long student_id;

    @ManyToOne
    @NonNull
    @JoinColumns(value = {
            @JoinColumn(name = "course_id", referencedColumnName = "course_id"),
            @JoinColumn(name = "student_id", referencedColumnName = "student_id") })
    private StudentCourse student_course;

    @ManyToOne
    @NonNull
    @JoinColumns(value = {
            @JoinColumn(name = "course_id", referencedColumnName = "course_id", updatable = false, insertable = false),
            @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id", updatable = false, insertable = false) })
    private Lesson lesson;

    @Column(name = "score")
    @NonNull
    private long score;
}
