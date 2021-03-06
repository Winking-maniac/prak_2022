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
    public StudentLesson(Long lesson_id,
                         Long course_id,
                         Long student_id,
                         Long score) {
        this.lesson_id = lesson_id;
        this.course_id = course_id;
        this.student_id = student_id;
        this.score = score;
    }

    @Id
    @Column
    @NonNull
    private Long lesson_id;

    @Id
    @Column
    @NonNull
    private Long course_id;

    @Id
    @Column
    @NonNull
    private Long student_id;

    @ManyToOne
    @NonNull
    @JoinColumns(value = {
            @JoinColumn(updatable = false, insertable = false, name = "course_id", referencedColumnName = "course_id"),
            @JoinColumn(updatable = false, insertable = false, name = "student_id", referencedColumnName = "student_id") })
    private StudentCourse student_course;

    @ManyToOne
    @NonNull
    @JoinColumns(value = {
            @JoinColumn(name = "course_id", referencedColumnName = "course_id", updatable = false, insertable = false),
            @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id", updatable = false, insertable = false) })
    private Lesson lesson;

    @Column(name = "score")
    @NonNull
    private Long score;
}
