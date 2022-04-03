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
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;
}
