package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "student_course_link")
@IdClass(StudentCourseId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourse {
    public StudentCourse(Long student_id, Long course_id) {
        this.student_id = student_id;
        this.course_id = course_id;
    }

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
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", updatable = false, insertable = false)
    private Student student;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", updatable = false, insertable = false)
    private Course course;
}
