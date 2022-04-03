package ru.msu.prak_2022.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "teacher_course_link")
@IdClass(TeacherCourseId.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TeacherCourse {
    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long course_id;

    @Id
    @Column(updatable = false, insertable = false)
    @NonNull
    private long teacher_id;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @NonNull
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    private Course course;

    @Column(name = "is_admin")
    @NonNull
    private boolean is_admin;
}
